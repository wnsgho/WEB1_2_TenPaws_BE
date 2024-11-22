package com.example.tenpaws.domain.shelter.service;

import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.shelter.dto.ShelterResponseDTO;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public ShelterResponseDTO toResponseDTO(Shelter shelter) {
        return new ShelterResponseDTO(
                shelter.getId(),
                shelter.getUsername(),
                shelter.getShelterName(),
                shelter.getAddress(),
                shelter.getPhoneNumber(),
                shelter.getEmail()
        );
    }


    // 전체 쉘터 조회 (엔티티 -> DTO 변환)
    public List<ShelterResponseDTO> getAllShelters() {
        return shelterRepository.findAll().stream()
                .map(shelter -> new ShelterResponseDTO(
                        shelter.getId(),
                        shelter.getUsername(),
                        shelter.getShelterName(),
                        shelter.getAddress(),
                        shelter.getPhoneNumber(),
                        shelter.getEmail()
                ))
                .collect(Collectors.toList());
    }

    // ID로 쉘터 조회
    public ShelterResponseDTO getShelterById(Long id) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.SHELTER_NOT_FOUND));
        return new ShelterResponseDTO(
                shelter.getId(),
                shelter.getUsername(),
                shelter.getShelterName(),
                shelter.getAddress(),
                shelter.getPhoneNumber(),
                shelter.getEmail()
        );
    }

    // 쉘터 추가
    public ShelterResponseDTO createShelter(ShelterRequestDTO requestDTO) {
        Shelter shelter = requestDTO.toEntity();
        Shelter savedShelter = shelterRepository.save(shelter);
        return toResponseDTO(savedShelter);
    }

    // 쉘터 업데이트
    public ShelterResponseDTO updateShelter(Long id, ShelterRequestDTO requestDTO) {
        Shelter existingShelter = shelterRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.SHELTER_NOT_FOUND));

        existingShelter.setUsername(requestDTO.getUsername());
        existingShelter.setPw(requestDTO.getPw());
        existingShelter.setShelterName(requestDTO.getShelterName());
        existingShelter.setAddress(requestDTO.getAddress());
        existingShelter.setPhoneNumber(requestDTO.getPhoneNumber());
        existingShelter.setEmail(requestDTO.getEmail());

        Shelter updatedShelter = shelterRepository.save(existingShelter);
        return toResponseDTO(updatedShelter);
    }

    // 쉘터 삭제
    public void deleteShelter(Long id) {
        if (!shelterRepository.existsById(id)) {
            throw new BaseException(ErrorCode.SHELTER_NOT_FOUND);
        }
        shelterRepository.deleteById(id);
    }
}