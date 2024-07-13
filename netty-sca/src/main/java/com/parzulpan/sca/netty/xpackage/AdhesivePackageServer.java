package com.parzulpan.sca.netty.xpackage;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 演示数据粘包问题的服务端
 *
 * @author panpan
 * @since 2024/07
 */
public class AdhesivePackageServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();

        server.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer());

        server.bind("127.0.0.1", 8888).sync();
    }
}
