package com.example.tenpaws.domain.chat.unread.service;

import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesRequest;
import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesResponse;
import com.example.tenpaws.domain.chat.unread.repository.UnReadChatMessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnReadChatMessagesServiceImpl implements UnReadChatMessagesService {
    private final UnReadChatMessagesRepository unReadChatMessagesRepository;

    @Override
    public UnReadChatMessagesResponse create(UnReadChatMessagesRequest unReadChatMessagesRequest) {

        return null;
    }

    @Override
    public UnReadChatMessagesResponse update(UnReadChatMessagesRequest unReadChatMessagesRequest) {
        return null;
    }

    @Override
    public List<UnReadChatMessagesResponse> read(UnReadChatMessagesRequest unReadChatMessagesRequest) {
        return List.of();
    }
}
