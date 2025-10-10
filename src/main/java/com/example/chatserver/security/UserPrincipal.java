package com.example.chatserver.security;

public class UserPrincipal {
    private String username;
    // Bạn có thể thêm các trường khác từ JWT payload vào đây
    // private Long userId;
    // private String email;

    // Constructor, getters...
    public UserPrincipal(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}