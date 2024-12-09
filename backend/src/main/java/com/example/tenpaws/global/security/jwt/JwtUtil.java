package com.example.tenpaws.global.security.jwt;

import com.example.tenpaws.global.entity.UserRole;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {
    private SecretKey secretKey;
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

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
    public String createSocialJwt(String userId, String email) {
        long oneHour = 3600000L; // 1시간
        return Jwts.builder()
                .claim("category", "Social")
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", UserRole.ROLE_USER.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + oneHour))
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

    // 블랙리스트에 토큰 추가
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // 블랙리스트 확인
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // 필요에 따라 사용, 일단 쓸 거 같지는 않은데 필요하긴 할 듯
    public void removeFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }

    // 엑세스 토큰 만료 여부 및 블랙리스트 확인
    public boolean isValidAccessToken(String token) {
        try {
            if (isBlacklisted(token)) {
                return false; // 토큰이 블랙리스트에 있음 == 유효하지 않음
            }
            return !isExpired(token); // 토큰이 만료x == true, 토큰이 만료 == false
        } catch (Exception e) {
            return false; // 예외 발생한 경우, 토큰이 유효하지 않음 false
        }
    }

    public void removeExpiredTokensFromBlacklist() {
        blacklistedTokens.removeIf(this::isExpired);
    }
}
