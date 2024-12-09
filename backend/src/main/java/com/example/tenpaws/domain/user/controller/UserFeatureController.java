package com.example.tenpaws.domain.user.controller;

import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.domain.admin.repository.AdminRepository;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.entity.OAuth2UserEntity;
import com.example.tenpaws.domain.user.entity.User;
import com.example.tenpaws.domain.user.repositoty.OAuth2UserRepository;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import com.example.tenpaws.global.entity.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/features")
@RequiredArgsConstructor
@Tag(name = "유저 기능 API", description = "클라이언트로 요청을 받아 데이터를 반환해주는 기능 컨트롤러")
@Slf4j
public class UserFeatureController {

    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final AdminRepository adminRepository;
    private final OAuth2UserRepository oAuth2UserRepository;

    @Operation(summary = "유저 role 반환", description = "유저 role 반환을 위한 API")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_SHELTER')")
    @GetMapping("/role")
    public ResponseEntity<Map<String, String>> getRole(Authentication authentication) {

        // 엑세스 토큰 인증이 되지 않는 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("Error", "Unauthorized"));
        }

        String email = authentication.getName();

        // 일반 유저 정보인 지 체크
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.ok(Map.of("role", "ROLE_USER"));
        }

        // 보호소 유저 정보인 지 체크
        if (shelterRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.ok(Map.of("role", "ROLE_SHELTER"));
        }

        // 관리자 유저 정보인 지 체크, 슈퍼 관리자와 일반 관리자 구분
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            String role = admin.getUserRole().name();

            if ("ROLE_SUPER_ADMIN".equals(role)) {
                return ResponseEntity.ok(Map.of("role", "ROLE_SUPER_ADMIN"));
            } else if ("ROLE_ADMIN".equals(role)) {
                return ResponseEntity.ok(Map.of("role", "ROLE_ADMIN"));
            }
        }

        // 소셜 로그인 유저 정보인 지 체크
        if (oAuth2UserRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.ok(Map.of("role", "ROLE_USER"));
        }

        return ResponseEntity.status(404).body(Map.of("Error", "User not found"));
    }

    // 모든 사용자 이메일 중복 체크 가입 로직
    @Operation(summary = "이메일 중복 체크", description = "이메일 중복 체크를 위한 API")
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean isDuplicate =
                userRepository.existsByEmail(email) ||
                        shelterRepository.existsByEmail(email) ||
                        adminRepository.existsByEmail(email) ||
                        oAuth2UserRepository.existsByEmail(email);

        // 사용 가능 여부는 중복 여부의 반대
        boolean isAvailable = !isDuplicate;

        return ResponseEntity.ok(Map.of("isAvailable", isAvailable));
    }

    // 사용자의 id 반환 api
    @Operation(summary = "사용자 id 반환", description = "사용자 id 반환을 위한 API")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_SHELTER')")
    @GetMapping("/user-id")
    public ResponseEntity<Map<String, Object>> getUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("Authentication is null : {}", authentication);
            return ResponseEntity.status(401).body(Map.of("Error", "Unauthorized"));
        }

        String email = authentication.getName();

        if (userRepository.findByEmail(email).isPresent()) {
            User user = userRepository.findByEmail(email).get();
            return ResponseEntity.ok(Map.of("Id", user.getId()));
        }

        if (oAuth2UserRepository.findByEmail(email).isPresent()) {
            OAuth2UserEntity oAuth2UserEntity = oAuth2UserRepository.findByEmail(email).get();
            return ResponseEntity.ok(Map.of("Id", oAuth2UserEntity.getUserId()));
        }

        if (shelterRepository.findByEmail(email).isPresent()) {
            Shelter shelter = shelterRepository.findByEmail(email).get();
            return ResponseEntity.ok(Map.of("Id", shelter.getId()));
        }

        if (adminRepository.findByEmail(email).isPresent()) {
            Admin admin = adminRepository.findByEmail(email).get();
            return ResponseEntity.ok(Map.of("Id", admin.getId()));
        }

        return ResponseEntity.status(404).body(Map.of("Error", "User not found"));
    }
}
