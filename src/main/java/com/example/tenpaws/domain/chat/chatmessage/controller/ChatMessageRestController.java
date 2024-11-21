package com.example.tenpaws.domain.chat.chatmessage.controller;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageRequest;
import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageResponse;
import com.example.tenpaws.domain.chat.chatmessage.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatmessages")
public class ChatMessageRestController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<List<ChatMessageResponse>> getChatMessages(@PathVariable("chatRoomId") Long chatRoomId) {
        return ResponseEntity.ok(chatMessageService.getChatMessagesByChatRoomId(chatRoomId));
    }

    @PostMapping
    public ResponseEntity<ChatMessageResponse> createChatMessage(@Valid @RequestBody ChatMessageRequest chatMessageRequest) {
        return ResponseEntity.ok(chatMessageService.createChatMessage(chatMessageRequest));
    }
}
