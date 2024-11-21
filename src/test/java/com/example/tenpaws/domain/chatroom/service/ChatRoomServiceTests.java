package com.example.tenpaws.domain.chatroom.service;

import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomRequest;
import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomResponse;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import com.example.tenpaws.domain.chat.chatroom.service.ChatRoomService;
import com.example.tenpaws.global.exception.BaseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatRoomServiceTests {
    @Autowired
    private ChatRoomService chatRoomService;

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
    @Transactional
    void create() {
        ChatRoomRequest chatRoomRequest = ChatRoomRequest.builder()
                .userId(1L)
                .shelterId(3L)
                .build();

        ChatRoomResponse chatRoomResponse = chatRoomService.create(chatRoomRequest);

        assertNotNull(chatRoomResponse);
    }

    @Test
    void getChatRoom() {
        Long chatRoomId = 1L;

        ChatRoomResponse chatRoomResponse = chatRoomService.getChatRoom(chatRoomId);

        assertNotNull(chatRoomResponse);
    }

    @Test
    @Transactional
    void delete() {
        Long chatRoomId = 1L;

        chatRoomService.delete(chatRoomId);

        assertThrows(BaseException.class, () -> chatRoomService.getChatRoom(chatRoomId));
    }

    @Test
    void getChatRoomByUserIdAndShelterId() {
        Long userId = 1L;
        Long shelterId = 1L;

        ChatRoomResponse chatRoomResponse = chatRoomService.getChatRoomByUserIdAndShelterId(userId, shelterId);

        assertNotNull(chatRoomResponse);
    }

    @Test
    void getChatRoomsByUserId() {
        Long userId = 1L;

        List<ChatRoomResponse> chatRoomResponseList = chatRoomService.getChatRoomsByUserId(userId);

        assertEquals(2, chatRoomResponseList.size());
    }

    @Test
    void getChatRoomsByShelterId() {
        Long shelterId = 1L;

        List<ChatRoomResponse> chatRoomResponseList = chatRoomService.getChatRoomsByShelterId(shelterId);

        assertEquals(2, chatRoomResponseList.size());
    }
}
