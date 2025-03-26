package com.example.johnson.service;

import com.example.johnson.dto.GraphResponseDTO;
import com.example.johnson.exception.NegativeCycleException;
import com.example.johnson.model.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JohnsonService}.
 *
 * <p><strong>Overview:</strong></p>
 * This test suite ensures the correct operation of Johnson's Algorithm, including:
 * <ul>
 *     <li>Valid path calculations</li>
 *     <li>Detection of negative-weight cycles</li>
 *     <li>Correct computation for disconnected graphs</li>
 *     <li>Zero-weight edges and self-loops</li>
 * </ul>
 *
 * <p><strong>Logging & Traceability:</strong></p>
 * These tests verify behavioral correctness; actual logging is handled inside the service
 * and can be verified using mock appenders or integration logs if needed.
 *
 * <p><strong>Custom Exception Handling:</strong></p>
 * {@link NegativeCycleException} is thrown for invalid graphs with negative cycles.
 *
 * @version 1.0
 * @since 2025-03-26
 */
class JohnsonServiceTest {

    private final JohnsonService service = new JohnsonService();

    /**
     * Test 1: Computes shortest paths correctly for a valid graph.
     * Scenario:
     * A → B (3), B → C (1), A → C (10)
     * Expected: A → C = 4 (via B), B → C = 1
     */
    @Test
    @DisplayName("Test 1: Valid graph - Correct shortest paths")
    void testShortestPaths_ValidGraph() {
        Graph graph = new Graph(List.of("A", "B", "C"));
        graph.addEdge("A", "B", 3);
        graph.addEdge("B", "C", 1);
        graph.addEdge("A", "C", 10);

        GraphResponseDTO response = service.computeAllPairsShortestPaths(graph);

        assertNotNull(response);
        assertEquals(4, response.getShortestPaths().get("A").get("C")); // A → B → C
        assertEquals(3, response.getShortestPaths().get("A").get("B"));
        assertEquals(1, response.getShortestPaths().get("B").get("C"));
        assertEquals(0, response.getShortestPaths().get("A").get("A"));
    }

    /**
     * Test 2: Graph contains a negative-weight cycle.
     * Scenario:
     * A → B (1), B → C (-2), C → A (-2)
     * Expected: NegativeCycleException is thrown
     */
    @Test
    @DisplayName("Test 2: Negative cycle detection - Throws exception")
    void testShortestPaths_NegativeCycle() {
        Graph graph = new Graph(List.of("A", "B", "C"));
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", -2);
        graph.addEdge("C", "A", -2); // Total cycle weight = -3

        NegativeCycleException ex = assertThrows(NegativeCycleException.class, () ->
                service.computeAllPairsShortestPaths(graph));

        assertEquals("Graph contains negative cycles", ex.getMessage());
    }

    /**
     * Test 3: Disconnected graph – unreachable nodes should have infinite distances.
     * Scenario:
     * A → B (5), but C is disconnected
     */
    @Test
    @DisplayName("Test 3: Disconnected graph - Unreachable distances")
    void testShortestPaths_DisconnectedGraph() {
        Graph graph = new Graph(List.of("A", "B", "C"));
        graph.addEdge("A", "B", 5);

        GraphResponseDTO response = service.computeAllPairsShortestPaths(graph);

        int unreachable = Integer.MAX_VALUE / 2;
        assertEquals(unreachable, response.getShortestPaths().get("A").get("C"));
        assertEquals(unreachable, response.getShortestPaths().get("B").get("C"));
        assertEquals(unreachable, response.getShortestPaths().get("C").get("A"));
    }

    /**
     * Test 4: Handles zero-weight edge correctly.
     * Scenario:
     * A → B (0)
     * Expected: Distance A → B = 0
     */
    @Test
    @DisplayName("Test 4: Zero-weight edge - Valid computation")
    void testShortestPaths_ZeroWeightEdge() {
        Graph graph = new Graph(List.of("A", "B"));
        graph.addEdge("A", "B", 0);

        GraphResponseDTO response = service.computeAllPairsShortestPaths(graph);
        assertEquals(0, response.getShortestPaths().get("A").get("B"));
    }

    /**
     * Test 5: Graph with a self-loop edge.
     * Scenario:
     * A → A (2), A → B (4)
     * Expected: A → A = 0 (self-loops are ignored in Johnson’s adjusted weights)
     */
    @Test
    @DisplayName("Test 5: Self-loop edge does not interfere")
    void testShortestPaths_SelfLoop() {
        Graph graph = new Graph(List.of("A", "B"));
        graph.addEdge("A", "A", 2);  // Self-loop
        graph.addEdge("A", "B", 4);

        GraphResponseDTO response = service.computeAllPairsShortestPaths(graph);
        assertEquals(4, response.getShortestPaths().get("A").get("B"));
        assertEquals(0, response.getShortestPaths().get("A").get("A")); // Identity distance
    }
}
