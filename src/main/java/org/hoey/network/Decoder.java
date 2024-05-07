package org.hoey.network;

import org.hoey.io.ReadBuffer;

import java.util.List;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/7 23:31
 */
@FunctionalInterface
public interface Decoder {

    void decode(ReadBuffer readBuffer, List<Object> entityList);

}
