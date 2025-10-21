package com.example.chatserver.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Chatroom getReferenceById(Long id);

    Optional<Chatroom> findChatroomIdBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
