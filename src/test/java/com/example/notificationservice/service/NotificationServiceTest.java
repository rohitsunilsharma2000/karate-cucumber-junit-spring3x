package com.example.notificationservice.service;

import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.exception.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationService} class.
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
     * Setup the mocks before each test.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test that the sendEmail method calls EmailService.sendEmail
     * and does not throw any exceptions for a valid input.
     */
    @Test
    public void testSendEmail_Success() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Call the method under test.
        notificationService.sendEmail(to, subject, body);

        // Verify that the email service was called exactly once with the provided arguments.
        verify(emailService, times(1)).sendEmail(to, subject, body);
    }

    /**
     * Test that the sendSms method calls SmsService.sendSms
     * and does not throw any exceptions for a valid input.
     */
    @Test
    public void testSendSms_Success() {
        String to = "1234567890";
        String message = "Test SMS";

        // Call the method under test.
        notificationService.sendSms(to, message);

        // Verify that the SMS service was called exactly once with the provided arguments.
        verify(smsService, times(1)).sendSms(to, message);
    }

    /**
     * Test that the sendPushNotification method calls PushNotificationService.sendPushNotification
     * and does not throw any exceptions for a valid input.
     */
    @Test
    public void testSendPushNotification_Success() {
        String to = "deviceToken";
        String message = "Test Push";

        // Call the method under test.
        notificationService.sendPushNotification(to, message);

        // Verify that the push notification service was called exactly once with the provided arguments.
        verify(pushNotificationService, times(1)).sendPushNotification(to, message);
    }

    /**
     * Test that sendNotification dispatches the email channel when the NotificationType is EMAIL.
     */
    @Test
    public void testSendNotification_Email() {
        String recipient = "test@example.com";
        String subjectOrMessage = "Test Email";

        // Call the method under test with EMAIL type.
        notificationService.sendNotification(NotificationType.EMAIL, recipient, subjectOrMessage);

        // Verify that email service is called with the expected parameters.
        verify(emailService, times(1)).sendEmail(recipient, subjectOrMessage, "This is the email body.");
    }

    /**
     * Test that sendNotification dispatches the SMS channel when the NotificationType is SMS.
     */
    @Test
    public void testSendNotification_Sms() {
        String recipient = "1234567890";
        String subjectOrMessage = "Test SMS";

        // Call the method under test with SMS type.
        notificationService.sendNotification(NotificationType.SMS, recipient, subjectOrMessage);

        // Verify that SMS service is called with the expected parameters.
        verify(smsService, times(1)).sendSms(recipient, subjectOrMessage);
    }

    /**
     * Test that sendNotification dispatches the push channel when the NotificationType is PUSH.
     */
    @Test
    public void testSendNotification_Push() {
        String recipient = "deviceToken";
        String subjectOrMessage = "Test Push";

        // Call the method under test with PUSH type.
        notificationService.sendNotification(NotificationType.PUSH, recipient, subjectOrMessage);

        // Verify that push notification service is called with the expected parameters.
        verify(pushNotificationService, times(1)).sendPushNotification(recipient, subjectOrMessage);
    }

    /**
     * Test that sendNotification throws NotificationException when a null NotificationType is provided.
     */
    @Test
    public void testSendNotification_InvalidType_Null() {
        // Expect a NotificationException when calling with null NotificationType.
        NotificationException exception = assertThrows(NotificationException.class, () ->
                notificationService.sendNotification(null, "recipient", "message")
        );
        assertTrue(exception.getMessage().contains("Unsupported notification type"));
    }

    /**
     * Test that sendEmail propagates and wraps exceptions thrown by EmailService.
     */
    @Test
    public void testSendEmail_ExceptionHandling() {
        String to = "error@example.com";
        String subject = "Test Exception";
        String body = "Test Body";

        // Simulate an exception thrown by the email service.
        doThrow(new RuntimeException("Simulated error")).when(emailService).sendEmail(to, subject, body);

        // Verify that a NotificationException is thrown with the correct message.
        NotificationException exception = assertThrows(NotificationException.class, () ->
                notificationService.sendEmail(to, subject, body)
        );
        assertTrue(exception.getMessage().contains("Error sending email"));
    }

    /**
     * Test that sendSms propagates and wraps exceptions thrown by SmsService.
     */
    @Test
    public void testSendSms_ExceptionHandling() {
        String to = "0000000000";
        String message = "Test SMS Exception";

        // Simulate an exception thrown by the SMS service.
        doThrow(new RuntimeException("Simulated SMS error")).when(smsService).sendSms(to, message);

        // Verify that a NotificationException is thrown with the correct message.
        NotificationException exception = assertThrows(NotificationException.class, () ->
                notificationService.sendSms(to, message)
        );
        assertTrue(exception.getMessage().contains("Error sending SMS"));
    }

    /**
     * Test that sendPushNotification propagates and wraps exceptions thrown by PushNotificationService.
     */
    @Test
    public void testSendPushNotification_ExceptionHandling() {
        String to = "errorDeviceToken";
        String message = "Test Push Exception";

        // Simulate an exception thrown by the push notification service.
        doThrow(new RuntimeException("Simulated push error")).when(pushNotificationService).sendPushNotification(to, message);

        // Verify that a NotificationException is thrown with the correct message.
        NotificationException exception = assertThrows(NotificationException.class, () ->
                notificationService.sendPushNotification(to, message)
        );
        assertTrue(exception.getMessage().contains("Error sending push notification"));
    }
}
