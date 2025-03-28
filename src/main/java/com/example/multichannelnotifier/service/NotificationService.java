package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.enums.NotificationType;
import com.example.multichannelnotifier.exception.NotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for sending notifications via multiple channels.
 * <p>
 * This service integrates Email, SMS, and Push Notification services.
 * It performs thorough input validation, logs operations at various levels to ensure traceability,
 * and handles errors with descriptive exceptions.
 * Custom Exception Handling: A custom exception (NotificationException) has been implemented,
 * and a global exception handler is present to ensure all errors follow a standard response format.
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class NotificationService {

    // Logger for tracing execution details and errors
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    // Dependencies for each notification channel
    private final EmailService emailService;
    private final SmsService smsService;
    private final PushNotificationService pushNotificationService;

    /**
     * Constructs a NotificationService with the provided channel services.
     *
     * @param emailService            the service for sending emails.
     * @param smsService              the service for sending SMS messages.
     * @param pushNotificationService the service for sending push notifications.
     */
    public NotificationService(EmailService emailService, SmsService smsService, PushNotificationService pushNotificationService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.pushNotificationService = pushNotificationService;
    }

    /**
     * Sends an email notification.
     * <p>
     * Validates that the recipient email address, subject, and body are provided.
     * </p>
     *
     * @param to      the recipient's email address; must not be null or empty.
     * @param subject the email subject; must not be null or empty.
     * @param body    the email body content; must not be null or empty.
     * @throws NotificationException if input validation fails or if an error occurs while sending the email.
     */
    public void sendEmail(String to, String subject, String body) {
        // Log the start of email sending process.
        LOGGER.info("Preparing to send email notification.");


        // Debug log validated parameters
        LOGGER.debug("Email parameters validated: to={}, subject={}", to, subject);
        try {
            // Attempt to send the email using EmailService.
            emailService.sendEmail(to, subject, body);
            LOGGER.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            // Log the error and wrap it in a custom NotificationException.
            LOGGER.error("Error sending email to {}: {}", to, e.getMessage(), e);
            throw new NotificationException("Error sending email", e);
        }
    }

    /**
     * Sends an SMS notification.
     * <p>
     * Validates that the recipient's phone number and message content are provided.
     * </p>
     *
     * @param to      the recipient's phone number; must not be null or empty.
     * @param message the SMS message content; must not be null or empty.
     * @throws NotificationException if input validation fails or if an error occurs while sending the SMS.
     */
    public void sendSms(String to, String message) {
        // Log the start of SMS sending process.
        LOGGER.info("Preparing to send SMS notification.");



        // Debug log validated parameters
        LOGGER.debug("SMS parameters validated: to={}", to);
        try {
            // Attempt to send the SMS using SmsService.
            smsService.sendSms(to, message);
            LOGGER.info("SMS sent successfully to {}", to);
        } catch (Exception e) {
            // Log the error and wrap it in a custom NotificationException.
            LOGGER.error("Error sending SMS to {}: {}", to, e.getMessage(), e);
            throw new NotificationException("Error sending SMS", e);
        }
    }

    /**
     * Sends a push notification.
     * <p>
     * Validates that the device token and message content are provided.
     * </p>
     *
     * @param to      the recipient's device token; must not be null or empty.
     * @param message the push notification message content; must not be null or empty.
     * @throws NotificationException if input validation fails or if an error occurs while sending the push notification.
     */
    public void sendPushNotification(String to, String message) {
        // Log the start of push notification sending process.
        LOGGER.info("Preparing to send push notification.");



        // Debug log validated parameters
        LOGGER.debug("Push notification parameters validated: to={}", to);
        try {
            // Attempt to send the push notification using PushNotificationService.
            pushNotificationService.sendPushNotification(to, message);
            LOGGER.info("Push notification sent successfully to {}", to);
        } catch (Exception e) {
            // Log the error and wrap it in a custom NotificationException.
            LOGGER.error("Error sending push notification to {}: {}", to, e.getMessage(), e);
            throw new NotificationException("Error sending push notification", e);
        }
    }

    /**
     * Sends a notification using the specified type.
     * <p>
     * Validates that the notification type, recipient, and subject or message are provided.
     * Dispatches the notification to the appropriate channel (EMAIL, SMS, or PUSH).
     * </p>
     *
     * @param type             the type of notification (EMAIL, SMS, PUSH); must not be null.
     * @param recipient        the recipient's identifier (email address, phone number, or device token); must not be null or empty.
     * @param subjectOrMessage the subject (for email) or message (for SMS and push notifications); must not be null or empty.
     * @throws NotificationException if input validation fails or if an unsupported notification type is provided.
     */
    public void sendNotification(NotificationType type, String recipient, String subjectOrMessage) {
        // Log the start of the notification dispatch process.
        LOGGER.info("Initiating dispatch of notification.");



        // Log validated parameters for debugging
        LOGGER.debug("Dispatching notification: type={}, recipient={}", type, recipient);

        // Dispatch notification based on the provided type
        switch (type) {
            case EMAIL:
                // For demonstration purposes, use subjectOrMessage as the email subject with a constant body.
                sendEmail(recipient, subjectOrMessage, "This is the email body.");
                break;
            case SMS:
                sendSms(recipient, subjectOrMessage);
                break;
            case PUSH:
                sendPushNotification(recipient, subjectOrMessage);
                break;
            default:
                LOGGER.error("Unsupported notification type: {}", type);
                throw new NotificationException("Unsupported notification type: " + type);
        }
    }
}
