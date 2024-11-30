package com.example.tenpaws.domain.notification.controller;

import com.example.tenpaws.domain.notification.dto.response.NotificationResponse;
import com.example.tenpaws.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 * WebSocket 통신을 위한 컨트롤러
 * REST 컨트롤러와 달리 @Controller 어노테이션 사용
 * 클라이언트의 실시간 요청을 처리
 */
@Controller
@RequiredArgsConstructor
public class NotificationWebSocketController {
    private final NotificationService notificationService;

    /**
     * 클라이언트가 알림 목록을 요청할 때 호출되는 메소드
     * @MessageMapping: 클라이언트가 메시지를 보낼 주소
     * @SendToUser: 응답을 특정 사용자에게만 전송
     */
    @MessageMapping("/notifications/list")
    @SendToUser("/queue/notifications/list")
    public Page<NotificationResponse> getNotifications(Long userId, Pageable pageable) {
        return notificationService.getList(userId, pageable);
    }

    /**
     * 클라이언트가 읽지 않은 알림 개수를 요청할 때 호출되는 메소드
     */
    @MessageMapping("/notifications/count")
    @SendToUser("/queue/notifications/count")
    public Long getUnreadCount(Long userId) {
        return notificationService.getUnreadCount(userId);
    }
}