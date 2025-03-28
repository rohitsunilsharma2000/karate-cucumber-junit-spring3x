package com.example.multichannelnotifier.exception;

/**
 * Custom exception for notification errors.
 * <p>
 * This exception indicates that an error occurred within the Notification Service,
 * typically due to invalid input, processing errors, or unexpected failures during
 * notification delivery. It provides a mechanism for centralized exception handling
 * and consistent error messaging throughout the application.
 * </p>
 *
 * <p>
 * <strong>Pass/Fail Conditions:</strong><br>
 * Pass: Notification process completes successfully without triggering this exception.<br>
 * Fail: An error is encountered during the notification process (e.g., invalid inputs,
 * processing issues), resulting in this exception being thrown.
 * </p>
 *
 * <p>
 * <strong>Usage:</strong><br>
 * This exception should be thrown to signal that an error condition has occurred
 * in the notification process, allowing for appropriate error handling and logging at higher levels.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public class NotificationException extends RuntimeException {

    /**
     * Constructs a new {@code NotificationException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public NotificationException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code NotificationException} with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
