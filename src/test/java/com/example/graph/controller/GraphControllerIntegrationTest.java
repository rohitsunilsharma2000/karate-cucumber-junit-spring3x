package com.example.graph.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetBridges_ValidRequest_ReturnsBridges() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "vertices", 5,
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2),
                        List.of(2, 0),
                        List.of(1, 3),
                        List.of(3, 4)
                )
        );

        mockMvc.perform(post("/graph/bridges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetArticulationPoints_ValidRequest_ReturnsPoints() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "vertices", 5,
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2),
                        List.of(2, 0),
                        List.of(1, 3),
                        List.of(3, 4)
                )
        );

        mockMvc.perform(post("/graph/articulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBridges_InvalidRequest_MissingVertices() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2)
                )
        );

        mockMvc.perform(post("/graph/bridges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetArticulationPoints_InvalidRequest_EmptyEdges() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "vertices", 3,
                "edges", List.of()
        );

        mockMvc.perform(post("/graph/articulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }
}
