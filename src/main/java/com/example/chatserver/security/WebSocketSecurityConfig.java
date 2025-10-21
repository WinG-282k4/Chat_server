package com.example.chatserver.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        // TẮT BẢO MẬT:
        // Cấu hình này nói với Spring Security rằng "cho bất kỳ tin nhắn nào (anyMessage),
        // hãy cho phép (permitAll) mà không cần xác thực".
        messages.anyMessage().permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        // Tắt tính năng CSRF (Cross-Site Request Forgery) cho WebSocket.
        // Điều này là cần thiết để các client bên ngoài (như Postman) có thể kết nối.
        return true;
    }
}