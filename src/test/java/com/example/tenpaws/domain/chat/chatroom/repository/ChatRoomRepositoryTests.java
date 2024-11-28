package com.example.tenpaws.domain.chat.chatroom.repository;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ChatRoomRepositoryTests {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp() {
        IntStream.rangeClosed(1, 3).forEach(i -> {
            ChatRoom chatRoom = ChatRoom.builder()
                    .user1("user1")
                    .user2("shelter" + i)
                    .build();
            chatRoomRepository.save(chatRoom);
        });
    }

    @AfterEach
    void tearDown() {
        chatRoomRepository.deleteAll();
    }

    @Test
    void findByUsers() {
        String user1 = "user1";
        String user2 = "shelter1";

        ChatRoom chatRoom = chatRoomRepository.findByUsers(user1, user2).get();

        assertNotNull(chatRoom);
    }

    @Test
    void findByUser() {
        String user = "user1";

        List<ChatRoom> chatRoomList = chatRoomRepository.findByUser(user);

        assertFalse(chatRoomList.isEmpty());
    }
}
