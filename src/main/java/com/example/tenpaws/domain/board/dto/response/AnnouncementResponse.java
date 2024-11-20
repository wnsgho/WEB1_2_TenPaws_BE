package com.example.tenpaws.domain.board.dto.response;


import com.example.tenpaws.domain.board.entity.Announcement;
import lombok.Getter;

@Getter
public class AnnouncementResponse {

    private final Long id;
    private final String title;
    private final String content;

    public AnnouncementResponse(Announcement announcement) {
        this.id = announcement.getId();
        this.title = announcement.getTitle();
        this.content = announcement.getContent();
    }
}
