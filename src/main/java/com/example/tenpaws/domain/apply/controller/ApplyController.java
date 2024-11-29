package com.example.tenpaws.domain.apply.controller;

import com.example.tenpaws.domain.apply.dto.ApplyDto;
import com.example.tenpaws.domain.apply.service.ApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applypet")
public class ApplyController {

    private final ApplyService applyService;

    // 회원의 입양 신청
    @PostMapping
    public ResponseEntity<String> applyForPet(@RequestParam Long petId, @RequestParam Long userId) {
        //applyId 내에 들어가는 petId와 userId는 사용자가 신청하려는 동물과 사용자를 식별하는 값일 뿐, Apply 엔티티의 고유 ID인 applyId와는 구분됨
        try {
            applyService.ensureUserCanApply(userId, petId);
            applyService.applyForPet(petId, userId);
            return ResponseEntity.ok("Application submitted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 입양 신청 취소
    @PostMapping("/{applyId}/cancel")  // applyId 내에서 userId로 "필터링"하는 것이기 때문에 쿼리파라미터로 받음
    public ResponseEntity<String> cancelApply(
            @PathVariable Long applyId,
            @RequestParam Long userId) {
        applyService.cancelApply(applyId, userId);
        return ResponseEntity.ok("Application has been canceled successfully.");
    }

    // 회원 입양 신청 조회
    @GetMapping("/{userId}/list")
    public ResponseEntity<List<ApplyDto>> listApply(@PathVariable Long userId) {
        List<ApplyDto> userApplies = applyService.getApplies(userId);
        return ResponseEntity.ok(userApplies);
    }

    // 보호소 신청 목록 조회
    @GetMapping("/shelter/{shelterId}")
    public ResponseEntity<List<ApplyDto>> getAppliesForShelter(@PathVariable Long shelterId) {
        List<ApplyDto> applies = applyService.getAppliesForShelter(shelterId);
        return ResponseEntity.ok(applies);
    }

    // 신청 상태 변경
    @PutMapping("/{shelterId}/status") // shelterId로 고정된 상태에서 applyId는 정렬, status의 변화에 따라 여러가지 상태 중 하나를 나타내기 때문에 쿼리파라미터로 받음
    public ResponseEntity<ApplyDto> updateApplyStatus(@RequestParam Long applyId, @RequestParam String status, @PathVariable Long shelterId) {
        ApplyDto updatedApply = applyService.updateApplyStatus(applyId, status);
        return ResponseEntity.ok(updatedApply);
    }
}
