package com.example.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for sending email notifications.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email.
     *
     * @param to      the recipient's email address
     * @param subject the email subject
     * @param body    the email body content
     */
    public void sendEmail(String to, String subject, String body) {
        // Add your email sending logic (e.g., using JavaMailSender)
        logger.info("Sending email to: {}, subject: {}", to, subject);
        // Simulated email sending
    }
}
