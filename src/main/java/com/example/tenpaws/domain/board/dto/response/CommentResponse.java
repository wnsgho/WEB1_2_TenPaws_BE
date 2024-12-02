package com.example.tenpaws.domain.board.dto.response;

import com.example.tenpaws.domain.board.entity.Comment;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentResponse {
    private final Long id;
    private final String content;
    private final String adminName;
    private final LocalDate created_at;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.adminName = comment.getAdmin().getUsername();
        this.created_at = comment.getCreated_at();
    }
}
