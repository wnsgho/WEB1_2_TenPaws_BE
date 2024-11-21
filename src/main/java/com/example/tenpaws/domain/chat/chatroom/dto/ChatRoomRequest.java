package com.example.tenpaws.domain.chat.chatroom.dto;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatRoomRequest {
    @NonNull
    private Long userId;

    @NonNull
    private Long shelterId;

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
