package com.example.chatserver.message_interaction;

import com.example.chatserver.message.Messages;
import com.example.chatserver.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "message_interactions")
public class MessageInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MessageInteractionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Messages message;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String type;  // e.g., "LIKE", "LOVE", "HAHA"

    @Column(nullable = false)
    private long timestamp; // lưu khi người dùng tương tác

    public MessageInteraction() {
        // Default constructor
    }
    public MessageInteraction(Messages message, User user, String type, long timestamp) {
        this.message = message;
        this.user = user;
        this.type = type;
        this.timestamp = timestamp;
    }
    // Getters and Setters
    public Long getMessageInteractionId() {
        return MessageInteractionId;
    }
    public void setMessageInteractionId(Long messageInteractionId) {
        MessageInteractionId = messageInteractionId;
    }

    public Messages getMessage() {
        return message;
    }

    public void setMessage(Messages message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}