package com.example.tenpaws.global.security.service;

import com.example.tenpaws.global.security.entity.RefreshEntity;
import com.example.tenpaws.global.security.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshRepository refreshRepository;

    @Scheduled(cron = "0 * * * * ?") // 매 1분마다 실행
    public void deleteExpiredTokens() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        // 만료된 토큰만 조회
        List<RefreshEntity> expiredTokens = refreshRepository.findExpiredTokens(now.format(formatter));

        // 만료된 토큰 삭제
        expiredTokens.forEach(entity -> {
            System.out.println("Deleting expired token: " + entity.getId());
            refreshRepository.delete(entity);
        });
    }
}


