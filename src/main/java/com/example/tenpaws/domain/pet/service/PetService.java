package com.example.tenpaws.domain.pet.service;

import com.example.tenpaws.domain.pet.dto.PetRequestDTO;
import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.repository.PetRepository;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private PetRepository petRepository;

    public List<PetResponseDTO> getAllPets(Long shelterId) {
        Shelter shelter = getShelter(shelterId);
        return petRepository.findByShelter(shelter).stream()
                .map(this::toPetResponseDTO)
                .collect(Collectors.toList());
    }

    public PetResponseDTO getPetById(Long shelterId, Long petId) {
        Shelter shelter = getShelter(shelterId);
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isPresent()) {
            Pet pet = optionalPet.get();
            if (!pet.getShelter().equals(shelter)) {
                throw new BaseException(ErrorCode.NOT_ASSIGNED);
            }
            return toPetResponseDTO(pet);
        } else {
            throw new BaseException(ErrorCode.PET_NOT_FOUND);
        }
    }

    public PetResponseDTO createPet(Long shelterId, PetRequestDTO requestDTO) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = requestDTO.toEntity();
        pet.setShelter(shelter);
        shelter.addPet(pet);
        Pet savedPet = petRepository.save(pet);
        return toPetResponseDTO(savedPet);
    }

    public PetResponseDTO updatePet(Long shelterId, Long petId, PetRequestDTO requestDTO) {
        Shelter shelter = getShelter(shelterId);
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isPresent()) {
            Pet pet = optionalPet.get();
            if (!pet.getShelter().equals(shelter)) {
                throw new BaseException(ErrorCode.NOT_ASSIGNED);
            }

            // Update the pet with new data from requestDTO
            pet.setSpecies(requestDTO.getSpecies());
            pet.setSize(requestDTO.getSize());
            pet.setAge(requestDTO.getAge());
            pet.setPersonality(requestDTO.getPersonality());
            pet.setExerciseLevel(requestDTO.getExerciseLevel());

            Pet updatedPet = petRepository.save(pet);
            return toPetResponseDTO(updatedPet);
        } else {
            throw new BaseException(ErrorCode.PET_NOT_FOUND);
        }
    }

    public void deletePet(Long shelterId, Long petId) {
        Shelter shelter = getShelter(shelterId);
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isPresent()) {
            Pet pet = optionalPet.get();
            if (!pet.getShelter().equals(shelter)) {
                throw new BaseException(ErrorCode.NOT_ASSIGNED);
            }
            shelter.removePet(pet);
            petRepository.delete(pet);
        } else {
            throw new BaseException(ErrorCode.PET_NOT_FOUND);
        }
    }

    private Shelter getShelter(Long shelterId) {
        return shelterRepository.findById(shelterId)
                .orElseThrow(() -> new BaseException(ErrorCode.SHELTER_NOT_FOUND));
    }

    private Pet getPet(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(ErrorCode.PET_NOT_FOUND));
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