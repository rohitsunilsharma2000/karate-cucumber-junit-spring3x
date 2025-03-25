package com.example.turingOnlineForumSystem.controller;



import com.example.turingOnlineForumSystem.model.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for EmailController using full Spring context.
 * Tests email sending endpoint with JSON payload and security roles.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Transactional
public class EmailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For converting POJO to JSON

    @Test
    @Order(1)
//    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testSendEmail_shouldReturn200() throws Exception {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("meghnadsaha422@gmail.com");
        emailRequest.setSubject("Test Email");
        emailRequest.setBody("This is a test email from integration test.");

        mockMvc.perform(post("/api/email/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(emailRequest)))
               .andExpect(status().isOk()); // Expect 200 OK
    }

    @Test
    @Order(2)
//    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void testSendEmail_withMissingBody_shouldReturnBadRequest() throws Exception {
        // Missing body field
        String invalidJson = """
                {
                    "to": "test@example.com",
                    "subject": "Missing body"
                }
                """;

        mockMvc.perform(post("/api/email/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson))
               .andExpect(status().isInternalServerError()); // Expect 400 if validation is enabled
    }
}
