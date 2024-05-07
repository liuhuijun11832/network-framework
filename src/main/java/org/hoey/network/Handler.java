package org.hoey.network;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/7 23:33
 */
public interface Handler {

    /**
     * 连接建立
     * @param channel
     */
    void onConnected(Channel channel);

    /**
     * 接收到数据
     * @param channel
     * @param msg
     */
    void onRecv(Channel channel, Object msg);

    /**
     * 连接断开
     * @param channel
     */
    void onShutdown(Channel channel);

    /**
     * 连接释放
     * @param channel
     */
    void onRemoved(Channel channel);

}
