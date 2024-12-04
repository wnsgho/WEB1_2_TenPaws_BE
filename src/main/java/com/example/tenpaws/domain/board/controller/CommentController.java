package com.example.tenpaws.domain.board.controller;

import com.example.tenpaws.domain.board.dto.request.CommentRequest;
import com.example.tenpaws.domain.board.dto.response.CommentResponse;
import com.example.tenpaws.domain.board.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "답변 기능 API", description = "답변 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/inquiries/{inquiryId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "답변 생성", description = "답변 생성을 위한 API")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long inquiryId,
            @RequestBody CommentRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(commentService.create(inquiryId, request, email));
    }

    @Operation(summary = "답변 수정", description = "답변 수정을 위한 API")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN') and @ownershipVerifier.isCommentOwner(#commentId, authentication.name)")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.update(commentId, request));
    }

    @Operation(summary = "답변 삭제", description = "답변 삭제를 위한 API")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN') and @ownershipVerifier.isCommentOwner(#commentId, authentication.name)")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok("답변이 성공적으로 삭제되었습니다.");
    }
}