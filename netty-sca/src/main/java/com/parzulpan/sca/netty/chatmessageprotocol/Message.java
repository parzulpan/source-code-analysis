package com.parzulpan.sca.netty.chatmessageprotocol;

/**
 * @author panpan
 * @since 2024/07
 */
public class Message {
    private int messageType;
    private int sequenceId;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
}
