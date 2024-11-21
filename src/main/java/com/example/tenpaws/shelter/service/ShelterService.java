package com.example.tenpaws.shelter.service;

import com.example.tenpaws.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.shelter.dto.ShelterResponseDTO;
import com.example.tenpaws.shelter.entity.Shelter;
import com.example.tenpaws.shelter.exception.BadRequestException;
import com.example.tenpaws.shelter.exception.ShelterNotFoundException;
import com.example.tenpaws.shelter.repository.ShelterRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
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
                .orElseThrow(() -> new ShelterNotFoundException("해당 아이디: " + id));
        return new ShelterResponseDTO(
                shelter.getId(),
                shelter.getUsername(),
                shelter.getShelterName(),
                shelter.getAddress(),
                shelter.getPhoneNumber(),
                shelter.getEmail()
        );
    }

    // 쉘터 등록
    public ShelterResponseDTO createShelter(ShelterRequestDTO shelterRequestDTO) {
        if (shelterRequestDTO.getUsername() == null || shelterRequestDTO.getPw() == null) {
            throw new BadRequestException("유저 이름과 비밀번호는 필수입니다.");
        }
        Shelter shelter = new Shelter();
        shelter.setUsername(shelterRequestDTO.getUsername());
        shelter.setPw(shelterRequestDTO.getPw());
        shelter.setShelterName(shelterRequestDTO.getShelterName());
        shelter.setAddress(shelterRequestDTO.getAddress());
        shelter.setPhoneNumber(shelterRequestDTO.getPhoneNumber());
        shelter.setEmail(shelterRequestDTO.getEmail());
        Shelter savedShelter = shelterRepository.save(shelter);
        return new ShelterResponseDTO(
                savedShelter.getId(),
                savedShelter.getUsername(),
                savedShelter.getShelterName(),
                savedShelter.getAddress(),
                savedShelter.getPhoneNumber(),
                savedShelter.getEmail()
        );
    }

    // 쉘터 수정 (DTO -> 엔티티 변환)
    public ShelterResponseDTO updateShelter(Long id, ShelterRequestDTO shelterRequestDTO) {
        Shelter existingShelter = shelterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디 보호소를 찾을 수 없습니다: " + id));

        existingShelter.setUsername(shelterRequestDTO.getUsername());
        existingShelter.setPw(shelterRequestDTO.getPw());
        existingShelter.setShelterName(shelterRequestDTO.getShelterName());
        existingShelter.setAddress(shelterRequestDTO.getAddress());
        existingShelter.setPhoneNumber(shelterRequestDTO.getPhoneNumber());
        existingShelter.setEmail(shelterRequestDTO.getEmail());
        Shelter updatedShelter = shelterRepository.save(existingShelter);
        return new ShelterResponseDTO(
                updatedShelter.getId(),
                updatedShelter.getUsername(),
                updatedShelter.getShelterName(),
                updatedShelter.getAddress(),
                updatedShelter.getPhoneNumber(),
                updatedShelter.getEmail()
        );
    }

    // 쉘터 삭제
    public void deleteShelter(Long id) {
        if (!shelterRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 아이디 보호소를 찾을 수 없습니다: " + id);
        }
        shelterRepository.deleteById(id);
    }
}



