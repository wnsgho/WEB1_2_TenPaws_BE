package com.example.tenpaws.domain.chat.chatroom.entity;

import com.example.tenpaws.domain.chat.chatmessage.entity.ChatMessage;
import com.example.tenpaws.domain.chat.unread.entity.UnReadChatMessages;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "chatroom_id")
    private List<ChatMessage> chatMessages;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "chatroom_id")
    private List<UnReadChatMessages> unreadChatMessages;

    @Builder
    public ChatRoom(Long id, String user1, String user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }
}
