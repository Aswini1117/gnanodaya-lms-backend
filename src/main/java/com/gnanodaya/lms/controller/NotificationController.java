package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.Notification;
import com.gnanodaya.lms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Notification>>> getNotifications(
            @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Notifications",
                notificationService.getUserNotifications(userId)));
    }

    @GetMapping("/{userId}/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount(
            @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Unread count",
                Map.of("count", notificationService.getUnreadCount(userId))));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Notification>> markAsRead(
            @PathVariable Long notificationId) {
        return ResponseEntity.ok(ApiResponse.success("Marked as read",
                notificationService.markAsRead(notificationId)));
    }

    @PutMapping("/read-all/{userId}")
    public ResponseEntity<ApiResponse<Void>> markAllRead(
            @PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success("All marked as read", null));
    }
}