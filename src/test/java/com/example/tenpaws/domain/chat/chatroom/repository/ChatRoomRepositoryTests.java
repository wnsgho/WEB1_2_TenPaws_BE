package com.example.tenpaws.domain.chat.chatroom.repository;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChatRoomRepositoryTests {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

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
    void findByUsers() {
        String user1 = "user1";
        String user2 = "shelter1";

        ChatRoom chatRoom = chatRoomRepository.findByUsers(user1, user2).get();

        assertEquals(1L, chatRoom.getId());
    }

    @Test
    void findByUser() {
        String user = "user1";

        List<ChatRoom> chatRoomList = chatRoomRepository.findByUser(user);

        assertEquals(3, chatRoomList.size());
    }
}
