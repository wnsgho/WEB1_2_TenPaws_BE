package com.example.tenpaws.domain.user.entity;

import com.example.tenpaws.global.entity.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2UserEntity {

    @Id
    private String userId;
    private String email;
    private String type;
    private String role;

    public OAuth2UserEntity (String userId, String email, String type) {
        this.userId = userId;
        this.email = email;
        this.type = type;
        this.role = UserRole.ROLE_USER.name();
    }
}
