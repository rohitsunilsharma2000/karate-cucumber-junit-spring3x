package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the UserController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void testCreateUser_shouldReturnCreatedUser() throws Exception {
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setCreatedAt(LocalDateTime.now());

        mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("john_doe"))
               .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @Order(2)
    public void testGetAllUsers_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void testGetUserById_shouldReturnUser() throws Exception {
        User savedUser = new User();
        savedUser.setUsername("alice");
        savedUser.setEmail("alice@example.com");
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser = userRepository.save(savedUser);

        mockMvc.perform(get("/api/users/" + savedUser.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("alice"))
               .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    @Order(4)
    public void testUpdateUser_shouldReturnUpdatedUser() throws Exception {
        User savedUser = new User();
        savedUser.setUsername("bob");
        savedUser.setEmail("bob@example.com");
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser = userRepository.save(savedUser);

        User updatedUser = new User();
        updatedUser.setUsername("bob_updated");
        updatedUser.setEmail("bob_updated@example.com");

        mockMvc.perform(put("/api/users/" + savedUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedUser)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("bob_updated"))
               .andExpect(jsonPath("$.email").value("bob_updated@example.com"));
    }
}
