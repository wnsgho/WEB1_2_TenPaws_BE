package com.example.tenpaws.domain.notification.service;

import com.example.tenpaws.domain.notification.Repository.NotificationRepository;
import com.example.tenpaws.domain.notification.dto.request.CreateNotificationRequest;
import com.example.tenpaws.domain.notification.dto.response.NotificationResponse;
import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.domain.notification.sse.SseEmitters;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final NotificationRepository notificationRepository;
    private final SseEmitters sseEmitters;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        try {
            emitter.send(SseEmitter.event()
                    .name("SSE")
                    .data("연결되었습니다.", MediaType.APPLICATION_JSON));

            sseEmitters.add(userId, emitter);
        } catch (IOException e) {
            log.error("SSE 연결 중 오류 발생", e);
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @Override
    @Transactional
    public Notification create(CreateNotificationRequest request) {
        Notification savedNotification = notificationRepository.save(request.toEntity());

        try {
            notify(request.getUserId(), new NotificationResponse(savedNotification));
        } catch (Exception e) {
            log.error("실시간 알림 전송 실패", e);
        }

        return savedNotification;
    }

    @Override
    public void notify(Long userId, NotificationResponse notification) {
        SseEmitter emitter = sseEmitters.get(userId);
        if (emitter != null) {
            sendToClient(emitter, "notification", notification);
        }
    }

    private void sendToClient(SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(System.currentTimeMillis()))
                    .name(eventName)
                    .data(data, MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            log.error("SSE 이벤트 전송 실패", e);
            emitter.complete();
            Long userId = getCurrentSubscriberId(emitter);
            if (userId != null) {
                sseEmitters.remove(userId);
            }
        } catch (Exception e) {
            log.error("SSE 이벤트 전송 중 예상치 못한 오류 발생", e);
            emitter.complete();
        }
    }

    private Long getCurrentSubscriberId(SseEmitter emitter) {
        return sseEmitters.getAll().entrySet().stream()
                .filter(entry -> entry.getValue().equals(emitter))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
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