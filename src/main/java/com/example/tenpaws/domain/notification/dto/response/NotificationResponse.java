package com.example.tenpaws.domain.notification.dto.response;

import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.domain.notification.entity.NotificationType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private final Long id;
    private final String content;
    private final NotificationType type;
    private final Boolean isRead;
    private final LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.type = notification.getType();
        this.isRead = notification.getIsRead();
        this.createdAt = notification.getCreatedAt();
    }
}
