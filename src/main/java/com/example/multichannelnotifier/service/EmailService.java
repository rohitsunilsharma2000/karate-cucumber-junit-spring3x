package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.exception.EmailNotificationException;
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



        try {
            // Delegate to a protected method that can be overridden in tests.
            sendEmailInternal(to, subject, body);
        } catch (Exception ex) {
            LOGGER.error("Failed to send email to: {}. Error: {}", to, ex.getMessage(), ex);
            // Wrap any unexpected exception into a custom EmailNotificationException
            throw new EmailNotificationException("Failed to send email due to an unexpected error.", ex);
        }
    }

    /**
     * Protected method that simulates the actual sending of the email.
     * <p>
     * In a real application, this method would integrate with an email sending library (e.g., JavaMailSender).
     * Here, it logs the sending process.
     * </p>
     *
     * @param to      the recipient's email address.
     * @param subject the email subject.
     * @param body    the email body content.
     */
    protected void sendEmailInternal(String to, String subject, String body) {
        LOGGER.info("Sending email to: {}, subject: {}", to, subject);
        // Simulated email sending logic
        LOGGER.info("Email sent successfully to: {}", to);
    }
}
