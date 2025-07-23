package com.example.chatserver.message;

import java.util.List;
import java.util.Optional;

import com.example.chatserver.common.ApiResponse;
import com.example.chatserver.user.UserRepository;
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
        List<Messages> messages = messageRepo.findAll();
        List<MessageDTO> messageDTOs = messages.stream()
                .map(Mapper::toDTO)
                .toList();
        return new ApiResponse("success", "Get all messages success", messageDTOs);
    }

    // GET message theo ID
    @GetMapping("/{id}")
    public ApiResponse getMessage(@PathVariable Long id) {
        List<Messages> message = messageRepo.findById(id)
                .map(List::of)
                .orElse(List.of());
        List<MessageDTO> messageDTOs = message.stream()
                .map(Mapper::toDTO)
                .toList();
        return new ApiResponse("success", "Get message success", messageDTOs);
    }


    // POST tạo message mới
    @PostMapping
    public ApiResponse createMessage(@RequestBody MessageDTO message) {

        return null;
    }

    // PUT cập nhật message theo ID
    @PutMapping("/{id}")
    public ApiResponse updateMessage(
            @PathVariable Long id,
            @RequestBody MessageDTO newMessage
    ) {
        try {
            // Kiểm tra xem message có tồn tại không
            Messages updateMessage = messageRepo.findById(id).orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
            // mapping partial update trực tiếp
            Optional.ofNullable(newMessage.getContent()).ifPresent(updateMessage::setContent);
            Optional.ofNullable(newMessage.getContent()).ifPresent(updateMessage::setContent);
            Optional.ofNullable(newMessage.getType()).ifPresent(updateMessage::setType);
            Optional.ofNullable(newMessage.getStatus()).ifPresent(updateMessage::setStatus);

            return new ApiResponse("success", "Update message success", Mapper.toDTO(messageService.save(updateMessage)));
        }catch (Exception e){
            return new ApiResponse("error", "Update message failed: " + e.getMessage(), null);
        }
    }

    // DELETE message theo ID
    @DeleteMapping("/{id}")
    public ApiResponse deleteMessage(@PathVariable Long id) {
        messageRepo.deleteById(id);
        return new ApiResponse("success", "Delete message success", null);
    }
}
