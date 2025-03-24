package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.model.EmailRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceTest {

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("recipient@example.com");
        emailRequest.setSubject("Unit Test");
        emailRequest.setBody("Unit test email body");

        emailService.sendEmail(emailRequest);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage msg = messageCaptor.getValue();
        assertThat(msg.getTo()).contains("recipient@example.com");
        assertThat(msg.getSubject()).isEqualTo("Unit Test");
        assertThat(msg.getText()).isEqualTo("Unit test email body");
    }
}
