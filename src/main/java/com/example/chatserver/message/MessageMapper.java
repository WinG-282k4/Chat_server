package com.example.chatserver.message;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MessageMapper {

//    @Mapping(source = "sender.userId", target = "senderID")
//    @Mapping(source = "receiver.userId", target = "receiverID")
//    @Mapping(source = "chatRoom.chatroomId", target = "chatRoomID")
//    @Mapping(source = "replyToMessage.messageId", target = "replyToMessageId")
    MessageDTO toDTO(Messages message);

//    @Mapping(source = "senderID", target = "sender", qualifiedByName = "idToUser")
//    @Mapping(source = "receiverID", target = "receiver", qualifiedByName = "idToUser")
//    @Mapping(source = "chatRoomID", target = "chatRoom", qualifiedByName = "idToChatroom")
//    @Mapping(source = "replyToMessageId", target = "replyToMessage", qualifiedByName = "idToMessage")
    Messages toEntity(MessageDTO dto);

    // Mapping ID -> entity
    @Named("idToUser")
    default User idToUser(Long id) {
        if (id == null) return null;
        User u = new User();
        u.setUserId(id);
        return u;
    }

    @Named("idToChatroom")
    default Chatroom idToChatroom(Long id) {
        if (id == null) return null;
        Chatroom c = new Chatroom();
        c.setChatroomId(id);
        return c;
    }

    @Named("idToMessage")
    default Messages idToMessage(Long id) {
        if (id == null) return null;
        return new Messages(id);
    }
}
