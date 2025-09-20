package com.example.chatserver.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntrospectResponse {
    private Boolean active;
}
