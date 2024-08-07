package com.parzulpan.sca.netty.xpackage;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 演示半包问题的客户端
 *
 * @author panpan
 * @since 2024/07
 */
public class HalfPackageClient {
    public static void main(String[] args) {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();

        try {
            client
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                // 在通道准备就绪后会触发的事件
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) {
                                    // 向服务端发送十次数据，每次发送十个字节！
                                    for (int i = 0; i < 10; i++) {
                                        ByteBuf buffer = ctx.alloc().buffer();
                                        buffer.writeBytes(new byte[]
                                                {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'x', 'y', 'z'});
                                        ctx.writeAndFlush(buffer);
                                    }
                                }
                            });
                        }
                    }).connect("127.0.0.1", 8888).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
