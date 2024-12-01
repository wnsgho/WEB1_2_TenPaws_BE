package com.example.tenpaws.domain.notification.sse;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitters {
    private static final Logger log = LoggerFactory.getLogger(SseEmitters.class);

    // ConcurrentHashMap으로 thread-safe 보장
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 재시도 관련 상수 추가
    private static final int MAX_RETRY_COUNT = 3;
    private static final long RETRY_DELAY_MS = 1000;

    // 기존 add 메서드 개선: 로깅 추가 및 기존 연결 정리
    public SseEmitter add(UserIdentifier userIdentifier, SseEmitter emitter) {
        String key = userIdentifier.toKey();

        // 기존 연결이 있다면 정리
        SseEmitter existingEmitter = this.emitters.get(key);
        if (existingEmitter != null) {
            existingEmitter.complete();
            this.emitters.remove(key);
            log.info("기존 SSE 연결 종료: {}", key);
        }

        this.emitters.put(key, emitter);
        log.info("새로운 SSE 연결 추가: {}", key);

        // 콜백에 로깅 추가
        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.remove(key);
            log.info("SSE 연결 타임아웃: {}", key);
        });

        emitter.onCompletion(() -> {
            this.emitters.remove(key);
            log.info("SSE 연결 완료: {}", key);
        });

        emitter.onError(e -> {
            emitter.completeWithError(e);
            this.emitters.remove(key);
            log.error("SSE 연결 에러: {}", key, e);
        });

        return emitter;
    }

    // 기존 메서드들 유지
    public SseEmitter get(UserIdentifier userIdentifier) {
        return this.emitters.get(userIdentifier.toKey());
    }

    public void remove(UserIdentifier userIdentifier) {
        String key = userIdentifier.toKey();
        SseEmitter emitter = this.emitters.remove(key);
        if (emitter != null) {
            emitter.complete();
            log.info("SSE 연결 제거: {}", key);
        }
    }

    // 수정된 getAll 메서드: 불변 맵 반환
    public Map<String, SseEmitter> getAll() {
        return Collections.unmodifiableMap(this.emitters);
    }

    // 새로 추가: 하트비트 전송
    @Scheduled(fixedRate = 45000) // 45초마다 실행
    public void sendHeartbeat() {
        emitters.forEach((key, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("heartbeat")
                        .data("", MediaType.APPLICATION_JSON));
                log.debug("하트비트 전송 성공: {}", key);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(key);
                log.warn("하트비트 전송 실패로 연결 종료: {}", key);
            }
        });
    }

    // 새로 추가: 알림 전송 with 재시도 로직
    public boolean sendNotification(UserIdentifier userIdentifier, Object data) {
        String key = userIdentifier.toKey();
        SseEmitter emitter = this.emitters.get(key);

        if (emitter == null) {
            return false;
        }

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            try {
                emitter.send(SseEmitter.event()
                        .name("알림")
                        .data(data, MediaType.APPLICATION_JSON));
                return true;
            } catch (IOException e) {
                log.warn("알림 전송 실패 (시도 {}/{}): {}", i + 1, MAX_RETRY_COUNT, key);
                if (i < MAX_RETRY_COUNT - 1) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        // 최대 재시도 횟수 초과 시 연결 종료
        emitter.complete();
        this.emitters.remove(key);
        log.error("알림 전송 최대 재시도 횟수 초과로 연결 종료: {}", key);
        return false;
    }

    // 새로 추가: 서버 종료 시 정리
    @PreDestroy
    public void destroy() {
        emitters.forEach((key, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("shutdown")
                        .data("서버가 종료됩니다.", MediaType.APPLICATION_JSON));
                emitter.complete();
                log.info("서버 종료로 인한 SSE 연결 종료: {}", key);
            } catch (IOException e) {
                log.error("서버 종료 시 SSE 연결 종료 실패: {}", key);
            }
        });
        emitters.clear();
    }
}