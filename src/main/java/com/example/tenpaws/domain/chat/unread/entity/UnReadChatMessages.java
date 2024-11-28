package com.example.tenpaws.domain.chat.unread.entity;

import com.example.tenpaws.domain.chat.chatroom.entity.ChatRoom;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unread_chatmessages")
@Getter
@NoArgsConstructor
public class UnReadChatMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    private int unReadCount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatroom;

    @Builder
    public UnReadChatMessages(Long id, String username, int unReadCount, ChatRoom chatroom) {
        this.id = id;
        this.username = username;
        this.unReadCount = unReadCount;
        this.chatroom = chatroom;
    }

    public void changeUnReadCount() {
        this.unReadCount++;
    }
}
