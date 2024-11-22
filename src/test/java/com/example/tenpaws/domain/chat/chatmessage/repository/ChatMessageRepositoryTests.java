package com.example.tenpaws.domain.chat.chatmessage.repository;

import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    static void setUpBeforeClass(@Autowired ChatMessageRepository chatMessageRepository, @Autowired ChatRoomRepository chatRoomRepository) throws Exception {
        ChatRoom chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .user1("user1")
                        .user2("user2")
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("user first chat")
                        .sender("user")
//                        .receiver("shelter")
                        .chatRoom(chatRoom)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("shelter first chat")
                        .sender("shelter")
//                        .receiver("user")
                        .chatRoom(chatRoom)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("user second chat")
                        .sender("user")
//                        .receiver("shelter")
                        .chatRoom(chatRoom)
                        .build());
    }

    @AfterAll
    static void tearDownAfterClass(@Autowired ChatMessageRepository chatMessageRepository, @Autowired ChatRoomRepository chatRoomRepository) throws Exception {
        chatMessageRepository.deleteAll();
        chatRoomRepository.deleteAll();
    }

    @Test
    @Transactional
    void findByChatRoomId() {
        Long chatRoomId = 1L;

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(chatRoomId);

        assertEquals(3, chatMessageList.size());
    }
}
