package com.example.tenpaws.domain.chat.chatroom.dto;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import lombok.*;

@Data
@NoArgsConstructor
@ToString
public class ChatRoomRequest {
    @NonNull
    private Long userId;

    @NonNull
    private Long shelterId;

    @Builder
    public ChatRoomRequest(@NonNull Long userId, @NonNull Long shelterId) {
        this.userId = userId;
        this.shelterId = shelterId;
    }

    public ChatRoomRequest(ChatRoom chatRoom) {
        this.userId = chatRoom.getUserId();
        this.shelterId = chatRoom.getShelterId();
    }

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .userId(userId)
                .shelterId(shelterId)
                .build();
    }
}
