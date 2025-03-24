package com.example.supportservice.controller;


import com.example.supportservice.dto.TicketRequest;
import com.example.supportservice.dto.TicketResponse;
import com.example.supportservice.enums.Priority;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String ADMIN_TOKEN;

    @BeforeEach
    void setup() throws Exception {
        // Get JWT token dynamically
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("username", "admin@example.com");
        loginPayload.put("password", "admin123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(response, Map.class);
        ADMIN_TOKEN = "Bearer " + map.get("token");
    }

    @Test
    void testCreateAndGetTicket() throws Exception {
        TicketRequest request = new TicketRequest();
        request.setSubject("Integration Test Ticket");
        request.setDescription("This is a test ticket");
        request.setPriority(Priority.MEDIUM);

        // Create
        MvcResult result = mockMvc.perform(post("/api/tickets")
                        .header("Authorization", ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        TicketResponse created = objectMapper.readValue(result.getResponse().getContentAsString(), TicketResponse.class);

        // Get
        mockMvc.perform(get("/api/tickets/" + created.getId())
                        .header("Authorization", ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Integration Test Ticket"));
    }

    @Test
    void testListTickets() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tickets")
                        .header("Authorization", ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        List<?> tickets = objectMapper.readValue(response, List.class);
        assertThat(tickets).isNotNull();
    }

    // Add similar tests for update, assign, auto-assign, attachment upload
}
