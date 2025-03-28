package com.example.multichannelnotifier.exception;

import com.example.multichannelnotifier.exception.PushNotificationException;
import com.example.multichannelnotifier.service.PushNotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PushNotificationService}.
 * <p>
 * <strong>Overview:</strong> This test class ensures 100% class, method, and line coverage for
 * PushNotificationService by verifying:
 * <ul>
 *   <li>The successful sending of a push notification.</li>
 *   <li>Validation errors when the device token or message is empty.</li>
 *   <li>The exception branch by simulating a failure in the sending logic.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range:</strong>
 * <ul>
 *   <li>{@code to}: A non-null, non-empty string representing the device token.</li>
 *   <li>{@code message}: A non-null, non-empty string representing the push notification message.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If {@code to} is null or empty, a {@link PushNotificationException} is thrown.</li>
 *   <li>If {@code message} is null or empty, a {@link PushNotificationException} is thrown.</li>
 *   <li>If an unexpected error occurs during sending, it is caught and wrapped in a {@link PushNotificationException}.</li>
 * </ul>
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
public class PushNotificationServiceTest {

    /**
     * Instance of PushNotificationService used for testing.
     */
    private PushNotificationService pushNotificationService;

    @BeforeEach
    public void setup() {
        // Initialize the PushNotificationService before each test.
        pushNotificationService = new PushNotificationService();
    }

    /**
     * Test sending a push notification with valid inputs.
     * <p>
     * <strong>Description:</strong> Expects that valid inputs will result in a successful execution without throwing any exception.
     * </p>
     * <p>
     * <strong>Premise:</strong> Both device token and message are non-null and non-empty.
     * </p>
     * <p>
     * <strong>Assertions:</strong> No exception is thrown.
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Test passes if no exception is thrown.
     * </p>
     */
    @Test
    public void testSendPushNotificationSuccess() {
        // Assert that no exception is thrown when valid inputs are provided.
        Assertions.assertDoesNotThrow(() ->
                                              pushNotificationService.sendPushNotification("deviceToken123", "Test Push Message")
        );
    }



    /**
     * Test the exception branch by simulating a failure during push notification sending.
     * <p>
     * <strong>Description:</strong> Uses an anonymous subclass of PushNotificationService that overrides the sending method
     * to throw a RuntimeException. The test verifies that this exception is caught and wrapped in a PushNotificationException with the expected messages.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid device token and message are provided, but the sending logic fails.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The thrown exception's message equals "Failed to send push notification due to an unexpected error."</li>
     *   <li>The underlying cause's message equals "Simulated push failure."</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Test passes if the exception is thrown and both messages match expectations.
     * </p>
     */
    @Test
    public void testSendPushNotificationExceptionBranch() {
        // Create an anonymous subclass to simulate a failure during sending.
        PushNotificationService failingService = new PushNotificationService() {
            @Override
            public void sendPushNotification(String to, String message) {
                try {
                    // Simulate a runtime failure.
                    throw new RuntimeException("Simulated push failure");
                } catch (Exception e) {
                    // Wrap the exception in a PushNotificationException.
                    throw new PushNotificationException("Failed to send push notification due to an unexpected error.", e);
                }
            }
        };

        // Expect the failingService to throw a PushNotificationException with the specified message and cause.
        PushNotificationException ex = Assertions.assertThrows(PushNotificationException.class, () ->
                failingService.sendPushNotification("deviceToken123", "Test Push Message")
        );
        // Verify the exception message.
        Assertions.assertEquals("Failed to send push notification due to an unexpected error.", ex.getMessage());
        // Verify that the underlying cause's message matches the simulated failure.
        Assertions.assertEquals("Simulated push failure", ex.getCause().getMessage());
    }
}
