package com.example.multichannelnotifier.exception;

/**
 * Custom exception for handling push notification errors.
 * <p>
 * This exception is thrown when invalid input is provided for sending a push notification or when an error occurs during
 * the push notification sending process.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public class PushNotificationException extends RuntimeException {

    /**
     * Constructs a new PushNotificationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public PushNotificationException(String message) {
        super(message);
    }

    /**
     * Constructs a new PushNotificationException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public PushNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
