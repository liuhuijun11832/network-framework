package org.hoey.network;

import org.hoey.config.WriterConfig;

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

}
