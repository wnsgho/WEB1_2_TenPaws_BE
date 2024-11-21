package com.example.tenpaws.domain.chatroom.repository;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChatRoomRepositoryTests {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeAll
    public static void setUp(@Autowired ChatRoomRepository chatRoomRepository) {
        for (long i = 1L; i <= 2L; i++) {
            for (long j = 1L; j <= 2L; j++) {
                ChatRoom chatRoom = ChatRoom.builder()
                        .userId(i)
                        .shelterId(j)
                        .build();
                chatRoomRepository.save(chatRoom);
            }
        }
    }

    @AfterAll
    public static void tearDown(@Autowired ChatRoomRepository chatRoomRepository) {
        chatRoomRepository.deleteAll(chatRoomRepository.findAll());
    }

    @Test
    void findByUserIdAndShelterId() {
        Long userId = 1L;
        Long shelterId = 1L;

        ChatRoom chatRoom = chatRoomRepository.findByUserIdAndShelterId(userId, shelterId);

        assertEquals(1L, chatRoom.getUserId());
    }

    @Test
    void findByUserId() {
        Long userId = 1L;

        List<ChatRoom> chatRoomList = chatRoomRepository.findByUserId(userId);

        assertEquals(2, chatRoomList.size());
    }

    @Test
    void findByShelterID() {
        Long shelterId = 1L;

        List<ChatRoom> chatRoomList = chatRoomRepository.findByShelterId(shelterId);

        assertEquals(2, chatRoomList.size());
    }
}
