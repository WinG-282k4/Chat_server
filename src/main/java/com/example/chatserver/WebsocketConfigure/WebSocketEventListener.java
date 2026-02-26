package com.example.chatserver.WebsocketConfigure;

import com.example.chatserver.user.UserDTO;
import com.example.chatserver.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * Listen for WebSocket session disconnects and broadcast a user-disconnected message
 * so frontend lists update even when clients close the tab or network drops.
 */
@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        Principal principal = event.getUser();
        if (principal == null) return;

        String username = principal.getName();
        if (username == null) return;

        UserDTO dto = userService.disconnect(username);
        if (dto != null) {
            // Broadcast to the same destination your controller uses
            messagingTemplate.convertAndSend("/users/topic", dto);
        }
    }
}
