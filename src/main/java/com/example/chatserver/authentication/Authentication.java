package com.example.chatserver.authentication;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Data
@Builder
@Getter
public class Authentication {
    private String username;
    private String password;
}
