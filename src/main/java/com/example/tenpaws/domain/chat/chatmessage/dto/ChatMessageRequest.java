package com.example.tenpaws.domain.chat.chatmessage.dto;

import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ChatMessageRequest {
    @NotBlank
    private String message;

    @NotBlank
    private String sender;

    @NonNull
    private Long chatRoomId;

    @Builder
    public ChatMessageRequest(String message, String sender, @NonNull Long chatRoomId) {
        this.message = message;
        this.sender = sender;
        this.chatRoomId = chatRoomId;
    }

    public ChatMessageRequest(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
        this.sender = chatMessage.getSender();
        this.chatRoomId = chatMessage.getChatRoom().getId();
    }

    public ChatMessage toEntity(ChatRoom chatRoom) {
        return ChatMessage.builder()
                .message(message)
                .sender(sender)
                .chatRoom(chatRoom)
                .build();
    }
}
