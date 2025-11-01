package com.example.chatserver.message;

import com.example.chatserver.security.UserPrincipal;
import com.example.chatserver.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
            Principal principal,
            @Payload MessageDTO chatMessage
    ) {

        // Lấy username từ Principal để đồng bộ với user-destination
        String username = principal.getName();
        chatMessage.setSenderId(userService.getUserIdByUsername(username));

        MessageDTO newmessage = messageService.CreateMessage(chatMessage);
        String receiverUsername = userService.getUsernameByUserId(newmessage.getReceiverId());

        // Gửi tới đích cá nhân của người nhận
        messagingTemplate.convertAndSendToUser(
                receiverUsername, "/queue/messages",
                newmessage
        );
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors") // Gửi tin nhắn lỗi CHỈ cho người dùng đã gửi
    public String handleException(MethodArgumentNotValidException exception) {

        // Trả về một tin nhắn lỗi thân thiện cho client
        return "Lỗi: Nội dung tin nhắn không hợp lệ hoặc bị rỗng.";
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
