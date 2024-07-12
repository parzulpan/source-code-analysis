package com.parzulpan.sca.netty.protobuf;

import com.google.protobuf.ByteString;

/**
 * @author panpan
 * @since 2024/07
 */
public class ProtoExample {
    public static void main(String[] args) {
        ChatMessageOuterClass.ChatMessage.Builder builder = ChatMessageOuterClass.ChatMessage.newBuilder()
                .setSenderId("Alice")
                .setReceiverId("Bob")
                .setContent("Hell0, Bob!")
                .setMessageType(ChatMessageOuterClass.ChatMessage.MessageType.TEXT);

        ByteString byteString = builder.build().toByteString();
        System.out.println(byteString);

    }
}
