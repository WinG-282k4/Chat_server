package com.example.chatserver.Chatroomparticipant;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.user.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chatroom_participants")
public class ChatroomParticipant {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private  Long ChatroomParticipantId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatroom_id", referencedColumnName = "chatroomId")
    private Chatroom chatroom; // ID of the chatroom

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user; // ID of the user who is a participant in the chatroom

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private role participantRole; // Role of the participant in the chatroom

    public enum role {
        ADMIN,
        MEMBER,
        GUEST
    }
}
