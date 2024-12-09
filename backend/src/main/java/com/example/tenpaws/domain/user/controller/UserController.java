package com.example.tenpaws.domain.user.controller;

import com.example.tenpaws.domain.recommendation.service.RecommendService;
import com.example.tenpaws.domain.shelter.dto.ShelterRequestDTO;
import com.example.tenpaws.domain.shelter.service.ShelterService;
import com.example.tenpaws.domain.user.dto.*;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.domain.user.service.UserService;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "사용자 기능 API", description = "사용자 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RecommendService recommendService;
    private final ShelterService shelterService;

    // 일반 유저 가입
    @Operation(summary = "회원가입", description = "일반 유저 회원가입을 위한 API")
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

    // 보호소 유저 가입
    @Operation(summary = "회원가입", description = "보호소 유저 회원가입을 위한 API")
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
    @Operation(summary = "반려 동물 추천", description = "사용자의 선호 기준에 맞는 반려동물 추천")
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
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "더이상 추천할 동물이 없습니다. 처음부터 다시 추천합니다.");
            return ResponseEntity.ok(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류: " + e.getMessage()));
        }
    }

    // 단일 유저 조회
    @Operation(summary = "일반 유저 조회", description = "일반 유저 조회를 위한 API, 관리자는 모든 회원 정보 열람 가능, 일반 회원은 자신의 정보만 열람 가능")
    @PreAuthorize("hasRole('ROLE_USER') and @userServiceImpl.isUserOwn(#id) or hasRole('ROLE_SHELTER') and @userServiceImpl.isUserOwn(#id) or hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 단일 소셜 유저 조회
    @Operation(summary = "소셜 유저 조회", description = "소셜 유저 조회를 위한 API")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my-info")
    public ResponseEntity<Object> getMyInfo(Authentication authentication) {
        String userId = authentication.getName();
        log.info("단일 소셜 로그인 조회 : {}", userId);
        return ResponseEntity.ok(userService.getSocialUserInfo(userId));
    }

    // 유저 정보 수정
    @Operation(summary = "유저 정보 수정", description = "유저 정보 수정을 위한 API")
    @PreAuthorize("hasRole('ROLE_USER') and @userServiceImpl.isUserOwn(#id) or hasRole('ROLE_SHELTER') and @userServiceImpl.isUserOwn(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateRequestDTO));
    }

    // 유저 탈퇴
    @Operation(summary = "유저 탈퇴", description = "유저 탈퇴를 위한 API")
    @PreAuthorize("hasRole('ROLE_USER') and @userServiceImpl.isUserOwn(#id) or hasRole('ROLE_SHELTER') and @userServiceImpl.isUserOwn(#id) or hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 모든 일반 유저 불러오기
    @Operation(summary = "모든 유저 정보 조회", description = "모든 유저 정보 조회을 위한 API, 관리자만 가능")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/retrieve-web")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 모든 소셜 유저 불러오기
    @Operation(summary = "모든 소셜 유저 정보 조회", description = "모든 소셜 유저 정보 조회을 위한 API, 관리자만 가능")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/retrieve-social")
    public ResponseEntity<List<OAuth2UserDTO>> getAllSocialUsers() {
        return ResponseEntity.ok(userService.getAllSocialUsers());
    }

    // 소셜 로그인 유저 이름 변경
    @Operation(summary = "소셜 유저 이름 수정", description = "소셜 유저 이름 수정을 위한 API")
    @PatchMapping("/{userId}/username")
    public ResponseEntity<OAuth2UserDTO> updateUsername(
            @PathVariable String userId,
            @RequestBody UpdateSocialUsernameRequestDTO requestDTO) {
        OAuth2UserDTO responseDTO = userService.updateSocialUsername(userId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // 본인을 제외한 모든 유저 불러오기 for Chat
    @Operation(summary = "본인 제외 모든 유저 정보 조회", description = "본인 제외 모든 유저 정보 조회을 위한 API")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SHELTER')")
    @GetMapping("/chat-users")
    public ResponseEntity<List<UserResponseForChatDTO>> getAllUsersForChat(Authentication authentication) {
        String userEmail = authentication.getName();
        List<UserResponseForChatDTO> result = new ArrayList<>();
        boolean userRemoved;

        List<UserResponseForChatDTO> normalUser = new ArrayList<>(userService.getAllUsers().stream().map(userResponseDTO ->
                new UserResponseForChatDTO(userResponseDTO.getUsername(), userResponseDTO.getEmail())).toList());
        List<UserResponseForChatDTO> socialUser = new ArrayList<>(userService.getAllSocialUsers().stream().map(oAuth2UserDTO ->
                new UserResponseForChatDTO(oAuth2UserDTO.getUserId(), oAuth2UserDTO.getEmail())).toList());
        List<UserResponseForChatDTO> shelterUser = new ArrayList<>(shelterService.getAllShelters().stream().map(shelterResponseDTO ->
                new UserResponseForChatDTO(shelterResponseDTO.getShelterName(), shelterResponseDTO.getEmail())).toList());

        userRemoved = normalUser.removeIf(userResponseForChatDTO -> userEmail.equals(userResponseForChatDTO.getEmail()));
        if (!userRemoved) {
            userRemoved = socialUser.removeIf(userResponseForChatDTO -> userEmail.equals(userResponseForChatDTO.getEmail()));
        }
        if (!userRemoved) {
            shelterUser.removeIf(userResponseForChatDTO -> userEmail.equals(userResponseForChatDTO.getEmail()));
        }

        result.addAll(normalUser);
        result.addAll(socialUser);
        result.addAll(shelterUser);

        return ResponseEntity.ok(result);
    }
}
