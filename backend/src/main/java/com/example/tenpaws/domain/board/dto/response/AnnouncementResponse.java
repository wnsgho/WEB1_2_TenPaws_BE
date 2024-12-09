package com.example.tenpaws.domain.board.dto.response;

import com.example.tenpaws.domain.board.entity.Announcement;
import com.example.tenpaws.domain.board.entity.AnnouncementCategory;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AnnouncementResponse {
    private final Long id;
    private final AnnouncementCategory category;
    private final String title;
    private final String content;
    private final Long viewCount;
    private final LocalDate created_at;

    public AnnouncementResponse(Announcement announcement) {
        this.id = announcement.getId();
        this.category = announcement.getCategory();
        this.title = announcement.getTitle();
        this.content = announcement.getContent();
        this.viewCount = announcement.getViewCount();
        this.created_at = announcement.getCreated_at();
    }
}
