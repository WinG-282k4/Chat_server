package com.example.chatserver.message;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    // This class will contain business logic related to messages
    // For example, methods to create, update, delete, and retrieve messages

    // Example method to create a new message
    public Messages createMessage(Messages message) {
         // Logic to save the message to the database
         return messageRepository.save(message);
     }

//    Example method to get all messages
     public List<Messages> getAllMessages() {
         return messageRepository.findAll();
     }

    // Add more methods as needed for your application

}
