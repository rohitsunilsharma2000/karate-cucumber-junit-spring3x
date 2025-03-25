package com.example.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for sending push notifications.
 */
@Service
public class PushNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    /**
     * Sends a push notification.
     *
     * @param to      the recipient's device token
     * @param message the push notification message content
     */
    public void sendPushNotification(String to, String message) {
        // Add your push notification sending logic (e.g., integrating with Firebase Cloud Messaging)
        logger.info("Sending push notification to: {}", to);
        // Simulated push notification sending
    }
}
