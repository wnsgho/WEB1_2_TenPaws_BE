package com.example.tenpaws.domain.chat.chatroom.repository;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select cr from ChatRoom cr where (cr.user1 =: user1 and cr.user2 = :user2) or (cr.user1 =: user2 and cr.user2 = :user1)")
    Optional<ChatRoom> findByUsers(String user1, String user2);

    @Query("select cr from ChatRoom cr where cr.user1 = :user or cr.user2 = :user order by cr.id asc")
    List<ChatRoom> findByUser(String user);
}
