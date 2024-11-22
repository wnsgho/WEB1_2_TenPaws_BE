package com.example.tenpaws.domain.board.dto.response;

import com.example.tenpaws.domain.board.entity.Announcement;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AnnouncementResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDate created_at;

    public AnnouncementResponse(Announcement announcement) {
        this.id = announcement.getId();
        this.title = announcement.getTitle();
        this.content = announcement.getContent();
        this.created_at = announcement.getCreated_at();
    }
}
