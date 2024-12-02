package com.example.tenpaws.domain.board.dto.response;

import com.example.tenpaws.domain.board.entity.Inquiry;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class InquiryDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String writerName;
    private final String writerType;
    private final Long viewCount;
    private final LocalDate created_at;
    private final List<CommentResponse> comments;

    public InquiryDetailResponse(Inquiry inquiry, List<CommentResponse> comments) {
        this.id = inquiry.getId();
        this.title = inquiry.getTitle();
        this.content = inquiry.getContent();
        if (inquiry.getUser() != null) {
            this.writerName = inquiry.getUser().getUsername();
            this.writerType = "USER";
        } else {
            this.writerName = inquiry.getShelter().getShelterName();
            this.writerType = "SHELTER";
        }
        this.viewCount = inquiry.getViewCount();
        this.created_at = inquiry.getCreated_at();
        this.comments = comments;
    }
}