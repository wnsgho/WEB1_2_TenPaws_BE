package com.example.tenpaws.domain.chat.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClosedChatRoomResponse {
    private Long chatRoomId;
    private String message;
}
