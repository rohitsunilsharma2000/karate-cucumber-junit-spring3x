package com.example.multichannelnotifier.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link EmailNotificationException} class.
 * <p>
 * <strong>Overview:</strong>
 * This test class verifies that the constructors of {@link EmailNotificationException} correctly assign the error message
 * and cause. It ensures full coverage of:
 * <ul>
 *   <li>The constructor that accepts only a message.</li>
 *   <li>The constructor that accepts both a message and a cause.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Test Coverage:</strong> The tests aim to achieve 100% coverage for the constructors of this exception class.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range:</strong>
 * <ul>
 *   <li>{@code errorMessage}: A non-null, non-empty string representing the error message.</li>
 *   <li>{@code cause}: A {@link Throwable} representing the underlying cause, which may be null if not provided.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If the exception does not return the expected error message or cause, the test will fail.</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class EmailNotificationExceptionTest {

    /**
     * Tests the constructor of EmailNotificationException with a valid message.
     * <p>
     * <strong>Description:</strong> This test ensures that when an EmailNotificationException is created with only a message,
     * the message is correctly stored and the cause is null.
     * </p>
     * <p>
     * <strong>Premise:</strong> A valid, non-null error message is provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception instance is not null.</li>
     *   <li>The exception's message matches the provided message.</li>
     *   <li>The exception's cause is null.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail:</strong> Test passes if all assertions hold; otherwise, it fails.
     * </p>
     */
    @Test
    @DisplayName("Test EmailNotificationException constructor with valid message")
    void testConstructorWithMessage() {
        // Arrange: Define an error message for the exception.
        String errorMessage = "Email sending failed";

        // Act: Create an EmailNotificationException instance with the message.
        EmailNotificationException exception = new EmailNotificationException(errorMessage);

        // Assert: Validate that the exception is correctly constructed.
        assertNotNull(exception, "EmailNotificationException instance should not be null");
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        assertNull(exception.getCause(), "The exception cause should be null when not provided");
    }

    /**
     * Tests the constructor of EmailNotificationException with a valid message and a cause.
     * <p>
     * <strong>Description:</strong> This test ensures that when an EmailNotificationException is created with both a message and a cause,
     * both fields are correctly stored.
     * </p>
     * <p>
     * <strong>Premise:</strong> A valid, non-null error message and a non-null Throwable cause are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception instance is not null.</li>
     *   <li>The exception's message matches the provided message.</li>
     *   <li>The exception's cause matches the provided cause.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail:</strong> Test passes if both the message and cause are set as expected; otherwise, it fails.
     * </p>
     */
    @Test
    @DisplayName("Test EmailNotificationException constructor with valid message and cause")
    void testConstructorWithMessageAndCause() {
        // Arrange: Define an error message and a cause.
        String errorMessage = "Email sending failed due to connection error";
        Throwable cause = new RuntimeException("SMTP connection error");

        // Act: Create an EmailNotificationException instance with both message and cause.
        EmailNotificationException exception = new EmailNotificationException(errorMessage, cause);

        // Assert: Validate that both the message and cause are correctly assigned.
        assertNotNull(exception, "EmailNotificationException instance should not be null");
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        assertEquals(cause, exception.getCause(), "The exception cause should match the provided cause");
    }
}
