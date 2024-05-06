package org.hoey.os;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.util.NativeUtil;

/**
 * sealed标识符，这表明该接口是一个密封接口，只允许存在指定的这几个实现类，密封接口的主要目的是为Java编译器提供一些帮助，比如在针对密封接口的switch语句中，编译器就可以提前知道所有可能出现的实现类的类型，这样我们可以忽略掉默认的default分支
 *
 * @author liuhuijun
 * @since 2024/5/06 23:31
 */
public sealed interface OsNetworkLibrary permits WindowsNetworkLibrary, LinuxNetworkLibrary, MacOSNetworkLibrary {
        OsNetworkLibrary CURRENT = switch (NativeUtil.osType()) {
            case Windows -> new WindowsNetworkLibrary();
            case Linux -> new LinuxNetworkLibrary();
            case MacOS -> new MacOSNetworkLibrary();
            default -> throw new FrameworkException(ExceptionType.NETWORK, "Unsupported operating system");
        };
    }