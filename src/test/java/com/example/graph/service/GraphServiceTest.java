package com.example.graph.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link GraphService} class.
 *
 * <p>This test suite validates the correctness of the graph analysis algorithms, specifically:</p>
 * <ul>
 *   <li>{@code findBridges(int, List<List<Integer>>)}</li>
 *   <li>{@code findArticulationPoints(int, List<List<Integer>>)}</li>
 * </ul>
 *
 * <p>Each test case checks either:</p>
 * <ul>
 *   <li>The detection of correct graph elements (bridges or articulation points).</li>
 *   <li>The return of empty results when no such critical elements exist.</li>
 * </ul>
 *
 * <p><strong>Note:</strong> The service is instantiated manually to test in isolation, without reliance on Spring context or dependencies.</p>
 *
 * @author
 * @since 2025-03-26
 */
@SpringBootTest
public class GraphServiceTest {

    private final GraphService graphService = new GraphService();

    /**
     * Verifies that the bridge detection algorithm returns the correct set of bridge edges.
     * <pre>
     * Graph:
     *   0 - 1 - 3 - 4
     *    \  |
     *      2
     * </pre>
     * Expected bridges: 1-3 and 3-4
     */
    @Test
    public void testFindBridges_WithValidGraph_ReturnsCorrectBridges() {
        int vertices = 5;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0),
                List.of(1, 3),
                List.of(3, 4)
        );

        List<String> bridges = graphService.findBridges(vertices, edges);
        assertEquals(2, bridges.size());
        assertTrue(bridges.contains("1-3") || bridges.contains("3-1"));
        assertTrue(bridges.contains("3-4") || bridges.contains("4-3"));
    }

    /**
     * Verifies that articulation points are correctly identified for a non-trivial graph.
     * Expected articulation points: vertex 1 and vertex 3
     */
    @Test
    public void testFindArticulationPoints_WithValidGraph_ReturnsCorrectPoints() {
        int vertices = 5;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0),
                List.of(1, 3),
                List.of(3, 4)
        );

        List<Integer> points = graphService.findArticulationPoints(vertices, edges);
        assertEquals(2, points.size());
        assertTrue(points.contains(1));
        assertTrue(points.contains(3));
    }

    /**
     * Verifies that the bridge detection algorithm returns an empty list
     * when there are no bridges in a cyclic graph.
     */
    @Test
    public void testFindBridges_WithNoBridgeGraph_ReturnsEmptyList() {
        int vertices = 3;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0)
        );

        List<String> bridges = graphService.findBridges(vertices, edges);
        assertTrue(bridges.isEmpty());
    }

    /**
     * Verifies that the articulation point detection algorithm returns an empty list
     * when all nodes are part of a strongly connected component.
     */
    @Test
    public void testFindArticulationPoints_WithNoArticulationPointGraph_ReturnsEmptyList() {
        int vertices = 3;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0)
        );

        List<Integer> points = graphService.findArticulationPoints(vertices, edges);
        assertTrue(points.isEmpty());
    }
}
