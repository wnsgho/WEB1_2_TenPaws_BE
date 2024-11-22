package com.example.tenpaws.domain.board.dto.request;

import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.domain.board.entity.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateAnnouncementRequest {

    private Long adminId;
    private String title;
    private String content;

    public Announcement toEntity(Admin admin) {
        return Announcement.builder()
                .admin(admin)
                .title(title)
                .content(content)
                .build();
    }
}
