package com.parzulpan.sca.netty.xpackage;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 演示半包问题的服务端
 *
 * @author panpan
 * @since 2024/07
 */
public class HalfPackageServer {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();


        server
                .group(group)
                .channel(NioServerSocketChannel.class)
                // 调整服务端的接收窗口大小为四字节
                .option(ChannelOption.SO_RCVBUF, 4)
                .childHandler(new ServerInitializer())
                .bind("127.0.0.1", 8888);
    }
}
