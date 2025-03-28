package com.example.multichannelnotifier.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link NotificationException} class.
 * <p>
 * <strong>Overview:</strong> This test class verifies that the constructors of the NotificationException class
 * correctly set the error message and the cause. Two tests are provided:
 * <ul>
 *   <li>One for the constructor that accepts only a message.</li>
 *   <li>One for the constructor that accepts both a message and a cause.</li>
 * </ul>
 * These tests ensure 100% coverage for the constructor logic.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range:</strong>
 * <ul>
 *   <li>{@code errorMessage}: A non-null, non-empty string describing the error.</li>
 *   <li>{@code cause}: A non-null {@link Throwable} representing the underlying cause; it may be null when not provided.</li>
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
class NotificationExceptionTest {

    /**
     * Tests the constructor of NotificationException with a valid message.
     * <p>
     * <strong>Description:</strong> This test verifies that when a NotificationException is instantiated with only a message,
     * the message is correctly stored and the cause remains null.
     * </p>
     * <p>
     * <strong>Premise:</strong> A valid error message (non-null and non-empty) is provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception instance is not null.</li>
     *   <li>The exception's message equals the provided error message.</li>
     *   <li>The exception's cause is null.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> The test passes if all assertions are met; otherwise, it fails.
     * </p>
     */
    @Test
    @DisplayName("Test NotificationException constructor with valid message")
    void testConstructorWithMessage() {
        // Arrange: Define a valid error message.
        String errorMessage = "Error occurred";

        // Act: Create an instance of NotificationException using the message-only constructor.
        NotificationException exception = new NotificationException(errorMessage);

        // Assert: Verify that the instance is properly constructed.
        // The exception instance must not be null.
        assertNotNull(exception, "NotificationException instance should not be null");
        // The exception message must match the provided error message.
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        // Since no cause was provided, the exception's cause should be null.
        assertNull(exception.getCause(), "The exception cause should be null when not provided");
    }

    /**
     * Tests the constructor of NotificationException with a valid message and a cause.
     * <p>
     * <strong>Description:</strong> This test verifies that when a NotificationException is instantiated with both a message and a cause,
     * both fields are correctly stored.
     * </p>
     * <p>
     * <strong>Premise:</strong> A valid error message and a valid non-null cause (Throwable) are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception instance is not null.</li>
     *   <li>The exception's message equals the provided error message.</li>
     *   <li>The exception's cause equals the provided Throwable.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> The test passes if the exception contains the expected message and cause; otherwise, it fails.
     * </p>
     */
    @Test
    @DisplayName("Test NotificationException constructor with valid message and cause")
    void testConstructorWithMessageAndCause() {
        // Arrange: Define a valid error message and a Throwable cause.
        String errorMessage = "Error occurred with cause";
        Throwable cause = new RuntimeException("Underlying cause");

        // Act: Create an instance of NotificationException using the constructor that accepts both message and cause.
        NotificationException exception = new NotificationException(errorMessage, cause);

        // Assert: Validate that the exception is constructed with the provided message and cause.
        // The exception instance must not be null.
        assertNotNull(exception, "NotificationException instance should not be null");
        // The exception message must equal the provided error message.
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        // The exception cause must equal the provided Throwable cause.
        assertEquals(cause, exception.getCause(), "The exception cause should match the provided cause");
    }
}
