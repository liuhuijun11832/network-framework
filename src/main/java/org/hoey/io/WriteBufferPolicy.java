package org.hoey.io;

/**
 * 定义内存扩容策略和关闭行为
 *
 * @author liuhuijun
 * @since 2024/4/11 9:06
 */
public interface WriteBufferPolicy {

    /**
     * 扩容
     * @param writeBuffer 待扩容的buffer
     * @param nextIndex   该次写入后的writeIndex
     */
    void resize(WriteBuffer writeBuffer, long nextIndex);

    void close(WriteBuffer writeBuffer);

}
