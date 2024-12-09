package com.example.tenpaws.domain.user.controller;

import com.example.tenpaws.global.entity.UserRole;
import com.example.tenpaws.global.security.entity.RefreshEntity;
import com.example.tenpaws.global.security.jwt.JwtUtil;
import com.example.tenpaws.global.security.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "리프레시 토큰 재발급 API", description = "엑세스 만료, 리프레시 만료 전일 경우, 엑세스와 리프레시 토큰 모두 재발급")
@RequestMapping("/api/v1")
public class ReissueController {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Operation(summary = "리프레시 토큰 재발급", description = "클라이언트로부터 데이터를 받아 만료 체크")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            if (jwtUtil.isExpired(refresh)) {
                return new ResponseEntity<>("Refresh token expired", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getEmail(refresh);
        UserRole userRole = jwtUtil.getRole(refresh);

        String newAccessToken = jwtUtil.createJwt("access", email, userRole, 3_600_000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", email, userRole, 86_400_000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(email, newRefreshToken, 86_400_000L);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(Map.of(
                "message", "Tokens reissued successfully",
                "accessToken", "Bearer " + newAccessToken
        ), HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}