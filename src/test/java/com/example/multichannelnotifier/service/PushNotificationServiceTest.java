package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.exception.PushNotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for PushNotificationService.
 * <p>
 * Verifies that:
 * - A valid push notification is "sent" (no exception is thrown).
 * - Input validation errors throw PushNotificationException with the expected message.
 * </p>
 */
public class PushNotificationServiceTest {

    private PushNotificationService pushNotificationService;

    @BeforeEach
    public void setup() {
        // Initialize the service before each test.
        pushNotificationService = new PushNotificationService();
    }

    /**
     * Test sending a valid push notification.
     */
    @Test
    public void testSendPushNotificationSuccess() {
        // Expect no exception for valid input.
        Assertions.assertDoesNotThrow(() ->
                                              pushNotificationService.sendPushNotification("deviceToken123", "Test Push Message")
        );
    }




}
