package com.example.chatserver.message;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.chatroom.ChatroomRepository; // Giả sử bạn có repository này
import com.example.chatserver.user.User;
import com.example.chatserver.user.UserRepository; // Giả sử bạn có repository này
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class MessageMapper {

    // --- Dependency Injection ---
    // Sử dụng constructor injection (cách làm tốt nhất)
    // để inject các repository cần thiết cho việc chuyển đổi.
    protected UserRepository userRepository;
    protected ChatroomRepository chatroomRepository;
    protected MessageRepository messageRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setChatroomRepository(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // --- Chuyển đổi từ Entity sang DTO ---
    // Dùng để hiển thị dữ liệu ra cho client
    @Mapping(source = "sender.username", target = "sendername") // Giả sử User entity có trường 'username'
    @Mapping(source = "receiver.userId", target = "receiverId")
    @Mapping(source = "chatRoom.chatroomId", target = "chatRoomId")
    @Mapping(source = "replyToMessage.messageId", target = "replyToMessageId")
    public abstract MessageDTO toDto(Messages entity);

    public abstract List<MessageDTO> toDtoList(List<Messages> entities);

    // --- Chuyển đổi từ DTO sang Entity ---
    // Dùng khi nhận dữ liệu từ client để lưu vào database
    @Mapping(source = "sendername", target = "sender", qualifiedByName = "usernameToUserEntity")
    @Mapping(source = "receiverId", target = "receiver", qualifiedByName = "idToUserEntity")
    @Mapping(source = "chatRoomId", target = "chatRoom", qualifiedByName = "idToChatroomEntity")
    @Mapping(source = "replyToMessageId", target = "replyToMessage", qualifiedByName = "idToMessageEntity")
    @Mapping(target = "interactions", ignore = true) // Bỏ qua trường này khi mapping từ DTO
    public abstract Messages toEntity(MessageDTO dto);


    // --- Các phương thức Helper (dùng bởi qualifiedByName) ---

    @Named("usernameToUserEntity")
    protected User usernameToUserEntity(String username) {
        if (username == null) {
            return null;
        }
        // Cần có phương thức findByUsername trong UserRepository
        return userRepository.findByUsername(username).orElse(null);
    }

    @Named("idToUserEntity")
    protected User idToUserEntity(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.getReferenceById(id);
    }

    @Named("idToChatroomEntity")
    protected Chatroom idToChatroomEntity(Long id) {
        if (id == null) {
            return null;
        }
        return chatroomRepository.getReferenceById(id);
    }

    @Named("idToMessageEntity")
    protected Messages idToMessageEntity(Long id) {
        if (id == null) {
            return null;
        }
        return messageRepository.getReferenceById(id);
    }
}