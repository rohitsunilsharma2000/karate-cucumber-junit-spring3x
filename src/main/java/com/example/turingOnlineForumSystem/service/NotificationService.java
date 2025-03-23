package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.NotificationRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void sendNotification(Long recipientId, String message) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
        log.info("Notification sent to user {}: {}", recipientId, message);

        notificationRepository.save(notification);
        log.info("Notification sent to user {}: {}", recipientId, message);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        notification.setIsRead(true);
        notificationRepository.save(notification);
        log.info("Notification ID {} marked as read", notificationId);
    }

    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification not found");
        }
        notificationRepository.deleteById(notificationId);
        log.info("Notification ID {} deleted", notificationId);
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByRecipientId(userId);
    }

    public void sendNotification(User recipient, String message) {
        Notification notification = Notification.builder()
                .message(message)
                .recipient(recipient)
                .isRead(false)
                .build();


        notificationRepo.save(notification);
        log.info("Sent notification to user {}: {}", recipient.getId(), message);
    }

}

