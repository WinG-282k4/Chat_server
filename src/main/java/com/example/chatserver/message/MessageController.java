package com.example.chatserver.message;

import com.example.chatserver.security.UserPrincipal;
import com.example.chatserver.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    // This class will handle HTTP requests related to messages
    // For example, you can add methods to create, read, update, and delete messages

    // Example method to get all messages
    // @GetMapping
    // public List<MessageRepository> getAllMessages() {
    //     return messageService.getAllMessages();
    // }

    // Example method to create a new message
    // @PostMapping
    // public MessageRepository createMessage(@RequestBody MessageRepository message) {
    //     return messageService.createMessage(message);
    // }

    // Add more methods as needed for your application
    @Autowired
    private final MessageRepository messageRepo;

    @Autowired
    private final MessageService messageService;

    @Autowired
    private final MessageMapper Mapper;

    @Autowired
    private final UserService userService;

    private final SimpMessagingTemplate messagingTemplate;

    // GET message theo ID
    @GetMapping("/{receiverid}")
    public ResponseEntity<List<MessageDTO>> getMessage(
            @PathVariable Long receiverid,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Long senderid = userService.getUserIdByUsername(user.getUsername());
        return ResponseEntity.ok(messageService.findAllMessages(senderid, receiverid));
    }

    // POST tạo message mới
    @MessageMapping("/chat") // Lắng nghe trên destination WebSocket
    public void sendMessage(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Payload MessageDTO chatMessage
    ) {
        MessageDTO newmessage = messageService.CreateMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(
                newmessage.getReceiverId().toString(),"/queue/messages",
                newmessage.getContent()
        );

    }

    // PUT cập nhật message theo ID
    @PutMapping("/{id}") // ID của message cần cập nhật
    public ResponseEntity updateMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @RequestBody MessageDTO newMessage
    ) {
        return null;
    }

    // DELETE message theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id
    ) {
        messageService.DeleteMessage(id, userPrincipal.getUsername());
        return ResponseEntity.ok(Map.of(
                "status", "Delete successful"
                ));
    }

}
