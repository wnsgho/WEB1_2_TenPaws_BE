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
    private String user1;

    @NotBlank
    private String user2;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getId();
        this.user1 = chatRoom.getUser1();
        this.user2 = chatRoom.getUser2();
    }

    @Builder
    public ChatRoomResponse(Long chatRoomId, String user1, String user2) {
        this.chatRoomId = chatRoomId;
        this.user1 = user1;
        this.user2 = user2;
    }
}
