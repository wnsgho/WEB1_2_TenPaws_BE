package com.example.tenpaws.domain.chat.chatroom.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chatrooms")
@Getter
@ToString
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long shelterId;

    @Builder
    public ChatRoom(Long id, Long userId, Long shelterId) {
        this.id = id;
        this.userId = userId;
        this.shelterId = shelterId;
    }
}
