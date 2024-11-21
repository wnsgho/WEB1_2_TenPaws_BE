package com.example.tenpaws.domain.chat.chatroom.dto;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@ToString
public class ChatRoomResponse {
    private Long chatRoomId;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long shelterId;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getId();
        this.userId = chatRoom.getUserId();
        this.shelterId = chatRoom.getShelterId();
    }

    @Builder
    public ChatRoomResponse(Long chatRoomId, Long userId, Long shelterId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.shelterId = shelterId;
    }
}
