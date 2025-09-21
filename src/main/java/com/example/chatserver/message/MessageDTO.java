package com.example.chatserver.message;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
public class MessageDTO {

    private Long messageId;
    private Long senderID;
    private Long receiverID;
    private String content;
    private long timestamp;
    private Long chatRoomID;
    private String type;
    private String status;
    private Long replyToMessageId;
    private long likeCount;
}
