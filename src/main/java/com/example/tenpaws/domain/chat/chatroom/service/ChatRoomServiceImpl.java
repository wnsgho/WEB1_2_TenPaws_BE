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

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public ChatRoomResponse create(ChatRoomRequest chatRoomRequest) {
        try {
            return new ChatRoomResponse(chatRoomRepository.save(chatRoomRequest.toEntity()));
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
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new BaseException(ErrorCode.CHATROOM_NOT_FOUND));
        try {
            chatRoomRepository.delete(chatRoom);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.CHATROOM_NOT_DELETED);
        }
    }

    @Override
    public ChatRoomResponse getChatRoomByUserIdAndShelterId(Long userId, Long shelterId) {
        try {
            return new ChatRoomResponse(chatRoomRepository.findByUserIdAndShelterId(userId, shelterId));
        } catch (Exception e) {
            throw new BaseException(ErrorCode.CHATROOM_NOT_FOUND);
        }
    }

    @Override
    public List<ChatRoomResponse> getChatRoomsByUserId(Long userId) {
        return chatRoomRepository.findByUserId(userId).stream().map(ChatRoomResponse::new).toList();
    }

    @Override
    public List<ChatRoomResponse> getChatRoomsByShelterId(Long shelterId) {
        return chatRoomRepository.findByShelterId(shelterId).stream().map(ChatRoomResponse::new).toList();
    }
}
