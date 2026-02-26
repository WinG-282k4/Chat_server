package com.example.chatserver.WebsocketConfigure;

import com.example.chatserver.user.UserDTO;
import com.example.chatserver.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

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
            System.out.println("[WebSocketEventListener] broadcasting disconnect for " + username + " to /topic");
            messagingTemplate.convertAndSend("/topic", dto);
        }
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        Principal principal = event.getUser();
        if (principal == null) return;

        String username = principal.getName();
        if (username == null) return;

        UserDTO dto = userService.connect(username);
        if (dto != null) {
            System.out.println("[WebSocketEventListener] broadcasting connect for " + username + " to /topic");
            messagingTemplate.convertAndSend("/topic", dto);
        }
    }
}
