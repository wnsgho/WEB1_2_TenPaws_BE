package com.example.tenpaws.domain.user.controller;

import com.example.tenpaws.domain.recommendation.service.RecommendService;
import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.user.dto.UserJoinDTO;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.domain.user.service.UserService;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RecommendService recommendService;

    @PostMapping("/regular/join")
    public ResponseEntity<?> usersJoin(
            @Valid @RequestBody UserJoinDTO userJoinDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 에러 메시지를 중복 없이 처리
            Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage(),
                            (existing, replacement) -> existing
                    ));
            return ResponseEntity.badRequest().body(errorMap);
        }

        try {
            userService.registerUser(userJoinDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Successfully Created"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/shelter/join")
    public ResponseEntity<?> sheltersJoin(
            @Valid @RequestBody ShelterRequestDTO shelterRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 에러 메시지를 중복 없이 처리
            Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField(),
                            error -> error.getDefaultMessage(),
                            (existing, replacement) -> existing
                    ));
            return ResponseEntity.badRequest().body(errorMap);
        }

        try {
            userService.registerShelter(shelterRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Successfully Created"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * 사용자의 선호 기준에 맞는 반려동물 추천
     */
    @PostMapping("/{id}/recommend-pet")
    public ResponseEntity<Map<String, Object>> recommendPet(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));

            // 추천 서비스 호출
            Map<String, Object> recommendationResult = recommendService.recommendPet(user);

            Map<String, Object> response = new HashMap<>();
            response.put("recommendation", recommendationResult.get("content")); // AI의 추천 메시지
            response.put("petId", recommendationResult.get("petId")); // 추천된 반려동물 ID
            response.put("pet", recommendationResult.get("pet")); // Pet 정보

            return ResponseEntity.ok(response);
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "추천 실패: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류: " + e.getMessage()));
        }
    }
}
