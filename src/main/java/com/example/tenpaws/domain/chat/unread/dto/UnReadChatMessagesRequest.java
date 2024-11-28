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

    private int unReadCount;

    @Builder
    public UnReadChatMessagesRequest(@NonNull Long chatRoomId, @NonNull String username, int unReadCount) {
        this.chatRoomId = chatRoomId;
        this.username = username;
        this.unReadCount = unReadCount;
    }

    public UnReadChatMessages toEntity(ChatRoom chatRoom) {
        return UnReadChatMessages.builder()
                .chatroom(chatRoom)
                .username(username)
                .build();
    }
}
