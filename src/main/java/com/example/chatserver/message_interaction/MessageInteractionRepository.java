package com.example.chatserver.message_interaction;

import com.example.chatserver.message.MessageRepository;
import com.example.chatserver.user.UserRepository;
import jakarta.persistence.*;

@Entity
@Table(name = "message_interactions")
public class MessageInteractionRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private MessageRepository message;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserRepository user;

    @Column(nullable = false, length = 20)
    private String type;  // e.g., "LIKE", "LOVE", "HAHA"

    @Column(nullable = false)
    private long timestamp; // lưu khi người dùng tương tác

    public MessageInteractionRepository(Long id, MessageRepository message, UserRepository user, String type, long timestamp) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageRepository getMessage() {
        return message;
    }

    public void setMessage(MessageRepository message) {
        this.message = message;
    }

    public UserRepository getUser() {
        return user;
    }

    public void setUser(UserRepository user) {
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