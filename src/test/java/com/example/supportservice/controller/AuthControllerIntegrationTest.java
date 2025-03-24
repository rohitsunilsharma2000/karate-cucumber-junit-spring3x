package com.example.supportservice.controller;


import com.example.supportservice.dto.LoginRequest;
import com.example.supportservice.dto.RegisterRequest;
import com.example.supportservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/auth";
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("testuser@example.com");
        request.setPassword("password123");
        request.setUsername("testuser");

        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/register", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("User registered with ID:");
    }

    @Test
    public void testLoginUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("loginuser@example.com");
        request.setPassword("password123");
        request.setUsername("loginuser");
        restTemplate.postForEntity(getBaseUrl() + "/register", request, String.class);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("loginuser@example.com");
        loginRequest.setPassword("password123");

        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/login", loginRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("token");
    }

    @Test
    public void testProfileEndpoint_Unauthorized() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/profile", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
