package com.example.tenpaws.domain.pet.service;

import com.example.tenpaws.domain.pet.dto.PetRequestDTO;
import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.exception.PetException;
import com.example.tenpaws.domain.pet.repository.PetRepository;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
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
            throw new BaseException(ErrorCode.NOT_ASSIGNED);
        }
        return toPetResponseDTO(pet);
    }

    public PetResponseDTO createPet(Long shelterId, PetRequestDTO requestDTO) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = requestDTO.toEntity();
        Pet savedPet = petRepository.save(pet);
        return toPetResponseDTO(savedPet);
    }

    public PetResponseDTO updatePet(Long shelterId, Long petId, PetRequestDTO requestDTO) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = getPet(petId);
        if (!pet.getShelter().equals(shelter)) {
            throw new BaseException(ErrorCode.NOT_ASSIGNED);
        }
        pet = requestDTO.toEntity(); // update existing pet with new data
        Pet updatedPet = petRepository.save(pet);
        return toPetResponseDTO(updatedPet);
    }

    public void deletePet(Long shelterId, Long petId) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = getPet(petId);
        if (!pet.getShelter().equals(shelter)) {
            throw new BaseException(ErrorCode.NOT_ASSIGNED);
        }
        petRepository.delete(pet);
    }

    private Shelter getShelter(Long shelterId) {
        return shelterRepository.findById(shelterId)
                .orElseThrow(() -> new BaseException(ErrorCode.SHELTER_NOT_FOUND));
    }

    private Pet getPet(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(ErrorCode.PET_NOT_FOUND));
    }


}