package com.example.videohosting.controller;

import com.example.videohosting.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 🧪 **UserControllerTest**
 * <p>
 * This test class verifies the functionality of the UserController endpoints.
 * It uses:
 * - @SpringBootTest to load the full application context.
 * - @AutoConfigureMockMvc to configure the web environment.
 * - @Transactional to auto-rollback DB changes after each test.
 * - @WithMockUser to simulate a secured user context.
 * - @TestMethodOrder with @Order to control the execution order of tests.
 * <p>
 * File: src/test/java/com/example/videohosting/controller/UserControllerTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for user registration.
     * <p>
     * This test sends a POST request to "/api/users/register" with a JSON payload representing
     * a new user. It verifies that the response contains the expected user attributes and a generated ID.
     */
    @Test
    @Order(1)
    public void testRegisterUser() throws Exception {
        // Create a sample user payload
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole("USER");

        // Convert the user object to JSON format
        String userJson = objectMapper.writeValueAsString(user);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}
