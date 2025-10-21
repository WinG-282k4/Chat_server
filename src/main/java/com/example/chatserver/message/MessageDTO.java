package com.example.chatserver.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private long timestamp;
    private Long chatroomId;
    private String type;
    private String status;
    private Long replyToMessageId;
    private long likeCount;
}
