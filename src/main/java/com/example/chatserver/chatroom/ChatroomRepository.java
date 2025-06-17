package com.example.chatserver.chatroom;

import com.example.chatserver.user.UserRepository;
import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@jakarta.persistence.Entity
@Table(name = "chatroom")
public class ChatroomRepository {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "userId")
    private UserRepository createdBy; // User ID of the creator

    @Column(name = "created_at")
    private long createdAt; // Timestamp of creation
    @Column(name = "is_active")
    private boolean isActive; // Indicates if the chatroom is active or archived
    @Column(name = "profile_picture_url")
    private String profilePictureUrl; // URL for the chatroom's profile picture
    @Column(name = "last_message")
    private String lastMessage; // Last message sent in the chatroom

    @ManyToMany
    @JoinTable(
            name = "chatroom_participants",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserRepository> participants = new HashSet<>();// Set of participants in the chatroom

    @ManyToMany
    @JoinTable(
            name = "chatroom_admins",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserRepository> admins = new HashSet<>();// Set of admins in the chatroom

    @ManyToMany
    @JoinTable(
            name = "chatroom_banned",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserRepository> bannedUsers = new HashSet<>();// Set of banned users in the chatroom

    // Default constructor
    public ChatroomRepository() {
        // Default constructor
    }
    public ChatroomRepository(String name, UserRepository createdBy, long createdAt, boolean isActive, String profilePictureUrl) {
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters and Setters


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRepository getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserRepository createdBy) {
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

    public Set<UserRepository> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserRepository> participants) {
        this.participants = participants;
    }

    public Set<UserRepository> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<UserRepository> admins) {
        this.admins = admins;
    }

    public Set<UserRepository> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(Set<UserRepository> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }
}
