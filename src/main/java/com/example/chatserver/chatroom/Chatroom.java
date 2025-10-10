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
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long chatroomId;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "userId")
    private User createdBy; // User ID of the creator

    @Column(name = "created_at")
    private long createdAt; // Timestamp of creation
    @Column(name = "is_active")
    private boolean isActive; // Indicates if the chatroom is active or archived
    @Column(name = "profile_picture_url")
    private String profilePictureUrl; // URL for the chatroom's profile picture
    @Column(name = "last_message")
    private String lastMessage; // Last message sent in the chatroom

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatroomParticipant> participants = new HashSet<>();

}
