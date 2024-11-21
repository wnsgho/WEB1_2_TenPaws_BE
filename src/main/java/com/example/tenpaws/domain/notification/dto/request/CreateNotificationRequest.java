package com.example.tenpaws.domain.notification.dto.request;

import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.domain.notification.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateNotificationRequest {

    @NotBlank
    private String content;

    @NotBlank
    private NotificationType type;

    @NotBlank
    private Boolean isRead;

    @NotBlank
    private Long userId;

    public Notification toEntity() {
        return Notification.builder()
                .content(content)
                .type(type)
                .userId(userId)
                .build();
    }
}
