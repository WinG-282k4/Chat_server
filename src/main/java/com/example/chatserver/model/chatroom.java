package com.example.chatserver.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "chatroom")
public class chatroom {
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
    private Set<user> participants = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "chatroom_admins",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<user> admins = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "chatroom_banned",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<user> bannedUsers = new HashSet<>();

    // Default constructor
    public chatroom(Long id, String name, String createdBy, long createdAt, boolean isActive, String profilePictureUrl, Set<user> participants, Set<user> admins, Set<user> bannedUsers) {
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

    public Set<user> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<user> participants) {
        this.participants = participants;
    }

    public Set<user> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<user> admins) {
        this.admins = admins;
    }

    public Set<user> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(Set<user> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }
}
