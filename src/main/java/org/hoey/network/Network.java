package org.hoey.network;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.exception.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/15 22:13
 */
public final class Network extends AbstractLifeCycle {
    private static final Logger logger = LoggerFactory.getLogger(Network.class);

    private static final AtomicBoolean instanceFlag = new AtomicBoolean(false);

    public Network() {
        if (!instanceFlag.compareAndSet(false, true)) {
            throw new FrameworkException(ExceptionType.NETWORK, Constants.UNREACHED);
        }
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected void doExit() throws InterruptedException {

    }
}
