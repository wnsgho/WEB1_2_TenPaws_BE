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
@Tag(name = "질의응답 기능 API", description = "질의응답 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/faqs")
public class FaqController {
    private final FaqService faqService;

    @Operation(summary = "모든 질의응답 조회", description = "모든 질의응답 조회 API")
    @GetMapping
    public ResponseEntity<List<FaqResponse>> getAll() {
        return ResponseEntity.ok(faqService.findAll());
    }

    @Operation(summary = "최상단 질의응답 조회", description = "최상단 질의응답 조회 API")
    @GetMapping("/top-level")
    public ResponseEntity<List<FaqResponse>> getTopLevelFaq() {
        return ResponseEntity.ok(faqService.read());
    }

    @Operation(summary = "???", description = "???")
    @GetMapping("/{parentId}")
    public ResponseEntity<List<FaqResponse>> getChildFaq(@PathVariable("parentId") Long parentId) {
        return ResponseEntity.ok(faqService.findByParentId(parentId));
    }

    @Operation(summary = "질의응답 생성", description = "질의응답 생성 API")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<FaqResponse> createFaq(@Valid @RequestBody FaqRequest faqRequest) {
        return ResponseEntity.ok(faqService.create(faqRequest));
    }

    @Operation(summary = "모든 질의응답 조회", description = "모든 질의응답 조회 API")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @PutMapping("/{faqId}")
    public ResponseEntity<FaqResponse> updateFaq(@Valid @RequestBody FaqRequest faqRequest, @PathVariable("faqId") Long faqId) {
        faqRequest.setFaqId(faqId);
        return ResponseEntity.ok(faqService.update(faqRequest));
    }

    @Operation(summary = "질의응답 삭제", description = "질의응답 삭제 API")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{faqId}")
    public ResponseEntity<Map<String, String>> deleteFaq(@PathVariable("faqId") Long faqId) {
        faqService.delete(faqId);
        return ResponseEntity.ok(Map.of("message", "Faq deleted"));
    }
}
