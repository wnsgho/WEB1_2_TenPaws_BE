package com.example.tenpaws.domain.notification.dto.response;

import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.domain.notification.entity.NotificationType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private final String content;
    private final LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
    }
}
