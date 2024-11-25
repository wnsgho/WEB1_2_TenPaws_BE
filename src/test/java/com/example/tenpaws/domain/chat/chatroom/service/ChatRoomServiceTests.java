package com.example.tenpaws.domain.chat.chatroom.service;

import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomRequest;
import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomResponse;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import com.example.tenpaws.global.exception.BaseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatRoomServiceTests {
    @Autowired
    private ChatRoomService chatRoomService;

    @BeforeAll
    public static void setUp(@Autowired ChatRoomRepository chatRoomRepository) {
        IntStream.rangeClosed(1, 3).forEach(i -> {
            ChatRoom chatRoom = ChatRoom.builder()
                    .user1("user1")
                    .user2("shelter" + i)
                    .build();
            chatRoomRepository.save(chatRoom);
        });
    }

    @AfterAll
    public static void tearDown(@Autowired ChatRoomRepository chatRoomRepository) {
        chatRoomRepository.deleteAll(chatRoomRepository.findAll());
    }

    @Test
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
    void getChatRoomByUsers() {
        String user1 = "user1";
        String user2 = "shelter1";

        ChatRoomResponse chatRoomResponse = chatRoomService.getChatRoomByUsers(user1, user2);

        assertNotNull(chatRoomResponse);
    }

    @Test
    void getChatRoomsByUser() {
        String user = "user1";

        List<ChatRoomResponse> chatRoomResponseList = chatRoomService.getChatRoomsByUser(user);

        assertEquals(3, chatRoomResponseList.size());
    }
}
