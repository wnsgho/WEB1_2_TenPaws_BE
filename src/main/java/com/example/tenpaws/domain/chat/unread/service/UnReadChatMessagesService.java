package com.example.tenpaws.domain.chat.unread.service;

import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesRequest;
import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesResponse;

import java.util.List;

public interface UnReadChatMessagesService {
    UnReadChatMessagesResponse create(Long chatRoomId, String user1, String user2);

    UnReadChatMessagesResponse update(UnReadChatMessagesRequest unReadChatMessagesRequest);

    List<UnReadChatMessagesResponse> read(String username);
}