package org.hoey.network;

import org.hoey.exception.constant.Constants;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/15 22:09
 */
public abstract class AbstractLifeCycle implements LifeCycle {

    private final AtomicInteger state = new AtomicInteger(Constants.INITIAL);

    @Override
    public void init() {
        if (state.compareAndSet(Constants.INITIAL, Constants.STARTING)) {
            doInit();
        }else {
            throw new IllegalStateException("LifeCycle already started");
        }
    }

    @Override
    public void exit() throws InterruptedException {
        if (state.compareAndSet(Constants.RUNNING, Constants.STOPPED)) {
            doExit();
        }else {
            throw new IllegalStateException("LifeCycle already stopped");
        }
    }

    protected abstract void doInit();

    protected abstract void doExit() throws InterruptedException;
}
