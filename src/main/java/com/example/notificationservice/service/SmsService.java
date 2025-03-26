package com.example.notificationservice.service;

import com.example.notificationservice.exception.SmsNotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for sending SMS notifications.
 * <p>
 * This service provides functionality to send SMS notifications using an underlying SMS gateway API.
 * It validates that required parameters are not null or empty and uses detailed logging to trace the sending process.
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

    /**
     * Sends an SMS to the specified phone number.
     * <p>
     * This method validates that the recipient phone number and message content are provided.
     * If either parameter is invalid, a {@link SmsNotificationException} is thrown.
     * Logging is implemented at various levels:
     * - INFO: when starting and after successful sending.
     * - DEBUG: for detailed parameter logging.
     * - ERROR: if input validation fails or if an exception is encountered during sending.
     * </p>
     *
     * @param to      the recipient's phone number; must not be null or empty.
     * @param message the SMS message content; must not be null or empty.
     * @throws SmsNotificationException if the recipient phone number or message content is null/empty or if sending fails.
     */
    public void sendSms(String to, String message) {
        LOGGER.info("Initiating SMS send process.");

        // Input validation
        if (!StringUtils.hasText(to)) {
            LOGGER.error("SMS sending failed: recipient phone number is null or empty.");
            throw new SmsNotificationException("Recipient phone number must not be null or empty.");
        }
        if (!StringUtils.hasText(message)) {
            LOGGER.error("SMS sending failed: message content is null or empty.");
            throw new SmsNotificationException("SMS message content must not be null or empty.");
        }
        LOGGER.debug("SMS parameters validated: to={}, message={}", to, message);

        try {
            // Simulated SMS sending logic
            LOGGER.info("Sending SMS to: {}", to);
            // Actual integration with an SMS gateway API would occur here.
            LOGGER.info("SMS sent successfully to: {}", to);
        } catch (Exception e) {
            LOGGER.error("Error sending SMS to {}: {}", to, e.getMessage(), e);
            throw new SmsNotificationException("Failed to send SMS due to an unexpected error.", e);
        }
    }
}
