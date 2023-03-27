package org.jeecg.modules.monitor.handler;

import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.monitor.engine.ISocketHandler;

import java.net.InetSocketAddress;

/**
 * @Author : nadir
 * @create 2023/3/24 15:09
 */
@ChannelHandler.Sharable
@Slf4j
public class MR_IPM7_Handler implements ISocketHandler {
    @Override
    public void msgRead(InetSocketAddress address, String msg) {
        log.info("MR-接收:" + msg);
    }

    @Override
    public void exceptionCaught(Throwable cause) {
        ISocketHandler.super.exceptionCaught(cause);
    }
}
