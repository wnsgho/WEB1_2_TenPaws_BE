package com.example.tenpaws.domain.chat.unread.dto;

import com.example.tenpaws.domain.chat.unread.entity.UnReadChatMessages;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import lombok.Builder;
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

    @Builder
    public UnReadChatMessagesRequest(@NonNull Long chatRoomId, @NonNull String username) {
        this.chatRoomId = chatRoomId;
        this.username = username;
    }

    public UnReadChatMessages toEntity(ChatRoom chatRoom) {
        return UnReadChatMessages.builder()
                .chatroom(chatRoom)
                .username(username)
                .build();
    }
}
