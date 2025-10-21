package com.example.chatserver.message;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.chatroom.ChatroomService;
import com.example.chatserver.user.User;
import com.example.chatserver.user.UserRepository;
import com.example.chatserver.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final MessageMapper messageConvecter;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ChatroomService chatroomService;

    // This class will contain business logic related to messages
    // For example, methods to create, update, delete, and retrieve messages

    public Optional<String> getMessageBySenderAndReceiver(
            Long senderId,
            Long receiverId
    ) {
        return messageRepository.findContentBySenderIdAndReceiverId(senderId, receiverId);
    }
    @Transactional
    public Messages save(Messages msg) {

        //SetChatRoom ID
        if (msg.getChatroom() == null && msg.getReceiver() != null) {
            Long chatroomId = chatroomService.getChatroomId(
                    msg.getSender().getUserId(),
                    msg.getReceiver().getUserId(),
                    true
            ).orElseThrow(() -> new RuntimeException("Failed to get or create chatroom"));
            msg.setChatroomId(chatroomId);
        }
        // Set the timestamp
        msg.setTimestamp(System.currentTimeMillis());
        return messageRepository.save(msg);
    }

    @Transactional
    public MessageDTO CreateMessage(MessageDTO message) {

        Messages msg = messageConvecter.toEntity(message);
        msg.setStatus("SENT");

        msg = save(msg);

        return MessageDTO.builder()
                .messageId(msg.getMessageId())
                .senderId(msg.getSenderId())
                .receiverId(msg.getReceiverId())
                .chatroomId(msg.getChatroomId())
                .content(msg.getContent())
                .timestamp(msg.getTimestamp())
                .build();
    }

    @Transactional
    public List<MessageDTO> findAllMessages(
            Long senderId,
            Long receiverId
    ){
        var ChatroomId = chatroomService.getChatroomId(
                senderId,
                receiverId,
                false
        ).orElseThrow(() -> new RuntimeException("Chatroom not found"));
        var messages = messageRepository.findAllBychatroomId(ChatroomId);
        return messageConvecter.toDTOs(messages);
    }

    @Transactional
    public MessageDTO updateMessage(MessageDTO newMessage) {
        Messages existingMessage = messageRepository.findById(newMessage.getMessageId())
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Update fields
        if (newMessage.getContent() != null) {
            existingMessage.setContent(newMessage.getContent());
        }

        // Save changes
        Messages updatedMessage = save(existingMessage);

        // Convert to DTO
        return MessageDTO.builder()
                .messageId(newMessage.getMessageId())
                .senderId(updatedMessage.getSenderId())
                .receiverId(updatedMessage.getReceiverId())
                .chatroomId(updatedMessage.getChatroomId())
                .content(updatedMessage.getContent())
                .timestamp(updatedMessage.getTimestamp())
                .build();
    }

    public void DeleteMessage(Long id, String username) {

    }
}
