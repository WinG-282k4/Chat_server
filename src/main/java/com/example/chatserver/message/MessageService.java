package com.example.chatserver.message;

import com.example.chatserver.user.User;
import com.example.chatserver.user.UserRepository;
import com.example.chatserver.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageConvecter;

    @Autowired
    private UserRepository userRepository;

    // This class will contain business logic related to messages
    // For example, methods to create, update, delete, and retrieve messages

    @Transactional
    public Messages save(Messages msg) {
        // Set the timestamp
        msg.setTimestamp(System.currentTimeMillis());
        return messageRepository.save(msg);
    }

    public MessageDTO CreateMessage(MessageDTO message) {

        User sender = userRepository.findByUsername(message.getSendername());
        Messages msg = messageConvecter.toEntity(message);
        msg.setSender(sender);
        msg.setStatus("SENT");

        msg = save(msg);

        return MessageDTO.builder()
                .sendername(msg.getSender().getName())
                .content(msg.getContent())
                .receiverID(msg.getChatRoom() == null && msg.getReceiver() != null ? msg.getReceiver().getUserId() : null)
                .chatRoomID(msg.getChatRoom() != null ? msg.getChatRoom().getChatroomId() : null)
                .timestamp(msg.getTimestamp())
                .type(msg.getType())
                .status(msg.getStatus())
                .build();
    }

    // Other business logic methods can be added here


}
