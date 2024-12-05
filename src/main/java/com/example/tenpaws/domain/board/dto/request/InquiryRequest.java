package com.example.tenpaws.domain.board.dto.request;

import com.example.tenpaws.domain.board.entity.Inquiry;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InquiryRequest {
    private String title;
    private String content;

    public Inquiry toEntity(User user) {
        return Inquiry.builder()
                .writerEmail(user.getEmail())
                .writerName(user.getUsername())
                .title(title)
                .content(content)
                .viewCount(0L)
                .build();
    }

    public Inquiry toEntity(Shelter shelter) {
        return Inquiry.builder()
                .writerEmail(shelter.getEmail())
                .writerName(shelter.getShelterName())
                .title(title)
                .content(content)
                .viewCount(0L)
                .build();
    }
}