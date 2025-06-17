package com.example.chatserver.message;

import com.example.chatserver.chatroom.ChatroomRepository;
import com.example.chatserver.message_interaction.MessageInteractionRepository;
import com.example.chatserver.user.UserRepository;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@jakarta.persistence.Entity
@Table(name = "messenger")
public class MessageRepository {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "userId")
    private UserRepository sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "userId")
    private UserRepository receiver;

    @Column(name = "content")
    private String content;
    @Column(name = "timestamp")
    private long timestamp;
    @Column(name = "is_read")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private ChatroomRepository chatRoom;

    @Column(name = "type")
    private String type; // e.g., text, image, video
    @Column(name = "status")
    private String status; // e.g., sent, delivered, seen
    @Column(name = "last_message")
    private String lastMessage; // Last message in the chatroom

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_message_id", referencedColumnName = "messageId")
    private MessageRepository replyToMessage; // Message being replied to, if any

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageInteractionRepository> interactions = new ArrayList<>();

    public long countByType(String type) {
        if (interactions == null) return 0;
        return interactions.stream()
                .filter(i -> type.equalsIgnoreCase(i.getType()))
                .count();
    }

    public MessageRepository() {
        // Default constructor
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public UserRepository getSender() {
        return sender;
    }

    public void setSender(UserRepository sender) {
        this.sender = sender;
    }

    public UserRepository getReceiver() {
        return receiver;
    }

    public void setReceiver(UserRepository receiver) {
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

    public ChatroomRepository getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatroomRepository chatRoom) {
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public MessageRepository getReplyToMessage() {
        return replyToMessage;
    }

    public void setReplyToMessage(MessageRepository replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    public List<MessageInteractionRepository> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<MessageInteractionRepository> interactions) {
        this.interactions = interactions;
    }
}
