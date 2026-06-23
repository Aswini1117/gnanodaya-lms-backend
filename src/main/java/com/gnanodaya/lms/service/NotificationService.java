package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.Notification;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.repository.NotificationRepository;
import com.gnanodaya.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public Notification sendNotification(Long userId, String title,
                                         String message, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.save(Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build());
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository
                .countByUserIdAndIsRead(userId, false);
    }

    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new RuntimeException(
                        "Notification not found"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository
                .findByUserIdAndIsRead(userId, false);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
    }
}