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

/**
 * Integration tests for {@link GraphController}.
 *
 * <p>
 * This class verifies the actual behavior of the graph-related REST endpoints by launching
 * the Spring Boot context and executing HTTP requests through {@link MockMvc}.
 * </p>
 *
 * <p>
 * <strong>Target Endpoints:</strong>
 * <ul>
 *     <li>POST /graph/bridges</li>
 *     <li>POST /graph/articulation</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Focus:</strong>
 * Validates end-to-end request processing, including:
 * <ul>
 *     <li>JSON input parsing</li>
 *     <li>Validation constraints</li>
 *     <li>Controller and service layer interaction</li>
 *     <li>HTTP response status codes</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerIntegrationTest {

    /**
     * Injected MockMvc to simulate HTTP requests to controller endpoints.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Injected ObjectMapper for serializing request bodies to JSON format.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Verifies that a valid request to /graph/bridges returns a 200 OK response.
     *
     * <p><strong>Scenario:</strong> Valid graph with 5 vertices and known bridges.</p>
     * <p><strong>Pass:</strong> Returns HTTP 200.</p>
     * <p><strong>Fail:</strong> Any non-200 status indicates a failure in controller validation or service logic.</p>
     */
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

    /**
     * Verifies that a valid request to /graph/articulation returns a 200 OK response.
     *
     * <p><strong>Scenario:</strong> Valid graph input that includes articulation points.</p>
     * <p><strong>Pass:</strong> HTTP 200 response indicating successful processing.</p>
     * <p><strong>Fail:</strong> Any non-200 status.</p>
     */
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

    /**
     * Tests missing "vertices" field in request to /graph/bridges, expecting a 400 Bad Request.
     *
     * <p><strong>Scenario:</strong> Input validation fails due to missing required field.</p>
     * <p><strong>Pass:</strong> HTTP 400 is returned by validation framework.</p>
     * <p><strong>Fail:</strong> If status is not 400, validation isn't working as expected.</p>
     */
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

    /**
     * Tests empty edges list for /graph/articulation, expecting 400 Bad Request.
     *
     * <p><strong>Scenario:</strong> Edge list is empty, violating @NotEmpty constraint.</p>
     * <p><strong>Pass:</strong> HTTP 400 is returned due to validation failure.</p>
     * <p><strong>Fail:</strong> Any other status implies a missing or incorrect validation rule.</p>
     */
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
