package com.example.tenpaws.domain.board.controller;

import com.example.tenpaws.domain.board.dto.response.CommentResponse;
import com.example.tenpaws.domain.board.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "관리자 답변 조회 API", description = "관리자가 작성한 답변 조회 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @Operation(summary = "내가 작성한 답변 목록 조회", description = "현재 로그인한 관리자가 작성한 모든 답변 목록을 조회하는 API")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("/my")
    public ResponseEntity<Page<CommentResponse>> getMyList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(commentService.getMyList(email, pageable));
    }
}