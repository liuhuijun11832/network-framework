package org.hoey.network;

import org.hoey.io.WriteBuffer;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/7 23:24
 */
@FunctionalInterface
public interface Encoder {  
    void encode(WriteBuffer writeBuffer, Object o);
}

