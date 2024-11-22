package com.example.tenpaws.domain.notification.service;

import com.example.tenpaws.domain.notification.dto.request.CreateNotificationRequest;
import com.example.tenpaws.domain.notification.dto.response.NotificationResponse;
import com.example.tenpaws.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Notification create(CreateNotificationRequest request);
    Page<NotificationResponse> getList(Long userId, Pageable pageable);
    void markAsRead(Long notificationId);
    Long getUnreadCount(Long userId);
    void delete(Long notificationId);
}
