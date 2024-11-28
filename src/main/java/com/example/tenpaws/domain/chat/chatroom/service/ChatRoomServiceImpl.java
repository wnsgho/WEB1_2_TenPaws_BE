package com.example.tenpaws.domain.chat.chatroom.service;

import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomRequest;
import com.example.tenpaws.domain.chat.chatroom.dto.ChatRoomResponse;
import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.domain.chat.chatroom.repository.ChatRoomRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public ChatRoomResponse create(ChatRoomRequest chatRoomRequest) {
        try {
            Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByUsers(chatRoomRequest.getUser1(), chatRoomRequest.getUser2());
            return optionalChatRoom.map(ChatRoomResponse::new)
                    .orElseGet(() -> new ChatRoomResponse(chatRoomRepository.save(chatRoomRequest.toEntity())));
        } catch (Exception e) {
            throw new BaseException(ErrorCode.CHATROOM_NOT_REGISTERED);
        }
    }

    @Override
    public ChatRoomResponse getChatRoom(Long chatRoomId) {
        return new ChatRoomResponse(
                chatRoomRepository.findById(chatRoomId)
                        .orElseThrow(() -> new BaseException(ErrorCode.CHATROOM_NOT_FOUND)));
    }

    @Override
    public void delete(Long chatRoomId) {
        chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHATROOM_NOT_FOUND));
        try {
            chatRoomRepository.deleteById(chatRoomId);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.CHATROOM_NOT_DELETED);
        }
    }

    @Override
    public ChatRoomResponse getChatRoomByUsers(String user1, String user2) {
        return new ChatRoomResponse(
                chatRoomRepository.findByUsers(user1, user2)
                        .orElseThrow(() -> new BaseException(ErrorCode.CHATROOM_NOT_FOUND)));
    }

    @Override
    public List<ChatRoomResponse> getChatRoomsByUser(String user) {
        return chatRoomRepository.findByUser(user).stream().map(ChatRoomResponse::new).toList();
    }
}
