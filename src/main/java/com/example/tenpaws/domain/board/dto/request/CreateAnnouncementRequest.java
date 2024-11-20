package com.example.tenpaws.domain.board.dto.request;

import com.example.tenpaws.domain.board.entity.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateAnnouncementRequest {

    private String title;
    private String content;

    public Announcement toEntity() {
        return Announcement.builder()
                .title(title)
                .content(content)
                .build();
    }
}
