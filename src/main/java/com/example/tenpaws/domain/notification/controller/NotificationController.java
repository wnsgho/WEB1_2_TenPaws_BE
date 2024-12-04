package com.example.tenpaws.domain.notification.controller;

import com.example.tenpaws.domain.notification.dto.response.NotificationResponse;
import com.example.tenpaws.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Tag(name = "알림 기능 API", description = "알림 기능을 모아둔 컨트롤러 입니다")
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        String email = authentication.getName();
        return notificationService.subscribe(email);
    }

    @Operation(summary = "알림 정보 페이징", description = "알림 정보 페이징 API")
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getList(
            Authentication authentication,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String email = authentication.getName();
        return ResponseEntity.ok(notificationService.getList(email, pageable));
    }

    @Operation(summary = "알림 읽음 표시", description = "알림 읽음 표시 API")
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Message read status updated successfully");
    }

    @Operation(summary = "???", description = "???")
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(notificationService.getUnreadCount(email));
    }

    @Operation(summary = "알림 삭제", description = "알림 삭제 API")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> delete(@PathVariable Long notificationId) {
        notificationService.delete(notificationId);
        return ResponseEntity.ok("Notification successfully deleted");
    }
}