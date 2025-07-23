package com.example.chatserver.message;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageConvecter;

    // This class will contain business logic related to messages
    // For example, methods to create, update, delete, and retrieve messages

    @Transactional
    public Messages save(Messages msg) {
        // Set the timestamp
        msg.setTimestamp(System.currentTimeMillis());
        return messageRepository.save(msg);
    }

}
