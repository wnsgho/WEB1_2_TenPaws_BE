package com.example.tenpaws.domain.notification.controller;

import com.example.tenpaws.domain.notification.dto.request.CreateNotificationRequest;
import com.example.tenpaws.domain.notification.dto.response.NotificationResponse;
import com.example.tenpaws.domain.notification.entity.Notification;
import com.example.tenpaws.domain.notification.service.NotificationService;
import com.example.tenpaws.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 생성
    @PostMapping
    public ResponseEntity<ApiResponse<Notification>> create(@RequestBody CreateNotificationRequest request) {
        Notification savedNotification = notificationService.create(request);

        return ResponseEntity.ok()
                .body(ApiResponse.success(savedNotification));
    }

    // 알림 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getList(
            @RequestParam Long userId,
            @PageableDefault(sort = "userId", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.success(notificationService.getList(userId, pageable)));
    }

    // 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);

        return ResponseEntity.ok(ApiResponse.success(null, "Message read status updated successfully"));
    }

    // 안 읽은 알림 수 조회
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@RequestParam Long userId) {

        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnreadCount(userId)));
    }

    // 알림 삭제
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long notificationId) {
        notificationService.delete(notificationId);

        return ResponseEntity.ok(ApiResponse.success(null, "Notification successfully deleted"));
    }
}
