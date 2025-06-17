package com.example.chatserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class message {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    String messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private user sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private user receiver;

    String content;
    long timestamp;
    boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private  chatroom chatRoom;

    String type; // e.g., text, image, video
    String status; // e.g., sent, delivered, seen

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "reply_to_message_id", referencedColumnName = "messageId")
    private message replyToMessage; // Message being replied to, if any
    String reaction; // e.g., like, love, laugh, etc

    // Default constructor
    public message(String messageId, user sender, user receiver, String content, long timestamp, boolean isRead, chatroom chatRoom, String type, String status, message replyToMessage, String reaction) {
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

    public user getSender() {
        return sender;
    }

    public void setSender(user sender) {
        this.sender = sender;
    }

    public user getReceiver() {
        return receiver;
    }

    public void setReceiver(user receiver) {
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

    public chatroom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(chatroom chatRoom) {
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

    public message getReplyToMessage() {
        return replyToMessage;
    }

    public void setReplyToMessage(message replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}
