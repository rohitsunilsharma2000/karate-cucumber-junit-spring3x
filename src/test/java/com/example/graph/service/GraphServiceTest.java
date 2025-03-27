package com.example.graph.service;

import com.example.graph.exception.GraphAnalysisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GraphService}.
 *
 * <p>This class verifies the correctness and robustness of graph-related computations:
 * <ul>
 *   <li>Correct identification of bridges and articulation points in an undirected graph</li>
 *   <li>Proper exception handling in edge cases and faulty inputs</li>
 *   <li>Validation for empty graph structures</li>
 * </ul>
 * </p>
 *
 * @since 2025-03-27
 */
class GraphServiceTest {

    private GraphService graphService;

    @BeforeEach
    void setUp() {
        graphService = new GraphService();
    }

    /**
     * Tests successful identification of bridges in a well-formed graph.
     */
    @Test
    void testFindBridges_Success() {
        int vertices = 5;
        List<List<Integer>> edges = Arrays.asList(
                Arrays.asList(1, 0),
                Arrays.asList(0, 2),
                Arrays.asList(2, 1),
                Arrays.asList(0, 3),
                Arrays.asList(3, 4)
        );

        List<String> bridges = graphService.findBridges(vertices, edges);
        assertEquals(2, bridges.size());
        assertTrue(bridges.contains("3-4") || bridges.contains("4-3"));
        assertTrue(bridges.contains("0-3") || bridges.contains("3-0"));
    }

    /**
     * Tests that an empty graph results in an empty list of bridges.
     */
    @Test
    void testFindBridges_EmptyGraph() {
        List<String> bridges = graphService.findBridges(0, Collections.emptyList());
        assertNotNull(bridges);
        assertTrue(bridges.isEmpty());
    }

    /**
     * Tests that the service throws a {@link GraphAnalysisException} for malformed input (e.g., null edge).
     */
    @Test
    void testFindBridges_Exception() {
        Exception exception = assertThrows(GraphAnalysisException.class, () -> {
            graphService.findBridges(3, Arrays.asList(
                    Arrays.asList(0, 1),
                    null
            ));
        });
        assertTrue(exception.getMessage().contains("Something went wrong while detecting bridges"));
    }

    /**
     * Tests successful identification of articulation points in a well-formed graph.
     */
    @Test
    void testFindArticulationPoints_Success() {
        int vertices = 5;
        List<List<Integer>> edges = Arrays.asList(
                Arrays.asList(1, 0),
                Arrays.asList(0, 2),
                Arrays.asList(2, 1),
                Arrays.asList(0, 3),
                Arrays.asList(3, 4)
        );

        List<Integer> aps = graphService.findArticulationPoints(vertices, edges);
        assertEquals(2, aps.size());
        assertTrue(aps.contains(0));
        assertTrue(aps.contains(3));
    }

    /**
     * Tests that an empty graph results in an empty list of articulation points.
     */
    @Test
    void testFindArticulationPoints_EmptyGraph() {
        List<Integer> aps = graphService.findArticulationPoints(0, Collections.emptyList());
        assertNotNull(aps);
        assertTrue(aps.isEmpty());
    }

    /**
     * Tests that the service throws a {@link GraphAnalysisException} for malformed input (e.g., null edge).
     */
    @Test
    void testFindArticulationPoints_Exception() {
        Exception exception = assertThrows(GraphAnalysisException.class, () -> {
            graphService.findArticulationPoints(3, Arrays.asList(
                    Arrays.asList(0, 1),
                    null
            ));
        });
        assertTrue(exception.getMessage().contains("Something went wrong while detecting articulation points"));
    }
}
