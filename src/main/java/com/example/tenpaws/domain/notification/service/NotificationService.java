package com.example.tenpaws.domain.notification.service;

import com.example.tenpaws.domain.notification.dto.request.CreateNotificationRequest;
import com.example.tenpaws.domain.notification.dto.response.NotificationResponse;
import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.global.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(UserRole userRole, Long userId);
    void notify(UserRole userRole, Long userId, NotificationResponse notification);

    Notification create(CreateNotificationRequest request);
    Page<NotificationResponse> getList(Long userId, Pageable pageable);
    void markAsRead(Long notificationId);
    Long getUnreadCount(Long userId);
    void delete(Long notificationId);
}
