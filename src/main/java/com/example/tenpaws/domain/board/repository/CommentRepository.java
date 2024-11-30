package com.example.tenpaws.domain.board.repository;

import com.example.tenpaws.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByInquiryId(Long inquiryId);
}