package com.example.tenpaws.domain.chat.unread.repository;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import com.example.tenpaws.domain.chat.unread.entity.UnReadChatMessages;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    static void setUpBeforeClass(@Autowired ChatRoomRepository chatRoomRepository, @Autowired UnReadChatMessagesRepository unReadChatMessagesRepository) throws Exception {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .user1("user1")
                .user2("user2")
                .build());
        unReadChatMessagesRepository.save(UnReadChatMessages.builder()
                .chatroom(chatRoom)
                .username("user1")
                .unReadCount(2)
                .build());
    }

    @AfterAll
    static void tearDownAfterClass(@Autowired ChatRoomRepository chatRoomRepository, @Autowired UnReadChatMessagesRepository unReadChatMessagesRepository) throws Exception {
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
        Long chatRoomId = 1L;
        String username1 = "user1";

        UnReadChatMessages unReadChatMessages = unReadChatMessagesRepository.findByChatRoomIdAndUsername(chatRoomId, username1);
        Assertions.assertEquals(2, unReadChatMessages.getUnReadCount());
    }
}
