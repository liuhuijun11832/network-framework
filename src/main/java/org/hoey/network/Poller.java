package org.hoey.network;

import org.hoey.config.PollerConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/22 23:32
 */
@Slf4j
public final class Poller {

    private final Thread pollerThread;

    public Thread thread(){
        return this.pollerThread;
    }

    private Thread createPollerThread(PollerConfig pollerConfig){
        // todo
        return null;
    }

    public Poller(PollerConfig pollerConfig) {
        this.pollerThread = createPollerThread(pollerConfig);
    }


}
