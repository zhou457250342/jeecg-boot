package org.jeecg.modules.monitor.engine;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CompletableFuture;

/**
 * @Author : nadir
 * @create 2023/3/22 17:23
 */
public abstract class SocketListener implements IDisposable {
    protected final ChannelHandler _channelHandler;
    protected final String _address;
    protected final int _port;

    private final int freqMonitor = 1000 * 5;
    private boolean isMonitor = true;

    public SocketListener(ChannelHandler channelHandler, String address, int port) {
        _channelHandler = channelHandler;
        _address = address;
        _port = port;
    }

    abstract void init();

    abstract void start();

    abstract void close();

    abstract boolean isActive() throws Exception;


    public static SocketListener server(ISocketHandler socketHandler, String address, int port) {
        SocketListener socketListener = new SocketListenerServer(socketHandler, address, port);
        socketListener.init();
        socketListener.monitor();
        return socketListener;
    }

    public static SocketListener client(ISocketHandler socketHandler, String address, int port) {
        SocketListener socketListener = new SocketListenerClient(socketHandler, address, port);
        socketListener.init();
        socketListener.monitor();
        return socketListener;
    }

    public void monitor() {
        CompletableFuture.runAsync(() -> {
            while (isMonitor) {
                try {
                    if (!isActive()) start();
                    Thread.sleep(freqMonitor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void disposed() {
        isMonitor = false;
        close();
    }
}

class SocketListenerServer extends SocketListener {
    private final EventLoopGroup _bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup _workerGroup = new NioEventLoopGroup();
    private ChannelFuture channelFuture;
    private ServerBootstrap serverBootstrap;

    SocketListenerServer(ChannelHandler channelHandler, String address, int port) {
        super(channelHandler, address, port);
    }

    SocketListenerServer(ISocketHandler socketHandler, String address, int port) {
        super(new SocketInitializer(socketHandler), address, port);
    }

    @Override
    public boolean isActive() throws Exception {
        return channelFuture != null && channelFuture.isSuccess() && channelFuture.sync().channel().isActive();
    }

    @Override
    void init() {
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(_bossGroup, _workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(_channelHandler);
    }

    @Override
    public void start() {
        CompletableFuture.runAsync(() -> {
            try {
                if (channelFuture != null) channelFuture.cancel(false);
                channelFuture = serverBootstrap.bind(_port).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                _bossGroup.shutdownGracefully();
                _workerGroup.shutdownGracefully();
            }
        });
    }

    @Override
    public void close() {
        _bossGroup.shutdownGracefully();
        _workerGroup.shutdownGracefully();
    }
}

class SocketListenerClient extends SocketListener {
    private final EventLoopGroup _group = new NioEventLoopGroup();
    private ChannelFuture channelFuture;
    private Bootstrap bootstrap;

    SocketListenerClient(ChannelHandler channelHandler, String address, int port) {
        super(channelHandler, address, port);
    }

    SocketListenerClient(ISocketHandler socketHandler, String address, int port) {
        super(new SocketInitializer(socketHandler), address, port);
    }


    @Override
    public boolean isActive() throws Exception {
        return channelFuture != null && channelFuture.isSuccess() && channelFuture.sync().channel().isActive();
    }

    @Override
    void init() {
        bootstrap = new Bootstrap();
        bootstrap.group(_group)
                .channel(NioSocketChannel.class)
                .handler(_channelHandler);
    }

    @Override
    public void start() {
        CompletableFuture.runAsync(() -> {
            try {
                if (channelFuture != null)
                    channelFuture.cancel(false);
                channelFuture = bootstrap.connect(_address, _port);
//            cf.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                    if (!channelFuture.isSuccess()) {
//                        //重连交给后端线程执行
//                        channelFuture.sync().channel().eventLoop().schedule(() -> {
//                            System.err.println("重连服务端...");
//                            try {
//                                start();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }, 3000, TimeUnit.MILLISECONDS);
//                    } else {
//                        System.out.println("服务端连接成功...");
//                    }
//                }
//            });
                Channel ch = channelFuture.sync().channel();
                ch.closeFuture().sync();
//            ChannelFuture lastWriteFuture = null;
//            for (int i = 0; i < 10; i++) {
//                lastWriteFuture = ch.writeAndFlush("Hi,Java.");
//            }
//            if (lastWriteFuture != null) {
//                lastWriteFuture.sync();
//            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放资源
//            _group.shutdownGracefully();
            }
        });
    }

    @Override
    public void close() {
        _group.shutdownGracefully();
    }
}

