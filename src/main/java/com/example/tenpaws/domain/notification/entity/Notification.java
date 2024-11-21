package com.example.tenpaws.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Boolean isRead;

    private Long userId;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Notification(String content, NotificationType type, Long userId) {
        this.content = content;
        this.type = type;
        this.userId = userId;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
