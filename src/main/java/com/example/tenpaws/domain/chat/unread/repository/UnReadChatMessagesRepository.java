package com.example.tenpaws.domain.chat.unread.repository;

import com.example.tenpaws.domain.chat.unread.entity.UnReadChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnReadChatMessagesRepository extends JpaRepository<UnReadChatMessages, Long> {
    @Query("select uc from UnReadChatMessages uc where uc.username = :username")
    List<UnReadChatMessages> findBydUsername(String username);

    @Query("select uc from UnReadChatMessages uc where uc.chatroom.id = :chatRoomId and uc.username = :username")
    Optional<UnReadChatMessages> findByChatRoomIdAndUsername(Long chatRoomId, String username);
}
