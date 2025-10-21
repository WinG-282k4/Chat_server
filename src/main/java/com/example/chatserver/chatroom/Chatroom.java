package com.example.chatserver.chatroom;

import com.example.chatserver.Chatroomparticipant.ChatroomParticipant;
import com.example.chatserver.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chatroom")
public class Chatroom {

    @Id
    private Long chatroomId;

    @Column(name = "sender_id")
    private Long senderId; // User ID of the sender

    @Column(name = "receiver_id")
    private Long receiverId; // User ID of the receiver

    @Column(name = "created_by")
    private Long createdById; // User ID of the creator

    @Column(name = "created_at")
    private long createdAt; // Timestamp of creation
    @Column(name = "is_active")
    private boolean isActive; // Indicates if the chatroom is active or archived
    @Column(name = "profile_picture_url")
    private String profilePictureUrl; // URL for the chatroom's profile picture
    @Column(name = "last_message")
    private String lastMessage; // Last message sent in the chatroom

    //Thiết lập quan hệ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "userId", insertable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "userId", insertable = false, updatable = false)
    private User receiver;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "userId", insertable = false, updatable = false)
    private User createdBy; // User ID of the creator

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatroomParticipant> participants = new HashSet<>();

}
