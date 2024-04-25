package org.hoey.network;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.exception.constant.Constants;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/25 22:50
 */
public record Mutex(Lock lock) implements AutoCloseable {

    public Mutex {
        if (lock == null) {
            throw new FrameworkException(ExceptionType.CONTEXT, Constants.UNREACHED);
        }
    }

    public Mutex() {
        this(new ReentrantLock());
    }

    public Mutex acquire() {
        lock.lock();
        return this;
    }

    @Override
    public void close() {
        lock.unlock();
    }
}
