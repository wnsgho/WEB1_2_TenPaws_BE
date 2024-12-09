package com.example.tenpaws.domain.chat.unread.dto;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.unread.entity.UnReadChatMessages;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UnReadChatMessagesRequest {
    @NonNull
    private Long chatRoomId;

    @NonNull
    private String userEmail;

    private int unReadCount;

    @Builder
    public UnReadChatMessagesRequest(@NonNull Long chatRoomId, @NonNull String userEmail, int unReadCount) {
        this.chatRoomId = chatRoomId;
        this.userEmail = userEmail;
        this.unReadCount = unReadCount;
    }

    public UnReadChatMessages toEntity(ChatRoom chatRoom) {
        return UnReadChatMessages.builder()
                .chatroom(chatRoom)
                .username(userEmail)
                .build();
    }
}
