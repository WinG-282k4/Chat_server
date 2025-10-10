package com.example.chatserver.message;

import com.example.chatserver.user.User;
import com.example.chatserver.user.UserRepository;
import com.example.chatserver.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Transactional
    public MessageDTO CreateMessage(MessageDTO message) {

        User sender = userRepository.findByUsername(message.getSendername())
                .orElseThrow(() -> new UsernameNotFoundException("Sender not found"));
        Messages msg = messageConvecter.toEntity(message);
        msg.setSender(sender);
        msg.setStatus("SENT");

        msg = save(msg);

        return MessageDTO.builder()
                .messageId(msg.getMessageId())
                .sendername(msg.getSender().getName())
                .content(msg.getContent())
                .receiverId(msg.getChatRoom() == null && msg.getReceiver() != null ? msg.getReceiver().getUserId() : null)
                .chatRoomId(msg.getChatRoom() != null ? msg.getChatRoom().getChatroomId() : null)
                .timestamp(msg.getTimestamp())
                .type(msg.getType())
                .status(msg.getStatus())
                .build();
    }

    @Transactional
    public MessageDTO updateMessage(MessageDTO newMessage) {
        Messages existingMessage = messageRepository.findById(newMessage.getMessageId())
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Check sender
        if (newMessage.getSendername() == null ||
                !newMessage.getSendername().equals(existingMessage.getSender().getUsername())) {
            throw new RuntimeException("You are not the sender of this message");
        }

        // Update fields
        if (newMessage.getContent() != null) {
            existingMessage.setContent(newMessage.getContent());
        }

        // Save changes
        Messages updatedMessage = save(existingMessage);

        // Convert to DTO
        return MessageDTO.builder()
                .messageId(updatedMessage.getMessageId())
                .sendername(updatedMessage.getSender().getName())
                .content(updatedMessage.getContent())
                .receiverId(updatedMessage.getChatRoom() == null && updatedMessage.getReceiver() != null
                        ? updatedMessage.getReceiver().getUserId() : null)
                .chatRoomId(updatedMessage.getChatRoom() != null
                        ? updatedMessage.getChatRoom().getChatroomId() : null)
                .timestamp(updatedMessage.getTimestamp())
                .type(updatedMessage.getType())
                .status(updatedMessage.getStatus())
                .build();
    }

    public void DeleteMessage(Long id, String username) {

    }
}
