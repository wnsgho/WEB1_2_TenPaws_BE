package com.example.tenpaws.domain.chat.chatmessage.controller;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageRequest;
import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageResponse;
import com.example.tenpaws.domain.chat.chatmessage.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;

    @MessageMapping("/chat/send/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, ChatMessageRequest chatMessageRequest) {
        printUserSubscriptions(userRegistry);
        String receiver = chatMessageRequest.getReceiver();
        boolean isReceiverConnected = userRegistry.getUsers().stream()
                .anyMatch(user -> user.getName().equals(receiver));

        chatMessageRequest.setChatRoomId(Long.valueOf(chatRoomId));
        ChatMessageResponse chatMessageResponse = chatMessageService.createChatMessage(chatMessageRequest);
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + chatRoomId,
                chatMessageResponse
        );
        if (!isReceiverConnected) {
            System.out.println("상대방 접속 X");
            messagingTemplate.convertAndSendToUser(
                    receiver,
                    "/queue/notifications",
                    "새 채팅 메시지가 도착했습니다!"
            );
        }
    }

    public void printUserSubscriptions(SimpUserRegistry userRegistry) {
        System.out.println("userRegistry: " + userRegistry);
        for (SimpUser user : userRegistry.getUsers()) {
            System.out.println("User: " + user.getName());
            user.getSessions().forEach(session -> {
                System.out.println("Subscribed to: " + session.getSubscriptions());
            });
        }
    }
}
