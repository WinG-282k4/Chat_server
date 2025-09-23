package com.example.chatserver.invalidJWT;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRspository extends JpaRepository<InvalidToken, String> {
}
