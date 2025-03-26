package com.example.notificationservice.service;

import com.example.notificationservice.exception.PushNotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PushNotificationService} class.
 * <p>
 * This test class verifies that the {@link PushNotificationService#sendPushNotification(String, String)}
 * method behaves as expected. It ensures that valid input results in successful execution without exceptions,
 * and invalid inputs (null or empty device token or message) cause a {@link PushNotificationException} to be thrown
 * with the appropriate error message.
 * </p>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> When valid parameters are provided, the method executes successfully (i.e. does not throw an exception).</li>
 *   <li><strong>Fail:</strong> When the device token or message is null or empty, the method throws a {@link PushNotificationException}
 *       with the expected error message.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class PushNotificationServiceTest {

    // Create an instance of the PushNotificationService to test its sendPushNotification method.
    private final PushNotificationService pushNotificationService = new PushNotificationService();

    /**
     * Tests that sendPushNotification completes successfully when valid parameters are provided.
     * <p>
     * <strong>Description:</strong> This test verifies that if a valid device token and message are provided,
     * the method completes without throwing an exception.
     * </p>
     */
    @Test
    @DisplayName("Test sendPushNotification with valid parameters")
    void testSendPushNotification_Success() {
        // Arrange
        String deviceToken = "validDeviceToken";
        String message = "Test push message";

        // Act & Assert: The method should execute without throwing an exception.
        assertDoesNotThrow(() -> pushNotificationService.sendPushNotification(deviceToken, message),
                           "sendPushNotification should not throw an exception when valid parameters are provided");
    }

    /**
     * Tests that sendPushNotification throws a PushNotificationException when the device token is null.
     * <p>
     * <strong>Description:</strong> This test verifies that if the device token is null,
     * sendPushNotification throws a PushNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendPushNotification with null device token")
    void testSendPushNotification_NullDeviceToken() {
        // Arrange
        String deviceToken = null;
        String message = "Test push message";

        // Act & Assert: Expect a PushNotificationException with the appropriate error message.
        PushNotificationException exception = assertThrows(PushNotificationException.class,
                                                           () -> pushNotificationService.sendPushNotification(deviceToken, message),
                                                           "sendPushNotification should throw PushNotificationException when device token is null");
        assertEquals("Device token must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that sendPushNotification throws a PushNotificationException when the device token is empty.
     * <p>
     * <strong>Description:</strong> This test verifies that if the device token is an empty string or contains only whitespace,
     * sendPushNotification throws a PushNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendPushNotification with empty device token")
    void testSendPushNotification_EmptyDeviceToken() {
        // Arrange
        String deviceToken = "   ";
        String message = "Test push message";

        // Act & Assert: Expect a PushNotificationException with the appropriate error message.
        PushNotificationException exception = assertThrows(PushNotificationException.class,
                                                           () -> pushNotificationService.sendPushNotification(deviceToken, message),
                                                           "sendPushNotification should throw PushNotificationException when device token is empty");
        assertEquals("Device token must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that sendPushNotification throws a PushNotificationException when the message is null.
     * <p>
     * <strong>Description:</strong> This test verifies that if the push notification message is null,
     * sendPushNotification throws a PushNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendPushNotification with null message")
    void testSendPushNotification_NullMessage() {
        // Arrange
        String deviceToken = "validDeviceToken";
        String message = null;

        // Act & Assert: Expect a PushNotificationException with the appropriate error message.
        PushNotificationException exception = assertThrows(PushNotificationException.class,
                                                           () -> pushNotificationService.sendPushNotification(deviceToken, message),
                                                           "sendPushNotification should throw PushNotificationException when message is null");
        assertEquals("Push notification message content must not be null or empty.", exception.getMessage());
    }

    /**
     * Tests that sendPushNotification throws a PushNotificationException when the message is empty.
     * <p>
     * <strong>Description:</strong> This test verifies that if the push notification message is an empty string or contains only whitespace,
     * sendPushNotification throws a PushNotificationException with the expected error message.
     * </p>
     */
    @Test
    @DisplayName("Test sendPushNotification with empty message")
    void testSendPushNotification_EmptyMessage() {
        // Arrange
        String deviceToken = "validDeviceToken";
        String message = "   ";

        // Act & Assert: Expect a PushNotificationException with the appropriate error message.
        PushNotificationException exception = assertThrows(PushNotificationException.class,
                                                           () -> pushNotificationService.sendPushNotification(deviceToken, message),
                                                           "sendPushNotification should throw PushNotificationException when message is empty");
        assertEquals("Push notification message content must not be null or empty.", exception.getMessage());
    }
}
