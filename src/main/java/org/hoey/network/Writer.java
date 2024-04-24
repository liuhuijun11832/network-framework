package org.hoey.network;

import org.hoey.config.WriterConfig;
import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.exception.constant.Constants;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/22 23:36
 */
@Slf4j
public final class Writer {

    private final Thread writerThread;

    private static final AtomicInteger counter = new AtomicInteger(0);

    private static final Queue<WriterTask> queue = new LinkedTransferQueue<>();


    public Thread thread(){
        return this.writerThread;
    }

    public Thread createWriterThread(WriterConfig config){
        // todo
        return null;
    }

    public Writer(WriterConfig writerConfig){
        this.writerThread = createWriterThread(writerConfig);
    }

    public void submit(WriterTask writerTask){
        if (writerTask == null || !queue.offer(writerTask)) {
            throw new FrameworkException(ExceptionType.NETWORK, Constants.UNREACHED);
        }
    }

}
