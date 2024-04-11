package org.hoey.io;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;

import java.lang.foreign.MemorySegment;

/**
 * 写入缓冲区
 *
 * @author liuhuijun
 * @since 2024/4/11 9:06
 */
public class WriteBuffer implements AutoCloseable{

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


    public long writeIndex() {
        return this.writeIndex;
    }

    @Override
    public void close() throws Exception {

    }

    static final class HeapWriteBufferPolicy implements WriteBufferPolicy {

        private byte[] data;

        @Override
        public void resize(WriteBuffer writeBuffer, long nextIndex) {
            if(nextIndex < 0 || nextIndex > Integer.MAX_VALUE){
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
}
