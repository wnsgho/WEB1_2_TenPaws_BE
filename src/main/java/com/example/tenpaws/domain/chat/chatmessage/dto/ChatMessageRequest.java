package com.example.tenpaws.domain.chat.chatmessage.dto;

import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import com.example.tenpaws.global.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
public class ChatMessageRequest {
    @NotBlank
    private String message;

    @NonNull
    private Long senderId;

    @NonNull
    private String senderType;

    @NonNull
    private Long chatRoomId;

    @Builder
    public ChatMessageRequest(String message, @NonNull Long senderId, @NonNull String senderType, @NonNull Long chatRoomId) {
        this.message = message;
        this.senderId = senderId;
        this.senderType = senderType;
        this.chatRoomId = chatRoomId;
    }

    public ChatMessageRequest(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
        this.senderId = chatMessage.getSenderId();
        this.senderType = chatMessage.getSenderType().name();
        this.chatRoomId = chatMessage.getChatRoom().getId();
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .message(message)
                .senderId(senderId)
                .senderType(UserRole.valueOf(senderType))
                .chatRoomId(chatRoomId)
                .build();
    }
}
