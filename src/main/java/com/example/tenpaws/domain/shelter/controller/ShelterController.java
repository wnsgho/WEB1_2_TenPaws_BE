package com.example.tenpaws.domain.shelter.controller;

import com.example.tenpaws.domain.shelter.service.ShelterService;
import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.shelter.dto.ShelterResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shelters")
public class ShelterController {

    private final ShelterService shelterService;

    @Autowired
    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    // 전체 쉘터 조회
    @GetMapping
    public List<ShelterResponseDTO> getAllShelters() {
        return shelterService.getAllShelters();
    }

    // ID로 쉘터 조회
    @GetMapping("/{id}")
    public ShelterResponseDTO getShelterById(@PathVariable Long id) {
        return shelterService.getShelterById(id);
    }

    // 쉘터 등록
    @PostMapping
    public ResponseEntity<Object> createShelter(@RequestBody ShelterRequestDTO shelterRequestDTO) {
        ShelterResponseDTO createdShelter = shelterService.createShelter(shelterRequestDTO);
        return new ResponseEntity<>(createdShelter, HttpStatus.CREATED);
    }

    // 쉘터 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateShelter(@PathVariable Long id, @RequestBody ShelterRequestDTO shelterRequestDTO) {
        ShelterResponseDTO updatedShelter = shelterService.updateShelter(id, shelterRequestDTO);
        return new ResponseEntity<>(updatedShelter, HttpStatus.OK);
    }

    // 쉘터 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
        return new ResponseEntity<>("삭제 완료", HttpStatus.NO_CONTENT);
    }
}

