package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.board.dto.request.CreateCommentRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateCommentRequest;
import com.example.tenpaws.domain.board.dto.response.CommentResponse;

public interface CommentService {
    CommentResponse create(Long inquiryId, CreateCommentRequest request);
    CommentResponse update(Long commentId, UpdateCommentRequest request);
    void delete(Long commentId);
}
