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
    private String sender;

    @Builder
    public ChatMessageResponse(String message, @NonNull LocalDateTime chatDate, String sender) {
        this.message = message;
        this.chatDate = chatDate;
        this.sender = sender;
    }

    public ChatMessageResponse(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
        this.chatDate = chatMessage.getChatDate();
        this.sender = chatMessage.getSender();
    }
}
