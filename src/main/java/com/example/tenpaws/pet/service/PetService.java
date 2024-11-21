package com.example.tenpaws.pet.service;

import com.example.tenpaws.pet.dto.PetRequestDTO;
import com.example.tenpaws.pet.dto.PetResponseDTO;
import com.example.tenpaws.pet.entity.Pet;
import com.example.tenpaws.pet.exception.PetException;
import com.example.tenpaws.pet.repository.PetRepository;
import com.example.tenpaws.shelter.entity.Shelter;
import com.example.tenpaws.shelter.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    public List<PetResponseDTO> getAllPets(Long shelterId) {
        Shelter shelter = getShelter(shelterId);
        return petRepository.findByShelter(shelter).stream()
                .map(this::toPetResponseDTO)
                .collect(Collectors.toList());
    }

    public PetResponseDTO getPetById(Long shelterId, Long petId) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = getPet(petId);
        if (!pet.getShelter().equals(shelter)) {
            throw new PetException("This pet does not belong to this shelter.");
        }
        return toPetResponseDTO(pet);
    }

    public PetResponseDTO createPet(Long shelterId, PetRequestDTO petRequestDTO) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = new Pet();
        pet.setSpecies(petRequestDTO.getSpecies());
        pet.setSize(petRequestDTO.getSize());
        pet.setAge(petRequestDTO.getAge());
        pet.setPersonality(petRequestDTO.getPersonality());
        pet.setExerciseLevel(petRequestDTO.getExerciseLevel());
        pet.setShelter(shelter);
        Pet savedPet = petRepository.save(pet);
        return toPetResponseDTO(savedPet);
    }

    public PetResponseDTO updatePet(Long shelterId, Long petId, PetRequestDTO petRequestDTO) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = getPet(petId);
        if (!pet.getShelter().equals(shelter)) {
            throw new PetException("The pet is not assigned to this shelter.");
        }
        pet.setSpecies(petRequestDTO.getSpecies());
        pet.setSize(petRequestDTO.getSize());
        pet.setAge(petRequestDTO.getAge());
        pet.setPersonality(petRequestDTO.getPersonality());
        pet.setExerciseLevel(petRequestDTO.getExerciseLevel());
        Pet updatedPet = petRepository.save(pet);
        return toPetResponseDTO(updatedPet);
    }

    public void deletePet(Long shelterId, Long petId) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = getPet(petId);
        if (!pet.getShelter().equals(shelter)) {
            throw new PetException("The pet is not assigned to this shelter.");
        }
        petRepository.delete(pet);
    }

    private Shelter getShelter(Long shelterId) {
        return shelterRepository.findById(shelterId)
                .orElseThrow(() -> new PetException("Shelter not found with ID: " + shelterId));
    }

    private Pet getPet(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new PetException("Pet not found with ID: " + petId));
    }

    private PetResponseDTO toPetResponseDTO(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setPetId(pet.getId());
        dto.setSpecies(pet.getSpecies());
        dto.setSize(pet.getSize());
        dto.setAge(pet.getAge());
        dto.setPersonality(pet.getPersonality());
        dto.setExerciseLevel(pet.getExerciseLevel());
        return dto;
    }
}