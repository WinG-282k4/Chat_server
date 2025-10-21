package com.example.chatserver.authentication;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String token;
    private Long userId;
    private String name;
    private boolean authenticated;
}
