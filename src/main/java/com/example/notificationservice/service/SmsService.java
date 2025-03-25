package com.example.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for sending SMS notifications.
 */
@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    /**
     * Sends an SMS.
     *
     * @param to      the recipient's phone number
     * @param message the SMS message content
     */
    public void sendSms(String to, String message) {
        // Add your SMS sending logic (e.g., using an SMS gateway API)
        logger.info("Sending SMS to: {}", to);
        // Simulated SMS sending
    }
}
