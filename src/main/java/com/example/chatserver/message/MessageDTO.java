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
    private String sendername;
    private Long receiverId;
    private String content;
    private long timestamp;
    private Long chatRoomId;
    private String type;
    private String status;
    private Long replyToMessageId;
    private long likeCount;
}
