package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.board.dto.request.CreateCommentRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateCommentRequest;
import com.example.tenpaws.domain.board.entity.Comment;

public interface CommentService {
    Comment create(Long inquiryId, CreateCommentRequest request);
    Comment update(Long commentId, UpdateCommentRequest request);
    void delete(Long commentId);
}
