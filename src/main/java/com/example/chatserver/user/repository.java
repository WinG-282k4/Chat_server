package com.example.chatserver.user;

import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name = "user")
public class repository {
    String userId;
    String username;
    String email;
    String password; // In a real application, this should be hashed
    String name;
    String profilePictureUrl;
    String status; // e.g., online, offline, away
    long lastActive; // Timestamp of the last activity

    public repository(String userId, String username, String email, String password, String profilePictureUrl, String status, long lastActive) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
        this.status = status;
        this.lastActive = lastActive;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getLastActive() {
        return lastActive;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = lastActive;
    }
}
