package com.example.notificationservice.controller;

import com.example.notificationservice.enums.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link NotificationController}.
 * <p>
 * <strong>Overview:</strong>
 * This integration test class verifies that the {@link NotificationController} endpoint
 * for sending notifications works as expected for various notification types (EMAIL, SMS, PUSH).
 * It uses Spring Boot's {@link MockMvc} to simulate HTTP requests and validate responses.
 * </p>
 *
 * <p>
 * <strong>Functionality:</strong>
 * <ul>
 *   <li>Ensures that a notification is sent successfully for each supported notification type.</li>
 *   <li>Verifies that valid requests return an HTTP 200 (OK) status.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Pass/Fail Conditions:</strong>
 * <ul>
 *   <li><strong>Pass:</strong> A successful HTTP 200 (OK) response is returned when valid parameters are provided.</li>
 *   <li><strong>Fail:</strong> Any response other than HTTP 200 (OK) indicates a failure in processing the notification request.</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests sending an email notification.
     * <p>
     * <strong>Description:</strong>
     * This test simulates a request to the /notifications/send endpoint to send an email notification.
     * It provides valid parameters: notification type as EMAIL, a recipient email address, and a subject/message.
     * </p>
     *
     * <p>
     * <strong>Pass/Fail Conditions:</strong>
     * <ul>
     *   <li><strong>Pass:</strong> The endpoint returns HTTP 200 (OK) indicating the email notification was processed successfully.</li>
     *   <li><strong>Fail:</strong> Any status code other than 200 indicates a failure in sending the email notification.</li>
     * </ul>
     * </p>
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test sending an email notification")
    public void testSendNotificationEmail() throws Exception {
        mockMvc.perform(post("/notifications/send")
                                .param("type", NotificationType.EMAIL.toString())
                                .param("recipient", "test@example.com")
                                .param("subjectOrMessage", "Test Email")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(status().isOk());
    }

    /**
     * Tests sending an SMS notification.
     * <p>
     * <strong>Description:</strong>
     * This test simulates a request to the /notifications/send endpoint to send an SMS notification.
     * It provides valid parameters: notification type as SMS, a recipient phone number, and a subject/message.
     * </p>
     *
     * <p>
     * <strong>Pass/Fail Conditions:</strong>
     * <ul>
     *   <li><strong>Pass:</strong> The endpoint returns HTTP 200 (OK) indicating the SMS notification was processed successfully.</li>
     *   <li><strong>Fail:</strong> Any status code other than 200 indicates a failure in sending the SMS notification.</li>
     * </ul>
     * </p>
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test sending an SMS notification")
    public void testSendNotificationSms() throws Exception {
        mockMvc.perform(post("/notifications/send")
                                .param("type", NotificationType.SMS.toString())
                                .param("recipient", "1234567890")
                                .param("subjectOrMessage", "Test SMS")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(status().isOk());
    }

    /**
     * Tests sending a push notification.
     * <p>
     * <strong>Description:</strong>
     * This test simulates a request to the /notifications/send endpoint to send a push notification.
     * It provides valid parameters: notification type as PUSH, a recipient device token, and a subject/message.
     * </p>
     *
     * <p>
     * <strong>Pass/Fail Conditions:</strong>
     * <ul>
     *   <li><strong>Pass:</strong> The endpoint returns HTTP 200 (OK) indicating the push notification was processed successfully.</li>
     *   <li><strong>Fail:</strong> Any status code other than 200 indicates a failure in sending the push notification.</li>
     * </ul>
     * </p>
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test sending a push notification")
    public void testSendNotificationPush() throws Exception {
        mockMvc.perform(post("/notifications/send")
                                .param("type", NotificationType.PUSH.toString())
                                .param("recipient", "deviceToken")
                                .param("subjectOrMessage", "Test Push")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(status().isOk());
    }
}
