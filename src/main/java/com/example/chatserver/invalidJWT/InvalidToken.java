package com.example.chatserver.invalidJWT;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InvalidToken {

    @Id
    private String jti;

    private Instant expiry_time;
}
