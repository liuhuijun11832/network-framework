package org.hoey.io;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.util.NativeUtil;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public final class ReadBuffer {

    private final MemorySegment memorySegment;

    private final long size;

    private long readIndex;


    public ReadBuffer(MemorySegment memorySegment) {
        this.memorySegment = memorySegment;
        this.size = memorySegment.byteSize();
        this.readIndex = 0;
    }

    public long size() {
        return size;
    }

    public long readIndex() {
        return readIndex;
    }

    public void setReadIndex(long index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        readIndex = index;
    }

    public byte readByte() {
        long nextIndex = readIndex + ValueLayout.JAVA_BYTE.byteSize();
        if (nextIndex > size) {
            throw new FrameworkException(ExceptionType.NATIVE, "read index overflow");
        }
        byte b = NativeUtil.getByte(memorySegment, readIndex);
        readIndex = nextIndex;
        return b;
    }

    public byte[] readBytes(int count) {
        long nextIndex = readIndex + count;
        if (nextIndex > size) {
            throw new FrameworkException(ExceptionType.NATIVE, "read index overflow");
        }
        byte[] bytes = memorySegment.asSlice(readIndex, count).toArray(ValueLayout.JAVA_BYTE);
        readIndex = nextIndex;
        return bytes;
    }

    public short readShort() {
        long nextIndex = readIndex + ValueLayout.JAVA_SHORT.byteSize();
        if (nextIndex > size) {
            throw new FrameworkException(ExceptionType.NATIVE, "read index overflow");
        }
        short s = NativeUtil.getShort(memorySegment, readIndex);
        readIndex = nextIndex;
        return s;
    }

    public int readInt() {
        long nextIndex = readIndex + ValueLayout.JAVA_INT.byteSize();
        if (nextIndex > size) {
            throw new FrameworkException(ExceptionType.NATIVE, "read index overflow");
        }
        int i = NativeUtil.getInt(memorySegment, readIndex);
        readIndex = nextIndex;
        return i;
    }

    public long readLong() {
        long nextIndex = readIndex + ValueLayout.JAVA_LONG.byteSize();
        if (nextIndex > size) {
            throw new FrameworkException(ExceptionType.NATIVE, "read index overflow");
        }
        long l = NativeUtil.getLong(memorySegment, readIndex);
        readIndex = nextIndex;
        return l;
    }

    public MemorySegment readMemorySegment(long size) {
        long nextIndex = readIndex + size;
        if (nextIndex > this.size) {
            throw new FrameworkException(ExceptionType.NATIVE, "read index overflow");
        }
        MemorySegment ms = memorySegment.asSlice(readIndex, size);
        readIndex = nextIndex;
        return ms;
    }

    public MemorySegment readHeapSegment(long size) {
        MemorySegment ms = readMemorySegment(size);
        if (ms.isNative()) {
            long len = ms.byteSize();
            byte[] bytes = new byte[(int) size];
            MemorySegment h = MemorySegment.ofArray(bytes);
            MemorySegment.copy(ms, 0, h, 0, len);
            return h;
        } else {
            return ms;
        }
    }
}
