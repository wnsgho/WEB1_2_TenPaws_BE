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
public class AdminResponseDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole userRole;

    public static AdminResponseDTO fromEntity(Admin admin) {
        return AdminResponseDTO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .userRole(admin.getUserRole())
                .build();
    }
}

