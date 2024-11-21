package com.example.tenpaws.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAnnouncementRequest {
    private Long userId;
    private String title;
    private String content;
}
