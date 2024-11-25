package com.example.tenpaws.domain.chat.chatmessage.entity;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatmessages")
@NoArgsConstructor
@Getter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private final LocalDateTime chatDate = LocalDateTime.now();

    @Column(nullable = false)
    private String sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(Long id, String message, String sender, ChatRoom chatRoom) {
        this.id = id;
        this.message = message;
        this.sender = sender;
//        this.receiver = receiver;
        this.chatRoom = chatRoom;
    }
}
