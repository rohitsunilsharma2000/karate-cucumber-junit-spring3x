package com.example.notificationservice.service;

import com.example.notificationservice.exception.PushNotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for sending push notifications.
 * <p>
 * This service provides functionality to send push notifications using an underlying messaging system
 * (e.g., Firebase Cloud Messaging). It includes input validation to ensure that required parameters are not null or empty,
 * and implements detailed logging to trace the notification sending process.
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class PushNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationService.class);

    /**
     * Sends a push notification to the specified device token.
     * <p>
     * This method validates that the recipient device token and message content are provided.
     * If either parameter is invalid, a {@link PushNotificationException} is thrown.
     * The process is logged at different levels:
     * - INFO: when starting and after successful sending.
     * - DEBUG: for detailed parameter logging.
     * - ERROR: if input validation fails or if an exception is encountered during sending.
     * </p>
     *
     * @param to      the recipient's device token; must not be null or empty.
     * @param message the push notification message content; must not be null or empty.
     * @throws PushNotificationException if the device token or message is null/empty or if sending fails.
     */
    public void sendPushNotification(String to, String message) {
        LOGGER.info("Initiating push notification send process.");

        // Input validation
        if (!StringUtils.hasText(to)) {
            LOGGER.error("Push notification send failed: device token is null or empty.");
            throw new PushNotificationException("Device token must not be null or empty.");
        }
        if (!StringUtils.hasText(message)) {
            LOGGER.error("Push notification send failed: message content is null or empty.");
            throw new PushNotificationException("Push notification message content must not be null or empty.");
        }
        LOGGER.debug("Push notification parameters validated: to={}, message={}", to, message);

        try {
            // Simulated push notification sending logic
            LOGGER.info("Sending push notification to: {}", to);
            // Actual integration with push notification providers (e.g., Firebase Cloud Messaging) would occur here.
            LOGGER.info("Push notification sent successfully to: {}", to);
        } catch (Exception e) {
            LOGGER.error("Error sending push notification to {}: {}", to, e.getMessage(), e);
            throw new PushNotificationException("Failed to send push notification due to an unexpected error.", e);
        }
    }
}
