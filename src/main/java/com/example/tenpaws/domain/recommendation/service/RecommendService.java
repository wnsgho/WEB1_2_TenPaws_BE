package com.example.tenpaws.domain.recommendation.service;

import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.repository.PetRepository;
import com.example.tenpaws.domain.user.entity.User;
import io.jsonwebtoken.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {
    private final ApiService apiService;
    private final PetRepository petRepository;

    public String recommendPet(User user) {
        // Step 1: 사용자 선호 기준 가져오기
        String size = user.getPreferredSize();
        String personality = user.getPreferredPersonality();
        Integer exerciseLevel = user.getPreferredExerciseLevel();

        // Step 2: 보호소에서 등록된 모든 반려동물 데이터 가져오기
        List<Pet> allPets = petRepository.findAll();

        // Step 3: Pet 데이터를 AI에 전달하기 위한 포맷으로 변환
        String petDescriptions = allPets.stream()
                .map(pet -> String.format(
                        "Id: %s, Size: %s, Personality: %s, Exercise Level: %s",
                        pet.getId(), pet.getSize(), pet.getPersonality(), pet.getExerciseLevel()))
                .collect(Collectors.joining("\n"));

        String prompt = String.format(
                "User prefers: Size: %s, Personality: %s, Exercise Level: %s." +
                        "\nAvailable pets:\n%s\nPlease recommend only one pet that perfectly suits to the all of user's preferences. Id should be random. Size should be equal. id should be said. Answer in Korean.",
                size, personality, exerciseLevel, petDescriptions);

        // Step 4: OpenAI 호출
        try {
            return apiService.getRecommendation(prompt);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get recommendation from AI due to IOException", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get recommendation from AI", e);
        }
    }
}
