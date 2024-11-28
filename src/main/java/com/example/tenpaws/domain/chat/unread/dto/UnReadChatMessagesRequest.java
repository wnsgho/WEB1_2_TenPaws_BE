package com.example.tenpaws.domain.chat.unread.dto;

import com.example.tenpaws.domain.chat.unread.entity.UnReadChatMessages;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UnReadChatMessagesRequest {
    @NonNull
    private Long chatRoomId;

    @NonNull
    private String username;

    public UnReadChatMessages toEntity(ChatRoom chatRoom) {
        return UnReadChatMessages.builder()
                .chatroom(chatRoom)
                .username(username)
                .build();
    }
}
