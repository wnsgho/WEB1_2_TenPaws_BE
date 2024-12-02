package com.example.tenpaws.domain.notification.entity;

import com.example.tenpaws.global.entity.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean isRead;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Notification(String content, NotificationType type, Long userId, Long adminId, UserRole userRole) {
        this.content = content;
        this.type = type;
        this.userId = userId;
        this.userRole = userRole;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}