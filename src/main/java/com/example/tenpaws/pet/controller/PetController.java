package com.example.tenpaws.pet.controller;

import com.example.tenpaws.pet.dto.PetRequestDTO;
import com.example.tenpaws.pet.dto.PetResponseDTO;
import com.example.tenpaws.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelters/{shelterId}/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public List<PetResponseDTO> getAllPets(@PathVariable Long shelterId) {
        return petService.getAllPets(shelterId);
    }

    @GetMapping("/{petId}")
    public PetResponseDTO getPetById(@PathVariable Long shelterId, @PathVariable Long petId) {
        return petService.getPetById(shelterId, petId);
    }

    @PostMapping
    public PetResponseDTO createPet(@PathVariable Long shelterId, @RequestBody PetRequestDTO petRequestDTO) {
        return petService.createPet(shelterId, petRequestDTO);
    }

    @PutMapping("/{petId}")
    public PetResponseDTO updatePet(@PathVariable Long shelterId, @PathVariable Long petId, @RequestBody PetRequestDTO petRequestDTO) {
        return petService.updatePet(shelterId, petId, petRequestDTO);
    }

    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable Long shelterId, @PathVariable Long petId) {
        petService.deletePet(shelterId, petId);
    }
}