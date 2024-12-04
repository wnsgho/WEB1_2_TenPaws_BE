package com.example.tenpaws.domain.pet.service;

import com.example.tenpaws.domain.pet.dto.PetRequestDTO;
import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.repository.PetRepository;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

    private final ShelterRepository shelterRepository;
    private final PetRepository petRepository;
    private final String uploadDir = System.getProperty("user.dir") + "/uploads";


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

        // 이미지 파일 저장
        List<String> imageUrls = saveFiles(requestDTO.getImages());

        // Pet 엔티티 생성
        Pet pet = requestDTO.toEntity(shelter, imageUrls);
        shelter.addPet(pet);
        Pet savedPet = petRepository.save(pet);

        return PetResponseDTO.fromEntity(savedPet);
    }

    // 공통 이미지 저장 로직
    private List<String> saveFiles(List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            for (MultipartFile file : files) {
                try {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    File savedFile = new File(directory, fileName);
                    file.transferTo(savedFile);
                    imageUrls.add("/uploads/" + fileName); // 이미지 URL 생성
                } catch (IOException e) {
                    throw new RuntimeException("File upload failed: " + e.getMessage());
                }
            }
        }
        return imageUrls;
    }

    public PetResponseDTO updatePet(Long shelterId, Long petId, PetRequestDTO requestDTO) {
        // 보호소 확인
        Shelter shelter = getShelter(shelterId);

        // 기존 Pet 엔티티 조회
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new BaseException(ErrorCode.PET_NOT_FOUND));

        // 보호소 ID가 일치하지 않으면 에러 반환
        if (!existingPet.getShelter().equals(shelter)) {
            throw new BaseException(ErrorCode.NOT_ASSIGNED);
        }

        // 새로 업로드된 이미지를 처리
        List<String> newImageUrls = saveFiles(requestDTO.getImages());

        // 기존 이미지 URL과 병합
        List<String> allImageUrls = new ArrayList<>(existingPet.getImageUrls());
        allImageUrls.addAll(newImageUrls);

        // 엔티티 필드 업데이트
        existingPet.updateFields(requestDTO);
        existingPet.setImageUrls(allImageUrls);

        // 엔티티 저장
        Pet updatedPet = petRepository.save(existingPet);

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
