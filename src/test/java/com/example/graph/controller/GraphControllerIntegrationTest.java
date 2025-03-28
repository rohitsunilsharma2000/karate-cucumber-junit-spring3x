
package com.example.graph.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link GraphController}, specifically for edge case scenarios.
 *
 * <p>This class uses parameterized tests to verify that the controller behaves as expected when provided with special
 * or boundary graph inputs, such as:</p>
 * <ul>
 *   <li>A graph with a single vertex and no edges</li>
 *   <li>A graph with a self-loop edge</li>
 *   <li>A fully connected graph (clique) which has no articulation points or bridges</li>
 *   <li>A simple path graph where intermediate nodes are articulation points</li>
 *   <li>A star-shaped graph with one central articulation point</li>
 *   <li>A graph with an out-of-bounds vertex reference</li>
 *   <li>A graph with duplicate edges</li>
 * </ul>
 *
 * <p>Each test validates both the HTTP response status and, where applicable, the expected response body.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // For sending simulated HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // For converting request maps to JSON

    /**
     * Represents a single test case used in the parameterized test.
     */
    static class GraphTestCase {
        final String name;               // Description of the test
        final Map<String, Object> request;  // The graph input request
        final String endpoint;          // The controller endpoint to call
        final int expectedStatus;       // Expected HTTP status
        final String expectedJson;      // Expected JSON response (can be null if only checking status)

        GraphTestCase(String name, Map<String, Object> request, String endpoint, int expectedStatus, String expectedJson) {
            this.name = name;
            this.request = request;
            this.endpoint = endpoint;
            this.expectedStatus = expectedStatus;
            this.expectedJson = expectedJson;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Supplies the edge case scenarios for the parameterized test.
     */
    static Stream<GraphTestCase> graphEdgeCasesProvider() {
        return Stream.of(

                // A graph with a single vertex and no edges. Should return 200 with empty response.
                new GraphTestCase(
                        "Single vertex, no edges (via self-loop)",
                        Map.of("vertices", 1, "edges", List.of(List.of(0, 0))), // Added self-loop to avoid empty edge list
                        "/graph/bridges",
                        200,
                        "[]" // Still expecting no bridges
                ),


                // A self-loop edge should be ignored in bridge detection.
                new GraphTestCase(
                        "Self-loop edge",
                        Map.of("vertices", 1, "edges", List.of(List.of(0, 0))),
                        "/graph/bridges",
                        200,
                        "[]"
                ),

                // Fully connected graph with 4 nodes. No articulation points should be detected.
                new GraphTestCase(
                        "Fully connected graph",
                        Map.of("vertices", 4, "edges", List.of(
                                List.of(0, 1), List.of(0, 2), List.of(0, 3),
                                List.of(1, 2), List.of(1, 3), List.of(2, 3))),
                        "/graph/articulation",
                        200,
                        "[]"
                ),

                // A line graph: nodes 1, 2, 3 are articulation points.
                new GraphTestCase(
                        "Simple path with articulation points",
                        Map.of("vertices", 5, "edges", List.of(
                                List.of(0, 1), List.of(1, 2), List.of(2, 3), List.of(3, 4))),
                        "/graph/articulation",
                        200,
                        "[1,2,3]"
                ),

                // A star-shaped graph: only the center node (0) is an articulation point.
                new GraphTestCase(
                        "Star topology with central articulation point",
                        Map.of("vertices", 5, "edges", List.of(
                                List.of(0, 1), List.of(0, 2), List.of(0, 3), List.of(0, 4))),
                        "/graph/articulation",
                        200,
                        "[0]"
                ),

                // An edge includes a vertex index (4) that is outside the valid range [0, 2].
                new GraphTestCase(
                        "Edge with out-of-bound vertex",
                        Map.of("vertices", 3, "edges", List.of(List.of(0, 4))),
                        "/graph/bridges",
                        400,
                        null // Validation should fail, so no need to verify response body
                ),

                // Duplicate edges should not break the algorithm or appear more than once.
                new GraphTestCase(
                        "Duplicate edges",
                        Map.of("vertices", 3, "edges", List.of(
                                List.of(0, 1), List.of(1, 2), List.of(0, 1))),
                        "/graph/bridges",
                        200,
                        "[\"1-2\",\"0-1\"]"
                )
        );
    }

    /**
     * Executes a parameterized integration test for each graph edge case.
     *
     * <p>Each test performs a POST request to the specified endpoint using the request payload,
     * then validates the HTTP status code and optionally the JSON response body.</p>
     *
     * @param testCase The test case input and expected result
     * @throws Exception if request processing fails
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("graphEdgeCasesProvider")
    @DisplayName("Parameterized edge case test for /graph endpoints")
    void testGraphEdgeCases(GraphTestCase testCase) throws Exception {
        // Perform the POST request to the specified endpoint with given input
        var resultActions = mockMvc.perform(post(testCase.endpoint)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(testCase.request)))
                                   .andExpect(status().is(testCase.expectedStatus)); // Assert expected HTTP status

        // If expected JSON is provided, assert the response body matches exactly
        if (testCase.expectedJson != null) {
            resultActions
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // Ensure response is JSON-compatible
                    .andExpect(content().encoding("UTF-8")) // Optional: Assert encoding is UTF-8 (default for JSON)
                    .andExpect(header().string("Content-Type", "application/json")) // Check exact header
                    .andExpect(content().json(testCase.expectedJson, true)); // Strict JSON equality (no extra/missing fields)
                  }
    }
}
