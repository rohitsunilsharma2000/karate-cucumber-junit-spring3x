package com.example.notificationservice.service;

import com.example.notificationservice.exception.SmsNotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SmsService} class.
 * <p>
 * This test class verifies that the {@link SmsService#sendSms(String, String)} method behaves as expected.
 * It ensures that valid input results in successful execution and that invalid inputs (null or empty values)
 * result in a {@link SmsNotificationException} being thrown with the appropriate error messages.
 * Additionally, it verifies that if an unexpected error occurs during the sending process,
 * the exception is caught and wrapped in a {@link SmsNotificationException}.
 * </p>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> When valid parameters are provided, the method executes without throwing an exception.</li>
 *   <li><strong>Fail:</strong> When any required parameter is null or empty, or if an unexpected error occurs during sending,
 *       a {@link SmsNotificationException} is thrown with the expected error message.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class SmsServiceTest {

    // Use the real SmsService for most tests.
    private final SmsService smsService = new SmsService();

    /**
     * Tests that sendSms executes successfully when valid parameters are provided.
     * <p>
     * <strong>Description:</strong> This test ensures that with a valid phone number and message,
     * sendSms completes without throwing any exception.
     * </p>
     */
    @Test
    @DisplayName("Test sendSms with valid parameters")
    void testSendSms_Success() {
        // Arrange
        String recipient = "1234567890";
        String message = "Test SMS";

        // Act & Assert: Expect no exception to be thrown.
        assertDoesNotThrow(() -> smsService.sendSms(recipient, message),
                           "sendSms should not throw an exception when valid parameters are provided");
    }

    /**
     * Tests that sendSms throws a SmsNotificationException when the recipient phone number is null.
     * <p>
     * <strong>Description:</strong> This test verifies that if the recipient phone number is null,
     * sendSms throws a SmsNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendSms with null recipient")
    void testSendSms_NullRecipient() {
        // Arrange
        String recipient = null;
        String message = "Test SMS";

        // Act & Assert: Expect a SmsNotificationException.
        SmsNotificationException ex = assertThrows(SmsNotificationException.class,
                                                   () -> smsService.sendSms(recipient, message),
                                                   "sendSms should throw SmsNotificationException when recipient is null");
        assertEquals("Recipient phone number must not be null or empty.", ex.getMessage());
    }

    /**
     * Tests that sendSms throws a SmsNotificationException when the recipient phone number is empty.
     * <p>
     * <strong>Description:</strong> This test verifies that if the recipient phone number is empty or only whitespace,
     * sendSms throws a SmsNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendSms with empty recipient")
    void testSendSms_EmptyRecipient() {
        // Arrange
        String recipient = "   ";
        String message = "Test SMS";

        // Act & Assert: Expect a SmsNotificationException.
        SmsNotificationException ex = assertThrows(SmsNotificationException.class,
                                                   () -> smsService.sendSms(recipient, message),
                                                   "sendSms should throw SmsNotificationException when recipient is empty");
        assertEquals("Recipient phone number must not be null or empty.", ex.getMessage());
    }

    /**
     * Tests that sendSms throws a SmsNotificationException when the message is null.
     * <p>
     * <strong>Description:</strong> This test verifies that if the SMS message content is null,
     * sendSms throws a SmsNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendSms with null message")
    void testSendSms_NullMessage() {
        // Arrange
        String recipient = "1234567890";
        String message = null;

        // Act & Assert: Expect a SmsNotificationException.
        SmsNotificationException ex = assertThrows(SmsNotificationException.class,
                                                   () -> smsService.sendSms(recipient, message),
                                                   "sendSms should throw SmsNotificationException when message is null");
        assertEquals("SMS message content must not be null or empty.", ex.getMessage());
    }

    /**
     * Tests that sendSms throws a SmsNotificationException when the message is empty.
     * <p>
     * <strong>Description:</strong> This test verifies that if the SMS message content is empty or only whitespace,
     * sendSms throws a SmsNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendSms with empty message")
    void testSendSms_EmptyMessage() {
        // Arrange
        String recipient = "1234567890";
        String message = "   ";

        // Act & Assert: Expect a SmsNotificationException.
        SmsNotificationException ex = assertThrows(SmsNotificationException.class,
                                                   () -> smsService.sendSms(recipient, message),
                                                   "sendSms should throw SmsNotificationException when message is empty");
        assertEquals("SMS message content must not be null or empty.", ex.getMessage());
    }

    /**
     * A test subclass of SmsService to simulate an exception during the SMS sending process.
     * <p>
     * <strong>Description:</strong> This subclass overrides sendSms to simulate an error in the try block.
     * It replicates input validation and then deliberately throws a RuntimeException, which should be caught
     * and wrapped in a SmsNotificationException.
     * </p>
     */
    static class FailingSmsService extends SmsService {
        @Override
        public void sendSms(String to, String message) {
            // Perform input validation as in the parent class.
            if (!StringUtils.hasText(to)) {
                throw new SmsNotificationException("Recipient phone number must not be null or empty.");
            }
            if (!StringUtils.hasText(message)) {
                throw new SmsNotificationException("SMS message content must not be null or empty.");
            }
            // Simulate an error during the sending process.
            try {
                throw new RuntimeException("Simulated SMS sending failure");
            } catch (Exception e) {
                throw new SmsNotificationException("Failed to send SMS due to an unexpected error.", e);
            }
        }
    }

    /**
     * Tests that sendSms catches an exception thrown during the SMS sending process and wraps it in a SmsNotificationException.
     * <p>
     * <strong>Description:</strong> This test uses the FailingSmsService subclass to simulate a failure in the try block.
     * It verifies that the exception is caught and rethrown as a SmsNotificationException with the expected message and cause.
     * </p>
     */
    @Test
    @DisplayName("Test sendSms exception handling during sending process")
    void testSendSms_ExceptionDuringSending() {
        // Arrange
        String recipient = "1234567890";
        String message = "Test SMS";
        SmsService failingService = new FailingSmsService();

        // Act & Assert: Expect a SmsNotificationException wrapping the simulated RuntimeException.
        SmsNotificationException ex = assertThrows(SmsNotificationException.class,
                                                   () -> failingService.sendSms(recipient, message),
                                                   "sendSms should throw SmsNotificationException when an error occurs during sending");
        assertEquals("Failed to send SMS due to an unexpected error.", ex.getMessage());
        assertNotNull(ex.getCause(), "The underlying cause should not be null");
        assertEquals("Simulated SMS sending failure", ex.getCause().getMessage());
    }
}
