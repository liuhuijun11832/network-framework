package org.hoey.network;

/**
 * @author liuhuijun
 * @since 2024/5/7 23:34
 */
public record ChannelImpl(Socket socket, Encoder encoder, Decoder decoder, Handler handler, Poller poller,
                          Writer writer, Loc loc) implements Channel {

}
