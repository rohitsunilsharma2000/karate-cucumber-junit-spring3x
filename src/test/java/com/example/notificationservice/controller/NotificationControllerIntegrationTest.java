package com.example.notificationservice.controller;

import com.example.notificationservice.enums.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for NotificationController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSendNotificationEmail() throws Exception {
        mockMvc.perform(post("/notifications/send")
                                .param("type", NotificationType.EMAIL.toString())
                                .param("recipient", "test@example.com")
                                .param("subjectOrMessage", "Test Email"))
               .andExpect(status().isOk());
    }

    @Test
    public void testSendNotificationSms() throws Exception {
        mockMvc.perform(post("/notifications/send")
                                .param("type", NotificationType.SMS.toString())
                                .param("recipient", "1234567890")
                                .param("subjectOrMessage", "Test SMS"))
               .andExpect(status().isOk());
    }

    @Test
    public void testSendNotificationPush() throws Exception {
        mockMvc.perform(post("/notifications/send")
                                .param("type", NotificationType.PUSH.toString())
                                .param("recipient", "deviceToken")
                                .param("subjectOrMessage", "Test Push"))
               .andExpect(status().isOk());
    }
}
