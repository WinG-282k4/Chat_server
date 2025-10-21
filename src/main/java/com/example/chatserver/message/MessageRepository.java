package com.example.chatserver.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long>{

    Optional<String> findContentBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<Messages> findAllBychatroomId(Long chatroomId);
}
