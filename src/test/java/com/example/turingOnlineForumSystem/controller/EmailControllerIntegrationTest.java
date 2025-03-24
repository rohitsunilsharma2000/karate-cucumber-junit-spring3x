package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.EmailRequest;
import com.example.turingOnlineForumSystem.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailController.class)
@Import(EmailControllerIntegrationTest.TestSecurityConfig.class) // ⬅ disables Spring Security
public class EmailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Test
    void testSendEmail() throws Exception {
        String requestJson = """
                {
                    "to": "user@example.com",
                    "subject": "Test Subject",
                    "body": "This is a test email."
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/email/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
               .andExpect(status().isOk())
               .andExpect(content().string("Email sent successfully!"));

        // Verify the service method was invoked
        Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.any(EmailRequest.class));
    }

    // ✅ Disables security for this test
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}

