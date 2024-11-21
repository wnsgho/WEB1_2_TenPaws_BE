package com.example.tenpaws.domain.board.dto.response;

import com.example.tenpaws.domain.board.entity.Announcement;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AnnouncementListViewResponse {
    private final Long id;
    private final String title;
    private LocalDate created_at;

    public AnnouncementListViewResponse(Announcement announcement) {
        this.id = announcement.getId();
        this.title = announcement.getTitle();
        this.created_at = announcement.getCreated_at();
    }
}
