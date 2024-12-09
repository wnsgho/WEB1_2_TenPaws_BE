package com.example.tenpaws.domain.admin.dto;

import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.global.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequestDTO {

    private String username;
    private String password;
    private String email;
    private UserRole userRole;

    public Admin toEntity() {
        return Admin.builder()
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .userRole(this.userRole)
                .build();
    }
}
