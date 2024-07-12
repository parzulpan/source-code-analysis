package com.parzulpan.sca.netty.redisprotocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 基于Netty、RESP协议实现的Redis客户端
 *
 * @author panpan
 * @since 2024/07
 */
public class RedisClient {
    static final byte[] LINE = {13, 10};

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();

        try {
            client
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        // 通道建立成功后调用：向Redis发送一条set命令
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            String authCommand = "AUTH " + "xx";
                                            ByteBuf buffer1 = respCommand(authCommand);
                                            ctx.writeAndFlush(buffer1);

                                            String cmd = "set crm.test.name panpan";
                                            ByteBuf buffer2 = respCommand(cmd);
                                            ctx.channel().writeAndFlush(buffer2);
                                        }

                                        // Redis响应数据时触发：打印Redis的响应结果
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            // 接受Redis服务端执行指令后的结果
                                            ByteBuf buffer = (ByteBuf) msg;
                                            System.out.println(buffer.toString(CharsetUtil.UTF_8));
                                        }
                                    });
                        }
                    })
                    // 根据IP、端口连接Redis服务端
                    .connect("xxx", 6379).sync();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * RESP客户端协议
     * <p>
     * 1. 首先要求所有命令，都以*开头，后面跟着具体的子命令数量，接着用换行符分割。
     * 2. 接着需要先用$符号声明每个子命令的长度，然后再用换行符分割。
     * 3. 最后再拼接上具体的子命令，同样用换行符分割。
     */
    private static ByteBuf respCommand(String cmd) {
        // 先对传入的命令以空格进行分割
        String[] cmds = cmd.split(" ");
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        // 遵循RESP协议：先写入指令的个数
        buffer.writeBytes(("*" + cmds.length).getBytes());
        buffer.writeBytes(LINE);

        // 接着分别写入每个指令的长度以及具体值
        for (String s : cmds) {
            buffer.writeBytes(("$" + s.length()).getBytes());
            buffer.writeBytes(LINE);
            buffer.writeBytes(s.getBytes());
            buffer.writeBytes(LINE);
        }

        // 把转换成RESP格式的命令返回
        return buffer;
    }
}
