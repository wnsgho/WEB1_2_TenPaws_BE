package com.example.tenpaws.domain.chat.unread.controller;

import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesRequest;
import com.example.tenpaws.domain.chat.unread.service.UnReadChatMessagesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/unread")
@RequiredArgsConstructor
public class UnReadChatMessagesController {
    private final UnReadChatMessagesService unreadChatMessagesService;

    @PutMapping("/init")
    public void update(@Valid @RequestBody UnReadChatMessagesRequest unReadChatMessagesRequest) {
        unReadChatMessagesRequest.setUnReadCount(0);
        unreadChatMessagesService.update(unReadChatMessagesRequest);
    }
}
