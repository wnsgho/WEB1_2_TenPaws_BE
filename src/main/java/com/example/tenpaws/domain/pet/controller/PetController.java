package com.example.tenpaws.domain.pet.controller;

import com.example.tenpaws.domain.pet.dto.PetRequestDTO;
import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import com.example.tenpaws.domain.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/slist/{shelterId}") // 쉘터id 로 전체 조회기능인데, 전체 목록에 반영할 all list로 변경할 예정입니다
    public List<PetResponseDTO> getAllPets(@PathVariable Long shelterId) {
        return petService.getAllPets(shelterId);
    }

    @GetMapping("/{shelterId}/list/{petId}")
    public PetResponseDTO getPetById(@PathVariable Long shelterId, @PathVariable Long petId) {
        return petService.getPetById(shelterId, petId);
    }

    @PostMapping("/{shelterId}")
    public ResponseEntity<PetResponseDTO> createPet(@PathVariable Long shelterId, @RequestBody PetRequestDTO petRequestDTO) {
        PetResponseDTO createdPet = petService.createPet(shelterId, petRequestDTO);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @PutMapping("/{shelterId}/{petId}")
    public ResponseEntity<PetResponseDTO> updatePet(@PathVariable Long shelterId, @PathVariable Long petId, @RequestBody PetRequestDTO petRequestDTO) {
        PetResponseDTO updatedPet = petService.updatePet(shelterId, petId, petRequestDTO);
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    @DeleteMapping("/{shelterId}/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long shelterId, @PathVariable Long petId) {
        petService.deletePet(shelterId, petId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
