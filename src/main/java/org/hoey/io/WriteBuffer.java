package org.hoey.io;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.exception.constant.Constants;
import org.hoey.util.NativeUtil;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.StandardCharsets;

/**
 * 写入缓冲区
 *
 * @author liuhuijun
 * @since 2024/4/11 9:06
 */
public final class WriteBuffer implements AutoCloseable {

    private static final int DEFAULT_HEAP_BUFFER_SIZE = 32;
    private MemorySegment segment;
    private long size;
    private long writeIndex;
    private final WriteBufferPolicy policy;

    private WriteBuffer(MemorySegment segment, WriteBufferPolicy policy) {
        this.segment = segment;
        this.size = segment.byteSize();
        this.writeIndex = 0;
        this.policy = policy;
    }


    @Override
    public void close() throws Exception {
        policy.close(this);
    }

    public static WriteBuffer newDefaultWriteBuffer(Arena arena, long byteSize) {
        return new WriteBuffer(arena.allocate(byteSize), new DefaultWriteBufferPolicy(arena));
    }

    static final class HeapWriteBufferPolicy implements WriteBufferPolicy {

        private byte[] data;

        @Override
        public void resize(WriteBuffer writeBuffer, long nextIndex) {
            if (nextIndex < 0 || nextIndex > Integer.MAX_VALUE) {
                throw new FrameworkException(ExceptionType.NATIVE, "Heap writeBuffer size overflow");
            }
            int newLen = Math.max((int) nextIndex, data.length << 1);
            byte[] newData = new byte[newLen];
            System.arraycopy(data, 0, newData, 0, (int) writeBuffer.writeIndex());
            writeBuffer.segment = MemorySegment.ofArray(newData);
            writeBuffer.size = newLen;
        }

        @Override
        public void close(WriteBuffer writeBuffer) {
            // heap memory no need close
        }
    }

    record FixedWriBufferPolicy(Arena arena) implements WriteBufferPolicy {

        @Override
        public void resize(WriteBuffer writeBuffer, long nextIndex) {
            throw new FrameworkException(ExceptionType.NATIVE, "Current writeBuffer cannot resize");
        }

        @Override
        public void close(WriteBuffer writeBuffer) {
            arena.close();
        }
    }

    record DefaultWriteBufferPolicy(Arena arena) implements WriteBufferPolicy {

        @Override
        public void resize(WriteBuffer writeBuffer, long nextIndex) {
            // 已有的writeBuffer的大小扩容一倍,和nextIndex比较作为扩容依据
            long newLen = Math.max(nextIndex, writeBuffer.size << 1);
            if (newLen < 0) {
                throw new FrameworkException(ExceptionType.NATIVE, "MemorySize overflow");
            }
            // 用新的大小进行堆外分配内存
            MemorySegment memorySegment = arena.allocateArray(ValueLayout.JAVA_BYTE, newLen);
            // 内存拷贝操作,类似c语言的memcpy()
            MemorySegment.copy(writeBuffer.segment, 0, memorySegment, 0, writeBuffer.writeIndex);
            writeBuffer.segment = memorySegment;
            writeBuffer.size = newLen;
        }

        @Override
        public void close(WriteBuffer writeBuffer) {
            arena.close();
        }
    }

    static final class ReservedWriteBufferPolicy implements WriteBufferPolicy {

        private Arena arena = null;

        @Override
        public void resize(WriteBuffer writeBuffer, long nextIndex) {
            long newLen = Math.max(nextIndex, writeBuffer.size << 1);
            if (newLen < 0) {
                throw new FrameworkException(ExceptionType.NATIVE, "MemorySize overflow");
            }
            if (arena == null) {
                arena = Arena.ofConfined();
            }
            MemorySegment memorySegment = arena.allocateArray(ValueLayout.JAVA_BYTE, newLen);
            MemorySegment.copy(writeBuffer.segment, 0, memorySegment, 0, writeBuffer.writeIndex);
            writeBuffer.segment = memorySegment;
            writeBuffer.size = newLen;
        }

        @Override
        public void close(WriteBuffer writeBuffer) {
            if (arena != null) {
                arena.close();
            }
        }
    }

    public long size() {
        return this.size;
    }

    public long writeIndex() {
        return this.writeIndex;
    }

    public void resize(long nextIndex) {
        if (nextIndex < 0) {
            throw new FrameworkException(ExceptionType.NATIVE, "Index overflow");
        } else if (nextIndex > size) {
            policy.resize(this, nextIndex);
        }
    }

    public void writeByte(byte b) {
        long nextIndex = this.writeIndex + 1;
        resize(nextIndex);
        NativeUtil.setByte(segment, writeIndex, b);
        writeIndex = nextIndex;
    }

    public void writeBytes(byte[] b, int off, int len) {
        if (len < 0 || len > b.length - off) {
            throw new FrameworkException(ExceptionType.NATIVE, "Length overflow");
        }
        long nextIndex = this.writeIndex + len;
        resize(nextIndex);
        MemorySegment.copy(MemorySegment.ofArray(b), off, segment, writeIndex, len);
        writeIndex = nextIndex;
    }

    public void writeBytes(byte... bytes) {
        long nextIndex = writeIndex + bytes.length;
        resize(nextIndex);
        MemorySegment.copy(MemorySegment.ofArray(bytes), 0, segment, writeIndex, bytes.length);
        writeIndex = nextIndex;
    }

    public void writeShort(short s) {
        long nextIndex = writeIndex + ValueLayout.JAVA_SHORT.byteSize();
        resize(nextIndex);
        NativeUtil.setShort(segment, writeIndex, s);
        writeIndex = nextIndex;
    }

    public void writeInt(int i) {
        long nextIndex = writeIndex + ValueLayout.JAVA_INT.byteSize();
        resize(nextIndex);
        NativeUtil.setInt(segment, writeIndex, i);
        writeIndex = nextIndex;
    }

    public void writeLong(long l) {
        long nextIndex = writeIndex + ValueLayout.JAVA_LONG.byteSize();
        resize(nextIndex);
        NativeUtil.setLong(segment, writeIndex, l);
        writeIndex = nextIndex;
    }

    public void writeCStr(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        MemorySegment memorySegment = MemorySegment.ofArray(bytes);
        long len = memorySegment.byteSize();
        long nextIndex = writeIndex + len + 1;
        resize(nextIndex);
        MemorySegment.copy(memorySegment, 0, segment, writeIndex, len);
        NativeUtil.setByte(segment, writeIndex + len, Constants.NUT);
        writeIndex = nextIndex;
    }

    public void writeSegment(MemorySegment memorySegment) {
        long len = memorySegment.byteSize();
        long nextIndex = writeIndex + len;
        resize(nextIndex);
        MemorySegment.copy(memorySegment, 0, segment, writeIndex, len);
        writeIndex = nextIndex;
    }


    public void setByte(long index, byte value) {
        if(index + 1 > writeIndex) {
            throw new RuntimeException("Index out of bound");
        }
        NativeUtil.setByte(segment, index, value);
    }

    public void setShort(long index, short value) {
        if(index + 2 > writeIndex) {
            throw new RuntimeException("Index out of bound");
        }
        NativeUtil.setShort(segment, index, value);
    }

    public void setInt(long index, int value) {
        if(index + 4 > writeIndex) {
            throw new RuntimeException("Index out of bound");
        }
        NativeUtil.setInt(segment, index, value);
    }

    public void setLong(long index, long value) {
        if(index + 8 > writeIndex) {
            throw new RuntimeException("Index out of bound");
        }
        NativeUtil.setLong(segment, index, value);
    }


    public MemorySegment content() {
        return writeIndex == size ? segment : segment.asSlice(0L, writeIndex);
    }

    public WriteBuffer truncate(long offset) {
        if(offset > writeIndex) {
            throw new FrameworkException(ExceptionType.NATIVE, "Truncate index overflow");
        }
        WriteBuffer w = new WriteBuffer(segment.asSlice(offset, size - offset), policy);
        w.writeIndex = writeIndex - offset;
        return w;
    }




}
