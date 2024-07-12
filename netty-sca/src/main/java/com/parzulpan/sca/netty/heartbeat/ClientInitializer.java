package com.parzulpan.sca.netty.heartbeat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 客户端的初始化器
 *
 * @author panpan
 * @since 2024/07
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline
                // 配置如果3s内未触发写事件，就会触发写闲置事件
                .addLast("IdleStateHandler", new IdleStateHandler(0, 3, 0, TimeUnit.SECONDS))
                .addLast("Encoder", new StringEncoder(CharsetUtil.UTF_8))
                .addLast("Decoder", new StringDecoder(CharsetUtil.UTF_8))
                // 装载自定义的客户端心跳处理器
                .addLast("HeartbeatHandler", new HeartbeatClientHandler());
    }
}
