package com.example.maxflow.service;


import com.example.maxflow.exception.MaxFlowException;
import com.example.maxflow.model.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link MaxFlowService}.
 *
 * <p><strong>Overview:</strong></p>
 * This class contains unit tests to verify the behavior of the
 * {@link MaxFlowService#edmondsKarp(Graph, int, int)} method. The tests cover:
 * <ul>
 *     <li>Valid input scenarios (various graph configurations such as simple, classic, cycle, multiple paths, etc.).</li>
 *     <li>Error conditions including null graph, zero vertices, invalid source/sink indices, and negative capacities.</li>
 *     <li>Edge case when the source and sink are the same.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> The computed maximum flow matches the expected value for valid inputs.</li>
 *     <li><strong>Fail:</strong> An incorrect flow is computed or the expected exception is not thrown for invalid inputs.</li>
 * </ul>
 *
 * @version 1.1
 * @since 2025-03-26
 */
class MaxFlowServiceTest {

    private final MaxFlowService maxFlowService = new MaxFlowService();

    /**
     * Test 1: Null Graph.
     *
     * <p><strong>Scenario:</strong> A null graph is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A MaxFlowException is thrown with message "Graph cannot be null."</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown as expected.
     * <strong>Fail Condition:</strong> No exception is thrown.
     * </p>
     */
    @Test
    @DisplayName("Test 1: Null Graph - Exception Thrown")
    void testNullGraph() {
        MaxFlowException exception = assertThrows(MaxFlowException.class,
                                                  () -> maxFlowService.edmondsKarp(null, 0, 1));
        assertEquals("Graph cannot be null.", exception.getMessage());
    }

    /**
     * Test 2: Graph with Zero Vertices.
     *
     * <p><strong>Scenario:</strong> A graph with 0 vertices is created.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A MaxFlowException is thrown with message "Graph must have at least one vertex."</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown.
     * <strong>Fail Condition:</strong> No exception is thrown.
     * </p>
     */
    @Test
    @DisplayName("Test 2: Graph with Zero Vertices - Exception Thrown")
    void testGraphWithZeroVertices() {
        Graph graph = new Graph(0);
        MaxFlowException exception = assertThrows(MaxFlowException.class,
                                                  () -> maxFlowService.edmondsKarp(graph, 0, 0));
        assertEquals("Graph must have at least one vertex.", exception.getMessage());
    }

    /**
     * Test 3: Invalid Source Index (Negative).
     *
     * <p><strong>Scenario:</strong> A negative source index is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A MaxFlowException is thrown indicating an invalid source vertex.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with appropriate message.
     * <strong>Fail Condition:</strong> No exception is thrown.
     * </p>
     */
    @Test
    @DisplayName("Test 3: Negative Source Index - Exception Thrown")
    void testNegativeSourceIndex() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 10);
        MaxFlowException exception = assertThrows(MaxFlowException.class,
                                                  () -> maxFlowService.edmondsKarp(graph, -1, 2));
        assertTrue(exception.getMessage().contains("Invalid source vertex"));
    }

    /**
     * Test 4: Invalid Sink Index (Out of Range).
     *
     * <p><strong>Scenario:</strong> A sink index equal to the number of vertices is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A MaxFlowException is thrown indicating an invalid sink vertex.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with appropriate message.
     * <strong>Fail Condition:</strong> No exception is thrown.
     * </p>
     */
    @Test
    @DisplayName("Test 4: Out-of-Range Sink Index - Exception Thrown")
    void testOutOfRangeSinkIndex() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 10);
        MaxFlowException exception = assertThrows(MaxFlowException.class,
                                                  () -> maxFlowService.edmondsKarp(graph, 0, 3));
        assertTrue(exception.getMessage().contains("Invalid sink vertex"));
    }

    /**
     * Test 5: Source Equals Sink.
     *
     * <p><strong>Scenario:</strong> The source and sink indices are the same.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow is 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 0.
     * <strong>Fail Condition:</strong> Returns a non-zero value.
     * </p>
     */
    @Test
    @DisplayName("Test 5: Source Equals Sink - Max Flow 0")
    void testSourceEqualsSink() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 10);
        int maxFlow = maxFlowService.edmondsKarp(graph, 1, 1);
        assertEquals(0, maxFlow);
    }

    /**
     * Test 6: Negative Capacity Edge.
     *
     * <p><strong>Scenario:</strong> A graph contains an edge with negative capacity.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A MaxFlowException is thrown indicating a negative capacity.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with message indicating negative capacity.
     * <strong>Fail Condition:</strong> No exception is thrown.
     * </p>
     */
    @Test
    @DisplayName("Test 6: Negative Capacity Edge - Exception Thrown")
    void testNegativeCapacityEdge() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, -5);
        MaxFlowException exception = assertThrows(MaxFlowException.class,
                                                  () -> maxFlowService.edmondsKarp(graph, 0, 2));
        assertTrue(exception.getMessage().contains("Negative capacity"));
    }

    /**
     * Test 7: Valid Graph with Two Paths.
     *
     * <p><strong>Scenario:</strong> A graph with two augmenting paths from source (0) to sink (3).
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 15.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 15.
     * <strong>Fail Condition:</strong> Returns any value other than 15.
     * </p>
     */
    @Test
    @DisplayName("Test 7: Valid Graph with Two Paths - Max Flow 15")
    void testValidGraph_TwoPaths() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 10);
        graph.addEdge(2, 3, 5);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(15, maxFlow, "Max flow should be 15 for the given graph.");
    }

    /**
     * Test 8: Classic Graph Example.
     *
     * <p><strong>Scenario:</strong> A classic max flow example is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 23.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 23.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 8: Classic Graph - Max Flow 23")
    void testClassicGraph() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 16);
        graph.addEdge(0, 2, 13);
        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 3, 12);
        graph.addEdge(2, 1, 4);
        graph.addEdge(2, 4, 14);
        graph.addEdge(3, 2, 9);
        graph.addEdge(3, 5, 20);
        graph.addEdge(4, 3, 7);
        graph.addEdge(4, 5, 4);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 5);
        assertEquals(23, maxFlow, "Classic graph should result in max flow of 23.");
    }

    /**
     * Test 9: Cycle Graph.
     *
     * <p><strong>Scenario:</strong> A graph with a cycle is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 8 (based on the bottleneck on path 0->1->3).</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 8.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 9: Cycle Graph - Max Flow 8")
    void testCycleGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 8);
        graph.addEdge(1, 2, 5);
        graph.addEdge(2, 0, 3);
        graph.addEdge(1, 3, 10);
        graph.addEdge(2, 3, 7);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(8, maxFlow, "Cycle graph should yield max flow of 8 based on the bottleneck.");
    }

    /**
     * Test 10: Single Edge Graph.
     *
     * <p><strong>Scenario:</strong> A simple graph with one edge from 0 to 1.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 100.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 100.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 10: Single Edge Graph - Max Flow 100")
    void testSingleEdgeGraph() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, 100);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 1);
        assertEquals(100, maxFlow, "Single edge graph should yield max flow equal to the edge capacity (100).");
    }

    /**
     * Test 11: Disconnected Graph.
     *
     * <p><strong>Scenario:</strong> A graph with isolated vertices is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 0.
     * <strong>Fail Condition:</strong> Returns a non-zero value.
     * </p>
     */
    @Test
    @DisplayName("Test 11: Disconnected Graph - Max Flow 0")
    void testDisconnectedGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        // Vertices 2 and 3 remain disconnected.
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(0, maxFlow, "Disconnected graph should yield max flow of 0.");
    }

    /**
     * Test 12: Graph with Multiple Paths.
     *
     * <p><strong>Scenario:</strong> A graph with multiple distinct paths from source to sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 15.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 15.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 12: Multiple Paths - Max Flow 15")
    void testMultiplePaths() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 10);
        graph.addEdge(2, 3, 5);
        graph.addEdge(1, 4, 2);
        graph.addEdge(4, 3, 2);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(15, maxFlow, "Graph with multiple paths should yield a max flow of 15.");
    }

    /**
     * Test 13: Graph with Zero Capacity Edge.
     *
     * <p><strong>Scenario:</strong> A graph where one edge has zero capacity.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 0.
     * <strong>Fail Condition:</strong> Returns a non-zero value.
     * </p>
     */
    @Test
    @DisplayName("Test 13: Zero Capacity Edge - Max Flow 0")
    void testZeroCapacityEdge() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 0);
        graph.addEdge(1, 2, 10);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 2);
        assertEquals(0, maxFlow, "Graph with a zero capacity edge should yield max flow of 0.");
    }

    /**
     * Test 14: Graph with All Zero Capacity Edges.
     *
     * <p><strong>Scenario:</strong> A graph where every edge has zero capacity.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 0.
     * <strong>Fail Condition:</strong> Returns a non-zero value.
     * </p>
     */
    @Test
    @DisplayName("Test 14: All Zero Capacity Edges - Max Flow 0")
    void testAllZeroCapacityEdges() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 0);
        graph.addEdge(0, 2, 0);
        graph.addEdge(1, 3, 0);
        graph.addEdge(2, 3, 0);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(0, maxFlow, "Graph with all zero capacity edges should yield max flow of 0.");
    }

    /**
     * Test 15: Graph with Varying Capacities.
     *
     * <p><strong>Scenario:</strong> A graph with edges of different capacities.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 10.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 10.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 15: Varying Capacities - Max Flow 10")
    void testVaryingCapacities() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 20);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 15);
        graph.addEdge(3, 4, 10);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 4);
        assertEquals(10, maxFlow, "Graph with varying capacities should yield max flow of 10.");
    }

    /**
     * Test 16: Graph with Multiple Disjoint Paths.
     *
     * <p><strong>Scenario:</strong> A graph that contains two disjoint paths from source to sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 10 (sum of the flows through both disjoint paths).</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 10.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 16: Multiple Disjoint Paths - Max Flow 10")
    void testMultipleDisjointPaths() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 5, 5);
        graph.addEdge(4, 5, 5);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 5);
        assertEquals(10, maxFlow, "Graph with multiple disjoint paths should yield max flow of 10.");
    }

    /**
     * Test 17: Graph with Parallel Paths.
     *
     * <p><strong>Scenario:</strong> A graph with two parallel paths from source to sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 20.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 20.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 17: Parallel Paths - Max Flow 20")
    void testParallelPaths() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 4, 10);
        graph.addEdge(2, 4, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(3, 4, 5);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 4);
        assertEquals(20, maxFlow, "Graph with parallel paths should yield max flow of 20.");
    }

    /**
     * Test 18: Graph with Alternate Augmenting Paths.
     *
     * <p><strong>Scenario:</strong> A graph with multiple augmenting paths having different bottlenecks.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 10.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 10.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 18: Alternate Augmenting Paths - Max Flow 10")
    void testAlternateAugmentingPaths() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 8);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 15);
        graph.addEdge(3, 4, 10);
        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 4);
        assertEquals(10, maxFlow, "Graph with alternate augmenting paths should yield max flow of 10.");
    }
}
