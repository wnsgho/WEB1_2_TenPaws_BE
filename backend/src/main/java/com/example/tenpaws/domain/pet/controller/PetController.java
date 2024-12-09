package com.example.tenpaws.domain.pet.controller;

import com.example.tenpaws.domain.pet.dto.PetRequestDTO;
import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import com.example.tenpaws.domain.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "펫 기능 API", description = "펫 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetService petService;

    @Operation(summary = "반려동물 상세 정보 조회", description = "반려동물 상세 정보 조회를 위한 API")
    @PreAuthorize("permitAll()")
    @GetMapping("/{petId}")
    public PetResponseDTO getPetById(@PathVariable Long petId) {
        return petService.getPetById(petId, null);
    }

    @Operation(summary = "반려동물 전체 리스트 정보 조회", description = "반려동물 전체 리스트 정보 조회를 위한 API")
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<PetResponseDTO> getPetList() {
        return petService.getPetList();
    }

    @Operation(summary = "보호소 반려동물 정보 조회", description = "보호소별 반려동물 전체 정보 조회를 위한 API")
    @PreAuthorize("hasRole('ROLE_SHELTER')")
    @GetMapping("/{shelterId}/list")
    public List<PetResponseDTO> getAllPets(@PathVariable Long shelterId) {
        return petService.getAllPets(shelterId);
    }

    @Operation(summary = "ID를 통한 반려동물 정보 조회", description = "보호소ID를 통한 반려동물 상세 정보 조회를 위한 API")
    @PreAuthorize("hasRole('ROLE_SHELTER')")
    @GetMapping("/{petId}/{shelterId}")
    public PetResponseDTO getPetById(
            @PathVariable Long petId,
            @PathVariable Long shelterId) {
        return petService.getPetById(petId, shelterId);
    }

    @Operation(summary = "반려동물 등록", description = "반려동물 등록을 위한 API")
    @PreAuthorize("hasRole('ROLE_SHELTER') and @petService.isShelterApplicant(#shelterId)")
    @PostMapping("/{shelterId}")
    public ResponseEntity<PetResponseDTO> createPet(
            @PathVariable Long shelterId,
            @RequestPart("petData") PetRequestDTO petRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        petRequestDTO.setImages(images);
        PetResponseDTO createdPet = petService.createPet(shelterId, petRequestDTO);
        return ResponseEntity.ok(createdPet);
    }


    @Operation(summary = "반려동물 정보 수정", description = "반려동물 정보 수정을 위한 API")
    @PreAuthorize("hasRole('ROLE_SHELTER')and @petService.isShelterApplicant(#shelterId)")
    @PutMapping("/{shelterId}/{petId}")
    public ResponseEntity<PetResponseDTO> updatePet(
            @PathVariable Long shelterId,
            @PathVariable Long petId,
            @RequestPart("petData") PetRequestDTO petRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        petRequestDTO.setImages(images);
        PetResponseDTO updatedPet = petService.updatePet(shelterId, petId, petRequestDTO);
        return ResponseEntity.ok(updatedPet);
    }




    @Operation(summary = "반려동물 정보 삭제", description = "반려동물 정보 삭제를 위한 API")
    @PreAuthorize("hasRole('ROLE_SHELTER') and @petService.isShelterApplicant(#shelterId) or hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
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
