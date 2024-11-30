package com.example.tenpaws.domain.notification.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitters {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(Long userId, SseEmitter emitter) {
        this.emitters.put(userId, emitter);

        // 타임아웃 및 완료 시 자동 제거
        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.remove(userId);
        });

        emitter.onCompletion(() -> this.emitters.remove(userId));

        // 에러 발생 시 자동 제거
        emitter.onError(e -> {
            emitter.completeWithError(e);
            this.emitters.remove(userId);
        });

        return emitter;
    }

    public void remove(Long userId) {
        this.emitters.remove(userId);
    }

    public SseEmitter get(Long userId) {
        return this.emitters.get(userId);
    }

    public Map<Long, SseEmitter> getAll() {
        return this.emitters;
    }
}
