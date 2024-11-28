package com.example.tenpaws.domain.shelter.controller;

import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.shelter.dto.ShelterResponseDTO;
import com.example.tenpaws.domain.shelter.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shelters")
@RequiredArgsConstructor
public class ShelterController {
    private final ShelterService shelterService;

    // 전체 쉘터 조회
    @GetMapping
    public ResponseEntity<List<ShelterResponseDTO>> getAllShelters() {
        return ResponseEntity.ok(shelterService.getAllShelters());
    }

    // ID로 쉘터 조회
    @GetMapping("/{id}")
    public ResponseEntity<ShelterResponseDTO> getShelterById(@PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getShelterById(id));
    }

    // 쉘터 등록
    @PostMapping
    public ResponseEntity<ShelterResponseDTO> createShelter(@RequestBody ShelterRequestDTO shelterRequestDTO) {
        ShelterResponseDTO createdShelter = shelterService.createShelter(shelterRequestDTO);
        return ResponseEntity.ok(createdShelter);
    }

    // 쉘터 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<ShelterResponseDTO> updateShelter(@PathVariable Long id, @RequestBody ShelterRequestDTO shelterRequestDTO) {
        ShelterResponseDTO updatedShelter = shelterService.updateShelter(id, shelterRequestDTO);
        return ResponseEntity.ok(updatedShelter);
    }

    // 쉘터 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.ok("Deleted shelter");
    }
}

