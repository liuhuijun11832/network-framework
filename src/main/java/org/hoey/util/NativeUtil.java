package org.hoey.util;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.exception.constant.Constants;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.charset.StandardCharsets;

public final class NativeUtil {

    public static final MemorySegment NULL_POINTER = MemorySegment.ofAddress(0);

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

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

    public static int getInt(MemorySegment memorySegment, long index) {
        return (int) INT_HANDLER.get(memorySegment, index);
    }

    public static void setInt(MemorySegment memorySegment, long index, int value) {
        INT_HANDLER.set(memorySegment, index, value);
    }

    public static short getShort(MemorySegment memorySegment, long index) {
        return (short) SHORT_HANDLER.get(memorySegment, index);
    }

    public static void setShort(MemorySegment memorySegment, long index, short value) {
        SHORT_HANDLER.set(memorySegment, index, value);
    }

    public static long getLong(MemorySegment memorySegment, long index) {
        return (long) LONG_HANDLER.get(memorySegment, index);
    }

    public static void setLong(MemorySegment memorySegment, long index, long value) {
        LONG_HANDLER.set(memorySegment, index, value);
    }

    public static String getString(MemorySegment memorySegment) {
        return getString(memorySegment, 0);
    }

    public static String getString(MemorySegment ptr, int maxLength) {
        if (maxLength > 0) {
            byte[] bytes = new byte[maxLength];
            for (int i = 0; i < maxLength; i++) {
                byte b = getByte(ptr, i);
                if (b == 0) {
                    return new String(bytes, 0, i);
                }
                bytes[i] = b;
            }
            return new String(bytes);
        } else {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                byte b = getByte(ptr, i);
                if (b == Constants.NUT) {
                    byte[] bytes = new byte[i];
                    MemorySegment.copy(ptr, ValueLayout.JAVA_BYTE, 0, bytes, 0, i);
                    return new String(bytes, StandardCharsets.UTF_8);
                }
            }
        }
        throw new FrameworkException(ExceptionType.NATIVE, Constants.UNREACHED);
    }


    public static MemorySegment allocateString(Arena arena, String str) {
        return arena.allocateUtf8String(str);
    }

    public static MemorySegment allocateString(Arena arena, String str, int len) {
        MemorySegment memorySegment = MemorySegment.ofArray(str.getBytes(StandardCharsets.UTF_8));
        long size = memorySegment.byteSize();
        if (len < size +1) {
            throw new RuntimeException("String out of range");
        }
        MemorySegment allocated = arena.allocateArray(ValueLayout.JAVA_BYTE, len);
        MemorySegment.copy(memorySegment, 0, allocated, 0, size);
        setByte(memorySegment, size, Constants.NUT);
        return memorySegment;
    }

    public static boolean matches(MemorySegment m, long offset, byte[] bytes) {
        for(int index = 0; index < bytes.length; index++) {
            if (getByte(m, offset + index) != bytes[index]) {
                return false;
            }
        }
        return true;
    }

    public static int getCpuCount(){
        return CPU_COUNT;
    }


}
