package org.hoey;

import org.hoey.util.NativeUtil;
import org.slf4j.Logger;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Main {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment ms = arena.allocate(ValueLayout.JAVA_INT);
            logger.info("MemorySegment address: {}", ms.address());
            NativeUtil.setInt(ms, 0L, 123);
            logger.info("MemorySegment value: {}", NativeUtil.getInt(ms, 0L));

        }
    }
}