package org.jeecg.modules.monitor.engine;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @Author : nadir
 * @create 2023/3/22 17:58
 */
public class SocketInitializer extends ChannelInitializer<SocketChannel> {

    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();
    private final SocketHandler _serverHandler;

    public SocketInitializer(ISocketHandler socketHandler) {
        _serverHandler = new SocketHandler(socketHandler);
    }

    /**
     * 初始化通道的具体执行方法
     */
    @Override
    public void initChannel(SocketChannel ch) {
        // 通道 Channel 设置
        ChannelPipeline pipeline = ch.pipeline();
        // 设置(字符串)编码器和解码器
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);
        // 服务器端连接之后的执行器,接收到消息之后的业务处理
        pipeline.addLast(_serverHandler);
    }
}

@ChannelHandler.Sharable
class SocketHandler extends SimpleChannelInboundHandler<String> {
    private final ISocketHandler _socketHandler;

    SocketHandler(ISocketHandler socketHandler) {
        _socketHandler = socketHandler;
    }

    /**
     * 读取到客户端的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) {
        if (!request.isEmpty()) {
            _socketHandler.msgRead((InetSocketAddress) ctx.channel().remoteAddress(), request);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    /**
     * 数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 异常处理，打印异常并关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        _socketHandler.exceptionCaught(cause);
    }
}
