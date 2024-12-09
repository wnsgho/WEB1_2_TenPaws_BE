package com.example.tenpaws.domain.notification.sse;

import com.example.tenpaws.global.entity.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 회원이 일반, 보호소, 관리자 엔티티로 구분되어있어
 * UserRole + id로 식별
 */
@Getter
@RequiredArgsConstructor
public class UserIdentifier {
    private final UserRole userRole;
    private final Long userId;

    // 복합 키 생성 (예: "ROLE_ADMIN:3")
    public String toKey() {
        return userRole.name() + ":" + userId;
    }

    // 문자열로부터 UserIdentifier 객체 생성
    public static UserIdentifier fromKey(String key) {
        String[] parts = key.split(":");
        return new UserIdentifier(UserRole.valueOf(parts[0]), Long.parseLong(parts[1]));
    }
}