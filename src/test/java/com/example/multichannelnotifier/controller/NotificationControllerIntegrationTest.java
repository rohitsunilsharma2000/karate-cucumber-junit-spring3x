package com.example.multichannelnotifier.controller;

import com.example.multichannelnotifier.dto.NotificationRequestDTO;
import com.example.multichannelnotifier.enums.NotificationType;
import com.example.multichannelnotifier.exception.NotificationException;
import com.example.multichannelnotifier.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
 * Integration tests for {@link NotificationController}.
 * <p>
 * <strong>Overview:</strong> This test class verifies the end-to-end behavior of the
 * NotificationController by loading the full Spring Boot application context and using MockMvc.
 * It covers:
 * <ul>
 *   <li>Successful processing of valid notification requests.</li>
 *   <li>Validation errors resulting in HTTP 400 responses.</li>
 *   <li>Exception handling via the global exception handler, resulting in HTTP 500 responses when the service layer fails.</li>
 * </ul>
 * <strong>Test Coverage:</strong> Class, method, and line coverage are targeted to be 100%.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code type}: Must be a valid {@link NotificationType} (EMAIL, SMS, PUSH).</li>
 *   <li>{@code recipient}: A non-null, non-empty string representing the recipient's identifier (e.g., email, phone, or device token).</li>
 *   <li>{@code subjectOrMessage}: A non-null, non-empty string representing the email subject or message content.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If required fields are missing or empty, input validation will cause the controller to return HTTP 400.</li>
 *   <li>If the service layer throws a {@link NotificationException}, the global exception handler should convert it to an HTTP 500 response.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Constraints:</strong> The tests assume that the NotificationService is mocked to isolate the controller behavior.
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerIntegrationTest {

    /**
     * MockMvc instance for performing HTTP requests in tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked NotificationService to simulate service layer behavior.
     */
    @MockBean
    private NotificationService notificationService;

    /**
     * ObjectMapper for converting objects to JSON strings.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Integration test for a successful notification send.
     * <p>
     * <strong>Description:</strong> Sends a valid SMS notification request and verifies that:
     * <ul>
     *   <li>The response status is HTTP 200 (OK).</li>
     *   <li>The response message contains the expected success text.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Premise:</strong> The request DTO contains valid SMS parameters.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>Status code is 200.</li>
     *   <li>Response body contains "sms notification was successfully sent to 1234567890".</li>
     *   <li>NotificationService.sendNotification is invoked with correct parameters.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Pass if all assertions are met; otherwise, fail.
     * </p>
     */
    @Test
    public void testSendNotificationSuccessIntegration() throws Exception {
        // Create a valid notification request DTO for an SMS.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.SMS);
        requestDTO.setRecipient("1234567890");
        requestDTO.setSubjectOrMessage("Test SMS Message");

        // Perform a POST request to the /notifications/send endpoint.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               // Expect an HTTP 200 OK status.
               .andExpect(status().isOk())
               // Validate that the response message contains the expected success text.
               .andExpect(content().string(containsString("sms notification was successfully sent to 1234567890")));

        // Verify that NotificationService.sendNotification was called with the correct parameters.
        verify(notificationService).sendNotification(NotificationType.SMS, "1234567890", "Test SMS Message");
    }

    /**
     * Integration test for input validation errors.
     * <p>
     * <strong>Description:</strong> Sends a notification request with an empty recipient field,
     * expecting the controller to return an HTTP 400 Bad Request response due to validation failure.
     * </p>
     * <p>
     * <strong>Premise:</strong> The request DTO contains an empty recipient, which is invalid.
     * </p>
     * <p>
     * <strong>Assertions:</strong> The response status must be 400.
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Pass if a 400 status is returned; otherwise, fail.
     * </p>
     */
    @Test
    public void testSendNotificationValidationErrorIntegration() throws Exception {
        // Create a DTO with an empty recipient to trigger validation failure.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.EMAIL);
        requestDTO.setRecipient(""); // Invalid input.
        requestDTO.setSubjectOrMessage("Test Email Subject");

        // Perform the POST request and expect an HTTP 400 Bad Request status.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               .andExpect(status().isBadRequest());
    }

    /**
     * Integration test for exception handling in the controller.
     * <p>
     * <strong>Description:</strong> Configures the mocked NotificationService to throw a NotificationException
     * when processing a valid PUSH notification request. Verifies that the global exception handler intercepts the exception,
     * resulting in an HTTP 500 Internal Server Error with the correct exception details.
     * </p>
     * <p>
     * <strong>Premise:</strong> The request DTO contains valid PUSH notification parameters, but the service layer is simulated to fail.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The response status is 500.</li>
     *   <li>The resolved exception is a NotificationException with message "Simulated exception".</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Pass if the HTTP 500 status is returned and the exception details match; otherwise, fail.
     * </p>
     */
    @Test
    public void testSendNotificationExceptionIntegration() throws Exception {
        // Create a valid notification request DTO for a PUSH notification.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.PUSH);
        requestDTO.setRecipient("deviceToken123");
        requestDTO.setSubjectOrMessage("Test Push Message");

        // Configure the NotificationService mock to throw a NotificationException when sendNotification is called.
        doThrow(new NotificationException("Simulated exception"))
                .when(notificationService).sendNotification(any(), any(), any());

        // Perform the POST request and expect an HTTP 500 Internal Server Error.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               .andExpect(status().isInternalServerError())
               // Validate that the exception is resolved and has the expected properties.
               .andExpect(result -> {
                   Exception resolvedException = result.getResolvedException();
                   assertNotNull(resolvedException, "Resolved exception should not be null");
                   assertTrue(resolvedException instanceof NotificationException, "Expected NotificationException");
                   assertEquals("Simulated exception", resolvedException.getMessage());
               });

        // Verify that NotificationService.sendNotification was invoked with the expected parameters.
        verify(notificationService).sendNotification(NotificationType.PUSH, "deviceToken123", "Test Push Message");
    }
}
