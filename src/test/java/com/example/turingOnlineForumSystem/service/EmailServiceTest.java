package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.model.EmailRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender mailSender;

//    @Test
    void sendEmail_shouldSendMail() {
        // Arrange
        EmailRequest request = new EmailRequest();
        request.setTo("test@example.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");

        // Act
        emailService.sendEmail(request);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
