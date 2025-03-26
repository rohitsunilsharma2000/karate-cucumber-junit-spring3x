package com.example.notificationservice.exception;

/**
 * Custom exception for handling email notification errors.
 * <p>
 * This exception is thrown when invalid input is provided for sending an email or when an error occurs during
 * the email sending process.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public class EmailNotificationException extends RuntimeException {

    /**
     * Constructs a new EmailNotificationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public EmailNotificationException(String message) {
        super(message);
    }

    /**
     * Constructs a new EmailNotificationException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public EmailNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
