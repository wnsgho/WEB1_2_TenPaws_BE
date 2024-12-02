package com.example.tenpaws.domain.board.dto.request;

import com.example.tenpaws.domain.board.entity.AnnouncementCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAnnouncementRequest {
    private AnnouncementCategory category;
    private String title;
    private String content;
}
