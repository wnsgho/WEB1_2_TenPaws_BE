package com.example.tenpaws.domain.board.dto.response;

import com.example.tenpaws.domain.board.entity.Inquiry;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InquiryResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String writerName;
    private final Long viewCount;
    private final LocalDate created_at;

    public InquiryResponse(Inquiry inquiry) {
        this.id = inquiry.getId();
        this.title = inquiry.getTitle();
        this.content = inquiry.getContent();
        this.writerName = inquiry.getWriterName();
        this.viewCount = inquiry.getViewCount();
        this.created_at = inquiry.getCreated_at();
    }
}
