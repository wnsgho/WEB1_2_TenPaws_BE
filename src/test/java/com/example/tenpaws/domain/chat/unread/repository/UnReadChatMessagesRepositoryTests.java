package com.example.tenpaws.domain.chat.unread.repository;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import com.example.tenpaws.domain.chat.unread.entity.UnReadChatMessages;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class UnReadChatMessagesRepositoryTests {
    @Autowired
    private UnReadChatMessagesRepository unReadChatMessagesRepository;

    Long savedId;

    @BeforeEach
    void setUp(@Autowired ChatRoomRepository chatRoomRepository) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .user1("user1")
                .user2("user2")
                .build());
        savedId = chatRoom.getId();
        unReadChatMessagesRepository.save(UnReadChatMessages.builder()
                .chatroom(chatRoom)
                .username("user1")
                .unReadCount(2)
                .build());
    }

    @AfterEach
    void tearDown(@Autowired ChatRoomRepository chatRoomRepository) {
        chatRoomRepository.deleteAll();
    }

    @Test
    void testFindByUsername() {
        String username1 = "user1";

        List<UnReadChatMessages> unReadChatMessagesList = unReadChatMessagesRepository.findBydUsername(username1);
        Assertions.assertEquals(1, unReadChatMessagesList.size());
    }

    @Test
    void testFindByChatRoomIdAndUsername() {
        Long chatRoomId = savedId;
        String username1 = "user1";

        UnReadChatMessages unReadChatMessages = unReadChatMessagesRepository.findByChatRoomIdAndUsername(chatRoomId, username1).get();
        Assertions.assertEquals(2, unReadChatMessages.getUnReadCount());
    }
}
