package com.example.tenpaws.domain.notification.factory;

import com.example.tenpaws.domain.notification.dto.request.NotificationRequest;
import com.example.tenpaws.domain.notification.entity.NotificationType;
import com.example.tenpaws.domain.notification.template.NotificationTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationFactory {
    private final NotificationTemplate template;
    private static final String INQUIRY_ADMIN_EMAIL = "superadmin@tenpaws.com"; // 문의 담당 관리자 고유번호

    // 입양 신청 완료 알림 생성
    public NotificationRequest createAdoptionSubmittedNotification(String recipientEmail) {
        return NotificationRequest.builder()
                .type(NotificationType.ADOPTION_APPLICATION_SUBMITTED)
                .content(template.generateMessage(NotificationType.ADOPTION_APPLICATION_SUBMITTED))
                .recipientEmail(recipientEmail)
                .build();
    }

    // 입양 신청 접수 알림 생성 (보호소용)
    public NotificationRequest createAdoptionReceivedNotification(String recipientEmail, String userName, String petName) {
        return NotificationRequest.builder()
                .type(NotificationType.ADOPTION_APPLICATION_RECEIVED)
                .content(template.generateMessage(NotificationType.ADOPTION_APPLICATION_RECEIVED, userName, petName))
                .recipientEmail(recipientEmail)
                .build();
    }

    // 입양 신청 결과 알림 생성
    public NotificationRequest createAdoptionResultNotification(String recipientEmail, String petName) {
        return NotificationRequest.builder()
                .type(NotificationType.ADOPTION_APPLICATION_RESULT)
                .content(template.generateMessage(NotificationType.ADOPTION_APPLICATION_RESULT, petName))
                .recipientEmail(recipientEmail)
                .build();
    }

    // 문의 게시판에 문의 등록 시 문의 담당 관리자에게 알림 전송
    public NotificationRequest createInquirySubmittedNotification(String senderEmail) {
        return NotificationRequest.builder()
                .type(NotificationType.INQUIRY_SUBMITTED)
                .content(template.generateMessage(NotificationType.INQUIRY_SUBMITTED, senderEmail))
                .recipientEmail(INQUIRY_ADMIN_EMAIL)
                .build();
    }

    // 문의 답변 알림 생성
    public NotificationRequest createInquiryAnsweredNotification(String recipientEmail) {
        return NotificationRequest.builder()
                .type(NotificationType.INQUIRY_ANSWERED)
                .content(template.generateMessage(NotificationType.INQUIRY_ANSWERED))
                .recipientEmail(recipientEmail)
                .build();
    }
}