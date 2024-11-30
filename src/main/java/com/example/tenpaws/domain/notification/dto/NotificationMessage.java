package com.example.tenpaws.domain.notification.dto;

import com.example.tenpaws.domain.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * WebSocket을 통해 클라이언트에게 전송될 실시간 알림 메시지를 위한 DTO
 * REST API의 응답과 달리, WebSocket에서는 필요한 정보만 간단히 전달
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private Long id;
    private Long userId;
    private String content; // 알림 내용
    private NotificationType type; // 알림 유형 (시스템 or 댓글)
    private Boolean isRead; // 읽음 여부
}