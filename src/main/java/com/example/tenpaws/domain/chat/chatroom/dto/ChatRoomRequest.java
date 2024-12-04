package com.example.tenpaws.domain.chat.chatroom.dto;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    @NotBlank
    private String userEmail;

    @NotBlank
    private String oppositeEmail;

    @Builder
    public ChatRoomRequest(String userEmail, String oppositeEmail) {
        this.userEmail = userEmail;
        this.oppositeEmail = oppositeEmail;
    }

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .user1(userEmail)
                .user2(oppositeEmail)
                .build();
    }
}
