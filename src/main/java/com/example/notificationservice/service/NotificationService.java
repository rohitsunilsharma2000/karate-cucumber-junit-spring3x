package com.example.notificationservice.service;

import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.exception.NotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for sending notifications via multiple channels.
 * Integrates Email, SMS, and Push Notification services.
 */
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final EmailService emailService;
    private final SmsService smsService;
    private final PushNotificationService pushNotificationService;

    public NotificationService(EmailService emailService, SmsService smsService, PushNotificationService pushNotificationService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.pushNotificationService = pushNotificationService;
    }

    /**
     * Sends an email notification.
     *
     * @param to      the recipient's email address
     * @param subject the email subject
     * @param body    the email body content
     */
    public void sendEmail(String to, String subject, String body) {
        try {
            emailService.sendEmail(to, subject, body);
            logger.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Error sending email to {}: {}", to, e.getMessage());
            throw new NotificationException("Error sending email", e);
        }
    }

    /**
     * Sends an SMS notification.
     *
     * @param to      the recipient's phone number
     * @param message the SMS message content
     */
    public void sendSms(String to, String message) {
        try {
            smsService.sendSms(to, message);
            logger.info("SMS sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Error sending SMS to {}: {}", to, e.getMessage());
            throw new NotificationException("Error sending SMS", e);
        }
    }

    /**
     * Sends a push notification.
     *
     * @param to      the recipient's device token
     * @param message the push notification message content
     */
    public void sendPushNotification(String to, String message) {
        try {
            pushNotificationService.sendPushNotification(to, message);
            logger.info("Push notification sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Error sending push notification to {}: {}", to, e.getMessage());
            throw new NotificationException("Error sending push notification", e);
        }
    }

    /**
     * Sends a notification using the specified type.
     *
     * @param type             the type of notification (EMAIL, SMS, PUSH)
     * @param recipient        the recipient's identifier (email address, phone number, or device token)
     * @param subjectOrMessage the subject (for email) or message (for SMS and push notifications)
     */
    public void sendNotification(NotificationType type, String recipient, String subjectOrMessage) {
        if (type == null) {
            logger.error("Null notification type provided");
            throw new NotificationException("Unsupported notification type: null");
        }
        logger.debug("Dispatching notification: type={}, recipient={}", type, recipient);
        switch (type) {
            case EMAIL:
                sendEmail(recipient, subjectOrMessage, "This is the email body.");
                break;
            case SMS:
                sendSms(recipient, subjectOrMessage);
                break;
            case PUSH:
                sendPushNotification(recipient, subjectOrMessage);
                break;
            default:
                logger.error("Unsupported notification type: {}", type);
                throw new NotificationException("Unsupported notification type: " + type);
        }
    }

}
