package com.example.notificationservice.exception;


/**
 * Custom exception for notification errors.
 */
public class NotificationException extends RuntimeException {
    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
