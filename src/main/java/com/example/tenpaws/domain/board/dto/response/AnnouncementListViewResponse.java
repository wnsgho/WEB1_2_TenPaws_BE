package com.example.tenpaws.domain.board.dto.response;

import com.example.tenpaws.domain.board.entity.Announcement;
import lombok.Getter;

@Getter
public class AnnouncementListViewResponse {

    private final Long id;
    private final String title;

    public AnnouncementListViewResponse(Announcement announcement) {
        this.id = announcement.getId();
        this.title = announcement.getTitle();
    }
}
