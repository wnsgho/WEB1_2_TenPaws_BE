package com.example.tenpaws.domain.chat.chatmessage.entity;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import com.example.tenpaws.global.entity.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatmessages")
@NoArgsConstructor
@Getter
@ToString
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private final LocalDateTime chatDate = LocalDateTime.now();

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole senderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(Long id, String message, Long senderId, UserRole senderType, Long chatRoomId) {
        this.id = id;
        this.message = message;
        this.senderId = senderId;
        this.senderType = senderType;
        this.chatRoom = ChatRoom.builder().id(chatRoomId).build();
    }
}
