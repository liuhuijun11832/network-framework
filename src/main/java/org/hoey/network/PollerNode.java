package org.hoey.network;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/7 23:41
 */
public sealed interface PollerNode permits ProtocolPollerNode, SentryPollerNode {
}
