package com.example.chatserver.message;

import java.util.List;
import java.util.Optional;

import com.example.chatserver.common.ApiResponse;
import com.example.chatserver.security.UserPrincipal;
import com.example.chatserver.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    private MessageRepository messageRepo;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageMapper Mapper;

    // GET tất cả message
    @GetMapping
    public ApiResponse getAllMessages() {
        return null;
    }

    // GET message theo ID
    @GetMapping("/{id}")
    public ApiResponse getMessage(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return null;
    }


    // POST tạo message mới
    @MessageMapping("/chat.sendMessage") // Lắng nghe trên destination WebSocket
    @SendTo("/topic/public") // Gửi kết quả đến topic này
    public MessageDTO sendMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Payload MessageDTO chatMessage
    ) {
        // 1. Lấy thông tin người gửi từ principal đã được xác thực
        chatMessage.setSendername(userPrincipal.getUsername());

        // 2. Gọi service để LƯU tin nhắn vào database
        // Phương thức CreateMessage sẽ trả về tin nhắn đã được lưu (có thể có thêm id, timestamp...)
        MessageDTO savedMessage = messageService.CreateMessage(chatMessage);

        // 3. Return tin nhắn đã lưu. @SendTo sẽ lấy tin nhắn này và phát cho mọi người
        return savedMessage;
    }

    // PUT cập nhật message theo ID
    @PutMapping("/{id}") // ID của message cần cập nhật
    public ApiResponse updateMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @RequestBody MessageDTO newMessage
    ) {
        newMessage.setSendername(userPrincipal.getUsername());
        newMessage.setMessageId(id);
        System.out.println("message from request: " + newMessage);
        try {
            MessageDTO updatedMessage = messageService.updateMessage(newMessage);
            return new ApiResponse("success", "Update message success", updatedMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
//            return new ApiResponse("error", "Update message failed", e.getMessage());
        }
    }

    // DELETE message theo ID
    @DeleteMapping("/{id}")
    public ApiResponse deleteMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id
    ) {
        messageService.DeleteMessage(id, userPrincipal.getUsername());
        return new ApiResponse("success", "Delete message success", null);
    }

}
