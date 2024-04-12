package org.hoey;

import org.hoey.io.ReadBuffer;
import org.hoey.io.WriteBuffer;
import org.hoey.util.NativeUtil;
import org.slf4j.Logger;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Main {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment ms = arena.allocate(ValueLayout.JAVA_INT);
            logger.info("MemorySegment address: {}", ms.address());
            NativeUtil.setInt(ms, 0L, 123);
            logger.info("MemorySegment value: {}", NativeUtil.getInt(ms, 0L));

        }

        try(Arena arena = Arena.ofConfined()) {
            MemorySegment memorySegment = NativeUtil.allocateString(arena, "hello world");
            ReadBuffer readBuffer = new ReadBuffer(memorySegment);
            logger.info("Accessing readBuffer : {}", readBuffer.readCStr());
        }

        try (WriteBuffer writeBuffer = WriteBuffer.newDefaultWriteBuffer(Arena.ofConfined(), ValueLayout.JAVA_INT.byteSize())) {
            writeBuffer.writeInt(1);
            writeBuffer.writeInt(2);
            writeBuffer.writeInt(3);
            MemorySegment segment = writeBuffer.content();
            logger.info("Accessing index 0: {}", NativeUtil.getInt(segment, 0L));
            logger.info("Accessing index 4: {}", NativeUtil.getInt(segment, 4L));
            logger.info("Accessing index 8: {}", NativeUtil.getInt(segment, 8L));
        }

    }
}