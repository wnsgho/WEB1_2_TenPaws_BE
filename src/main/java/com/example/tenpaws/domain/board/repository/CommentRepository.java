package com.example.tenpaws.domain.board.repository;

import com.example.tenpaws.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}