package com.example.tenpaws.domain.pet.service;

import com.example.tenpaws.domain.apply.repository.ApplyRepository;
import com.example.tenpaws.domain.pet.dto.PetRequestDTO;
import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.repository.PetRepository;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

    private final ShelterRepository shelterRepository;
    private final PetRepository petRepository;

    public List<PetResponseDTO> getPetList() {
        return petRepository.findAll().stream()
                .map(PetResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PetResponseDTO> getAllPets(Long shelterId) {
        Shelter shelter = getShelter(shelterId);
        return petRepository.findByShelter(shelter).stream()
                .map(PetResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PetResponseDTO getPetById(Long petId, Long shelterId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(ErrorCode.PET_NOT_FOUND));

        if (shelterId != null) {
            Shelter shelter = getShelter(shelterId);
            if (!pet.getShelter().equals(shelter)) {
                throw new BaseException(ErrorCode.PET_NOT_FOUND);
            }
        }

        return PetResponseDTO.fromEntity(pet);
    }

    public PetResponseDTO createPet(Long shelterId, PetRequestDTO requestDTO) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = requestDTO.toEntity(shelter);
        shelter.addPet(pet);
        Pet savedPet = petRepository.save(pet);
        return PetResponseDTO.fromEntity(savedPet);
    }

    public PetResponseDTO updatePet(Long shelterId, Long petId, PetRequestDTO requestDTO) {
        Shelter shelter = getShelter(shelterId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(ErrorCode.PET_NOT_FOUND));
        if (!pet.getShelter().equals(shelter)) {
            throw new BaseException(ErrorCode.NOT_ASSIGNED);
        }
        pet.updateFields(requestDTO);

        Pet updatedPet = petRepository.save(pet);
        return PetResponseDTO.fromEntity(updatedPet);
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
    }  // 여기에 두는게 맞나?

    private Pet getPet(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(ErrorCode.PET_NOT_FOUND));
    }
}
