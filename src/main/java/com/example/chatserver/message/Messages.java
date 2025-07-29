package com.example.chatserver.message;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.message_interaction.MessageInteraction;
import com.example.chatserver.user.User;
import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@jakarta.persistence.Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "userId")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "userId")
    private User receiver;

    @Column(name = "content")
    private String content;
    @Column(name = "timestamp")
    private long timestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chatroomId")
    private Chatroom chatRoom;

    @Column(name = "type")
    private String type; // e.g., text, image, video
    @Column(name = "status")
    private String status; // e.g., sent, delivered, seen

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_message_id", referencedColumnName = "messageId")
    private Messages replyToMessage; // Message being replied to, if any

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageInteraction> interactions = new ArrayList<>();

    public long countByType(String type) {
        if (interactions == null) return 0;
        return interactions.stream()
                .filter(i -> type.equalsIgnoreCase(i.getType()))
                .count();
    }

    @Column(name = "like_count")
    private long likeCount;

    public Messages() {
        // Default constructor
    }

    public Messages(Long messageId){
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
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

    public Chatroom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(Chatroom chatRoom) {
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

    public Messages getReplyToMessage() {
        return replyToMessage;
    }

    public void setReplyToMessage(Messages replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    public List<MessageInteraction> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<MessageInteraction> interactions) {
        this.interactions = interactions;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }
}
