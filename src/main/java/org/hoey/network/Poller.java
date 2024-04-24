package org.hoey.network;

import org.hoey.config.PollerConfig;
import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.exception.constant.Constants;
import org.jctools.queues.atomic.MpscUnboundedAtomicArrayQueue;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/22 23:32
 */
@Slf4j
public final class Poller {

    private static final AtomicInteger counter = new AtomicInteger(0);

    private final Thread pollerThread;

    private final Queue<PollerTask> readerTaskQueue = new MpscUnboundedAtomicArrayQueue<>(1024);

    public Thread thread(){
        return this.pollerThread;
    }

    private Thread createPollerThread(PollerConfig pollerConfig){
        int sequence = counter.incrementAndGet();
        return Thread.ofPlatform().name(STR."poller-\{sequence}").unstarted(() -> {
            for (;;){
                // 多路复用监听

                // 队列事件处理
            }
        });
    }

    public Poller(PollerConfig pollerConfig) {
        this.pollerThread = createPollerThread(pollerConfig);
    }

    public void submit(PollerTask pollerTask){
        if (pollerTask == null || !readerTaskQueue.offer(pollerTask)) {
            throw new FrameworkException(ExceptionType.NETWORK, Constants.UNREACHED);
        }
    }


}
