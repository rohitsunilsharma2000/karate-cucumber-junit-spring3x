package com.example.notificationservice.service;

import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.exception.NotificationException;
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
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

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
        LOGGER.info("Preparing to send email notification.");
        if (!StringUtils.hasText(to)) {
            LOGGER.error("Email sending failed: recipient email is null or empty.");
            throw new NotificationException("Recipient email must not be null or empty.");
        }
        if (!StringUtils.hasText(subject)) {
            LOGGER.error("Email sending failed: subject is null or empty.");
            throw new NotificationException("Email subject must not be null or empty.");
        }
        if (!StringUtils.hasText(body)) {
            LOGGER.error("Email sending failed: email body is null or empty.");
            throw new NotificationException("Email body must not be null or empty.");
        }
        LOGGER.debug("Email parameters validated: to={}, subject={}", to, subject);
        try {
            emailService.sendEmail(to, subject, body);
            LOGGER.info("Email sent successfully to {}", to);
        } catch (Exception e) {
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
        LOGGER.info("Preparing to send SMS notification.");
        if (!StringUtils.hasText(to)) {
            LOGGER.error("SMS sending failed: recipient phone number is null or empty.");
            throw new NotificationException("Recipient phone number must not be null or empty.");
        }
        if (!StringUtils.hasText(message)) {
            LOGGER.error("SMS sending failed: message content is null or empty.");
            throw new NotificationException("SMS message content must not be null or empty.");
        }
        LOGGER.debug("SMS parameters validated: to={}", to);
        try {
            smsService.sendSms(to, message);
            LOGGER.info("SMS sent successfully to {}", to);
        } catch (Exception e) {
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
        LOGGER.info("Preparing to send push notification.");
        if (!StringUtils.hasText(to)) {
            LOGGER.error("Push notification sending failed: device token is null or empty.");
            throw new NotificationException("Device token must not be null or empty.");
        }
        if (!StringUtils.hasText(message)) {
            LOGGER.error("Push notification sending failed: message content is null or empty.");
            throw new NotificationException("Push notification message content must not be null or empty.");
        }
        LOGGER.debug("Push notification parameters validated: to={}", to);
        try {
            pushNotificationService.sendPushNotification(to, message);
            LOGGER.info("Push notification sent successfully to {}", to);
        } catch (Exception e) {
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
        LOGGER.info("Initiating dispatch of notification.");
        if (type == null) {
            LOGGER.error("Notification dispatch failed: null notification type provided.");
            throw new NotificationException("Unsupported notification type: null");
        }
        if (!StringUtils.hasText(recipient)) {
            LOGGER.error("Notification dispatch failed: recipient is null or empty.");
            throw new NotificationException("Recipient must not be null or empty.");
        }
        if (!StringUtils.hasText(subjectOrMessage)) {
            LOGGER.error("Notification dispatch failed: subject or message content is null or empty.");
            throw new NotificationException("Subject or message content must not be null or empty.");
        }
        LOGGER.debug("Dispatching notification: type={}, recipient={}", type, recipient);
        switch (type) {
            case EMAIL:
                // For demonstration, using subjectOrMessage as the email subject and a constant string as the body.
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
