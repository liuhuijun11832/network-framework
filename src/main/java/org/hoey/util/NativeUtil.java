package org.hoey.util;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public final class NativeUtil {

    public static final MemorySegment NULL_POINTER = MemorySegment.ofAddress(0);

    private static final VarHandle BYTE_HANDLE = MethodHandles.memorySegmentViewVarHandle(ValueLayout.JAVA_BOOLEAN);

    public NativeUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isNullPointer(MemorySegment memorySegment) {
        return memorySegment == null ||memorySegment.address() == 0;
    }

    public static byte getByte(MemorySegment memorySegment, long index) {
        return (byte) BYTE_HANDLE.get(memorySegment, index);
    }

    public static void setByte(MemorySegment memorySegment, long index, byte value) {
        BYTE_HANDLE.set(memorySegment, index, value);
    }



}
