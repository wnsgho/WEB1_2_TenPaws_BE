package com.example.tenpaws.domain.chat.chatmessage.service;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageRequest;
import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageResponse;
import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import com.example.tenpaws.domain.chat.chatmessage.repository.ChatMessageRepository;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import com.example.tenpaws.global.entity.UserRole;
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
        chatRoomRepository.save(
                ChatRoom.builder()
                        .userId(1L)
                        .shelterId(1L)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("user first chat")
                        .senderId(1L)
                        .senderType(UserRole.ROLE_USER)
                        .chatRoomId(1L)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("shelter first chat")
                        .senderId(1L)
                        .senderType(UserRole.ROLE_SHELTER)
                        .chatRoomId(1L)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("user second chat")
                        .senderId(1L)
                        .senderType(UserRole.ROLE_USER)
                        .chatRoomId(1L)
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
                .senderId(1L)
                .senderType(UserRole.ROLE_USER.name())
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
