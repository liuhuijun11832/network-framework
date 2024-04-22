package org.hoey.network;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;

/**
 * 表示一个服务器对外暴露的IP地址和端口号。该类非常简单，只包含一个IP地址的类型，一个String类型的IP地址字符串与一个int类型的port端口号
 * 使用record类，由JVM生成构造函数，性能会有一定提升
 *
 * @author liuhuijun
 * @since 2024/4/22 22:46
 */
public record Loc(
        IpType ipType,
        String ip,
        int port
) {

    private static final int PORT_MAX = 65535;

    public short shortPort(){
        if(port < 0 || port > PORT_MAX) {
            throw new FrameworkException(ExceptionType.NETWORK, "Port number overflow");
        }
        return (short) port;
    }

    @Override
    public String toString(){
        return STR."[\{ip == null || ip.isBlank() ? "localhost" : ip}:\{port}]";
    }

}
