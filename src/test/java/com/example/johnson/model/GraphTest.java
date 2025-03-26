package com.example.johnson.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link Graph}.
 *
 * <p><strong>Overview:</strong></p>
 * This test suite ensures correctness of the core {@code Graph} model used by the
 * Johnson’s Algorithm service. It verifies vertex setup, edge additions, and edge weights.
 *
 * <p><strong>Functionality Tested:</strong></p>
 * <ul>
 *   <li>Graph initialization with a list of vertices.</li>
 *   <li>Edge addition via {@link Graph#addEdge(String, String, int)}.</li>
 *   <li>Correct population of the adjacency list.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Criteria:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> All vertices and edges exist with expected weights.</li>
 *   <li><strong>Fail:</strong> Edge structure or weights are missing, incorrect, or improperly initialized.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public class GraphTest {

    /**
     * Test 1: Verify that the graph initializes correctly with the given vertices.
     *
     * <p><strong>Scenario:</strong> A graph is created with 3 vertices.</p>
     * <strong>Expected:</strong> Each vertex exists in the internal map with an empty adjacency list.
     */
    @Test
    @DisplayName("Test 1: Graph initializes with empty edge maps")
    void testGraphInitialization() {
        List<String> vertices = List.of("A", "B", "C");
        Graph graph = new Graph(vertices);

        assertEquals(3, graph.getVertices().size());
        assertTrue(graph.getEdges().containsKey("A"));
        assertTrue(graph.getEdges().containsKey("B"));
        assertTrue(graph.getEdges().containsKey("C"));
        assertTrue(graph.getEdges().get("A").isEmpty(), "Adjacency list for A should be empty");
        assertTrue(graph.getEdges().get("B").isEmpty());
        assertTrue(graph.getEdges().get("C").isEmpty());
    }

    /**
     * Test 2: Verify that adding a directed edge updates the adjacency list.
     *
     * <p><strong>Scenario:</strong> A → B (weight 5) is added.</p>
     * <strong>Expected:</strong> "A" has one outgoing edge to "B" with weight 5.
     */
    @Test
    @DisplayName("Test 2: Add edge A -> B with weight 5")
    void testAddEdge() {
        Graph graph = new Graph(List.of("A", "B"));
        graph.addEdge("A", "B", 5);

        Map<String, Map<String, Integer>> edges = graph.getEdges();
        assertTrue(edges.containsKey("A"));
        assertEquals(1, edges.get("A").size());
        assertEquals(5, edges.get("A").get("B"));
    }

    /**
     * Test 3: Add multiple edges and verify directed behavior.
     *
     * <p><strong>Scenario:</strong> Add edges A → B (2), B → C (4), A → C (7)</p>
     * <strong>Expected:</strong> Each vertex has correct outgoing edges only.
     */
    @Test
    @DisplayName("Test 3: Add multiple directed edges")
    void testMultipleEdges() {
        Graph graph = new Graph(List.of("A", "B", "C"));
        graph.addEdge("A", "B", 2);
        graph.addEdge("B", "C", 4);
        graph.addEdge("A", "C", 7);

        Map<String, Map<String, Integer>> edges = graph.getEdges();

        assertEquals(2, edges.get("A").size());
        assertEquals(9, edges.get("A").values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(7, edges.get("A").get("C"));
        assertEquals(4, edges.get("B").get("C"));
        assertTrue(edges.get("C").isEmpty(), "C should have no outgoing edges");
    }

    /**
     * Test 4: Overwriting an existing edge should replace the weight.
     *
     * <p><strong>Scenario:</strong> A → B is first set to 3, then updated to 9.</p>
     * <strong>Expected:</strong> Final edge weight is 9.
     */
    @Test
    @DisplayName("Test 4: Overwriting edge weight updates correctly")
    void testEdgeOverwrite() {
        Graph graph = new Graph(List.of("A", "B"));
        graph.addEdge("A", "B", 3);
        graph.addEdge("A", "B", 9); // Overwrite

        assertEquals(1, graph.getEdges().get("A").size());
        assertEquals(9, graph.getEdges().get("A").get("B"));
    }

    /**
     * Test 5: Self-loop edge is added and stored correctly.
     *
     * <p><strong>Scenario:</strong> A → A with weight 4.</p>
     * <strong>Expected:</strong> Edge exists and points to itself.
     */
    @Test
    @DisplayName("Test 5: Self-loop edge handled correctly")
    void testSelfLoopEdge() {
        Graph graph = new Graph(List.of("A"));
        graph.addEdge("A", "A", 4);

        assertTrue(graph.getEdges().containsKey("A"));
        assertEquals(1, graph.getEdges().get("A").size());
        assertEquals(4, graph.getEdges().get("A").get("A"));
    }
}
