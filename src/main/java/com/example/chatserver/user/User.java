package com.example.chatserver.user;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password; // In a real application, this should be hashed
    @Column(name = "name")
    private String name;
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    @Column(name = "status")
    private String status; // e.g., online, offline, away
    @Column(name = "last_active")
    private long lastActive; // Timestamp of the last activity

    public User() {
        // Default constructor
    }
    public User(String username, String email, String password, String name, String profilePictureUrl, String status, long lastActive) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
        this.status = status;
        this.lastActive = lastActive;
    }

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
