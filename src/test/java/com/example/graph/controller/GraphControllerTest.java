package com.example.graph.controller;

import com.example.graph.dto.GraphRequest;
import com.example.graph.service.GraphService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link com.example.graph.controller.GraphController} class.
 *
 * <p>
 * This class tests the REST endpoints exposed by the GraphController, which are responsible for:
 * <ul>
 *   <li>Detecting bridge edges in an undirected graph using the /graph/bridges endpoint.</li>
 *   <li>Detecting articulation points in an undirected graph using the /graph/articulation endpoint.</li>
 * </ul>
 * The tests use Spring Boot's {@code @WebMvcTest} to initialize the controller in isolation,
 * along with {@code MockMvc} for simulating HTTP requests and Mockito for mocking the service layer.
 * </p>
 *
 * <p>
 * <strong>Test Cases Covered:</strong>
 * <ul>
 *   <li><strong>Valid Request for Bridges:</strong> Ensures that a valid GraphRequest returns the expected list of bridge edges.</li>
 *   <li><strong>Valid Request for Articulation Points:</strong> Ensures that a valid GraphRequest returns the expected list of articulation point indices.</li>
 *   <li><strong>Validation Failure:</strong> Ensures that requests missing required fields (e.g., vertices) result in a 400 Bad Request with a proper error message.</li>
 * </ul>
 * </p>
 *
 * @author
 */
@WebMvcTest(GraphController.class)
public class GraphControllerTest {

    @Autowired
    private MockMvc mockMvc; // Used to perform HTTP requests in tests

    @MockBean
    private GraphService graphService; // Mocking GraphService dependency to isolate controller tests

    @Autowired
    private ObjectMapper objectMapper; // Used to convert Java objects to JSON strings

    /**
     * Tests the /graph/bridges endpoint with a valid GraphRequest.
     *
     * <p>
     * <strong>Scenario:</strong>
     * A valid request with 5 vertices and a list of edges is sent.
     * The GraphService is expected to return a list of bridge edges (e.g., "3-4" and "1-3").
     * </p>
     *
     * @throws Exception if the request processing fails.
     */
    @Test
    @DisplayName("Test getBridges endpoint - valid request")
    public void testGetBridges_ValidRequest() throws Exception {
        // Given: Define vertices, edges, and create a valid GraphRequest object.
        int vertices = 5;
        List<List<Integer>> edges = Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(1, 2),
                Arrays.asList(2, 0),
                Arrays.asList(1, 3),
                Arrays.asList(3, 4)
        );
        GraphRequest request = new GraphRequest();
        request.setVertices(vertices);
        request.setEdges(edges);

        // Expected response from the service layer
        List<String> bridges = Arrays.asList("3-4", "1-3");

        // When: Mock the GraphService behavior for findBridges.
        Mockito.when(graphService.findBridges(vertices, edges)).thenReturn(bridges);

        // Then: Perform POST request to /graph/bridges and validate the response.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$", containsInAnyOrder("3-4", "1-3")));
    }

    /**
     * Tests the /graph/articulation endpoint with a valid GraphRequest.
     *
     * <p>
     * <strong>Scenario:</strong>
     * A valid request with 5 vertices and a list of edges is sent.
     * The GraphService is expected to return a list of articulation point indices (e.g., 1 and 3).
     * </p>
     *
     * @throws Exception if the request processing fails.
     */
    @Test
    @DisplayName("Test getArticulationPoints endpoint - valid request")
    public void testGetArticulationPoints_ValidRequest() throws Exception {
        // Given: Define vertices, edges, and create a valid GraphRequest object.
        int vertices = 5;
        List<List<Integer>> edges = Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(1, 2),
                Arrays.asList(2, 0),
                Arrays.asList(1, 3),
                Arrays.asList(3, 4)
        );
        GraphRequest request = new GraphRequest();
        request.setVertices(vertices);
        request.setEdges(edges);

        // Expected response from the service layer
        List<Integer> articulationPoints = Arrays.asList(1, 3);

        // When: Mock the GraphService behavior for findArticulationPoints.
        Mockito.when(graphService.findArticulationPoints(vertices, edges)).thenReturn(articulationPoints);

        // Then: Perform POST request to /graph/articulation and validate the response.
        mockMvc.perform(post("/graph/articulation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0]").value(1))
               .andExpect(jsonPath("$[1]").value(3));
    }

    /**
     * Tests the /graph/bridges endpoint for a validation failure scenario.
     *
     * <p>
     * <strong>Scenario:</strong>
     * A request with missing vertices (null) is sent to trigger a validation error.
     * The expected response is a 400 Bad Request with a validation error message.
     * </p>
     *
     * @throws Exception if the request processing fails.
     */
    @Test
    @DisplayName("Test getBridges endpoint - validation failure (missing vertices)")
    public void testGetBridges_ValidationFailure_MissingVertices() throws Exception {
        // Given: Create a GraphRequest with missing vertices to simulate a validation error.
        GraphRequest request = new GraphRequest();
        // vertices not set (null), only edges are provided.
        request.setEdges(Collections.singletonList(Arrays.asList(0, 1)));

        // Then: Perform POST request and validate that a 400 status and validation message is returned.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}
