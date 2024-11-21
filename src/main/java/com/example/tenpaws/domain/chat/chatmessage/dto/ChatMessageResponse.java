package com.example.tenpaws.domain.chat.chatmessage.dto;

import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatMessageResponse {
    @NotBlank
    private String message;

    @NonNull
    private LocalDateTime chatDate;

    @NotBlank
    private String senderType;

    @Builder
    public ChatMessageResponse(String message, @NonNull LocalDateTime chatDate, String senderType) {
        this.message = message;
        this.chatDate = chatDate;
        this.senderType = senderType;
    }

    public ChatMessageResponse(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
        this.chatDate = chatMessage.getChatDate();
        this.senderType = chatMessage.getSenderType().name();
    }
}
