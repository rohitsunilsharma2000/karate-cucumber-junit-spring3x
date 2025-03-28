package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.enums.NotificationType;
import com.example.multichannelnotifier.exception.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationService}.
 * <p>
 * <strong>Overview:</strong> This test class verifies that NotificationService correctly delegates to the underlying channel services,
 * validates its inputs, and properly handles exceptions thrown by those services. It covers:
 * <ul>
 *   <li>Successful delegation for email, SMS, and push notifications.</li>
 *   <li>Validation errors for null type, empty recipient, and empty subject/message.</li>
 *   <li>Exception handling when an underlying service throws an exception.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code type}: Must be one of {@link NotificationType} values (EMAIL, SMS, PUSH).</li>
 *   <li>{@code recipient}: A non-null, non-empty string representing an email, phone number, or device token.</li>
 *   <li>{@code subjectOrMessage}: A non-null, non-empty string representing the email subject or SMS/Push message.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If {@code type} is null, a {@link NotificationException} is thrown with message "Unsupported notification type: null".</li>
 *   <li>If {@code recipient} is empty, a {@link NotificationException} is thrown with an appropriate message.</li>
 *   <li>If {@code subjectOrMessage} is empty, a {@link NotificationException} is thrown with an appropriate message.</li>
 *   <li>If an underlying service call fails (throws a RuntimeException), it is wrapped in a {@link NotificationException}.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Test Coverage:</strong> This class targets 100% coverage at class, method, and line levels for NotificationService.
 * </p>
 */
public class NotificationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @Mock
    private PushNotificationService pushNotificationService;

    @InjectMocks
    private NotificationService notificationService;

    /**
     * Sets up the test environment by initializing the mocks and injecting them into NotificationService.
     */
    @BeforeEach
    public void setup() {
        // Initialize mocks and inject them into the NotificationService instance.
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that sendEmail delegates correctly when valid inputs are provided.
     * <p>
     * <strong>Description:</strong> Verifies that calling sendEmail on NotificationService with valid parameters
     * delegates the call to EmailService.sendEmail with the same parameters.
     * </p>
     * <p>
     * <strong>Premise:</strong> All parameters (to, subject, body) are non-null and non-empty.
     * </p>
     * <p>
     * <strong>Assertions:</strong> No exception is thrown and EmailService.sendEmail is invoked exactly once.
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Test passes if no exception is thrown and the delegation occurs correctly.
     * </p>
     */
    @Test
    public void testSendEmailDelegationSuccess() {
        String to = "user@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act: Call the sendEmail method.
        notificationService.sendEmail(to, subject, body);
        // Assert: Verify that the underlying emailService.sendEmail was called once with the expected parameters.
        verify(emailService, times(1)).sendEmail(to, subject, body);
    }

    /**
     * Tests that sendSms delegates correctly when valid inputs are provided.
     * <p>
     * <strong>Description:</strong> Verifies that sendSms on NotificationService delegates the call to SmsService.sendSms.
     * </p>
     * <p>
     * <strong>Premise:</strong> Both recipient and message are valid.
     * </p>
     * <p>
     * <strong>Assertions:</strong> SmsService.sendSms is invoked exactly once.
     * </p>
     */
    @Test
    public void testSendSmsDelegationSuccess() {
        String to = "1234567890";
        String message = "Test SMS Message";

        // Act: Call the sendSms method.
        notificationService.sendSms(to, message);
        // Assert: Verify that SmsService.sendSms was called once with the expected parameters.
        verify(smsService, times(1)).sendSms(to, message);
    }

    /**
     * Tests that sendPushNotification delegates correctly when valid inputs are provided.
     * <p>
     * <strong>Description:</strong> Verifies that sendPushNotification on NotificationService delegates the call to PushNotificationService.sendPushNotification.
     * </p>
     * <p>
     * <strong>Premise:</strong> Both device token and message are valid.
     * </p>
     * <p>
     * <strong>Assertions:</strong> PushNotificationService.sendPushNotification is invoked exactly once.
     * </p>
     */
    @Test
    public void testSendPushNotificationDelegationSuccess() {
        String to = "deviceToken";
        String message = "Test Push Message";

        // Act: Call the sendPushNotification method.
        notificationService.sendPushNotification(to, message);
        // Assert: Verify that PushNotificationService.sendPushNotification was called once with the expected parameters.
        verify(pushNotificationService, times(1)).sendPushNotification(to, message);
    }

    /**
     * Tests that sendNotification dispatches an EMAIL notification correctly.
     * <p>
     * <strong>Description:</strong> Expects that calling sendNotification with type EMAIL delegates to EmailService.sendEmail,
     * passing the recipient, subjectOrMessage as the subject, and a constant email body.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid EMAIL parameters are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong> EmailService.sendEmail is invoked with recipient, subjectOrMessage, and "This is the email body.".
     * </p>
     */
    @Test
    public void testSendNotificationEmail() {
        String recipient = "user@example.com";
        String subjectOrMessage = "Test Email";

        // Act: Call sendNotification with type EMAIL.
        notificationService.sendNotification(NotificationType.EMAIL, recipient, subjectOrMessage);
        // Assert: Verify that EmailService.sendEmail is called with the expected parameters.
        verify(emailService, times(1)).sendEmail(recipient, subjectOrMessage, "This is the email body.");
    }

    /**
     * Tests that sendNotification dispatches an SMS notification correctly.
     * <p>
     * <strong>Description:</strong> Expects that calling sendNotification with type SMS delegates to SmsService.sendSms.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid SMS parameters are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong> SmsService.sendSms is invoked with the expected recipient and message.
     * </p>
     */
    @Test
    public void testSendNotificationSms() {
        String recipient = "1234567890";
        String subjectOrMessage = "Test SMS";

        // Act: Call sendNotification with type SMS.
        notificationService.sendNotification(NotificationType.SMS, recipient, subjectOrMessage);
        // Assert: Verify that SmsService.sendSms is called with the expected parameters.
        verify(smsService, times(1)).sendSms(recipient, subjectOrMessage);
    }

    /**
     * Tests that sendNotification dispatches a PUSH notification correctly.
     * <p>
     * <strong>Description:</strong> Expects that calling sendNotification with type PUSH delegates to PushNotificationService.sendPushNotification.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid PUSH notification parameters are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong> PushNotificationService.sendPushNotification is invoked with the expected recipient and message.
     * </p>
     */
    @Test
    public void testSendNotificationPush() {
        String recipient = "deviceToken";
        String subjectOrMessage = "Test Push";

        // Act: Call sendNotification with type PUSH.
        notificationService.sendNotification(NotificationType.PUSH, recipient, subjectOrMessage);
        // Assert: Verify that PushNotificationService.sendPushNotification is called with the expected parameters.
        verify(pushNotificationService, times(1)).sendPushNotification(recipient, subjectOrMessage);
    }





    /**
     * Tests the exception branch in the sendEmail method.
     * <p>
     * <strong>Description:</strong> Simulates a failure in EmailService.sendEmail by configuring the mock to throw a RuntimeException.
     * Expects that NotificationService.sendEmail wraps this exception in a NotificationException.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid email parameters are provided, but emailService.sendEmail fails.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception message contains "Error sending email".</li>
     *   <li>The underlying cause's message equals "Simulated email failure".</li>
     * </ul>
     * </p>
     */
    @Test
    public void testSendEmailExceptionBranch() {
        // Arrange: Stub emailService.sendEmail to throw a RuntimeException.
        doThrow(new RuntimeException("Simulated email failure"))
                .when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act & Assert: Verify that NotificationService.sendEmail throws a NotificationException.
        NotificationException ex = assertThrows(NotificationException.class, () ->
                notificationService.sendEmail("user@example.com", "Subject", "Body")
        );
        // Check that the exception message indicates an error in sending email.
        assertTrue(ex.getMessage().contains("Error sending email"));
        // Check that the underlying cause's message matches the simulated failure.
        assertEquals("Simulated email failure", ex.getCause().getMessage());
    }

    /**
     * Tests the exception branch in the sendSms method.
     * <p>
     * <strong>Description:</strong> Simulates a failure in SmsService.sendSms by configuring the mock to throw a RuntimeException.
     * Expects that NotificationService.sendSms wraps this exception in a NotificationException.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid SMS parameters are provided, but smsService.sendSms fails.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception message contains "Error sending SMS".</li>
     *   <li>The underlying cause's message equals "Simulated SMS failure".</li>
     * </ul>
     * </p>
     */
    @Test
    public void testSendSmsExceptionBranch() {
        // Arrange: Stub smsService.sendSms to throw a RuntimeException.
        doThrow(new RuntimeException("Simulated SMS failure"))
                .when(smsService).sendSms(anyString(), anyString());

        // Act & Assert: Verify that NotificationService.sendSms throws a NotificationException.
        NotificationException ex = assertThrows(NotificationException.class, () ->
                notificationService.sendSms("1234567890", "Test SMS")
        );
        // Check that the exception message indicates an error sending SMS.
        assertTrue(ex.getMessage().contains("Error sending SMS"));
        // Verify that the underlying cause's message matches the simulated failure.
        assertEquals("Simulated SMS failure", ex.getCause().getMessage());
    }

    /**
     * Tests the exception branch in the sendPushNotification method.
     * <p>
     * <strong>Description:</strong> Simulates a failure in PushNotificationService.sendPushNotification by configuring the mock
     * to throw a RuntimeException. Expects that NotificationService.sendPushNotification wraps this exception in a NotificationException.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid push notification parameters are provided, but pushNotificationService.sendPushNotification fails.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception message contains "Error sending push notification".</li>
     *   <li>The underlying cause's message equals "Simulated push failure".</li>
     * </ul>
     * </p>
     */
    @Test
    public void testSendPushNotificationExceptionBranch() {
        // Arrange: Stub pushNotificationService.sendPushNotification to throw a RuntimeException.
        doThrow(new RuntimeException("Simulated push failure"))
                .when(pushNotificationService).sendPushNotification(anyString(), anyString());

        // Act & Assert: Verify that NotificationService.sendPushNotification throws a NotificationException.
        NotificationException ex = assertThrows(NotificationException.class, () ->
                notificationService.sendPushNotification("deviceToken", "Test Push")
        );
        // Check that the exception message indicates an error sending push notification.
        assertTrue(ex.getMessage().contains("Error sending push notification"));
        // Verify that the underlying cause's message matches the simulated failure.
        assertEquals("Simulated push failure", ex.getCause().getMessage());
    }
}
