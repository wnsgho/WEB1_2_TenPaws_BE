package com.example.tenpaws.domain.notification.dto.request;

import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.domain.notification.entity.NotificationType;
import com.example.tenpaws.global.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {
    @NotNull(message = "알림 타입은 필수입니다")
    private NotificationType type;

    @NotBlank(message = "알림 내용은 필수입니다")
    private String content;

    private Long userId;

    private UserRole userRole;    // 알림을 받을 사용자의 역할

    public Notification toEntity() {
        return Notification.builder()
                .content(content)
                .type(type)
                .userId(userId)
                .userRole(userRole)
                .build();
    }
}
