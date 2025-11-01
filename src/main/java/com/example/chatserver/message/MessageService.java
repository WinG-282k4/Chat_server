package com.example.chatserver.message;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.chatroom.ChatroomRepository;
import com.example.chatserver.chatroom.ChatroomService;
import com.example.chatserver.user.User;
import com.example.chatserver.user.UserRepository;
import com.example.chatserver.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired // <-- 1. ĐẢM BẢO BẠN CÓ DÒNG NÀY
    private final ChatroomRepository chatroomRepository;

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
        if (msg.getChatroom() == null && msg.getReceiverId() != null) {

            // Lấy ID
            Long chatroomId = chatroomService.getChatroomId(
                    msg.getSenderId(),
                    msg.getReceiverId(),
                    true
            ).orElseThrow(() -> new RuntimeException("Failed to get or create chatroom"));

            // Gán ĐỐI TƯỢNG Chatroom
            msg.setChatroomId(chatroomId);
        }

        msg.setTimestamp(System.currentTimeMillis());

        // 5. Bây giờ lệnh save này sẽ THÀNH CÔNG
        return messageRepository.save(msg);
    }

    public MessageDTO CreateMessage(MessageDTO message) {

        Messages msg = messageConvecter.toEntity(message);
        msg.setStatus("SENT");

        msg = save(msg);

        return messageConvecter.toDTO(msg);
    }

    //Find all messages between sender and receiver
    public List<MessageDTO> findAllMessages(
            Long senderId,
            Long receiverId
    ){
        var ChatroomId = chatroomService.getChatroomId(
                senderId,
                receiverId,
                false
        );
        if (ChatroomId.isEmpty()) {
            return new ArrayList<>();
        }

        var messages = messageRepository.findAllBychatroomId(ChatroomId.get());
        return messageConvecter.toDTOs(messages);
    }

    @Transactional
    public ResponseEntity<MessageDTO> updateMessage(MessageDTO newMessage) {
        Messages existingMessage = messageRepository.findById(newMessage.getMessageId())
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Update fields
        if (newMessage.getContent() != null) {
            existingMessage.setContent(newMessage.getContent());
        }

        // Save changes
        Messages updatedMessage = save(existingMessage);

        // Convert to DTO
        return ResponseEntity.ok(MessageDTO.builder()
                .messageId(newMessage.getMessageId())
                .senderId(updatedMessage.getSenderId())
                .receiverId(updatedMessage.getReceiverId())
                .chatroomId(updatedMessage.getChatroomId())
                .content(updatedMessage.getContent())
                .timestamp(updatedMessage.getTimestamp())
                .build()
        );
    }

    public void DeleteMessage(Long id, String username) {

    }
}
