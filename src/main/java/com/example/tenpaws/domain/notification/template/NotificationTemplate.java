package com.example.tenpaws.domain.notification.template;

import com.example.tenpaws.domain.notification.entity.NotificationType;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationTemplate {

    public String generateMessage(NotificationType type, String... params) {
        return switch (type) {
//            case ADOPTION_APPLICATION_SUBMITTED -> "입양 신청이 완료되었습니다.";
//            case ADOPTION_APPLICATION_RECEIVED -> String.format("%s회원이 %s 반려동물을 입양 신청했습니다.", params[0], params[1]);
//            case ADOPTION_APPLICATION_RESULT -> String.format("%s 반려동물에 대한 입양 신청 결과가 등록되었습니다.", params[0]);
            case INQUIRY_SUBMITTED -> String.format("%s님이 문의를 등록하였습니다.", params[0]);
            case INQUIRY_ANSWERED -> "작성하신 문의에 답변이 등록되었습니다.";
            default -> throw new BaseException(ErrorCode.NOTIFICATION_TYPE_NOT_FOUND);
        };
    }
}