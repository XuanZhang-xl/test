package io.netty.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * created by zhangxuan9 on 2019/1/18
 */
public class EchoServer {
    private final int port;
    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(8080).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class) // 使用nio传输channel
                    .localAddress(new InetSocketAddress(port)) // 指定端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            // 异步绑定服务器，sync()阻塞等待直到绑定完成
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            // TODO:这儿是干嘛
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 释放资源
            group.shutdownGracefully().sync();
        }
    }
}
