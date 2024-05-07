package org.hoey.network;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/7 23:35
 */
public sealed interface Channel permits ChannelImpl{

    Socket socket();

    Encoder encoder();

    Decoder decoder();

    Handler handler();

    Poller poller();

    Writer writer();

    Loc loc();

}
