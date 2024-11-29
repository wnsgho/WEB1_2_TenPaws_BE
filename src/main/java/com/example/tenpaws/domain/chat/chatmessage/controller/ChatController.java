package com.example.tenpaws.domain.chat.chatmessage.controller;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageRequest;
import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageResponse;
import com.example.tenpaws.domain.chat.chatmessage.service.ChatMessageService;
import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesRequest;
import com.example.tenpaws.domain.chat.unread.service.UnReadChatMessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final UnReadChatMessagesService unReadChatMessagesService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;

    @MessageMapping("/chat/send/{chatRoomId}")
    public void sendMessage(@DestinationVariable Long chatRoomId, ChatMessageRequest chatMessageRequest) {
        String receiver = chatMessageRequest.getReceiver();
        String destination = "/topic/chatroom/" + chatRoomId;

        chatMessageRequest.setChatRoomId(chatRoomId);
        ChatMessageResponse chatMessageResponse = chatMessageService.createChatMessage(chatMessageRequest);

        messagingTemplate.convertAndSend(
                destination,
                chatMessageResponse
        );

        // 알림 완성되면 삭제
        if (!isUserSubscribed(receiver, destination)) {
            messagingTemplate.convertAndSendToUser(
                    receiver,
                    "/queue/notifications",
                    "새 채팅 메시지가 도착했습니다!"
            );
            unReadChatMessagesService.update(
                    UnReadChatMessagesRequest.builder().chatRoomId(chatRoomId).username(receiver).unReadCount(1).build());
        }
    }

    private boolean isUserSubscribed(String receiver, String destination) {
        SimpUser user = userRegistry.getUser(receiver);
        if (user == null) {
            return false;
        }

        for (SimpSession session : user.getSessions()) {
            for (SimpSubscription subscription : session.getSubscriptions()) {
                if (subscription.getDestination().equals(destination)) {
                    return true;
                }
            }
        }
        return false;
    }
}
