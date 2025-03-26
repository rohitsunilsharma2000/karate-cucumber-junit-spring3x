package com.example.notificationservice.enums;

/**
 * Enum representing the available notification types within the Notification Service.
 *
 * <p>
 * This enum defines the supported types of notifications that can be processed by the service.
 * It includes the following values:
 * </p>
 *
 * <ul>
 *     <li><strong>EMAIL:</strong> Indicates that the notification will be sent via email.</li>
 *     <li><strong>SMS:</strong> Indicates that the notification will be sent as an SMS message.</li>
 *     <li><strong>PUSH:</strong> Indicates that the notification will be delivered as a push notification.</li>
 * </ul>
 *
 * <p>
 * <strong>Pass/Fail Conditions:</strong><br>
 * Pass: The application correctly identifies and processes the specified notification type.<br>
 * Fail: An unsupported notification type may lead to processing errors.
 * </p>
 *
 * <p>
 * <strong>Usage:</strong> This enum is used throughout the Notification Service to determine the
 * delivery mechanism for a notification.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public enum NotificationType {
    EMAIL,
    SMS,
    PUSH
}
