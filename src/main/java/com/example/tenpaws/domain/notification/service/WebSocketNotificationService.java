package com.example.tenpaws.domain.notification.service;

import com.example.tenpaws.domain.notification.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * WebSocket을 통한 실시간 알림 전송을 담당하는 서비스
 * SimpMessagingTemplate을 사용하여 특정 사용자에게 메시지를 전송
 */
@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 특정 사용자에게 실시간 알림을 전송
     * @param notification 전송할 알림 정보
     *
     * convertAndSendToUser 메소드는 다음과 같이 동작합니다:
     * 1. 첫 번째 파라미터: 수신할 사용자의 ID
     * 2. 두 번째 파라미터: 구독 엔드포인트 (/queue/notifications)
     * 3. 세 번째 파라미터: 전송할 메시지 객체
     */
    public void sendNotification(NotificationMessage notification) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(notification.getUserId()),  // 사용자 ID를 문자열로 변환
                "/queue/notifications",                    // 클라이언트가 구독할 큐의 주소
                notification                              // 전송할 알림 메시지
        );
    }

    /**
     * 특정 사용자의 읽지 않은 알림 개수를 실시간으로 전송
     * 알림 상태가 변경될 때마다 호출되어 클라이언트의 UI를 업데이트
     */
    public void sendNotificationCount(Long userId, Long count) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/notifications/count",
                count
        );
    }
}
