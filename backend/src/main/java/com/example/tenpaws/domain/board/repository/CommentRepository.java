package com.example.tenpaws.domain.board.repository;

import com.example.tenpaws.domain.board.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByAdminEmail(String email, Pageable pageable);
}