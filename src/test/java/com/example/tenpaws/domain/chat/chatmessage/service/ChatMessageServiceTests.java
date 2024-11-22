package com.example.tenpaws.domain.chat.chatmessage.service;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageRequest;
import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageResponse;
import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import com.example.tenpaws.domain.chat.chatmessage.repository.ChatMessageRepository;
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
public class ChatMessageServiceTests {
    @Autowired
    private ChatMessageService chatMessageService;

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
    void createChatMessage() {
        ChatMessageRequest chatMessageRequest = ChatMessageRequest.builder()
                .message("test message")
                .sender("user")
//                .receiver("shelter")
                .chatRoomId(1L)
                .build();

        ChatMessageResponse chatMessageResponse = chatMessageService.createChatMessage(chatMessageRequest);

        assertEquals("test message", chatMessageResponse.getMessage());
    }

    @Test
    void getChatMessagesByChatRoomId() {
        Long chatRoomId = 1L;

        List<ChatMessageResponse> chatMessageResponseList = chatMessageService.getChatMessagesByChatRoomId(chatRoomId);

        assertEquals(3, chatMessageResponseList.size());
    }
}
