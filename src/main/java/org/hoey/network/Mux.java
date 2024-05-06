package org.hoey.network;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.exception.constant.Constants;
import org.hoey.util.NativeUtil;

import java.lang.foreign.MemorySegment;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/6 22:54
 */
public record Mux(MemorySegment memorySegment,
                  int epfd,
                  int kqfd) {

    public static Mux win(MemorySegment memorySegment){
        return new Mux(memorySegment, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    public static Mux linux(int epfd){
        return new Mux(NativeUtil.NULL_POINTER, epfd, Integer.MIN_VALUE);
    }

    public static Mux mac(int kqfd){
        return new Mux(NativeUtil.NULL_POINTER, Integer.MIN_VALUE, kqfd);
    }

    @Override
    public String toString() {
        if(memorySegment != NativeUtil.NULL_POINTER) {
            return String.valueOf(memorySegment.address());
        }else if(epfd != Integer.MIN_VALUE) {
            return String.valueOf(epfd);
        }else if(kqfd != Integer.MIN_VALUE) {
            return String.valueOf(kqfd);
        }else {
            throw new FrameworkException(ExceptionType.NETWORK, Constants.UNREACHED);
        }
    }

}
