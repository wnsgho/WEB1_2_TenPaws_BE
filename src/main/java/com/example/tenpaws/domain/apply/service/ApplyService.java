package com.example.tenpaws.domain.apply.service;

import com.example.tenpaws.domain.apply.dto.ApplyDto;
import com.example.tenpaws.domain.apply.entity.Apply;
import com.example.tenpaws.domain.apply.repository.ApplyRepository;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.repository.PetRepository;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public ApplyDto applyForPet(Long petId, Long userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 이미 신청했는지 확인
        boolean isAlreadyApplied = applyRepository.existsByPetAndUser(pet, user);
        if (isAlreadyApplied) {
            throw new RuntimeException("이미 신청하셨습니다.");
        }

        Apply apply = Apply.builder()
                .pet(pet)
                .user(user)
                .applyDate(new java.util.Date())
                .applyStatus(Apply.ApplyStatus.PENDING)
                .build();

        applyRepository.save(apply);
        return ApplyDto.fromEntity(apply);
    }

    // shelterId로 해당 보호소의 신청 목록 조회
    public List<ApplyDto> getAppliesForShelter(Long shelterId) {
        return applyRepository.findAllByPetShelterId(shelterId)
                .stream()
                .map(ApplyDto::fromEntity)
                .collect(Collectors.toList());
    }

    // status 변경
    public ApplyDto updateApplyStatus(Long applyId, String status) {
        Apply apply = applyRepository.findById(applyId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        try {
            Apply.ApplyStatus newStatus = Apply.ApplyStatus.valueOf(status.toUpperCase());
            apply.setApplyStatus(newStatus);
            applyRepository.save(apply);

            return ApplyDto.fromEntity(apply);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }

    public void ensureUserCanApply(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 이미 다른 동물에 신청했는지 확인
        boolean isAlreadyAppliedToOtherPets = applyRepository.existsByUserIdAndPetNot(userId, pet);
        if (isAlreadyAppliedToOtherPets) {
            throw new RuntimeException("This user has already applied for another pet.");
        }
    }

    public boolean existsByUserIdAndPetNot(Long userId, Pet pet) {
        return applyRepository.existsByUserIdAndPetNot(userId, pet);
    }
}
