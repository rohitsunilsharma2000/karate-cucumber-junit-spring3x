//package com.example.karate_cucumber_junit_spring3x;
//
//import com.example.karate_cucumber_junit_spring3x.model.EmailRequest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class EmailControllerIntegrationTest {
//
////    @LocalServerPort
////    private int port;
//
////    @Autowired
////    private RestTemplate restTemplate;
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    private String getBaseUrl() {
//        return "http://localhost:" + 8080 + "/api/email/send";
//    }
//
//    @Test
//    public void testSendEmail() {
//        // Create payload
//        EmailRequest emailRequest = new EmailRequest();
//        emailRequest.setTo("meghnadsaha422@gmail.com");
//        emailRequest.setSubject("Test Email");
//        emailRequest.setBody("This is a test email.");
//
//        // Set headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Create HTTP request
//        HttpEntity<EmailRequest> request = new HttpEntity<>(emailRequest, headers);
//
//        // Send POST request
//        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), request, String.class);
//
//        // Assert response
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Email sent successfully!", response.getBody());
//    }
//}
