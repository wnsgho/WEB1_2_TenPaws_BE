package com.example.tenpaws.domain.shelter.controller;

import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.shelter.dto.ShelterResponseDTO;
import com.example.tenpaws.domain.shelter.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shelters")
@Tag(name = "보호소 기능 API", description = "보호소 기능을 모아둔 컨트롤러 입니다")
@RequiredArgsConstructor
public class ShelterController {
    private final ShelterService shelterService;

    // 전체 쉘터 조회
    @Operation(summary = "전체 쉘터 조회", description = "전체 쉘터 정보 반환을 위한 API")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ShelterResponseDTO>> getAllShelters() {
        return ResponseEntity.ok(shelterService.getAllShelters());
    }

    // ID로 쉘터 조회
    @Operation(summary = "ID로 쉘터 조회", description = "ID를 통한 쉘터 정보 반환 API")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_SHELTER')")
    @GetMapping("/{id}")
    public ResponseEntity<ShelterResponseDTO> getShelterById(@PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getShelterById(id));
    }

    // 쉘터 정보 수정
    @Operation(summary = "쉘터 정보 수정", description = "쉘터 정보 수정을 위한 API")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or hasRole('ROLE_SHELTER') and @petService.isShelterApplicant(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<ShelterResponseDTO> updateShelter(@PathVariable Long id, @RequestBody ShelterRequestDTO shelterRequestDTO) {
        ShelterResponseDTO updatedShelter = shelterService.updateShelter(id, shelterRequestDTO);
        return ResponseEntity.ok(updatedShelter);
    }

    // 쉘터 삭제
    @Operation(summary = "쉘터 정보 삭제", description = "쉘터 정보 삭제를 위한 API")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or hasRole('ROLE_SHELTER') and @petService.isShelterApplicant(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.ok("Deleted shelter");
    }
}

