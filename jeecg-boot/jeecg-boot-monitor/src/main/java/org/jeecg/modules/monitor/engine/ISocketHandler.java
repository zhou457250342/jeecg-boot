package org.jeecg.modules.monitor.engine;

import java.net.InetSocketAddress;

/**
 * @Author : nadir
 * @create 2023/3/22 17:47
 */
public interface ISocketHandler {
    void msgRead(InetSocketAddress address, String msg);

    default void exceptionCaught(Throwable cause) {
        cause.printStackTrace();
    }
}
