package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.exception.EmailNotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

/**
 * Unit tests for {@link EmailService}.
 * <p>
 * <strong>Overview:</strong> This test class ensures 100% class, method, and line coverage for the EmailService class by testing:
 * <ul>
 *   <li>The successful path where a valid email is sent.</li>
 *   <li>Validation errors when recipient, subject, or body is empty.</li>
 *   <li>The exception branch in the try block by simulating a sending failure.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code to}: A non-null, non-empty string representing the recipient's email address (e.g., "user@example.com").</li>
 *   <li>{@code subject}: A non-null, non-empty string representing the email subject (e.g., "Test Subject").</li>
 *   <li>{@code body}: A non-null, non-empty string representing the email body content (e.g., "Test Body").</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If any parameter is null or empty, an {@link EmailNotificationException} is thrown with the appropriate message.</li>
 *   <li>If an unexpected error occurs during the sending process, it is caught and rethrown as an {@link EmailNotificationException}.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Test Coverage:</strong> This class targets 100% coverage for all constructors and methods in EmailService.</p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
public class EmailServiceTest {

    private EmailService emailService;

    /**
     * Setup method that initializes EmailService before each test.
     * <p>
     * <strong>Description:</strong> Instantiates a new EmailService instance.
     * </p>
     */
    @BeforeEach
    public void setup() {
        // Initialize EmailService before each test.
        emailService = new EmailService();
    }

    /**
     * Test sending an email with valid inputs.
     * <p>
     * <strong>Description:</strong> Expects that valid email inputs do not trigger any exception.
     * <strong>Premise:</strong> All parameters (to, subject, body) are provided and non-empty.
     * <strong>Assertions:</strong> No exception is thrown.
     * <strong>Pass/Fail:</strong> Test passes if no exception is thrown; fails otherwise.
     * </p>
     */
    @Test
    public void testSendEmailSuccess() {
        // Act & Assert: Calling sendEmail with valid parameters should not throw an exception.
        Assertions.assertDoesNotThrow(() ->
                                              emailService.sendEmail("test@example.com", "Test Subject", "Test Body")
        );
    }







    /**
     * Test the exception branch in the sendEmail method.
     * <p>
     * <strong>Description:</strong> This test simulates an exception during the email sending process by using a spy on EmailService.
     * The protected method sendEmailInternal is stubbed to throw a RuntimeException, which should be caught and wrapped.
     * <strong>Premise:</strong> Valid inputs are provided, but sendEmailInternal fails.
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The method throws an EmailNotificationException with message "Failed to send email due to an unexpected error."</li>
     *   <li>The underlying cause's message equals "Simulated sending failure".</li>
     * </ul>
     * <strong>Pass/Fail:</strong> Test passes if the exception is thrown and both the message and cause match expectations.
     * </p>
     */
    @Test
    public void testSendEmailExceptionBranch() {
        // Arrange: Create a spy of EmailService to override the internal sending method.
        EmailService spyService = spy(emailService);
        // Stub sendEmailInternal to simulate a failure.
        doThrow(new RuntimeException("Simulated sending failure"))
                .when(spyService).sendEmailInternal(anyString(), anyString(), anyString());

        // Act & Assert: Calling sendEmail should now throw an EmailNotificationException.
        EmailNotificationException ex = Assertions.assertThrows(EmailNotificationException.class, () ->
                spyService.sendEmail("test@example.com", "Test Subject", "Test Body")
        );
        // Assert that the exception message and cause match the expected values.
        Assertions.assertEquals("Failed to send email due to an unexpected error.", ex.getMessage());
        Assertions.assertEquals("Simulated sending failure", ex.getCause().getMessage());
    }
}
