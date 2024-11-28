package com.example.tenpaws.domain.chat.unread.service;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesRequest;
import com.example.tenpaws.domain.chat.unread.dto.UnReadChatMessagesResponse;
import com.example.tenpaws.domain.chat.unread.entity.UnReadChatMessages;
import com.example.tenpaws.domain.chat.unread.repository.UnReadChatMessagesRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnReadChatMessagesServiceImpl implements UnReadChatMessagesService {
    private final UnReadChatMessagesRepository unReadChatMessagesRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public UnReadChatMessagesResponse create(UnReadChatMessagesRequest unReadChatMessagesRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(unReadChatMessagesRequest.getChatRoomId())
                .orElseThrow(() -> new BaseException(ErrorCode.CHATROOM_NOT_FOUND));
        try {
            return new UnReadChatMessagesResponse(unReadChatMessagesRepository.save(unReadChatMessagesRequest.toEntity(chatRoom)));
        } catch (Exception e) {
            throw new BaseException(ErrorCode.UNREAD_CHAT_MESSAGES_NOT_REGISTERED);
        }
    }

    @Override
    @Transactional
    public UnReadChatMessagesResponse update(UnReadChatMessagesRequest unReadChatMessagesRequest) {
        UnReadChatMessages unReadChatMessages = unReadChatMessagesRepository.findByChatRoomIdAndUsername(
                unReadChatMessagesRequest.getChatRoomId(), unReadChatMessagesRequest.getUsername())
                .orElseThrow(() -> new BaseException(ErrorCode.UNREAD_CHAT_MESSAGES_NOT_FOUND));
        try {
            unReadChatMessages.changeUnReadCount();
        } catch (Exception e) {
            throw new BaseException(ErrorCode.UNREAD_CHAT_MESSAGES_NOT_MODIFIED);
        }
        return new UnReadChatMessagesResponse(unReadChatMessages);
    }

    @Override
    public List<UnReadChatMessagesResponse> read(String username) {
        return unReadChatMessagesRepository.findBydUsername(username)
                .stream().map(UnReadChatMessagesResponse::new).toList();
    }
}
