package com.example.tenpaws.global.security.jwt;

import com.example.tenpaws.global.entity.UserRole;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}")String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 이메일 검증
    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    // 아이디 검증
    public String getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", String.class);
    }

    // role 검증
    public UserRole getRole(String token) {
        String roleString = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
        return UserRole.valueOf(roleString);
    }

    // 만료 검증
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // 카테고리 검증
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    // 일반 유저 jwt 생성
    public String createJwt(String category, String email, UserRole role, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("email", email)
                .claim("role", role.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    // 소셜 유저 jwt 생성
    public String createSocialJwt(String userId) {
        long oneHour = 3600000L; // 1시간
        return Jwts.builder()
                .claim("category", "Social")
                .claim("userId", userId)
                .claim("role", UserRole.ROLE_USER.name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + oneHour))
                .signWith(secretKey)
                .compact();
    }

    // 소셜 유저 카테고리 & 토큰 만료 검증, 필요하면 쓰세요
    public boolean isValidSocialToken(String token) {
        try {
            String category = getCategory(token);
            return "Social".equals(category) && !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
