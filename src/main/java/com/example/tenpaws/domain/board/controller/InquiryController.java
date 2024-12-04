package com.example.tenpaws.domain.board.controller;

import com.example.tenpaws.domain.board.dto.request.InquiryRequest;
import com.example.tenpaws.domain.board.dto.response.InquiryDetailResponse;
import com.example.tenpaws.domain.board.dto.response.InquiryListViewResponse;
import com.example.tenpaws.domain.board.dto.response.InquiryResponse;
import com.example.tenpaws.domain.board.service.InquiryService;
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
@Tag(name = "문의 기능 API", description = "문의 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @Operation(summary = "문의 생성", description = "문의 생성 API")
    @PreAuthorize("hasAnyRole('USER', 'SHELTER')")
    @PostMapping
    public ResponseEntity<InquiryResponse> create(
            @RequestBody InquiryRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(inquiryService.create(request, email));
    }

    @Operation(summary = "문의 목록 조회", description = "문의 목록 조회 API")
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Page<InquiryListViewResponse>> getList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(inquiryService.getList(pageable));
    }

    @Operation(summary = "문의 조회", description = "문의 조회 API")
    @PreAuthorize("permitAll()")
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailResponse> findById(@PathVariable Long inquiryId) {
        return ResponseEntity.ok(inquiryService.findById(inquiryId));
    }

    @Operation(summary = "문의 변경", description = "문의 변경 API")
    @PreAuthorize("hasAnyRole('USER', 'SHELTER') and @ownershipVerifier.isInquiryOwner(#inquiryId, authentication.name)")
    @PutMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponse> update(
            @PathVariable Long inquiryId,
            @RequestBody InquiryRequest request) {
        return ResponseEntity.ok(inquiryService.update(inquiryId, request));
    }

    @Operation(summary = "문의 삭제", description = "문의 삭제 API")
    @PreAuthorize("hasAnyRole('USER', 'SHELTER') and @ownershipVerifier.isInquiryOwner(#inquiryId, authentication.name)")
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<String> delete(@PathVariable Long inquiryId) {
        inquiryService.delete(inquiryId);
        return ResponseEntity.ok("문의글이 성공적으로 삭제되었습니다.");
    }
}