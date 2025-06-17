package com.example.chatserver.chatroom;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@jakarta.persistence.Entity
@Table(name = "chatroom")
public class repository {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String createdBy; // User ID of the creator
    private long createdAt; // Timestamp of creation
    private boolean isActive; // Indicates if the chatroom is active or archived
    private String profilePictureUrl; // URL for the chatroom's profile picture

    @ManyToMany
    @JoinTable(
            name = "chatroom_participants",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<com.example.chatserver.user.repository> participants = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "chatroom_admins",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<com.example.chatserver.user.repository> admins = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "chatroom_banned",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<com.example.chatserver.user.repository> bannedUsers = new HashSet<>();

    // Default constructor
    public repository(Long id, String name, String createdBy, long createdAt, boolean isActive, String profilePictureUrl, Set<com.example.chatserver.user.repository> participants, Set<com.example.chatserver.user.repository> admins, Set<com.example.chatserver.user.repository> bannedUsers) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.profilePictureUrl = profilePictureUrl;
        this.participants = participants;
        this.admins = admins;
        this.bannedUsers = bannedUsers;
    }

    //Getters and Setters

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
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

    public Set<com.example.chatserver.user.repository> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<com.example.chatserver.user.repository> participants) {
        this.participants = participants;
    }

    public Set<com.example.chatserver.user.repository> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<com.example.chatserver.user.repository> admins) {
        this.admins = admins;
    }

    public Set<com.example.chatserver.user.repository> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(Set<com.example.chatserver.user.repository> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }
}
