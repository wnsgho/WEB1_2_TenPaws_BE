package com.example.tenpaws.domain.notification.controller;

import com.example.tenpaws.domain.notification.dto.request.CreateNotificationRequest;
import com.example.tenpaws.domain.notification.dto.response.NotificationResponse;
import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.domain.notification.service.NotificationService;
import com.example.tenpaws.global.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 본인 검증 추가 필요
    @GetMapping("/subscribe/{userRole}/{userId}")
    public SseEmitter subscribe(
            @PathVariable(name = "userRole") String userRoleStr,
            @PathVariable Long userId) {
        UserRole userRole = UserRole.valueOf("ROLE_" + userRoleStr.toUpperCase());
        return notificationService.subscribe(userRole, userId);
    }

    // 알림 생성
    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody CreateNotificationRequest request) {
        Notification savedNotification = notificationService.create(request);

        return ResponseEntity.ok()
                .body(savedNotification);
    }

    // 알림 목록 조회
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getList(
            @RequestParam Long userId,
            @PageableDefault(sort = "userId", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(notificationService.getList(userId, pageable));
    }

    // 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);

        return ResponseEntity.ok("Message read status updated successfully");
    }

    // 안 읽은 알림 수 조회
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@RequestParam Long userId) {

        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    // 알림 삭제
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> delete(@PathVariable Long notificationId) {
        notificationService.delete(notificationId);

        return ResponseEntity.ok("Notification successfully deleted");
    }
}
