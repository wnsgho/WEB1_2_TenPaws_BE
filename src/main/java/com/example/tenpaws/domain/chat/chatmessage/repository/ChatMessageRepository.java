package com.example.tenpaws.domain.chat.chatmessage.repository;

import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(Long chatRoomId);
}
