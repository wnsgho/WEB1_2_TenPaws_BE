package com.example.tenpaws.domain.notification.factory;

import com.example.tenpaws.domain.notification.dto.request.CreateNotificationRequest;
import com.example.tenpaws.domain.notification.entity.NotificationType;
import com.example.tenpaws.domain.notification.template.NotificationTemplate;
import com.example.tenpaws.global.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationFactory {
    private final NotificationTemplate template;
    private static final Long INQUIRY_ADMIN_ID = 1L; // 문의 담당 관리자 고유번호

//    // 입양 신청 완료 알림 생성
//    public CreateNotificationRequest createAdoptionSubmittedNotification(Long userId) {
//        return CreateNotificationRequest.builder()
//                .type(NotificationType.ADOPTION_APPLICATION_SUBMITTED)
//                .content(template.generateMessage(NotificationType.ADOPTION_APPLICATION_SUBMITTED))
//                .userId(userId)
//                .build();
//    }
//
//    // 입양 신청 접수 알림 생성 (보호소용)
//    public CreateNotificationRequest createAdoptionReceivedNotification(Long shelterId, String userName, String petName) {
//        return CreateNotificationRequest.builder()
//                .type(NotificationType.ADOPTION_APPLICATION_RECEIVED)
//                .content(template.generateMessage(NotificationType.ADOPTION_APPLICATION_RECEIVED, userName, petName))
//                .userId(shelterId)
//                .build();
//    }
//
//    // 입양 신청 결과 알림 생성
//    public CreateNotificationRequest createAdoptionResultNotification(Long userId, String petName) {
//        return CreateNotificationRequest.builder()
//                .type(NotificationType.ADOPTION_APPLICATION_RESULT)
//                .content(template.generateMessage(NotificationType.ADOPTION_APPLICATION_RESULT, petName))
//                .userId(userId)
//                .build();
//    }

    // 문의 게시판에 문의 등록 시 문의 담당 관리자에게 알림 전송
    public CreateNotificationRequest createInquirySubmittedNotification(String email) {
        return CreateNotificationRequest.builder()
                .type(NotificationType.INQUIRY_SUBMITTED)
                .content(template.generateMessage(NotificationType.INQUIRY_SUBMITTED, email))
                .userRole(UserRole.ROLE_ADMIN)
                .userId(INQUIRY_ADMIN_ID)
                .build();
    }

    // 문의 답변 알림 생성
    public CreateNotificationRequest createInquiryAnsweredNotification(Long userId, UserRole userRole) {
        return CreateNotificationRequest.builder()
                .type(NotificationType.INQUIRY_ANSWERED)
                .content(template.generateMessage(NotificationType.INQUIRY_ANSWERED))
                .userRole(userRole)
                .userId(userId)
                .build();
    }
}