package com.example.tenpaws.domain.chat.chatmessage.service;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageRequest;
import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageResponse;

import java.util.List;

public interface ChatMessageService {
    ChatMessageResponse createChatMessage(ChatMessageRequest chatMessageRequest);
    List<ChatMessageResponse> getChatMessagesByChatRoomId(Long chatRoomId);
}
