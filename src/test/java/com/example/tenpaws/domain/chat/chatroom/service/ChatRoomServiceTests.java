package com.example.tenpaws.domain.chat.chatroom.service;

import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomRequest;
import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomResponse;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import com.example.tenpaws.global.exception.BaseException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatRoomServiceTests {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    Long savedId;

    @BeforeEach
    void setUp() {
        IntStream.rangeClosed(1, 3).forEach(i -> {
            ChatRoom chatRoom = ChatRoom.builder()
                    .user1("user1")
                    .user2("shelter" + i)
                    .build();
            ChatRoom saved = chatRoomRepository.save(chatRoom);
            savedId = saved.getId();
        });
    }

    @AfterEach
    void tearDown() {
        chatRoomRepository.deleteAll();
    }

    @Test
    @Order(5)
    @Transactional
    void create() {
        ChatRoomRequest chatRoomRequest = ChatRoomRequest.builder()
                .user1("user")
                .user2("shelter")
                .build();

        ChatRoomResponse chatRoomResponse = chatRoomService.create(chatRoomRequest);

        assertNotNull(chatRoomResponse);
    }

    @Test
    @Order(2)
    void getChatRoom() {
        Long chatRoomId = savedId;

        ChatRoomResponse chatRoomResponse = chatRoomService.getChatRoom(chatRoomId);

        assertNotNull(chatRoomResponse);
    }

    @Test
    @Order(3)
    @Transactional
    void delete() {
        Long chatRoomId = savedId;

        chatRoomService.delete(chatRoomId);

        assertThrows(BaseException.class, () -> chatRoomService.getChatRoom(chatRoomId));
    }

    @Test
    @Order(1)
    void getChatRoomByUsers() {
        String user1 = "user1";
        String user2 = "shelter1";

        assertNotNull(chatRoomService.getChatRoomByUsers(user1, user2));
    }

    @Test
    void getChatRoomsByUser() {
        String user = "user1";

        List<ChatRoomResponse> chatRoomResponseList = chatRoomService.getChatRoomsByUser(user);

        assertFalse(chatRoomResponseList.isEmpty());
    }
}
