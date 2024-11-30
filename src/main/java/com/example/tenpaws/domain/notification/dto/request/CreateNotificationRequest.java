package com.example.tenpaws.domain.notification.dto.request;

import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.domain.notification.entity.NotificationType;
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

    @NotBlank
    private String content;

    @NotNull
    private NotificationType type;

    @NotNull
    private Long userId;

    private String[] params;

    public Notification toEntity() {
        return Notification.builder()
                .content(content)
                .type(type)
                .userId(userId)
                .build();
    }

    // 팩토리 메서드 패턴을 활용한 생성 메서드들
    public static CreateNotificationRequest fromTemplate(NotificationType type, Long userId, String... params) {
        return CreateNotificationRequest.builder()
                .type(type)
                .userId(userId)
                .params(params)
                .build();
    }
}