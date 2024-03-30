package org.hoey.util;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public final class NativeUtil {

    public static final MemorySegment NULL_POINTER = MemorySegment.ofAddress(0);

    /**
     * MethodHandle与VarHandle类均为Java语言自JDK9以后引入的新的反射调用机制，
     * 其中MethodHandle类可以被理解成函数指针，用于高效的访问函数，
     * VarHandle类可以被理解成对象指针，用于高效的访问变量。这两个类主要用于替换Java语言中陈旧的反射机制，并且不局限于访问Java中创建的方法与字段，还能够以统一的API实现对于Native方法与堆外内存的访问。
     */
    private static final VarHandle BYTE_HANDLE = MethodHandles.memorySegmentViewVarHandle(ValueLayout.JAVA_BYTE);

    private static final VarHandle INT_HANDLER = MethodHandles.memorySegmentViewVarHandle(ValueLayout.JAVA_INT_UNALIGNED);

    private static final VarHandle SHORT_HANDLER = MethodHandles.memorySegmentViewVarHandle(ValueLayout.JAVA_SHORT_UNALIGNED);
    private static final VarHandle LONG_HANDLER = MethodHandles.memorySegmentViewVarHandle(ValueLayout.JAVA_LONG_UNALIGNED);

    public NativeUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isNullPointer(MemorySegment memorySegment) {
        return memorySegment == null || memorySegment.address() == 0;
    }

    public static byte getByte(MemorySegment memorySegment, long index) {
        return (byte) BYTE_HANDLE.get(memorySegment, index);
    }

    public static void setByte(MemorySegment memorySegment, long index, byte value) {
        BYTE_HANDLE.set(memorySegment, index, value);
    }


}
