package com.example.tenpaws.domain.board.controller;

import com.example.tenpaws.domain.board.dto.request.CreateInquiryRequest;
import com.example.tenpaws.domain.board.dto.request.UpdateInquiryRequest;
import com.example.tenpaws.domain.board.dto.response.InquiryDetailResponse;
import com.example.tenpaws.domain.board.dto.response.InquiryListViewResponse;
import com.example.tenpaws.domain.board.dto.response.InquiryResponse;
import com.example.tenpaws.domain.board.entity.Inquiry;
import com.example.tenpaws.domain.board.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PreAuthorize("hasRole('USER') or hasRole('SHELTER')")
    @PostMapping
    public ResponseEntity<InquiryResponse> create(@RequestBody CreateInquiryRequest request) {
        Inquiry savedInquiry = inquiryService.create(request);
        return ResponseEntity.ok().body(new InquiryResponse(savedInquiry));
    }

    @GetMapping
    public ResponseEntity<Page<InquiryListViewResponse>> getList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(inquiryService.getList(pageable));
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailResponse> findById(@PathVariable Long inquiryId) {
        InquiryDetailResponse response = inquiryService.findById(inquiryId);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('SHELTER')")
    @PutMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponse> update(
            @PathVariable Long inquiryId,
            @RequestBody UpdateInquiryRequest request) {
        Inquiry updatedInquiry = inquiryService.update(inquiryId, request);
        return ResponseEntity.ok().body(new InquiryResponse(updatedInquiry));
    }

    /**
     * userId와 shelterId가 선택적인 값이므로 @RequestParam 적용.
     * PathVariable을 사용하면 URL이 복잡해짐
     * ex. @DeleteMapping("/{inquiryId}/users/{userId}/shelters/{shelterId}")
     */
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<String> delete(
            @PathVariable Long inquiryId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long shelterId) {
        inquiryService.delete(inquiryId, userId, shelterId);
        return ResponseEntity.ok("문의글이 성공적으로 삭제되었습니다.");
    }
}