package com.example.tenpaws.domain.apply.controller;

import com.example.tenpaws.domain.apply.entity.Apply;
import com.example.tenpaws.domain.apply.service.ApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applypet")
public class ApplyController {

    private final ApplyService applyService;

    // 회원의 입양 신청
    @PostMapping
    public String applyForPet(@RequestParam Long petId, @RequestParam Long userId) {
        applyService.applyForPet(petId, userId);
        return "success";
    }

    // 보호소 신청 목록 조회
    @GetMapping("/{shelterId}/applies")
    public List<Apply> getAppliesForUser(@RequestParam Long shelterId) {
        return applyService.getAppliesForShelter(shelterId);
    }

    // 신청 상태 변경
    @PutMapping("/{shelterId}/status")
    public String updateApplyStatus(@RequestParam Long applyId, @RequestParam String status) {
        applyService.updateApplyStatus(applyId, status);
        return "status updated";
    }

}
