package com.example.notificationservice.service;

import com.example.notificationservice.exception.EmailNotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link EmailService} class.
 * <p>
 * This test class verifies that the {@link EmailService#sendEmail(String, String, String)} method behaves as expected.
 * It ensures that valid input does not trigger exceptions and that invalid inputs (null or empty values)
 * result in an {@link EmailNotificationException} being thrown with the appropriate error messages.
 * </p>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> The method successfully sends an email (i.e. does not throw an exception)
 *       when valid parameters are provided.</li>
 *   <li><strong>Fail:</strong> The method throws an {@link EmailNotificationException} if any required parameter is null or empty.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class EmailServiceTest {

    // Instantiate the EmailService to test its sendEmail method.
    private final EmailService emailService = new EmailService();

    /**
     * Tests that sendEmail completes without exception when valid parameters are provided.
     * <p>
     * <strong>Description:</strong> This test verifies that when a valid recipient email, subject, and body are provided,
     * the sendEmail method executes without throwing an exception.
     * </p>
     */
    @Test
    @DisplayName("Test sendEmail with valid parameters")
    void testSendEmail_Success() {
        // Arrange
        String recipient = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act & Assert: The method should complete without throwing any exception.
        assertDoesNotThrow(() -> emailService.sendEmail(recipient, subject, body),
                           "sendEmail should not throw an exception when valid parameters are provided");
    }

    /**
     * Tests that sendEmail throws EmailNotificationException when the recipient email is null.
     * <p>
     * <strong>Description:</strong> This test verifies that if the recipient email is null,
     * sendEmail throws an EmailNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendEmail with null recipient")
    void testSendEmail_NullRecipient() {
        // Arrange
        String recipient = null;
        String subject = "Test Subject";
        String body = "Test Body";

        // Act & Assert: Expect an exception with the appropriate error message.
        EmailNotificationException exception = assertThrows(EmailNotificationException.class,
                                                            () -> emailService.sendEmail(recipient, subject, body),
                                                            "sendEmail should throw EmailNotificationException when recipient is null");
        assertEquals("Recipient email must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that sendEmail throws EmailNotificationException when the subject is null.
     * <p>
     * <strong>Description:</strong> This test verifies that if the email subject is null,
     * sendEmail throws an EmailNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendEmail with null subject")
    void testSendEmail_NullSubject() {
        // Arrange
        String recipient = "test@example.com";
        String subject = null;
        String body = "Test Body";

        // Act & Assert: Expect an exception with the appropriate error message.
        EmailNotificationException exception = assertThrows(EmailNotificationException.class,
                                                            () -> emailService.sendEmail(recipient, subject, body),
                                                            "sendEmail should throw EmailNotificationException when subject is null");
        assertEquals("Email subject must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that sendEmail throws EmailNotificationException when the email body is null.
     * <p>
     * <strong>Description:</strong> This test verifies that if the email body is null,
     * sendEmail throws an EmailNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendEmail with null body")
    void testSendEmail_NullBody() {
        // Arrange
        String recipient = "test@example.com";
        String subject = "Test Subject";
        String body = null;

        // Act & Assert: Expect an exception with the appropriate error message.
        EmailNotificationException exception = assertThrows(EmailNotificationException.class,
                                                            () -> emailService.sendEmail(recipient, subject, body),
                                                            "sendEmail should throw EmailNotificationException when body is null");
        assertEquals("Email body content must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that sendEmail throws EmailNotificationException when the recipient email is empty.
     * <p>
     * <strong>Description:</strong> This test verifies that if the recipient email is empty or only whitespace,
     * sendEmail throws an EmailNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendEmail with empty recipient")
    void testSendEmail_EmptyRecipient() {
        // Arrange
        String recipient = "   ";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act & Assert: Expect an exception with the appropriate error message.
        EmailNotificationException exception = assertThrows(EmailNotificationException.class,
                                                            () -> emailService.sendEmail(recipient, subject, body),
                                                            "sendEmail should throw EmailNotificationException when recipient is empty");
        assertEquals("Recipient email must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that sendEmail throws EmailNotificationException when the subject is empty.
     * <p>
     * <strong>Description:</strong> This test verifies that if the email subject is empty or only whitespace,
     * sendEmail throws an EmailNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendEmail with empty subject")
    void testSendEmail_EmptySubject() {
        // Arrange
        String recipient = "test@example.com";
        String subject = "   ";
        String body = "Test Body";

        // Act & Assert: Expect an exception with the appropriate error message.
        EmailNotificationException exception = assertThrows(EmailNotificationException.class,
                                                            () -> emailService.sendEmail(recipient, subject, body),
                                                            "sendEmail should throw EmailNotificationException when subject is empty");
        assertEquals("Email subject must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that sendEmail throws EmailNotificationException when the email body is empty.
     * <p>
     * <strong>Description:</strong> This test verifies that if the email body is empty or only whitespace,
     * sendEmail throws an EmailNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendEmail with empty body")
    void testSendEmail_EmptyBody() {
        // Arrange
        String recipient = "test@example.com";
        String subject = "Test Subject";
        String body = "   ";

        // Act & Assert: Expect an exception with the appropriate error message.
        EmailNotificationException exception = assertThrows(EmailNotificationException.class,
                                                            () -> emailService.sendEmail(recipient, subject, body),
                                                            "sendEmail should throw EmailNotificationException when body is empty");
        assertEquals("Email body content must not be null or empty.", exception.getMessage());
    }
}
