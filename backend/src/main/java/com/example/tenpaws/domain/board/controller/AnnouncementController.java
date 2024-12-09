package com.example.tenpaws.domain.board.controller;

import com.example.tenpaws.domain.board.dto.request.AnnouncementRequest;
import com.example.tenpaws.domain.board.dto.response.AnnouncementListViewResponse;
import com.example.tenpaws.domain.board.dto.response.AnnouncementResponse;
import com.example.tenpaws.domain.board.service.AnnouncementService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "공지사항 기능 API", description = "공지사항 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @Operation(summary = "공지사항 생성", description = "공지사항 생성을 위한 API")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<AnnouncementResponse> create(
            @RequestBody AnnouncementRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(announcementService.create(request, email));
    }

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록 조회를 위한 API")
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Page<AnnouncementListViewResponse>> getList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(announcementService.getList(pageable));
    }

    @Operation(summary = "공지사항 조회", description = "공지사항 조회를 위한 API")
    @PreAuthorize("permitAll()")
    @GetMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponse> findById(@PathVariable Long announcementId) {
        return ResponseEntity.ok(announcementService.findById(announcementId));
    }

    @Operation(summary = "공지사항 수정", description = "공지사항 수정을 위한 API, 관리자만 가능하게 설정")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN') and @ownershipVerifier.isAnnouncementOwner(#announcementId, authentication.name)")
    @PutMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponse> update(
            @PathVariable Long announcementId,
            @RequestBody AnnouncementRequest request) {
        return ResponseEntity.ok(announcementService.update(announcementId, request));
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제을 위한 API, 관리자만 가능하게 설정")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN') and @ownershipVerifier.isAnnouncementOwner(#announcementId, authentication.name)")
    @DeleteMapping("/{announcementId}")
    public ResponseEntity<String> delete(@PathVariable Long announcementId) {
        announcementService.delete(announcementId);
        return ResponseEntity.ok("Announcement successfully deleted");
    }

    @Operation(summary = "내가 작성한 공지사항 목록 조회", description = "관리자가 작성한 공지사항 목록을 조회하는 API")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("/my")
    public ResponseEntity<Page<AnnouncementListViewResponse>> getMyList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(announcementService.getMyList(email, pageable));
    }
}
