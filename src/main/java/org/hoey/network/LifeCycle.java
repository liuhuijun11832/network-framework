package org.hoey.network;

/**
 * 生命周期
 *
 * @author liuhuijun
 * @since 2024/4/15 22:08
 */
public interface LifeCycle {

    void init();

    void exit() throws InterruptedException;
}
