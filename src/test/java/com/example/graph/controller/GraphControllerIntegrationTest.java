package com.example.graph.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link GraphController}.
 *
 * <p>
 * This class verifies the behavior of the {@code /graph/bridges} and {@code /graph/articulation}
 * REST endpoints in a Spring Boot environment using {@link MockMvc}. These tests validate:
 * </p>
 *
 * <ul>
 *   <li>Correct HTTP status codes for valid and invalid input payloads</li>
 *   <li>Proper JSON request/response handling</li>
 *   <li>Validation constraint violations on the {@link com.example.graph.dto.GraphRequest}</li>
 *   <li>Behavior of {@link com.example.graph.exception.GlobalExceptionHandler} for edge cases</li>
 * </ul>
 *
 * <p>
 * This test suite ensures that the controller logic and request validation
 * are working as expected in a full Spring Boot context.
 * </p>
 *
 * @author
 * @since 2025-03-26
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Valid input test for /graph/bridges endpoint.
     * Expects HTTP 200 OK and successful processing.
     */
    @Test
    @DisplayName("POST /graph/bridges - Valid Input - Returns 200 OK")
    public void testFindBridges_ValidInput_ReturnsOk() throws Exception {
        Map<String, Object> request = Map.of(
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
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Valid input test for /graph/articulation endpoint.
     * Expects HTTP 200 OK and successful processing.
     */
    @Test
    @DisplayName("POST /graph/articulation - Valid Input - Returns 200 OK")
    public void testFindArticulationPoints_ValidInput_ReturnsOk() throws Exception {
        Map<String, Object> request = Map.of(
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
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Test missing 'vertices' field.
     * Expects HTTP 400 Bad Request due to validation failure.
     */
    @Test
    @DisplayName("POST /graph/bridges - Missing vertices - Returns 400 Bad Request")
    public void testFindBridges_MissingVertices_ReturnsBadRequest() throws Exception {
        Map<String, Object> request = Map.of(
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2)
                )
        );

        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    /**
     * Test empty 'edges' list.
     * Expects HTTP 400 Bad Request due to validation constraints.
     */
    @Test
    @DisplayName("POST /graph/articulation - Empty edges - Returns 400 Bad Request")
    public void testFindArticulationPoints_EmptyEdges_ReturnsBadRequest() throws Exception {
        Map<String, Object> request = Map.of(
                "vertices", 4,
                "edges", List.of()
        );

        mockMvc.perform(post("/graph/articulation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    /**
     * Test with malformed JSON body.
     * Expects HTTP 400 Bad Request due to JSON parsing error.
     */
    @Test
    @DisplayName("POST /graph/bridges - Invalid JSON - Returns 400 Bad Request")
    public void testFindBridges_InvalidJson_ReturnsBadRequest() throws Exception {
        String malformedJson = "{\"vertices\": 4, \"edges\": [[0,1], [1,2], [2]]";

        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
               .andExpect(status().isBadRequest());
    }
}
