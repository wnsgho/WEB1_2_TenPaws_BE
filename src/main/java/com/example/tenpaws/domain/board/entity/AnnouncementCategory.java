package com.example.tenpaws.domain.board.entity;

import lombok.Getter;

@Getter
public enum AnnouncementCategory {
    NOTICE("공지"),
    SUPPORT("지원");

    private final String displayName;

    AnnouncementCategory(String displayName) {
        this.displayName = displayName;
    }
}