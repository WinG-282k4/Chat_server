package com.example.chatserver.messenger;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "message")
public class repository {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    String messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private com.example.chatserver.user.repository sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private com.example.chatserver.user.repository receiver;

    String content;
    long timestamp;
    boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private jakarta.persistence.Entity chatRoom;

    String type; // e.g., text, image, video
    String status; // e.g., sent, delivered, seen

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "reply_to_message_id", referencedColumnName = "messageId")
    private repository replyToMessage; // Message being replied to, if any
    String reaction; // e.g., like, love, laugh, etc

    // Default constructor
    public repository(String messageId, com.example.chatserver.user.repository sender, com.example.chatserver.user.repository receiver, String content, long timestamp, boolean isRead, jakarta.persistence.Entity chatRoom, String type, String status, repository replyToMessage, String reaction) {
        this.messageId = messageId;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.chatRoom = chatRoom;
        this.type = type;
        this.status = status;
        this.replyToMessage = replyToMessage;
        this.reaction = reaction;
    }

    // Getters and Setters

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public com.example.chatserver.user.repository getSender() {
        return sender;
    }

    public void setSender(com.example.chatserver.user.repository sender) {
        this.sender = sender;
    }

    public com.example.chatserver.user.repository getReceiver() {
        return receiver;
    }

    public void setReceiver(com.example.chatserver.user.repository receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public jakarta.persistence.Entity getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(jakarta.persistence.Entity chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public repository getReplyToMessage() {
        return replyToMessage;
    }

    public void setReplyToMessage(repository replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}
