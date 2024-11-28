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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnReadChatMessagesServiceImpl implements UnReadChatMessagesService {
    private final UnReadChatMessagesRepository unReadChatMessagesRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public UnReadChatMessagesResponse create(Long chatRoomId, String user1, String user2) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHATROOM_NOT_FOUND));
        try {
            createTemplate(chatRoom, chatRoomId, user1);
            createTemplate(chatRoom, chatRoomId, user2);
            return new UnReadChatMessagesResponse();
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
            unReadChatMessages.changeUnReadCount(unReadChatMessagesRequest.getUnReadCount());
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

    private void createTemplate(ChatRoom chatRoom, Long chatRoomId, String user1) {
        Optional<UnReadChatMessages> optionalUnReadChatMessages = unReadChatMessagesRepository.findByChatRoomIdAndUsername(chatRoomId, user1);
        optionalUnReadChatMessages.map(UnReadChatMessagesResponse::new)
                .orElseGet(() -> new UnReadChatMessagesResponse(
                        unReadChatMessagesRepository.save(
                                UnReadChatMessagesRequest.builder().chatRoomId(chatRoomId).username(user1).build()
                                        .toEntity(chatRoom))));
    }
}
