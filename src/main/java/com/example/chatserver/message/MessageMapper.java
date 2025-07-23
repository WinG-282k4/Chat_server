package com.example.chatserver.message;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "sender.userId", target = "senderID")
    @Mapping(source = "receiver.userId", target = "receiverID")
    @Mapping(source = "chatRoom.chatroomId", target = "chatRoomID")
    @Mapping(source = "replyToMessage.messageId", target = "replyToMessageId")
    MessageDTO toDTO(Messages message);

    @Mapping(source = "senderID", target = "sender.userId")
    @Mapping(source = "receiverID", target = "receiver.userId")
    @Mapping(source = "chatRoomID", target = "chatRoom.chatroomId")
    @Mapping(source = "replyToMessageId", target = "replyToMessage.messageId")
    Messages toEntity(MessageDTO dto);

    // Các phương thức khác nếu cần thiết
}
//    public MessageDTO toDTO(Messages message) {
//        if (message == null) {
//            return null;
//        }
//
//        MessageDTO dto = new MessageDTO();
//        dto.setMessageId(message.getMessageId());
//        dto.setContent(message.getContent());
//        dto.setTimestamp(message.getTimestamp());
//        dto.setType(message.getType());
//        dto.setStatus(message.getStatus());
//
//        // Chuyển đổi User thành ID
//        if (message.getSender() != null) {
//            dto.setSenderUserID(message.getSender().getUserId());
//        }
//
//        if (message.getReceiver() != null) {
//            dto.setReceiverUserID(message.getReceiver().getUserId());
//        }
//
//        // Chuyển đổi Chatroom thành ID nếu có
//        if (message.getChatRoom() != null) {
//            dto.setChatRoomID(message.getChatRoom().getChatroomId());
//        }
//
//        // Lấy ID của message mà nó đang trả lời (nếu có)
//        if (message.getReplyToMessage() != null) {
//            dto.setReplyToMessageId(message.getReplyToMessage().getMessageId());
//        }
//
//        // Đếm số lượt "like"
//        dto.setLikeCount((int) message.countByType("like"));
//
//        return dto;
//    }
//
//    public Messages toEntity(MessageDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        Messages message = new Messages();
//        message.setMessageId(dto.getMessageId());
//        message.setContent(dto.getContent());
//        message.setTimestamp(dto.getTimestamp());
//        message.setType(dto.getType());
//        message.setStatus(dto.getStatus());
//
//        // Chuyển đổi ID thành User nếu có
//        if (dto.getSenderUserID() != null) {
//            User sender = new User();
//            sender.setUserId(dto.getSenderUserID());
//            message.setSender(sender);
//        }
//
//        if (dto.getReceiverUserID() != null) {
//            User receiver = new User();
//            receiver.setUserId(dto.getReceiverUserID());
//            message.setReceiver(receiver);
//        }
//
//        // Chuyển đổi ID thành Chatroom nếu có
//        if (dto.getChatRoomID() != null) {
//            Chatroom chatRoom = new Chatroom();
//            chatRoom.setChatroomId(dto.getChatRoomID());
//            message.setChatRoom(chatRoom);
//        }
//
//        //Lấy ID reply
//        if (dto.getReplyToMessageId() != null) {
//            Messages replyMessage = new Messages();
//            replyMessage.setMessageId(dto.getReplyToMessageId());
//            message.setReplyToMessage(replyMessage);
//        }
//
//        return message;
//    }
