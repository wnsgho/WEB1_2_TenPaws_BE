package com.example.tenpaws.domain.recommendation.service;

import com.example.tenpaws.domain.pet.dto.PetResponseDTO;
import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.pet.repository.PetRepository;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import io.jsonwebtoken.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

    private final ApiService apiService;
    private final PetRepository petRepository;

    public Map<String, Object> recommendPet(User user) {
        try {
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
                            "\nAvailable pets:\n%s\nPlease recommend only one pet that perfectly suits to the all of user's preferences. Size should be equal. id should be said as ID: ?. Answer in Korean.",
                    size, personality, exerciseLevel, petDescriptions);

            // Step 4: OpenAI 호출
            String aiResponseContent = apiService.getRecommendation(prompt);  // aiResponseContent로 바로 처리;
            String recommendedId = extractRecommendedId(aiResponseContent);

            if (recommendedId == null) {
                throw new BaseException(ErrorCode.RECOMMENDATION_FAILED);
            }

            // Step 5: 추천된 반려동물 정보 가져오기
            Pet recommendedPet = petRepository.findById(Long.valueOf(recommendedId))
                    .orElseThrow(() -> new BaseException(ErrorCode.PET_NOT_FOUND));

            // petResponseDTO로 변환
            PetResponseDTO petResponseDTO = PetResponseDTO.fromEntity(recommendedPet); //dto에 fromEntity 존재, 이걸로 정적 팩토리 메서드 사용

            Map<String, Object> result = new HashMap<>();
            result.put("content", aiResponseContent); // AI 응답의 content
            result.put("petId", petResponseDTO.getPetId()); // 추천된 Pet의 ID
            result.put("pet", petResponseDTO); // Pet 정보

            return result;
        } catch (IOException e) {
            throw new RuntimeException("Failed to get recommendation from AI due to IOException", e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractRecommendedId(String response) {
        Pattern pattern = Pattern.compile("ID:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}