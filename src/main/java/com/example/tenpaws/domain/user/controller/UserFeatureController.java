package com.example.tenpaws.domain.user.controller;

import com.example.tenpaws.domain.admin.repository.AdminRepository;
import com.example.tenpaws.domain.shelter.repository.ShelterRepository;
import com.example.tenpaws.domain.user.repositoty.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/features")
@RequiredArgsConstructor
public class UserFeatureController {

    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final AdminRepository adminRepository;

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

        return ResponseEntity.status(404).body(Map.of("Error", "User not found"));
    }

    // 모든 사용자 이메일 중복 체크 가입 로직
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean isAvailable = !userRepository.existsByEmail(email)
                && !shelterRepository.existsByEmail(email)
                && !adminRepository.existsByEmail(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }
}
