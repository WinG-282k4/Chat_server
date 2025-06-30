package com.example.chatserver.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

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

    // GET tất cả message
    @GetMapping
    public List<Messages> getAllMessages() {
        return messageRepo.findAll();
    }

    // GET message theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Messages> getMessageById(@PathVariable Long id) {
        return messageRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // POST tạo message mới
    @PostMapping
    public Messages createMessage(@RequestBody Messages message) {
        return messageRepo.save(message);
    }

    // PUT cập nhật message theo ID
    @PutMapping("/{id}")
    public Messages updateMessage(
            @PathVariable Long id,
            @RequestBody Messages newMessage
    ) {
        return messageRepo.findById(id)
                .map(msg -> {
                    msg.setContent(newMessage.getContent());
                    msg.setTimestamp(newMessage.getTimestamp());
                    msg.setType(newMessage.getType());
                    msg.setStatus(newMessage.getStatus());
                    msg.setLastMessage(newMessage.getLastMessage());
                    // set thêm các trường khác nếu cần
                    return messageRepo.save(msg);
                })
                .orElse(null);
    }

    // DELETE message theo ID
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageRepo.deleteById(id);
    }
}
