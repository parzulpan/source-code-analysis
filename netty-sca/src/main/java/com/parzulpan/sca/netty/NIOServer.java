package com.parzulpan.sca.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        System.out.println(">>>>>>>...NIO服务端启动...>>>>>>>>");
        // 1.创建服务端通道、选择器与字节缓冲区
        ServerSocketChannel ssc = ServerSocketChannel.open();
        Selector selector = Selector.open();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 2.为服务端绑定IP地址+端口
        ssc.bind(new InetSocketAddress("127.0.0.1", 8888));
        // 3.将服务端设置为非阻塞模式，同时绑定接收事件注册到选择器
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        // 4.通过选择器轮询所有已就绪的通道
        while (selector.select() > 0) {
            // 5.获取当前选择器上注册的通道中所有已经就绪的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            // 6.遍历得到的所有事件，并根据事件类型进行处理
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                // 7.如果是接收事件就绪，那则获取对应的客户端连接
                if (next.isAcceptable()) {
                    SocketChannel channel = ssc.accept();
                    // 8.将获取到的客户端连接置为非阻塞模式，绑定事件并注册到选择器上
                    channel.configureBlocking(false);
                    int event = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
                    channel.register(selector, event);
                    System.out.println("客户端连接：" + channel.getRemoteAddress());
                }
                // 9.如果是读取事件就绪，则先获取对应的通道连接
                else if (next.isReadable()) {
                    SocketChannel channel = (SocketChannel) next.channel();
                    // 10.然后从对应的通道中，将数据读取到缓冲区并输出
                    int len = -1;
                    while ((len = channel.read(buffer)) > 0) {
                        buffer.flip();
                        System.out.println("收到信息：" +
                                new String(buffer.array(), 0, buffer.remaining()));
                    }
                    buffer.clear();
                }
            }
            // 11.将已经处理后的事件从选择器上移除（选择器不会自动移除）
            iterator.remove();
        }
    }
}
