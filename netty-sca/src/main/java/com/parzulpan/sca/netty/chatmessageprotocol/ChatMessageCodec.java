package com.parzulpan.sca.netty.chatmessageprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 自定义消息传输协议
 * <p>
 * 在自定义传输协议时，咱们必然需要考虑几个因素，如下：
 *  魔数：用来第一时间判断是否为自己需要的数据包。
 *  版本号：提高协议的拓展性，方便后续对协议进行升级。
 *  序列化算法：消息正文具体该使用哪种方式进行序列化传输，例如Json、ProtoBuf、JDK...。
 *  消息类型：第一时间判断出当前消息的类型。
 *  消息序号：为了实现双工通信，客户端和服务端之间收/发消息不会相互阻塞。
 *  正文长度：提供给LTC解码器使用，防止解码时出现粘包、半包的现象。
 *  消息正文：本次消息要传输的具体数据。
 *
 * @author panpan
 * @since 2024/07
 */
// 用来标识当前处理器是否可在多线程环境下使用，如果带有该注解的处理器，则表示可以在多个通道间共用，因此只需要创建一个即可，
// 反之同理，如果不带有该注解的处理器，则每个通道需要单独创建使用。
@ChannelHandler.Sharable
public class ChatMessageCodec extends MessageToMessageCodec<ByteBuf, Message> {
    // 将数据在原生ByteBuf与Message之间进行相互转换

    /**
     * 消息出站时会经过的编码方法（将原生消息对象封装成自定义协议的消息格式）
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {

    }

    /**
     * 消息入站时会经过的解码方法（将自定义格式的消息转变为具体的消息对象）
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

    }
}
