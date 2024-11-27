package com.example.tenpaws.domain.apply.service;

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

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public void applyForPet(Long petId, Long userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Apply apply = Apply.builder()
                .pet(pet)
                .user(user)
                .applyDate(new java.util.Date())
                .applyStatus(Apply.ApplyStatus.PENDING)
                .build();

        applyRepository.save(apply);
    }

    // shelterId로 해당 보호소의 신청 목록 조회
    public List<Apply> getAppliesForShelter(Long shelterId) {
        return applyRepository.findAllByPetShelterId(shelterId);
    }

    // status 변경
    public void updateApplyStatus(Long applyId, String status) {
        Apply apply = applyRepository.findById(applyId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        try {
            Apply.ApplyStatus newStatus = Apply.ApplyStatus.valueOf(status.toUpperCase());
            apply.setApplyStatus(newStatus);
            applyRepository.save(apply);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }
}
