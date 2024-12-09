package com.example.tenpaws.global.security.service;

import com.example.tenpaws.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final JwtUtil jwtUtil;

    @Scheduled(cron = "0 0/5 * * * ?") // 엑세스 토큰은 1시간, 5분마다 블랙리스트 자동 삭제
    public void cleanExpiredTokens() {
        jwtUtil.removeExpiredTokensFromBlacklist();
    }
}