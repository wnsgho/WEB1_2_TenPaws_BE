package com.example.tenpaws.domain.chat.chatroom.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chatrooms")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long shelterId;
}
