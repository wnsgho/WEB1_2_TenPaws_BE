package com.example.tenpaws.domain.board.dto.request;

import com.example.tenpaws.domain.board.entity.Announcement;
import com.example.tenpaws.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateAnnouncementRequest {

    private Long userId;
    private String title;
    private String content;

    public Announcement toEntity(User user) {
        return Announcement.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
    }
}
