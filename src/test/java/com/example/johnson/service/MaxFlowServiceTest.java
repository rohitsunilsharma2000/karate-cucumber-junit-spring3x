package com.example.johnson.service;


import com.example.johnson.exception.MaxFlowException;
import com.example.johnson.model.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link MaxFlowService}.
 *
 * <p><strong>Overview:</strong></p>
 * This class contains unit tests to verify the behavior of the {@link MaxFlowService#edmondsKarp(Graph, int, int)}
 * method. The tests provide various graph configurations and validate that the correct maximum flow is computed.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Instantiate {@link MaxFlowService} and invoke {@code edmondsKarp} with a well-defined {@link Graph},
 *         a valid source vertex, and a valid sink vertex.</li>
 *     <li>Verify that the computed maximum flow matches the expected value.</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *     <li>The graph must have a valid number of vertices and a correct adjacency mapping.</li>
 *     <li>The source and sink indices must be within the valid range.</li>
 * </ul>
 *
 * <p><strong>Error Conditions:</strong></p>
 * <ul>
 *     <li>If the graph is null or if the source/sink indices are invalid, a {@link MaxFlowException} is thrown.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> The computed maximum flow matches the expected value for a given graph.</li>
 *     <li><strong>Fail:</strong> The computed maximum flow is incorrect or an exception is thrown unexpectedly.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class MaxFlowServiceTest {

    private final MaxFlowService maxFlowService = new MaxFlowService();

    /**
     * Test 1: Valid graph with two paths from source (0) to sink (3).
     *
     * <p><strong>Scenario:</strong> A graph with two augmenting paths is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 15.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 15.
     * <strong>Fail Condition:</strong> Returns any value other than 15.
     * </p>
     */
    @Test
    @DisplayName("Test 1: Valid Graph - Max Flow 15")
    void testCalculateMaxFlow_ValidGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 10);
        graph.addEdge(2, 3, 5);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(15, maxFlow);
    }

    /**
     * Test 2: Graph with no path from source to sink.
     *
     * <p><strong>Scenario:</strong> Vertex 2 is unreachable from vertex 0.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 0.
     * <strong>Fail Condition:</strong> Returns a non-zero value.
     * </p>
     */
    @Test
    @DisplayName("Test 2: No Path - Max Flow 0")
    void testCalculateMaxFlow_NoPath() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 10);
        // No edge from 1 to 2

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 2);
        assertEquals(0, maxFlow);
    }

    /**
     * Test 3: Invalid sink index.
     *
     * <p><strong>Scenario:</strong> An invalid sink index is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A {@link MaxFlowException} is thrown.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception message contains "Invalid sink vertex".
     * <strong>Fail Condition:</strong> No exception is thrown.
     * </p>
     */
    @Test
    @DisplayName("Test 3: Invalid Sink - Exception Thrown")
    void testCalculateMaxFlow_InvalidSink() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 10);
        MaxFlowException exception = assertThrows(MaxFlowException.class,
                                                  () -> maxFlowService.edmondsKarp(graph, 0, 3));
        assertTrue(exception.getMessage().contains("Invalid sink vertex"));
    }

    /**
     * Test 4: Classic max flow example.
     *
     * <p><strong>Scenario:</strong> A classic graph is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 23.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 23.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 4: Classic Graph - Max Flow 23")
    void testCalculateMaxFlow_ClassicGraph() {
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
        assertEquals(23, maxFlow);
    }

    /**
     * Test 5: Graph with a cycle.
     *
     * <p><strong>Scenario:</strong> A graph with a cycle is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 8 (bottleneck on path 0->1->3).</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 8.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 5: Cycle Graph - Max Flow 8")
    void testCalculateMaxFlow_CycleGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 8);
        graph.addEdge(1, 2, 5);
        graph.addEdge(2, 0, 3);
        graph.addEdge(1, 3, 10);
        graph.addEdge(2, 3, 7);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(8, maxFlow);
    }

    /**
     * Test 6: Graph with a single edge.
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
    @DisplayName("Test 6: Single Edge Graph - Max Flow 100")
    void testCalculateMaxFlow_SingleEdgeGraph() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, 100);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 1);
        assertEquals(100, maxFlow);
    }

    /**
     * Test 7: Disconnected graph.
     *
     * <p><strong>Scenario:</strong> A graph with isolated vertices.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 0.
     * <strong>Fail Condition:</strong> Returns a non-zero value.
     * </p>
     */
    @Test
    @DisplayName("Test 7: Disconnected Graph - Max Flow 0")
    void testCalculateMaxFlow_DisconnectedGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        // Vertices 2 and 3 disconnected

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(0, maxFlow);
    }

    /**
     * Test 8: Graph with multiple paths.
     *
     * <p><strong>Scenario:</strong> A graph with two distinct paths from source to sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 15.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 15.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 8: Multiple Paths - Max Flow 15")
    void testCalculateMaxFlow_MultiplePaths() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 10);
        graph.addEdge(2, 3, 5);
        graph.addEdge(1, 4, 2);
        graph.addEdge(4, 3, 2);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(15, maxFlow);
    }

    /**
     * Test 9: Graph with a zero capacity edge.
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
    @DisplayName("Test 9: Zero Capacity Edge - Max Flow 0")
    void testCalculateMaxFlow_ZeroCapacityEdge() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 0);
        graph.addEdge(1, 2, 10);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 2);
        assertEquals(0, maxFlow);
    }

    /**
     * Test 10: Graph with a reverse edge only.
     *
     * <p><strong>Scenario:</strong> A graph where only a reverse edge exists.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 0.
     * <strong>Fail Condition:</strong> Returns a non-zero value.
     * </p>
     */
    @Test
    @DisplayName("Test 10: Reverse Edge Only - Max Flow 0")
    void testCalculateMaxFlow_ReverseEdgeGraph() {
        Graph graph = new Graph(3);
        // Only reverse edge: 1->0 and valid edge 1->2; no forward path from 0.
        graph.addEdge(1, 0, 10);
        graph.addEdge(1, 2, 10);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 2);
        assertEquals(0, maxFlow);
    }

    /**
     * Test 11: Graph with a self-loop.
     *
     * <p><strong>Scenario:</strong> A graph where a vertex has an edge to itself.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The self-loop should not affect the maximum flow; expected flow equals 5.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 5.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 11: Self-Loop Graph - Max Flow 5")
    void testCalculateMaxFlow_SelfLoopGraph() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 0, 10);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 5);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 2);
        assertEquals(5, maxFlow);
    }

    /**
     * Test 12: Large graph chain.
     *
     * <p><strong>Scenario:</strong> A chain graph with 10 vertices, each edge with capacity 5.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 5 (bottleneck capacity).</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 5.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 12: Large Chain Graph - Max Flow 5")
    void testCalculateMaxFlow_LargeGraph() {
        int vertices = 10;
        Graph graph = new Graph(vertices);
        for (int i = 0; i < vertices - 1; i++) {
            graph.addEdge(i, i + 1, 5);
        }

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 9);
        assertEquals(5, maxFlow);
    }

    /**
     * Test 13: Graph with all zero capacity edges.
     *
     * <p><strong>Scenario:</strong> A graph where every edge has a capacity of 0.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 0.
     * <strong>Fail Condition:</strong> Returns a non-zero value.
     * </p>
     */
    @Test
    @DisplayName("Test 13: All Zero Capacity Edges - Max Flow 0")
    void testCalculateMaxFlow_AllZeroEdges() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 0);
        graph.addEdge(0, 2, 0);
        graph.addEdge(1, 3, 0);
        graph.addEdge(2, 3, 0);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 3);
        assertEquals(0, maxFlow);
    }

    /**
     * Test 14: Graph with varying capacities.
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
    @DisplayName("Test 14: Varying Capacities - Max Flow 10")
    void testCalculateMaxFlow_VaryingCapacities() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 20);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 15);
        graph.addEdge(3, 4, 10);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 4);
        assertEquals(10, maxFlow);
    }

    /**
     * Test 15: Graph with multiple disjoint paths.
     *
     * <p><strong>Scenario:</strong> A graph containing two disjoint paths from source to sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 10 (sum of the flows through both paths).</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 10.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 15: Multiple Disjoint Paths - Max Flow 10")
    void testCalculateMaxFlow_MultipleDisjointPaths() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 5, 5);
        graph.addEdge(4, 5, 5);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 5);
        assertEquals(10, maxFlow);
    }

    /**
     * Test 16: Graph with parallel paths.
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
    @DisplayName("Test 16: Parallel Paths - Max Flow 20")
    void testCalculateMaxFlow_ParallelPaths() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 4, 10);
        graph.addEdge(2, 4, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(3, 4, 5);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 4);
        assertEquals(20, maxFlow);
    }

    /**
     * Test 17: Graph with alternate augmenting paths.
     *
     * <p><strong>Scenario:</strong> A graph with multiple augmenting paths with different bottlenecks.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed max flow equals 10.</li>
     * </ul>
     * <strong>Pass Condition:</strong> Returns 10.
     * <strong>Fail Condition:</strong> Returns an incorrect value.
     * </p>
     */
    @Test
    @DisplayName("Test 17: Alternate Augmenting Paths - Max Flow 10")
    void testCalculateMaxFlow_AlternatePath() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 8);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 15);
        graph.addEdge(3, 4, 10);

        int maxFlow = maxFlowService.edmondsKarp(graph, 0, 4);
        assertEquals(10, maxFlow);
    }

    /**
     * Test 18: Null graph input.
     *
     * <p><strong>Scenario:</strong> A null graph is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A {@link MaxFlowException} is thrown with the message "Graph cannot be null."</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown as expected.
     * <strong>Fail Condition:</strong> No exception is thrown.
     * </p>
     */
    @Test
    @DisplayName("Test 18: Null Graph - Exception Thrown")
    void testCalculateMaxFlow_NullGraph() {
        MaxFlowException exception = assertThrows(MaxFlowException.class,
                                                  () -> maxFlowService.edmondsKarp(null, 0, 1));
        assertEquals("Graph cannot be null.", exception.getMessage());
    }
}
