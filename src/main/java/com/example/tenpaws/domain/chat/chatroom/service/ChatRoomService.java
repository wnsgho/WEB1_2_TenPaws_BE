package com.example.tenpaws.domain.chat.chatroom.service;

import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomRequest;
import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomResponse;

import java.util.List;

public interface ChatRoomService {
    ChatRoomResponse create(ChatRoomRequest chatRoomRequest);

    ChatRoomResponse getChatRoom(Long chatRoomId);

    void delete(Long chatRoomId);

    ChatRoomResponse getChatRoomByUserIdAndShelterId(Long userId, Long shelterId);

    List<ChatRoomResponse> getChatRoomsByUserId(Long userId);

    List<ChatRoomResponse> getChatRoomsByShelterId(Long shelterId);
}
