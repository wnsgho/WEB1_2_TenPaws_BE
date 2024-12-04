package com.example.tenpaws.domain.pet.controller;

import com.example.tenpaws.domain.pet.dto.PetRequestDTO;
import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import com.example.tenpaws.domain.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping("{petId}")
    public PetResponseDTO getPetById(@PathVariable Long petId) {
        return petService.getPetById(petId, null);
    }
    
    @GetMapping
    public List<PetResponseDTO> getPetList() {
        return petService.getPetList();
    }

    @GetMapping("/{shelterId}/list")
    public List<PetResponseDTO> getAllPets(@PathVariable Long shelterId) {
        return petService.getAllPets(shelterId);
    }

    @GetMapping("/{shelterId}/{petId}")
    public PetResponseDTO getPetById(
            @PathVariable Long shelterId,
            @PathVariable Long petId) {
        return petService.getPetById(shelterId, petId);
    }

    @PostMapping("/{shelterId}")
    public ResponseEntity<PetResponseDTO> createPet(
            @PathVariable Long shelterId,
            @RequestPart("petData") PetRequestDTO petRequestDTO) {
        PetResponseDTO createdPet = petService.createPet(shelterId, petRequestDTO);
        return ResponseEntity.ok(createdPet);
    }


    @PutMapping("/{shelterId}/{petId}")
    public ResponseEntity<PetResponseDTO> updatePet(
            @PathVariable Long shelterId,
            @PathVariable Long petId,
            @RequestPart("petData") PetRequestDTO petRequestDTO) {
        PetResponseDTO updatedPet = petService.updatePet(shelterId, petId, petRequestDTO);
        return ResponseEntity.ok(updatedPet);
    }


    @DeleteMapping("/{shelterId}/{petId}")
    public ResponseEntity<Map<String, String>> deletePet(
            @PathVariable Long shelterId,
            @PathVariable Long petId) {
        try {
            petService.deletePet(shelterId, petId);
            return ResponseEntity.ok(Map.of("result", "success"));
        } catch (Exception e) {
            // 예외 처리 로직
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("result", "fail"));
        }
    }


}
