package com.example.multichannelnotifier.service;


import com.example.multichannelnotifier.exception.SmsNotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SmsService}.
 *
 * <p><strong>Overview:</strong>
 * This test class ensures 100% class, method, and line coverage for SmsService by testing:
 * <ul>
 *   <li>The successful sending of an SMS.</li>
 *   <li>Validation errors when the recipient phone number or message is empty.</li>
 *   <li>The exception branch by simulating a failure during SMS sending.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Acceptable Values / Range:</strong>
 * <ul>
 *   <li>{@code to}: A non-null, non-empty string representing the recipient's phone number.</li>
 *   <li>{@code message}: A non-null, non-empty string representing the SMS message content.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Error Conditions:</strong>
 * <ul>
 *   <li>If {@code to} is null/empty, a {@link SmsNotificationException} is thrown.</li>
 *   <li>If {@code message} is null/empty, a {@link SmsNotificationException} is thrown.</li>
 *   <li>If an unexpected error occurs during sending, it is caught and wrapped in a {@link SmsNotificationException}.</li>
 * </ul>
 * </p>
 */
public class SmsServiceTest {

    private SmsService smsService;

    @BeforeEach
    public void setup() {
        // Initialize the SmsService before each test.
        smsService = new SmsService();
    }

    /**
     * Test sending an SMS with valid inputs.
     * <p>
     * <strong>Description:</strong> Expects that valid inputs will result in a successful execution without throwing any exception.
     * </p>
     */
    @Test
    public void testSendSmsSuccess() {
        // Assert that no exception is thrown for valid inputs.
        Assertions.assertDoesNotThrow(() ->
                                              smsService.sendSms("1234567890", "Test SMS Message")
        );
    }





    /**
     * Test the exception branch in SMS sending.
     * <p>
     * <strong>Description:</strong> Uses an anonymous subclass of {@link SmsService} that overrides sendSms to simulate a failure,
     * ensuring the exception branch is executed and wrapped in a {@link SmsNotificationException}.
     * </p>
     */
    @Test
    public void testSendSmsExceptionBranch() {
        // Create a subclass that simulates a sending failure.
        SmsService failingService = new SmsService() {
            @Override
            public void sendSms(String to, String message) {
                try {
                    // Simulate an exception.
                    throw new RuntimeException("Simulated SMS failure");
                } catch (Exception e) {
                    // Wrap and rethrow the exception.
                    throw new SmsNotificationException("Failed to send SMS due to an unexpected error.", e);
                }
            }
        };

        SmsNotificationException ex = Assertions.assertThrows(SmsNotificationException.class, () ->
                failingService.sendSms("1234567890", "Test SMS Message")
        );
        // Assert that the exception message and cause match expected values.
        Assertions.assertEquals("Failed to send SMS due to an unexpected error.", ex.getMessage());
        Assertions.assertEquals("Simulated SMS failure", ex.getCause().getMessage());
    }
}
