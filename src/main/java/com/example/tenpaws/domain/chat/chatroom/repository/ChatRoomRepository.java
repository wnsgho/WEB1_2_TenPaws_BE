package com.example.tenpaws.domain.chat.chatroom.repository;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUserIdAndShelterId(Long userId, Long shelterId);

    List<ChatRoom> findByUserId(Long userId);

    List<ChatRoom> findByShelterId(Long shelterId);
}
