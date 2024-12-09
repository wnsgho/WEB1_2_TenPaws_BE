package com.example.tenpaws.global.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
    ROLE_SUPER_ADMIN, ROLE_ADMIN, ROLE_SHELTER, ROLE_USER;

    @JsonCreator
    public static UserRole fromString(String role) {
        return UserRole.valueOf(role.toUpperCase());
    }
}
