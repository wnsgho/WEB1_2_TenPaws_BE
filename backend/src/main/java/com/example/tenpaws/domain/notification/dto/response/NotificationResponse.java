package com.example.tenpaws.domain.notification.dto.response;

import com.example.tenpaws.domain.notification.entity.Notification;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private final Long id;
    private final String content;
    private final boolean isRead;
    private final LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.isRead = notification.getIsRead();
        this.createdAt = notification.getCreatedAt();
    }
}
