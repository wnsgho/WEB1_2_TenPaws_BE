package com.example.tenpaws.domain.chat.chatmessage.controller;

import com.example.tenpaws.domain.chat.chatmessage.dto.ChatMessageResponse;
import com.example.tenpaws.domain.chat.chatmessage.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
