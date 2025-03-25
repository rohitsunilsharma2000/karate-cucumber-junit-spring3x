package com.example.notificationservice.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link NotificationException} class.
 */
public class NotificationExceptionTest {

    /**
     * Test that the constructor accepting only a message correctly sets the exception message.
     */
    @Test
    public void testConstructorWithMessage() {
        // Define an error message to be used in the exception.
        String errorMessage = "Error occurred";
        // Create a new NotificationException with the error message.
        NotificationException exception = new NotificationException(errorMessage);
        // Verify that the exception message is correctly set.
        assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Test that the constructor accepting both a message and a cause correctly sets both fields.
     */
    @Test
    public void testConstructorWithMessageAndCause() {
        // Define an error message and an underlying cause.
        String errorMessage = "Error occurred with cause";
        Throwable cause = new RuntimeException("Underlying cause");
        // Create a new NotificationException with both the error message and the cause.
        NotificationException exception = new NotificationException(errorMessage, cause);
        // Verify that the exception message is correctly set.
        assertEquals(errorMessage, exception.getMessage());
        // Verify that the exception cause is correctly set.
        assertEquals(cause, exception.getCause());
    }
}
