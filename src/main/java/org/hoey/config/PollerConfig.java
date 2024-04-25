package org.hoey.config;

import org.hoey.util.NativeUtil;

import lombok.Getter;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/22 23:31
 */
@Getter
public class PollerConfig {

    private int pollerCount = Math.max(NativeUtil.getCpuCount() >> 1, 4);
}
