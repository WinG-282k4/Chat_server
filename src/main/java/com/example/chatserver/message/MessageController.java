package com.example.chatserver.message;

import java.util.List;
import java.util.Optional;

import com.example.chatserver.common.ApiResponse;
import com.example.chatserver.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ApiResponse getMessage(@PathVariable Long id) {
        return null;
    }


    // POST tạo message mới
    @PostMapping
    public ApiResponse createMessage(HttpServletRequest request, @RequestBody MessageDTO message) {
        String payload = request.getAttribute("jwtPayload") != null ? (String) request.getAttribute("jwtPayload") : null;
        String sub = null;
        if (payload != null) {
            try {
                // Extract 'sub' from payload
                String[] parts = payload.split(",");
                for (String part : parts) {
                    if (part.contains("\"sub\"")) {
                        sub = part.split(":")[1].replaceAll("[\"}]", "").trim();
                        break;
                    }
                }
                System.out.println("Extracted sub: " + sub);
            } catch (Exception e) {
                return new ApiResponse("error", "Invalid payload format", null);
            }
        }
        message.setSendername(sub);
        System.out.println("message from request: " + message);
        return new ApiResponse("success", "Create message success", messageService.CreateMessage(message));
    }

    // PUT cập nhật message theo ID
    @PutMapping("/{id}")
    public ApiResponse updateMessage(
            @PathVariable Long id,
            @RequestBody MessageDTO newMessage
    ) {
        return null;
    }

    // DELETE message theo ID
    @DeleteMapping("/{id}")
    public ApiResponse deleteMessage(@PathVariable Long id) {
        messageRepo.deleteById(id);
        return new ApiResponse("success", "Delete message success", null);
    }
}
