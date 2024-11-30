package com.example.tenpaws.domain.notification.service;

import com.example.tenpaws.domain.notification.Repository.NotificationRepository;
import com.example.tenpaws.domain.notification.dto.NotificationMessage;
import com.example.tenpaws.domain.notification.dto.request.CreateNotificationRequest;
import com.example.tenpaws.domain.notification.dto.response.NotificationResponse;
import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final WebSocketNotificationService webSocketNotificationService;

    /**
     * 알림 생성 시 DB 저장과 함께 실시간 웹소켓 메시지도 발송
     */
    @Transactional
    public Notification create(CreateNotificationRequest request) {
        // 1. DB에 알림 저장
        Notification savedNotification = notificationRepository.save(request.toEntity());

        // 2. 실시간 알림 메시지 생성
        NotificationMessage message = NotificationMessage.builder()
                .id(savedNotification.getId())
                .content(savedNotification.getContent())
                .type(savedNotification.getType())
                .userId(savedNotification.getUserId())
                .isRead(savedNotification.getIsRead())
                .build();

        // 3. WebSocket을 통해 실시간 알림 전송
        webSocketNotificationService.sendNotification(message);

        // 4. 읽지 않은 알림 수 업데이트
        webSocketNotificationService.sendNotificationCount(
                savedNotification.getUserId(),
                notificationRepository.countByUserIdAndIsReadFalse(savedNotification.getUserId())
        );

        return savedNotification;
    }

    public Page<NotificationResponse> getList(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(NotificationResponse::new);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void delete(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notificationRepository.delete(notification);
    }
}
