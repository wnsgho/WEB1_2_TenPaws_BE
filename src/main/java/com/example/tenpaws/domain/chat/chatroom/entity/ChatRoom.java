package com.example.tenpaws.domain.chat.chatroom.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chatrooms")
@Getter
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String user1;

    @Column(nullable = false)
    private String user2;

    @Builder
    public ChatRoom(Long id, String user1, String user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }
}
