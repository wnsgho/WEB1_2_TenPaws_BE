package com.example.tenpaws.domain.chat.chatmessage.controller;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageRequest;
import com.example.tenpaws.domain.chat.chatmessage.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, ChatMessageRequest chatMessageRequest) {
        chatMessageRequest.setChatRoomId(Long.valueOf(chatRoomId));
        chatMessageService.createChatMessage(chatMessageRequest);
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + chatRoomId,
                "{\"content\":\"" + chatMessageRequest.getMessage() + "\", \"username\":\"" + chatMessageRequest.getSender() + "\"}"
        );
    }
}
