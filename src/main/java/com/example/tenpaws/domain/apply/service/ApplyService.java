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
import java.util.Optional;
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
        if (pet.getStatus() == Pet.PetStatus.APPLIED) {
            throw new RuntimeException("This pet has already been applied for.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ensureUserCanApply(userId, petId);

        // 이전에 취소된 신청이 있는지 확인
        // Optional에서 값을 추출
        Optional<Apply> existingApplicationOptional = applyRepository.findByPetAndUserAndApplyStatus(pet, user, Apply.ApplyStatus.CANCELED);

        if (existingApplicationOptional.isPresent()) {
            Apply existingApplication = existingApplicationOptional.get(); // Optional에서 값 가져오기
            // 취소된 신청을 복구
            existingApplication.setApplyStatus(Apply.ApplyStatus.PENDING);
            applyRepository.save(existingApplication);

            // 동물 상태를 다시 APPLIED로 변경
            pet.setStatus(Pet.PetStatus.APPLIED);
            petRepository.save(pet);

            return ApplyDto.fromEntity(existingApplication);
        }

        Apply apply = Apply.builder()
                .pet(pet)
                .user(user)
                .applyDate(new java.util.Date())
                .applyStatus(Apply.ApplyStatus.PENDING)
                .build();

        // 동물의 상태를 'APPLIED'로 변경
        pet.setStatus(Pet.PetStatus.APPLIED);
        petRepository.save(pet); // 상태 업데이트

        applyRepository.save(apply);
        return ApplyDto.fromEntity(apply);
    }

    // 신청 취소
    public void cancelApply(Long applyId, Long userId) {
        Apply apply = applyRepository.findById(applyId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // 신청자 확인
        if (!apply.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to cancel this application.");
        }

        // 신청 상태 변경
        apply.setApplyStatus(Apply.ApplyStatus.CANCELED);
        applyRepository.save(apply);

        // 동물 상태 변경
        Pet pet = apply.getPet();
        pet.setStatus(Pet.PetStatus.AVAILABLE);
        petRepository.save(pet);
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
        // 신청 상태가 거절 또는 취소일 경우, 동물 상태를 'AVAILABLE'로 변경
            if (newStatus == Apply.ApplyStatus.REJECTED || newStatus == Apply.ApplyStatus.CANCELED) {
                Pet pet = apply.getPet();
                pet.setStatus(Pet.PetStatus.AVAILABLE);
                petRepository.save(pet);
            }
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
        // 현재 동물에 신청했는지 확인
        // 취소 상태가 아닌 신청이 있는지 확인
        boolean isAlreadyAppliedToThisPet = applyRepository.existsByPetAndUserAndApplyStatusNot(pet, user, Apply.ApplyStatus.CANCELED);
        if (isAlreadyAppliedToThisPet) {
            throw new RuntimeException("You have already applied for this pet.");
        }
    }

    public boolean existsByUserIdAndPetNot(Long userId, Pet pet) {
        return applyRepository.existsByUserIdAndPetNot(userId, pet);
    }
}
