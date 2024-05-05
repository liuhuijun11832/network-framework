package org.hoey.os;

import org.hoey.exception.ExceptionType;
import org.hoey.exception.FrameworkException;
import org.hoey.util.NativeUtil;

public sealed interface OsNetworkLibrary permits WindowsNetworkLibrary, LinuxNetworkLibrary, MacOSNetworkLibrary {
        OsNetworkLibrary CURRENT = switch (NativeUtil.osType()) {
            case Windows -> new WindowsNetworkLibrary();
            case Linux -> new LinuxNetworkLibrary();
            case MacOS -> new MacOSNetworkLibrary();
            default -> throw new FrameworkException(ExceptionType.NETWORK, "Unsupported operating system");
        };
    }