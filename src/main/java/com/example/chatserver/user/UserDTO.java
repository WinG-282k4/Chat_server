package com.example.chatserver.user;

import org.springframework.stereotype.Component;

@Component
public class UserDTO {

    private Long userId;
    private String username;
    private String email;

    // Constructors
    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email) {
        this.userId = id;
        this.username = username;
        this.email = email;
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
}
