package com.parzulpan.sca.netty.xio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author panpan
 * @since 2024/07
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        System.out.println(">>>>>>>...NIO客户端启动...>>>>>>>>");
        // 1.创建一个TCP类型的通道并指定地址建立连接
        SocketChannel channel = SocketChannel.open(
                new InetSocketAddress("127.0.0.1", 8888));
        // 2.将通道置为非阻塞模式
        channel.configureBlocking(false);
        // 3.创建字节缓冲区，并写入要传输的消息数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String msg = "我是熊猫！";
        buffer.put(msg.getBytes());
        // 4.将缓冲区切换为读取模式
        buffer.flip();
        // 5.将带有数据的缓冲区写入通道，利用通道传输数据
        channel.write(buffer);
        // 6.传输完成后情况缓冲区、关闭通道
        buffer.clear();
        channel.close();
    }
}
