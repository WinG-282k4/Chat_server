package com.example.chatserver.chatroom;

import com.example.chatserver.Chatroomparticipant.ChatroomParticipant;
import com.example.chatserver.user.User;
import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@jakarta.persistence.Entity
@Table(name = "chatroom")
public class Chatroom {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long chatroomId;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "userId")
    private User createdBy; // User ID of the creator

    @Column(name = "created_at")
    private long createdAt; // Timestamp of creation
    @Column(name = "is_active")
    private boolean isActive; // Indicates if the chatroom is active or archived
    @Column(name = "profile_picture_url")
    private String profilePictureUrl; // URL for the chatroom's profile picture
    @Column(name = "last_message")
    private String lastMessage; // Last message sent in the chatroom

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatroomParticipant> participants = new HashSet<>();

    // Default constructor
    public Chatroom() {
        // Default constructor
    }

    // Constructor
    public Chatroom(String name, User createdBy, long createdAt, boolean isActive, String profilePictureUrl, String lastMessage) {
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.profilePictureUrl = profilePictureUrl;
        this.lastMessage = lastMessage;
    }

    // Getters and Setters
    public Long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Set<ChatroomParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ChatroomParticipant> participants) {
        this.participants = participants;
    }
}
