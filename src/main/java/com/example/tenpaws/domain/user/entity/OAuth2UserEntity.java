package com.example.tenpaws.domain.user.entity;

import com.example.tenpaws.global.entity.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class OAuth2UserEntity {

    @Id
    private String userId;
    private String email;
    private String type;
    private String role;
    private String username = UUID.randomUUID().toString(); // 유저 이름 변경 가능하게 필드 추가

    public OAuth2UserEntity (String userId, String email, String type) {
        this.userId = userId;
        this.email = email;
        this.type = type;
        this.role = UserRole.ROLE_USER.name();
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeUserRole(UserRole userRole) {
        this.role = userRole.name();
    }

    public void changeUsername(String username) {
        this.username = username;
    }
}
