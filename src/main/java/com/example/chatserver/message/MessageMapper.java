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
public interface MessageMapper {
    @Mapping(source = "chatroomId", target = "chatroom", qualifiedByName = "mapChatroomById")
    @Mapping(source = "senderId", target = "sender", qualifiedByName = "mapUserById")
    Messages toEntity(MessageDTO dto);

    @Mapping(source = "chatroom.chatroomId", target = "chatroomId")
    @Mapping(source = "sender.userId", target = "senderId")
    MessageDTO toDTO(Messages entity);

    List<MessageDTO> toDTOs(List<Messages> entities);

    @Named("mapChatroomById")
    default Chatroom mapChatroomById(Long chatroomId) {
        // Giả sử bạn có ChatroomRepository để tìm Chatroom theo ID
        return chatroomRepository.findById(chatroomId).orElse(null);
    }

    @Named("mapUserById")
    default User mapUserById(Long userId) {
        // Giả sử bạn có UserRepository để tìm User theo ID
        return userRepository.findById(userId).orElse(null);
    }

    @Autowired
    ChatroomRepository chatroomRepository = null;

    @Autowired
    UserRepository userRepository = null;
}