package com.example.tenpaws.domain.notification.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitters {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(UserIdentifier userIdentifier, SseEmitter emitter) {
        String key = userIdentifier.toKey();
        this.emitters.put(key, emitter);

        // 타임아웃 및 완료 시 자동 제거
        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.remove(key);
        });

        emitter.onCompletion(() -> this.emitters.remove(key));

        emitter.onError(e -> {
            emitter.completeWithError(e);
            this.emitters.remove(key);
        });

        return emitter;
    }

    public SseEmitter get(UserIdentifier userIdentifier) {
        return this.emitters.get(userIdentifier.toKey());
    }

    public void remove(UserIdentifier userIdentifier) {
        this.emitters.remove(userIdentifier.toKey());
    }

    public Map<String, SseEmitter> getAll() {
        return this.emitters;
    }
}
