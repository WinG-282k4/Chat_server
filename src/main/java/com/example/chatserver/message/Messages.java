package com.example.chatserver.message;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.message_interaction.MessageInteraction;
import com.example.chatserver.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@Data // Lưu ý: Dùng @Data với Entity JPA có thể gây ra lỗi. Cân nhắc dùng @Getter, @Setter, @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    // --- CÁC TRƯỜNG ID (DÙNG ĐỂ GHI) ---
    // JPA sẽ dùng các trường này để INSERT và UPDATE

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "chat_room_id")
    private Long chatroomId;

    @Column(name = "reply_to_message_id")
    private Long replyToMessageId;

    // --- CÁC TRƯỜNG KHÁC ---
    @Column(name = "content", nullable = false)
    private String content;
    private long timestamp;
    private String type;
    private String status;
    private long likeCount;

    // --- CÁC MỐI QUAN HỆ (DÙNG ĐỂ ĐỌC/QUERY) ---
    // để báo JPA không dùng các trường này khi GHI

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "userId", insertable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "userId", insertable = false, updatable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chatroomId", insertable = false, updatable = false)
    private Chatroom chatroom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_message_id", referencedColumnName = "messageId", insertable = false, updatable = false)
    private Messages replyToMessage;

    // Quan hệ này là đúng vì 'mappedBy' chỉ định rằng 'MessageInteraction'
    // mới là bên sở hữu quan hệ.
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageInteraction> interactions = new ArrayList<>();
}