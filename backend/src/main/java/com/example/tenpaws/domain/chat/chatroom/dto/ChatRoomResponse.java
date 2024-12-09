package com.example.tenpaws.domain.chat.chatroom.dto;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomResponse {
    @NonNull
    private Long chatRoomId;

    private int unReadCount;

    @NotBlank
    private String oppositeEmail;

    @NotBlank
    private String oppositeName;

    @NotBlank
    private String userEmail;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getId();
        this.userEmail = chatRoom.getUser1();
        this.oppositeEmail = chatRoom.getUser2();
    }
}
