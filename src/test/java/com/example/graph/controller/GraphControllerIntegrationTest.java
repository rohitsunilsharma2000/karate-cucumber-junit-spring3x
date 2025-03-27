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
 * This class verifies the behavior of the REST endpoints defined in the GraphController within a full Spring Boot application context.
 * The tests cover:
 * <ul>
 *   <li>HTTP status codes for valid and invalid input payloads</li>
 *   <li>Proper JSON request/response handling</li>
 *   <li>Validation constraint violations on the {@link com.example.graph.dto.GraphRequest} DTO</li>
 *   <li>Behavior of {@link com.example.graph.exception.GlobalExceptionHandler} for edge cases</li>
 * </ul>
 * </p>
 *
 * <p>
 * By running these tests, we ensure that the controller logic, validation, and exception handling work as expected when the application is fully bootstrapped.
 * </p>
 *
 * @author
 * @since 2025-03-26
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Simulates HTTP requests for integration testing

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to JSON and vice versa

    /**
     * Integration test for the /graph/bridges endpoint with valid input.
     *
     * <p>
     * <strong>Scenario:</strong> A valid request is sent with 5 vertices and a proper list of edges.
     * The test expects an HTTP 200 OK response, indicating that the request was processed successfully.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/bridges - Valid Input - Returns 200 OK")
    public void testFindBridges_ValidInput_ReturnsOk() throws Exception {
        // GIVEN: Build a valid request payload containing 5 vertices and a list of edges.
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

        // WHEN & THEN: Perform a POST request to /graph/bridges, then expect HTTP 200 OK.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Integration test for the /graph/articulation endpoint with valid input.
     *
     * <p>
     * <strong>Scenario:</strong> A valid request is sent with 5 vertices and a list of edges.
     * The test expects an HTTP 200 OK response, indicating that the endpoint processed the request successfully.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/articulation - Valid Input - Returns 200 OK")
    public void testFindArticulationPoints_ValidInput_ReturnsOk() throws Exception {
        // GIVEN: Build a valid request payload containing 5 vertices and a list of edges.
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

        // WHEN & THEN: Perform a POST request to /graph/articulation and expect HTTP 200 OK.
        mockMvc.perform(post("/graph/articulation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Integration test for the /graph/bridges endpoint when the 'vertices' field is missing.
     *
     * <p>
     * <strong>Scenario:</strong> A request is sent without the required 'vertices' field.
     * The test expects an HTTP 400 Bad Request response due to validation failure.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/bridges - Missing vertices - Returns 400 Bad Request")
    public void testFindBridges_MissingVertices_ReturnsBadRequest() throws Exception {
        // GIVEN: Build a request payload with the 'edges' field only, leaving out the required 'vertices'.
        Map<String, Object> request = Map.of(
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2)
                )
        );

        // WHEN & THEN: Perform a POST request to /graph/bridges and expect HTTP 400 Bad Request with a validation error message.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    /**
     * Integration test for the /graph/articulation endpoint when the 'edges' list is empty.
     *
     * <p>
     * <strong>Scenario:</strong> A request is sent with a valid 'vertices' value but an empty 'edges' list.
     * The test expects an HTTP 400 Bad Request response due to a validation error.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/articulation - Empty edges - Returns 400 Bad Request")
    public void testFindArticulationPoints_EmptyEdges_ReturnsBadRequest() throws Exception {
        // GIVEN: Build a request payload with a valid 'vertices' value but an empty list for 'edges'.
        Map<String, Object> request = Map.of(
                "vertices", 4,
                "edges", List.of()
        );

        // WHEN & THEN: Perform a POST request to /graph/articulation and expect HTTP 400 Bad Request with a validation error message.
        mockMvc.perform(post("/graph/articulation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    /**
     * Integration test for the /graph/bridges endpoint with a malformed JSON payload.
     *
     * <p>
     * <strong>Scenario:</strong> A request is sent with a malformed JSON body.
     * The test expects an HTTP 400 Bad Request response due to a JSON parsing error.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/bridges - Invalid JSON - Returns 400 Bad Request")
    public void testFindBridges_InvalidJson_ReturnsBadRequest() throws Exception {
        // GIVEN: Create a malformed JSON string (e.g., missing closing bracket).
        String malformedJson = "{\"vertices\": 4, \"edges\": [[0,1], [1,2], [2]]";

        // WHEN & THEN: Perform a POST request to /graph/bridges with the malformed JSON and expect HTTP 400 Bad Request.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
               .andExpect(status().isBadRequest());
    }
}
