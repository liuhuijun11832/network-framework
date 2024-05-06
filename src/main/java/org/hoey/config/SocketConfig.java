package org.hoey.config;

import lombok.Data;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/6 22:53
 */
@Data
public class SocketConfig {

    private boolean reuseAddr = true;

    private boolean keepAlive = false;

    private boolean tcpNoDelay = true;

    private boolean ipv6Only = false;

}
