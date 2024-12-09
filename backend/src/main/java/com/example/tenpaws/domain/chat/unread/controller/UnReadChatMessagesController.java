package com.example.tenpaws.domain.chat.unread.controller;

import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesRequest;
import com.example.tenpaws.domain.chat.unread.service.UnReadChatMessagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/unread")
@Tag(name = "읽지 않은 채팅메시지 처리 관련 API", description = "읽지 않은 채팅메시지 관련 처리 기능을 모아둔 컨트롤러 입니다")
@RequiredArgsConstructor
public class UnReadChatMessagesController {
    private final UnReadChatMessagesService unreadChatMessagesService;

    @Operation(summary = "읽지 않은 채팅메시지 카운트 초기화", description = "읽지 않은 채팅메시지 카운트 초기화 API")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN') or (hasAnyRole('ROLE_USER', 'ROLE_SHELTER') and #unReadChatMessagesRequest.userEmail == authentication.name)")
    @PutMapping("/init")
    public void update(@Valid @RequestBody UnReadChatMessagesRequest unReadChatMessagesRequest) {
        unReadChatMessagesRequest.setUnReadCount(0);
        unreadChatMessagesService.update(unReadChatMessagesRequest);
    }
}
