package com.parzulpan.sca.netty.heartbeat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 服务端的初始化器
 *
 * @author panpan
 * @since 2024/07
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                // 配置如果5s内未触发读事件，就会触发读闲置事件
                .addLast("IdleStateHandler", new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS))
                .addLast("Encoder", new StringEncoder(CharsetUtil.UTF_8))
                .addLast("Decoder", new StringDecoder(CharsetUtil.UTF_8))
                // 装载自定义的服务端心跳处理器
                .addLast("HeartbeatHandler", new HeartbeatServerHandler());
    }
}
