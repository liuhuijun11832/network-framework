package org.hoey.network;

import org.hoey.config.ListenerConfig;
import org.hoey.config.PollerConfig;
import org.hoey.config.WriterConfig;
import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.exception.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/15 22:13
 */
public final class Network extends AbstractLifeCycle {
    private static final Logger logger = LoggerFactory.getLogger(Network.class);

    private static final AtomicBoolean instanceFlag = new AtomicBoolean(false);

    private final State state = new State();

    private List<Poller> pollers = new ArrayList<>();

    private List<Writer> writers = new ArrayList<>();

    private Thread netThread;

    public Network(PollerConfig pollerConfig, WriterConfig writerConfig) {
        if (!instanceFlag.compareAndSet(false, true)) {
            throw new FrameworkException(ExceptionType.NETWORK, Constants.UNREACHED);
        }
        this.pollers = IntStream.range(0, pollerConfig.getPollerCount()).mapToObj(_ -> new Poller(pollerConfig)).toList();
        this.writers = IntStream.range(0, writerConfig.getWriterCount()).mapToObj(_ -> new Writer(writerConfig)).toList();
        this.netThread = createNetThread();
    }

    private Thread createNetThread() {
        return Thread.ofPlatform().unstarted(() -> {
            // 主线程
        });
    }

    public void addListener(ListenerConfig listenerConfig){
        try (Mutex _ = state.withMutex()) {
            int current = state.get();
            if (current > Constants.RUNNING) {
                throw new RuntimeException(Constants.UNREACHED);
            }
        }
    }

    @Override
    protected void doInit() {
        try (Mutex _ = state.withMutex()) {

        }
    }

    @Override
    protected void doExit() throws InterruptedException {
        try (Mutex _ = state.withMutex()) {
        }
    }
}
