package com.example.tenpaws.domain.chat.chatroom.controller;

import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomRequest;
import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomResponse;
import com.example.tenpaws.domain.chat.chatroom.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomResponse> findChatRoomById(@PathVariable("chatRoomId") Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getChatRoom(chatRoomId));
    }

    @GetMapping("/{userId}/{shelterId}")
    public ResponseEntity<ChatRoomResponse> findChatRoomByUserIdAndShelterId(@PathVariable("userId") Long userId, @PathVariable("shelterId") Long shelterId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomByUserIdAndShelterId(userId, shelterId));
    }

    @GetMapping("/user-{userId}")
    public ResponseEntity<List<ChatRoomResponse>> findChatRoomsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomsByUserId(userId));
    }

    @GetMapping("/shelter-{shelterId}")
    public ResponseEntity<List<ChatRoomResponse>> findChatRoomsByShelterId(@PathVariable("shelterId") Long shelterId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomsByShelterId(shelterId));
    }

    @PostMapping
    public ResponseEntity<ChatRoomResponse> createChatRoom(@Valid @RequestBody ChatRoomRequest chatRoomRequest) {
        return ResponseEntity.ok(chatRoomService.create(chatRoomRequest));
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Map<String, String>> deleteChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        chatRoomService.delete(chatRoomId);
        return ResponseEntity.ok(Map.of("message", "ChatRoom deleted"));
    }
}
