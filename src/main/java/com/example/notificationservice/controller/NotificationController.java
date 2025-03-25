package com.example.notificationservice.controller;

import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for sending notifications.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Endpoint to send a notification.
     *
     * @param type             the notification type (EMAIL, SMS, PUSH)
     * @param recipient        the recipient's identifier
     * @param subjectOrMessage the subject or message content
     */
    @PostMapping("/send")
    public void sendNotification(@RequestParam NotificationType type,
                                 @RequestParam String recipient,
                                 @RequestParam String subjectOrMessage) {
        notificationService.sendNotification(type, recipient, subjectOrMessage);
    }
}
