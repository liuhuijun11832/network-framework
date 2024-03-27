package org.hoey;

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
        }
    }
}