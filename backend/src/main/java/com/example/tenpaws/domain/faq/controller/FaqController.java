package com.example.tenpaws.domain.faq.controller;

import com.example.tenpaws.domain.faq.dto.FaqRequest;
import com.example.tenpaws.domain.faq.dto.FaqResponse;
import com.example.tenpaws.domain.faq.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "FAQ 기능 API", description = "FAQ 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/faqs")
public class FaqController {
    private final FaqService faqService;

    @Operation(summary = "모든 FAQ 조회", description = "모든 FAQ 조회 API")
    @GetMapping
    public ResponseEntity<List<FaqResponse>> getAll() {
        return ResponseEntity.ok(faqService.findAll());
    }

    @Operation(summary = "최상위 FAQ 조회", description = "최상위 FAQ 조회 API")
    @GetMapping("/top-level")
    public ResponseEntity<List<FaqResponse>> getTopLevelFaq() {
        return ResponseEntity.ok(faqService.read());
    }

    @Operation(summary = "하위 FAQ 조회", description = "하위 FAQ 조회 API")
    @GetMapping("/{parentId}")
    public ResponseEntity<List<FaqResponse>> getChildFaq(@PathVariable("parentId") Long parentId) {
        return ResponseEntity.ok(faqService.findByParentId(parentId));
    }

    @Operation(summary = "FAQ 생성", description = "FAQ 생성 API")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<FaqResponse> createFaq(@Valid @RequestBody FaqRequest faqRequest) {
        return ResponseEntity.ok(faqService.create(faqRequest));
    }

    @Operation(summary = "FAQ 수정", description = "FAQ 수정 API")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @PutMapping("/{faqId}")
    public ResponseEntity<FaqResponse> updateFaq(@Valid @RequestBody FaqRequest faqRequest, @PathVariable("faqId") Long faqId) {
        faqRequest.setFaqId(faqId);
        return ResponseEntity.ok(faqService.update(faqRequest));
    }

    @Operation(summary = "FAQ 삭제", description = "FAQ 삭제 API")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{faqId}")
    public ResponseEntity<Map<String, String>> deleteFaq(@PathVariable("faqId") Long faqId) {
        faqService.delete(faqId);
        return ResponseEntity.ok(Map.of("message", "Faq deleted"));
    }
}
