package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Fetch all notifications for a user.
     *
     * @param userId ID of the user
     * @return List of notifications
     */
    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        log.info("Fetching notifications for user {}", userId);
        return notificationService.getNotificationsForUser(userId);
    }

    /**
     * Mark a specific notification as read.
     *
     * @param notificationId ID of the notification
     */
    @PutMapping("/read/{notificationId}")
    public void markNotificationAsRead(@PathVariable Long notificationId) {
        log.info("Marking notification {} as read", notificationId);
        notificationService.markAsRead(notificationId);
    }

    /**
     * Delete a notification.
     *
     * @param notificationId ID of the notification
     */
    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        log.info("Deleting notification {}", notificationId);
        notificationService.deleteNotification(notificationId);
    }
}

