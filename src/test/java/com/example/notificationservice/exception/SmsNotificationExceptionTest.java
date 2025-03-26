package com.example.notificationservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SmsNotificationException} class.
 * <p>
 * This test class verifies that the {@link SmsNotificationException} constructors correctly assign the error message
 * and cause. Two tests are provided:
 * <ul>
 *   <li>Test for the constructor that accepts only a message.</li>
 *   <li>Test for the constructor that accepts both a message and a cause.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> The exception's message and cause are set correctly as per the inputs.</li>
 *   <li><strong>Fail:</strong> The exception does not return the expected message or cause.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class SmsNotificationExceptionTest {

    /**
     * Tests the constructor of SmsNotificationException with a valid message.
     * <p>
     * <strong>Description:</strong> This test ensures that when an exception is created with just a message,
     * the message is correctly stored and the cause is null.
     * </p>
     */
    @Test
    @DisplayName("Test SmsNotificationException constructor with valid message")
    void testConstructorWithMessage() {
        // Arrange
        String errorMessage = "Test error message";

        // Act: Create an instance with only a message.
        SmsNotificationException exception = new SmsNotificationException(errorMessage);

        // Assert: Verify that the message is correct and the cause is null.
        assertNotNull(exception, "SmsNotificationException instance should not be null");
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        assertNull(exception.getCause(), "The exception cause should be null when not provided");
    }

    /**
     * Tests the constructor of SmsNotificationException with a valid message and a cause.
     * <p>
     * <strong>Description:</strong> This test ensures that when an exception is created with both a message and a cause,
     * both the message and the cause are correctly stored in the exception.
     * </p>
     */
    @Test
    @DisplayName("Test SmsNotificationException constructor with valid message and cause")
    void testConstructorWithMessageAndCause() {
        // Arrange
        String errorMessage = "Test error with cause";
        Throwable cause = new RuntimeException("Underlying cause");

        // Act: Create an instance with a message and a cause.
        SmsNotificationException exception = new SmsNotificationException(errorMessage, cause);

        // Assert: Verify that both the message and cause are correctly set.
        assertNotNull(exception, "SmsNotificationException instance should not be null");
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        assertEquals(cause, exception.getCause(), "The exception cause should match the provided cause");
    }
}
