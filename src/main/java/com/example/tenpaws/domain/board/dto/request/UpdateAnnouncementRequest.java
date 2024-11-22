package com.example.tenpaws.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAnnouncementRequest {
    private String title;
    private String content;
}
