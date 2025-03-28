package com.example.multichannelnotifier.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SmsNotificationException} class.
 * <p>
 * <strong>Overview:</strong> This test class verifies that the constructors of the SmsNotificationException class
 * correctly assign the error message and the cause. It includes:
 * <ul>
 *   <li>A test for the constructor that accepts only a message.</li>
 *   <li>A test for the constructor that accepts both a message and a cause.</li>
 * </ul>
 * These tests aim to achieve 100% coverage for SmsNotificationException's constructor logic.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range:</strong>
 * <ul>
 *   <li>{@code errorMessage}: A non-null, non-empty string representing the error message.</li>
 *   <li>{@code cause}: A non-null {@link Throwable} representing the underlying cause; may be null if not provided.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If the exception does not return the expected message or cause, the test will fail.</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class SmsNotificationExceptionTest {

    /**
     * Tests the constructor of SmsNotificationException with a valid message.
     * <p>
     * <strong>Description:</strong> This test ensures that when an SmsNotificationException is created with just a message,
     * the message is stored correctly and the cause remains null.
     * </p>
     * <p>
     * <strong>Premise:</strong> A valid, non-null error message is provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception instance should not be null.</li>
     *   <li>The exception's message must match the provided error message.</li>
     *   <li>The exception's cause should be null.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> The test passes if all assertions hold; otherwise, it fails.
     * </p>
     */
    @Test
    @DisplayName("Test SmsNotificationException constructor with valid message")
    void testConstructorWithMessage() {
        // Arrange: Define a valid error message.
        String errorMessage = "Test error message";

        // Act: Create an instance of SmsNotificationException using the constructor that accepts only a message.
        SmsNotificationException exception = new SmsNotificationException(errorMessage);

        // Assert: Verify that the exception instance is created correctly.
        assertNotNull(exception, "SmsNotificationException instance should not be null");
        // Verify that the exception message matches the provided message.
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        // Verify that no cause is set when not provided.
        assertNull(exception.getCause(), "The exception cause should be null when not provided");
    }

    /**
     * Tests the constructor of SmsNotificationException with a valid message and a cause.
     * <p>
     * <strong>Description:</strong> This test ensures that when an SmsNotificationException is created with both a message and a cause,
     * both the error message and the underlying cause are stored correctly.
     * </p>
     * <p>
     * <strong>Premise:</strong> A valid error message and a non-null Throwable cause are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception instance should not be null.</li>
     *   <li>The exception's message must equal the provided error message.</li>
     *   <li>The exception's cause must equal the provided cause.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> The test passes if both the error message and cause are correctly set; otherwise, it fails.
     * </p>
     */
    @Test
    @DisplayName("Test SmsNotificationException constructor with valid message and cause")
    void testConstructorWithMessageAndCause() {
        // Arrange: Define a valid error message and create an underlying cause.
        String errorMessage = "Test error with cause";
        Throwable cause = new RuntimeException("Underlying cause");

        // Act: Create an instance of SmsNotificationException using the constructor that accepts both message and cause.
        SmsNotificationException exception = new SmsNotificationException(errorMessage, cause);

        // Assert: Verify that the exception is correctly constructed.
        assertNotNull(exception, "SmsNotificationException instance should not be null");
        // Check that the error message matches the provided message.
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        // Check that the exception cause matches the provided cause.
        assertEquals(cause, exception.getCause(), "The exception cause should match the provided cause");
    }
}
