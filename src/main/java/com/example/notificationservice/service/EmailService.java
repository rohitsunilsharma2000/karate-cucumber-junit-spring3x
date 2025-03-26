package com.example.notificationservice.service;

import com.example.notificationservice.exception.EmailNotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for sending email notifications.
 * <p>
 * This service provides functionality to send emails and includes input validation to ensure that all necessary
 * parameters are valid before proceeding. Detailed logging is implemented to trace the email sending process.
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email to the specified recipient.
     * <p>
     * This method validates the provided email address, subject, and body content to ensure they are not null or blank.
     * Detailed logging is used to trace the process, including input validation and the simulated email sending.
     * </p>
     *
     * @param to      the recipient's email address; must not be null or empty.
     * @param subject the email subject; must not be null or empty.
     * @param body    the email body content; must not be null or empty.
     * @throws EmailNotificationException if any of the input parameters are invalid or if sending fails.
     */
    public void sendEmail(String to, String subject, String body) {
        LOGGER.info("Initiating email send process.");

        // Input validation for email parameters
        if (!StringUtils.hasText(to)) {
            LOGGER.error("Invalid email address: Recipient email is null or empty.");
            throw new EmailNotificationException("Recipient email must not be null or empty.");
        }
        if (!StringUtils.hasText(subject)) {
            LOGGER.error("Invalid email subject: Subject is null or empty.");
            throw new EmailNotificationException("Email subject must not be null or empty.");
        }
        if (!StringUtils.hasText(body)) {
            LOGGER.error("Invalid email body: Body content is null or empty.");
            throw new EmailNotificationException("Email body content must not be null or empty.");
        }
        LOGGER.debug("Input validation passed for recipient: {}, subject: {}", to, subject);

        try {
            // Simulated email sending logic
            LOGGER.info("Sending email to: {}, subject: {}", to, subject);
            // Actual email sending logic would be implemented here (e.g., using JavaMailSender)
            LOGGER.info("Email sent successfully to: {}", to);
        } catch (Exception ex) {
            LOGGER.error("Failed to send email to: {}. Error: {}", to, ex.getMessage(), ex);
            // Wrap any unexpected exception into a custom EmailNotificationException
            throw new EmailNotificationException("Failed to send email due to an unexpected error.", ex);
        }
    }
}
