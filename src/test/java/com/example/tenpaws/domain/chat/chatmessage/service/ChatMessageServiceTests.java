package com.example.tenpaws.domain.chat.chatmessage.service;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageRequest;
import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageResponse;
import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import com.example.tenpaws.domain.chat.chatmessage.repository.ChatMessageRepository;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    Long savedId;

    @BeforeEach
    void setUp(@Autowired ChatMessageRepository chatMessageRepository, @Autowired ChatRoomRepository chatRoomRepository) {
        ChatRoom chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .user1("user1")
                        .user2("user2")
                        .build());
        savedId = chatRoom.getId();
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("user first chat")
                        .sender("user")
                        .chatRoom(chatRoom)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("shelter first chat")
                        .sender("shelter")
                        .chatRoom(chatRoom)
                        .build());
        chatMessageRepository.save(
                ChatMessage.builder()
                        .message("user second chat")
                        .sender("user")
                        .chatRoom(chatRoom)
                        .build());
    }

    @AfterEach
    void tearDown(@Autowired ChatRoomRepository chatRoomRepository) throws Exception {
        chatRoomRepository.deleteAll();
    }

    @Test
    @Transactional
    void createChatMessage() {
        ChatMessageRequest chatMessageRequest = ChatMessageRequest.builder()
                .message("test message")
                .sender("user")
                .chatRoomId(savedId)
                .build();

        ChatMessageResponse chatMessageResponse = chatMessageService.createChatMessage(chatMessageRequest);

        assertEquals("test message", chatMessageResponse.getMessage());
    }

    @Test
    @Transactional
    void getChatMessagesByChatRoomId() {
        Long chatRoomId = savedId;

        List<ChatMessageResponse> chatMessageResponseList = chatMessageService.getChatMessagesByChatRoomId(chatRoomId);

        assertEquals(3, chatMessageResponseList.size());
    }
}
