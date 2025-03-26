package com.example.johnson.controller;

import com.example.johnson.dto.GraphRequestDTO;
import com.example.johnson.dto.GraphRequestDTO.EdgeDTO;
import com.example.johnson.dto.GraphResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link GraphController}.
 *
 * <p><strong>Scope:</strong></p>
 * <ul>
 *   <li>Validates REST endpoint for all-pairs shortest path computation</li>
 *   <li>Tests valid, invalid, and edge-case graph inputs</li>
 *   <li>Verifies status codes and response bodies</li>
 * </ul>
 *
 * @since 2025-03-26
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GraphControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String URL = "/api/graph/shortest-paths";

    /**
     * Test 1: Valid graph input returns 200 OK with correct shortest paths.
     */
    @Test
    @DisplayName("Test 1: Valid graph returns shortest paths")
    void testValidGraph() {
        GraphRequestDTO request = new GraphRequestDTO();
        request.setVertices(List.of("A", "B", "C"));
        request.setEdges(List.of(
                edge("A", "B", 3),
                edge("B", "C", 2),
                edge("A", "C", 10)
        ));

        ResponseEntity<GraphResponseDTO> response = restTemplate.postForEntity(URL, request, GraphResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Map<String, Integer>> paths = response.getBody().getShortestPaths();
        assertEquals(5, paths.get("A").get("C"));  // A → B → C (3+2)
    }

    /**
     * Test 2: Graph with negative-weight cycle returns 400 Bad Request.
     */
    @Test
    @DisplayName("Test 2: Negative cycle returns 400 Bad Request")
    void testNegativeCycleGraph() {
        GraphRequestDTO request = new GraphRequestDTO();
        request.setVertices(List.of("X", "Y", "Z"));
        request.setEdges(List.of(
                edge("X", "Y", 1),
                edge("Y", "Z", -2),
                edge("Z", "X", -2)
        ));

        ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // ✅ Should now pass
        assertTrue(response.getBody().contains("Graph contains negative cycles"));
    }


    /**
     * Test 3: Empty vertex list returns 400 Bad Request (validation).
     */
    @Test
    @DisplayName("Test 3: Empty vertex list - Validation failure")
    void testEmptyVertices() {
        GraphRequestDTO request = new GraphRequestDTO();
        request.setVertices(List.of()); // Invalid
        request.setEdges(List.of(edge("A", "B", 5)));

        ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toLowerCase().contains("vertices"));
    }

    /**
     * Test 4: Disconnected graph - Unreachable node shows large constant distance.
     */
    @Test
    @DisplayName("Test 4: Disconnected vertex returns large (infinity) distance")
    void testDisconnectedGraph() {
        GraphRequestDTO request = new GraphRequestDTO();
        request.setVertices(List.of("A", "B", "C"));
        request.setEdges(List.of(edge("A", "B", 3))); // C is disconnected

        ResponseEntity<GraphResponseDTO> response = restTemplate.postForEntity(URL, request, GraphResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        int unreachable = Integer.MAX_VALUE / 2;
        assertEquals(unreachable, response.getBody().getShortestPaths().get("A").get("C"));
    }

    /**
     * Utility method to build an edge DTO.
     */
    private EdgeDTO edge(String from, String to, int weight) {
        EdgeDTO edge = new EdgeDTO();
        edge.setFrom(from);
        edge.setTo(to);
        edge.setWeight(weight);
        return edge;
    }
}
