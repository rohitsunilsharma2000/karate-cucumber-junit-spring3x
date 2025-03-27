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
 * <p>
 * This class verifies the correctness and robustness of graph-related computations by testing:
 * <ul>
 *   <li>Identification of bridges in an undirected graph</li>
 *   <li>Identification of articulation points in an undirected graph</li>
 *   <li>Handling of edge cases such as empty graph structures</li>
 *   <li>Proper exception handling for malformed inputs</li>
 * </ul>
 * </p>
 *
 * @since 2025-03-27
 */
class GraphServiceTest {

    // Instance of GraphService to test graph computation methods
    private GraphService graphService;

    /**
     * Initializes the GraphService instance before each test.
     */
    @BeforeEach
    void setUp() {
        // Create a new instance of GraphService before each test case
        graphService = new GraphService();
    }

    /**
     * Tests successful identification of bridges in a well-formed graph.
     *
     * <p>
     * <strong>GIVEN:</strong> A graph with 5 vertices and a list of edges forming cycles and a branch.
     * <br>
     * <strong>WHEN:</strong> The findBridges method is called.
     * <br>
     * <strong>THEN:</strong> Two bridge edges should be identified.
     * </p>
     */
    @Test
    void testFindBridges_Success() {
        // GIVEN: Define a graph with 5 vertices and specific edges.
        int vertices = 5;
        List<List<Integer>> edges = Arrays.asList(
                Arrays.asList(1, 0),
                Arrays.asList(0, 2),
                Arrays.asList(2, 1),
                Arrays.asList(0, 3),
                Arrays.asList(3, 4)
        );

        // WHEN: Call the findBridges method.
        List<String> bridges = graphService.findBridges(vertices, edges);

        // THEN: Validate that exactly 2 bridges are detected.
        assertEquals(2, bridges.size(), "Expected exactly 2 bridges to be detected");
        // Check that one of the bridges connects vertices 3 and 4.
        assertTrue(bridges.contains("3-4") || bridges.contains("4-3"), "Bridge between 3 and 4 should be detected");
        // Check that one of the bridges connects vertices 0 and 3.
        assertTrue(bridges.contains("0-3") || bridges.contains("3-0"), "Bridge between 0 and 3 should be detected");
    }

    /**
     * Tests that an empty graph (no vertices and no edges) results in an empty list of bridges.
     *
     * <p>
     * <strong>GIVEN:</strong> An empty graph with 0 vertices and an empty edges list.
     * <br>
     * <strong>WHEN:</strong> The findBridges method is called.
     * <br>
     * <strong>THEN:</strong> An empty list should be returned.
     * </p>
     */
    @Test
    void testFindBridges_EmptyGraph() {
        // GIVEN: An empty graph with 0 vertices and no edges.
        List<String> bridges = graphService.findBridges(0, Collections.emptyList());

        // THEN: Verify that the result is not null and is empty.
        assertNotNull(bridges, "Bridges list should not be null for an empty graph");
        assertTrue(bridges.isEmpty(), "Bridges list should be empty for an empty graph");
    }

    /**
     * Tests that a {@link GraphAnalysisException} is thrown for malformed input in findBridges.
     *
     * <p>
     * <strong>GIVEN:</strong> A graph input with valid vertices but containing a null edge.
     * <br>
     * <strong>WHEN:</strong> The findBridges method is called.
     * <br>
     * <strong>THEN:</strong> A GraphAnalysisException should be thrown with an appropriate error message.
     * </p>
     */
    @Test
    void testFindBridges_Exception() {
        // GIVEN: Define a graph with 3 vertices and an edge list that contains a null entry.
        Exception exception = assertThrows(GraphAnalysisException.class, () -> {
            graphService.findBridges(3, Arrays.asList(
                    Arrays.asList(0, 1),
                    null // Malformed input: null edge
            ));
        });

        // THEN: Verify that the exception message contains the expected error text.
        assertTrue(exception.getMessage().contains("Something went wrong while detecting bridges"),
                   "Expected error message to mention failure in detecting bridges");
    }

    /**
     * Tests successful identification of articulation points in a well-formed graph.
     *
     * <p>
     * <strong>GIVEN:</strong> A graph with 5 vertices and a list of edges forming cycles and a branch.
     * <br>
     * <strong>WHEN:</strong> The findArticulationPoints method is called.
     * <br>
     * <strong>THEN:</strong> Two articulation points should be identified.
     * </p>
     */
    @Test
    void testFindArticulationPoints_Success() {
        // GIVEN: Define a graph with 5 vertices and specific edges.
        int vertices = 5;
        List<List<Integer>> edges = Arrays.asList(
                Arrays.asList(1, 0),
                Arrays.asList(0, 2),
                Arrays.asList(2, 1),
                Arrays.asList(0, 3),
                Arrays.asList(3, 4)
        );

        // WHEN: Call the findArticulationPoints method.
        List<Integer> aps = graphService.findArticulationPoints(vertices, edges);

        // THEN: Validate that exactly 2 articulation points are detected.
        assertEquals(2, aps.size(), "Expected exactly 2 articulation points to be detected");
        // Check that the detected articulation points include vertex 0.
        assertTrue(aps.contains(0), "Vertex 0 should be identified as an articulation point");
        // Check that the detected articulation points include vertex 3.
        assertTrue(aps.contains(3), "Vertex 3 should be identified as an articulation point");
    }

    /**
     * Tests that an empty graph (no vertices and no edges) results in an empty list of articulation points.
     *
     * <p>
     * <strong>GIVEN:</strong> An empty graph with 0 vertices and an empty edges list.
     * <br>
     * <strong>WHEN:</strong> The findArticulationPoints method is called.
     * <br>
     * <strong>THEN:</strong> An empty list should be returned.
     * </p>
     */
    @Test
    void testFindArticulationPoints_EmptyGraph() {
        // GIVEN: An empty graph with 0 vertices and no edges.
        List<Integer> aps = graphService.findArticulationPoints(0, Collections.emptyList());

        // THEN: Verify that the result is not null and is empty.
        assertNotNull(aps, "Articulation points list should not be null for an empty graph");
        assertTrue(aps.isEmpty(), "Articulation points list should be empty for an empty graph");
    }

    /**
     * Tests that a {@link GraphAnalysisException} is thrown for malformed input in findArticulationPoints.
     *
     * <p>
     * <strong>GIVEN:</strong> A graph input with valid vertices but containing a null edge.
     * <br>
     * <strong>WHEN:</strong> The findArticulationPoints method is called.
     * <br>
     * <strong>THEN:</strong> A GraphAnalysisException should be thrown with an appropriate error message.
     * </p>
     */
    @Test
    void testFindArticulationPoints_Exception() {
        // GIVEN: Define a graph with 3 vertices and an edge list that contains a null entry.
        Exception exception = assertThrows(GraphAnalysisException.class, () -> {
            graphService.findArticulationPoints(3, Arrays.asList(
                    Arrays.asList(0, 1),
                    null // Malformed input: null edge
            ));
        });

        // THEN: Verify that the exception message contains the expected error text.
        assertTrue(exception.getMessage().contains("Something went wrong while detecting articulation points"),
                   "Expected error message to mention failure in detecting articulation points");
    }
}
