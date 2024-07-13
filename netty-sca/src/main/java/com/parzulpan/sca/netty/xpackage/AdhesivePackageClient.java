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
 * 演示数据粘包问题的客户端
 *
 * @author panpan
 * @since 2024/07
 */
public class AdhesivePackageClient {
    public static void main(String[] args) {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();

        try {
            client.group(worker);
            client.channel(NioSocketChannel.class);
            client.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        // 在通道准备就绪后会触发的事件
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            // 向服务端发送十次数据，每次发送一个字节！
                            for (int i = 0; i < 100; i++) {
                                System.out.println("正在向服务端发送第" + i + "次数据......");
                                ByteBuf buffer = ctx.alloc().buffer(1);
                                buffer.writeBytes(new byte[]{(byte) i});
                                ctx.writeAndFlush(buffer);
                            }
                        }
                    });
                }
            });

            client.connect("127.0.0.1", 8888).sync();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            worker.shutdownGracefully();
        }
    }
}
