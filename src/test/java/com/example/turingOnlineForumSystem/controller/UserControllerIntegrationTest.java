package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test user creation.
     */
    @Test
    void testCreateUser() throws Exception {
        User user = User.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password123")
                .createdAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    /**
     * Test fetching all users.
     */
    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test getting a user by ID.
     */
    @Test
    void testGetUserById() throws Exception {
        // First create a user
        User user = User.builder()
                .username("alice")
                .email("alice@example.com")
                .password("alicepass")
                .createdAt(LocalDateTime.now())
                .build();

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        // Then fetch the user by ID
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"));
    }

    /**
     * Test updating user profile.
     */
    @Test
    void testUpdateUser() throws Exception {
        // Create user first
        User user = User.builder()
                .username("mike")
                .email("mike@example.com")
                .password("mikepass")
                .createdAt(LocalDateTime.now())
                .build();

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        // Update request
        User updatedUser = User.builder()
                .username("mike_updated")
                .email("mike_new@example.com")
                .password("newpass")
                .build();

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("mike_updated"));
    }
}
