package com.example.chatserver.chatroom;

import com.example.chatserver.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    @Autowired
    private final ChatroomRepository chatroomRepository;

    // This class will contain business logic related to chatrooms
    // For example, methods to create, update, delete, and retrieve chatrooms
    public Optional<Long> getChatroomId(
            Long SenderId,
            Long ReceiverId,
            boolean createIfNotExist
    ) {
        return  chatroomRepository.findChatroomIdBySenderIdAndReceiverId(SenderId, ReceiverId)
                .map(chatroom -> chatroom.getChatroomId())
                .or(() -> {
                    if(createIfNotExist) {
                        var chatId = createChatroom(SenderId, ReceiverId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private Long createChatroom(Long senderId, Long receiverId) {
        var chatId = String.format("%s_%s_%s", senderId, receiverId, System.currentTimeMillis());
        String numericChatId = chatId.replace("_", "");
        Long chatIdlong = Long.parseLong(numericChatId.substring(0, Math.min(18, numericChatId.length())));

        Chatroom senderRiceiverChatroom = Chatroom.builder()
                .chatroomId(chatIdlong) // Giới hạn độ dài ID
                .senderId(senderId)
                .receiverId(receiverId)
                .createdById(null)
                .createdAt(System.currentTimeMillis())
                .isActive(true)
                .build();

        Chatroom ReceiverSenderChatroom = Chatroom.builder()
                .chatroomId(chatIdlong) // Giới hạn độ dài ID
                .senderId(receiverId)
                .receiverId(senderId)
                .createdById(null)
                .createdAt(System.currentTimeMillis())
                .isActive(true)
                .build();

        chatroomRepository.save(senderRiceiverChatroom);
        chatroomRepository.save(ReceiverSenderChatroom);

        return chatIdlong;
    }
}
