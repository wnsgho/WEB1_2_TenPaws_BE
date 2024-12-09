package com.example.tenpaws.domain.chat.chatmessage.repository;

import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChatMessageRepositoryTests {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    Long savedId;

    @BeforeEach
    void setUp(@Autowired ChatRoomRepository chatRoomRepository) {
        ChatRoom chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .user1("user1")
                        .user2("user2")
                        .build());
        savedId = chatRoom.getId();
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("user first chat")
                        .sender("user")
                        .chatRoom(chatRoom)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("shelter first chat")
                        .sender("shelter")
                        .chatRoom(chatRoom)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("user second chat")
                        .sender("user")
                        .chatRoom(chatRoom)
                        .build());
    }

    @AfterEach
    void tearDown(@Autowired ChatRoomRepository chatRoomRepository) {
        chatRoomRepository.deleteAll();
    }

    @Test
    @Transactional
    void findByChatRoomId() {
        Long chatRoomId = savedId;

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(chatRoomId);

        assertEquals(3, chatMessageList.size());
    }
}
