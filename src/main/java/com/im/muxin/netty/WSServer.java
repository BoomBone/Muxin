package com.im.muxin.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WSServer {
    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture future;

    private WSServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(null);
    }

    private static class SingletonWSServer {
        private final static WSServer INSTANCE = new WSServer();
    }

    public static WSServer getInstance() {
        return SingletonWSServer.INSTANCE;
    }

    public void start() {
        this.future = serverBootstrap.bind(8090);
        System.err.println("netty websocket server 启动完毕。。。");
    }

}
