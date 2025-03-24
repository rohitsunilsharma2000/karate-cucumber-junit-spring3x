package com.example.supportservice.controller;


import com.example.supportservice.dto.LoginRequest;
import com.example.supportservice.dto.RegisterRequest;
import com.example.supportservice.dto.UserDto;
import com.example.supportservice.enums.Role;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.UserRepository;
import com.example.supportservice.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // DB rollback after each test
public class AuthControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private JwtUtil jwtUtil;




    @Order(1)
    @Test
    void testRegisterUser() throws Exception {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "pass123", Role.ADMIN.toString());

        mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(content().string(org.hamcrest.Matchers.containsString("User registered with ID:")));
    }

    @Order(2)
    @Test
    @Transactional
    void testLoginUser() throws Exception {
        LoginRequest request = new LoginRequest("john@example.com", "pass123");

        // Mock token and user
        String token = "mock-token";
        User mockUser = User.builder()
//                            .id(1L)
                            .email("john@example.com")
                            .username("john")
                            .enabled(true)
                            .role(Role.ADMIN)
                            .build();

        when(jwtUtil.generateToken(any(), any())).thenReturn(token);

        // Simulate successful login by saving the user to the database
        userRepository.save(mockUser);

        mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.token").value("mock-token"))
               .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Order(3)
    @Test
    @WithMockUser(username = "john@example.com", roles = {"ADMIN"})
    void testProfileEndpoint() throws Exception {
        // Ensure user is in DB
        userRepository.save(User.builder()
                                .username("john")
                                .email("john@example.com")
                                .enabled(true)
                                .role(Role.ADMIN)
                                .build());

        mockMvc.perform(get("/api/auth/profile"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Order(4)
    @Test
    @WithMockUser(username = "john@example.com", roles = {"ADMIN"})
    void testUpdateProfile() throws Exception {
        // Pre-load user
        userRepository.save(User.builder()
                                .username("john")
                                .email("john@example.com")
                                .enabled(true)
                                .role(Role.ADMIN)
                                .build());

        UserDto update = new UserDto();
        update.setUsername("newJohn");

        mockMvc.perform(put("/api/auth/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(update)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("newJohn"));
    }
}
