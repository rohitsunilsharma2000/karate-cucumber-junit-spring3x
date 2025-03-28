package com.example.multichannelnotifier.controller;

import com.example.multichannelnotifier.dto.NotificationRequestDTO;
import com.example.multichannelnotifier.enums.NotificationType;
import com.example.multichannelnotifier.exception.GlobalExceptionHandler;
import com.example.multichannelnotifier.exception.NotificationException;
import com.example.multichannelnotifier.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link NotificationController} using @WebMvcTest.
 * <p>
 * <strong>Overview:</strong> This test class verifies the end-to-end behavior of the
 * NotificationController by loading the full Spring Boot context and simulating HTTP requests using MockMvc.
 * It covers:
 * <ul>
 *   <li>Successful processing of a valid notification request.</li>
 *   <li>Input validation errors resulting in HTTP 400 responses.</li>
 *   <li>Exception handling when the service layer throws a {@link NotificationException}, resulting in an HTTP 500 response.</li>
 * </ul>
 * <strong>Test Coverage:</strong> The class aims for 100% coverage at the class, method, and line levels.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code type}: A valid {@link NotificationType} value (EMAIL, SMS, or PUSH).</li>
 *   <li>{@code recipient}: A non-null, non-empty string (e.g., "user@example.com", "1234567890", or "deviceToken123").</li>
 *   <li>{@code subjectOrMessage}: A non-null, non-empty string representing the email subject (for EMAIL) or message content (for SMS/PUSH).</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>HTTP 400 (Bad Request) if input validation fails (e.g., empty recipient).</li>
 *   <li>HTTP 500 (Internal Server Error) if a {@link NotificationException} is thrown by the service layer.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Constraints:</strong> Security filters are disabled in tests to focus on controller behavior.
 * </p>
 */
@WebMvcTest(controllers = NotificationController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class NotificationControllerUnitTest {

    /**
     * MockMvc instance used to perform HTTP requests against the controller.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked NotificationService to simulate service layer behavior.
     */
    @MockBean
    private NotificationService notificationService;

    /**
     * ObjectMapper for serializing DTOs to JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Test successful notification send.
     * <p>
     * <strong>Description:</strong> This test sends a valid EMAIL notification request and verifies:
     * <ul>
     *   <li>The HTTP status is 200 (OK).</li>
     *   <li>The response message contains the expected success text.</li>
     *   <li>{@link NotificationService#sendNotification(NotificationType, String, String)} is invoked with the correct parameters.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Premise:</strong> The DTO is populated with valid EMAIL values:
     * - {@code type} = EMAIL
     * - {@code recipient} = "user@example.com"
     * - {@code subjectOrMessage} = "Test Subject"
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>Status code equals 200.</li>
     *   <li>Response body contains "email notification was successfully sent to user@example.com".</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail:</strong> Test passes if all assertions are met.
     * </p>
     */
    @Test
    public void testSendNotificationSuccess() throws Exception {
        // Create a valid notification request DTO for an EMAIL notification.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.EMAIL);
        requestDTO.setRecipient("user@example.com");
        requestDTO.setSubjectOrMessage("Test Subject");

        // Perform a POST request to /notifications/send with valid JSON content.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               // Expect HTTP 200 OK.
               .andExpect(status().isOk())
               // Check that the response message contains the expected text.
               .andExpect(content().string(containsString("email notification was successfully sent to user@example.com")));

        // Verify that NotificationService.sendNotification is called with the expected parameters.
        verify(notificationService).sendNotification(NotificationType.EMAIL, "user@example.com", "Test Subject");
    }

    /**
     * Test input validation error.
     * <p>
     * <strong>Description:</strong> This test sends a notification request with an empty recipient,
     * expecting a HTTP 400 (Bad Request) due to input validation failure.
     * </p>
     * <p>
     * <strong>Premise:</strong> The DTO contains an empty {@code recipient}, which is invalid.
     * </p>
     * <p>
     * <strong>Assertions:</strong> The response status must be 400.
     * </p>
     * <p>
     * <strong>Pass/Fail:</strong> Test passes if the controller returns HTTP 400.
     * </p>
     */
    @Test
    public void testSendNotificationValidationError() throws Exception {
        // Create a DTO with an empty recipient to trigger validation error.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.SMS);
        requestDTO.setRecipient("");  // Invalid input.
        requestDTO.setSubjectOrMessage("Test Message");

        // Perform the POST request and expect HTTP 400 Bad Request.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               .andExpect(status().isBadRequest());
    }

    /**
     * Test exception handling in the controller.
     * <p>
     * <strong>Description:</strong> This test simulates an exception in the service layer by configuring the mocked
     * {@link NotificationService} to throw a {@link NotificationException} when processing a valid PUSH notification request.
     * The test verifies that the global exception handler intercepts the exception and returns an HTTP 500 response.
     * </p>
     * <p>
     * <strong>Premise:</strong> The DTO is valid for a PUSH notification, but the service layer is simulated to fail.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The response status is 500 (Internal Server Error).</li>
     *   <li>The resolved exception is a {@link NotificationException} with the message "Simulated exception".</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail:</strong> Test passes if the HTTP 500 status and exception details match the expectations.
     * </p>
     */
    @Test
    public void testSendNotificationServiceException() throws Exception {
        // Create a valid notification request DTO for a PUSH notification.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.PUSH);
        requestDTO.setRecipient("deviceToken123");
        requestDTO.setSubjectOrMessage("Test Push Message");

        // Configure the mocked NotificationService to throw a NotificationException when sendNotification is called.
        doThrow(new NotificationException("Simulated exception"))
                .when(notificationService).sendNotification(any(), any(), any());

        // Perform the POST request and expect HTTP 500 Internal Server Error.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               .andExpect(status().isInternalServerError())
               // Validate that the response content type is JSON.
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               // Validate that the resolved exception's message equals "Simulated exception".
               .andExpect(result -> {
                   Exception resolvedException = result.getResolvedException();
                   assertNotNull(resolvedException, "Resolved exception should not be null");
                   assertTrue(resolvedException instanceof NotificationException, "Expected NotificationException");
                   assertEquals("Simulated exception", resolvedException.getMessage());
               });

        // Verify that NotificationService.sendNotification is called with the expected parameters.
        verify(notificationService).sendNotification(NotificationType.PUSH, "deviceToken123", "Test Push Message");
    }
}
