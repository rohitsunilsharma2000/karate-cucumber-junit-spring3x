package com.example.maxflow.service;

import com.example.maxflow.exception.MaxFlowException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MaxFlowService}.
 * <p>
 * This class contains unit tests that verify the behavior of the {@link MaxFlowService} implementation
 * for calculating the maximum flow using the Ford-Fulkerson algorithm. The tests cover various edge cases
 * and valid scenarios, ensuring robust input validation, proper exception handling, and accurate
 * flow computation.
 * </p>
 *
 * <p><strong>Test Cases Include:</strong></p>
 * <ul>
 *     <li>Null graph input.</li>
 *     <li>Empty graph (zero vertices).</li>
 *     <li>Non-square matrix validation.</li>
 *     <li>Negative capacity detection.</li>
 *     <li>Invalid source or sink indices.</li>
 *     <li>Source equals sink condition.</li>
 *     <li>Disconnected sink from source.</li>
 *     <li>Multiple valid graph scenarios of varying sizes.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@SpringBootTest
public class MaxFlowServiceTest {

    @Autowired
    private MaxFlowService maxFlowService;

    /**
     * Initializes any shared resources or configurations before each test.
     */
    @BeforeEach
    void setUp() {
        // Any common setup can be added here if necessary
    }

    /**
     * Tests that a null graph input results in a {@link MaxFlowException}.
     * <p>
     * <strong>Scenario:</strong> The input graph is null.
     * <strong>Premise:</strong> A null graph should not be processed.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A {@code MaxFlowException} is thrown with the message "Graph cannot be null."</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with the expected message.
     * <strong>Fail Condition:</strong> No exception or an unexpected message is thrown.
     * </p>
     */
    @Test
    @DisplayName("Null graph should throw MaxFlowException")
    public void testNullGraphThrowsException() {
        MaxFlowException exception = assertThrows(MaxFlowException.class, () ->
                maxFlowService.fordFulkerson(null, 0, 1)
        );
        assertEquals("Graph cannot be null.", exception.getMessage());
    }

    /**
     * Tests that an empty graph (0 vertices) results in a {@link MaxFlowException}.
     * <p>
     * <strong>Scenario:</strong> The input graph is an empty 2D array.
     * <strong>Premise:</strong> A graph must have at least one vertex.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A {@code MaxFlowException} is thrown with the message "Graph must have at least one vertex."</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with the expected message.
     * <strong>Fail Condition:</strong> No exception or an incorrect message is thrown.
     * </p>
     */
    @Test
    @DisplayName("Empty graph should throw MaxFlowException")
    public void testEmptyGraphThrowsException() {
        int[][] graph = new int[0][0];
        MaxFlowException exception = assertThrows(MaxFlowException.class, () ->
                maxFlowService.fordFulkerson(graph, 0, 0)
        );
        assertEquals("Graph must have at least one vertex.", exception.getMessage());
    }

    /**
     * Tests that a non-square matrix graph results in a {@link MaxFlowException}.
     * <p>
     * <strong>Scenario:</strong> The input graph is not a square matrix.
     * <strong>Premise:</strong> The graph must be represented by a square matrix.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A {@code MaxFlowException} is thrown with the message "Graph must be represented by a square matrix."</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with the expected message.
     * <strong>Fail Condition:</strong> No exception or an unexpected message is thrown.
     * </p>
     */
    @Test
    @DisplayName("Non-square graph should throw MaxFlowException")
    public void testNonSquareGraphThrowsException() {
        int[][] graph = new int[2][];
        graph[0] = new int[]{0, 1};
        graph[1] = new int[]{2}; // Non-square matrix
        MaxFlowException exception = assertThrows(MaxFlowException.class, () ->
                maxFlowService.fordFulkerson(graph, 0, 1)
        );
        assertEquals("Graph must be represented by a square matrix.", exception.getMessage());
    }

    /**
     * Tests that a graph with negative capacity values results in a {@link MaxFlowException}.
     * <p>
     * <strong>Scenario:</strong> One of the edges in the graph has a negative capacity.
     * <strong>Premise:</strong> All edge capacities must be non-negative.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A {@code MaxFlowException} is thrown with the message "Negative capacity detected at edge (0, 1)."</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with the expected message.
     * <strong>Fail Condition:</strong> No exception or an incorrect message is thrown.
     * </p>
     */
    @Test
    @DisplayName("Negative capacity should throw MaxFlowException")
    public void testNegativeCapacityThrowsException() {
        int[][] graph = {
                {0, -1},
                {0, 0}
        };
        MaxFlowException exception = assertThrows(MaxFlowException.class, () ->
                maxFlowService.fordFulkerson(graph, 0, 1)
        );
        assertEquals("Negative capacity detected at edge (0, 1).", exception.getMessage());
    }

    /**
     * Tests that an invalid source index (negative) results in a {@link MaxFlowException}.
     * <p>
     * <strong>Scenario:</strong> The source index is negative.
     * <strong>Premise:</strong> The source index must be within the valid range.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A {@code MaxFlowException} is thrown with the message "Invalid source index: -1"</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with the expected message.
     * <strong>Fail Condition:</strong> No exception or an unexpected message is thrown.
     * </p>
     */
    @Test
    @DisplayName("Invalid source index should throw MaxFlowException")
    public void testInvalidSourceIndexThrowsException() {
        int[][] graph = {
                {0, 10},
                {0, 0}
        };
        MaxFlowException exception = assertThrows(MaxFlowException.class, () ->
                maxFlowService.fordFulkerson(graph, -1, 1)
        );
        assertEquals("Invalid source index: -1", exception.getMessage());
    }

    /**
     * Tests that an invalid sink index (out of bounds) results in a {@link MaxFlowException}.
     * <p>
     * <strong>Scenario:</strong> The sink index exceeds the matrix dimensions.
     * <strong>Premise:</strong> The sink index must be within the valid range.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>A {@code MaxFlowException} is thrown with the message "Invalid sink index: 2"</li>
     * </ul>
     * <strong>Pass Condition:</strong> Exception is thrown with the expected message.
     * <strong>Fail Condition:</strong> No exception or an incorrect message is thrown.
     * </p>
     */
    @Test
    @DisplayName("Invalid sink index should throw MaxFlowException")
    public void testInvalidSinkIndexThrowsException() {
        int[][] graph = {
                {0, 10},
                {0, 0}
        };
        MaxFlowException exception = assertThrows(MaxFlowException.class, () ->
                maxFlowService.fordFulkerson(graph, 0, 2)
        );
        assertEquals("Invalid sink index: 2", exception.getMessage());
    }

    /**
     * Tests that when the source equals the sink, the maximum flow is zero.
     * <p>
     * <strong>Scenario:</strong> The source and sink indices are identical.
     * <strong>Premise:</strong> When the source equals the sink, no flow is needed.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The returned maximum flow is 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The maximum flow is correctly returned as 0.
     * <strong>Fail Condition:</strong> The returned flow is non-zero.
     * </p>
     */
    @Test
    @DisplayName("Source equals sink returns zero flow")
    public void testSourceEqualsSinkReturnsZeroFlow() {
        int[][] graph = {
                {0, 10},
                {0, 0}
        };
        int result = maxFlowService.fordFulkerson(graph, 0, 0);
        assertEquals(0, result);
    }

    /**
     * Tests a valid graph scenario using a well-known flow network example.
     * <p>
     * <strong>Scenario:</strong> A classic network represented by a 6x6 capacity matrix.
     * <strong>Premise:</strong> The network is valid and contains multiple augmenting paths.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed maximum flow is 23.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The maximum flow is correctly computed as 23.
     * <strong>Fail Condition:</strong> An incorrect flow value is computed.
     * </p>
     */
    @Test
    @DisplayName("Valid 6x6 graph returns correct maximum flow")
    public void testValidGraphReturnsCorrectMaxFlow() {
        int[][] graph = {
                {0, 16, 13, 0, 0, 0},
                {0, 0, 10, 12, 0, 0},
                {0, 4, 0, 0, 14, 0},
                {0, 0, 9, 0, 0, 20},
                {0, 0, 0, 7, 0, 4},
                {0, 0, 0, 0, 0, 0}
        };
        int result = maxFlowService.fordFulkerson(graph, 0, 5);
        assertEquals(23, result);
    }

    /**
     * Tests that a graph with a disconnected sink (unreachable from the source) returns a maximum flow of 0.
     * <p>
     * <strong>Scenario:</strong> The sink vertex is not connected to the source vertex.
     * <strong>Premise:</strong> There are no augmenting paths between the source and sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed maximum flow is 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The maximum flow is correctly computed as 0.
     * <strong>Fail Condition:</strong> A non-zero flow is computed.
     * </p>
     */
    @Test
    @DisplayName("Disconnected sink returns zero flow")
    public void testDisconnectedSinkReturnsZeroFlow() {
        int[][] graph = {
                {0, 10, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        int result = maxFlowService.fordFulkerson(graph, 0, 2);
        assertEquals(0, result);
    }

    /**
     * Tests a 3x3 graph scenario with multiple paths between source and sink.
     * <p>
     * <strong>Scenario:</strong> A small network where two distinct paths exist.
     * <strong>Premise:</strong> One direct path and one path via an intermediate vertex both contribute to the flow.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed maximum flow is 12.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The maximum flow is correctly computed as 12.
     * <strong>Fail Condition:</strong> An incorrect flow value is computed.
     * </p>
     */
    @Test
    @DisplayName("3x3 graph returns correct maximum flow")
    public void testThreeByThreeGraphReturnsCorrectMaxFlow() {
        int[][] graph = {
                {0, 5, 10},
                {0, 0, 2},
                {0, 0, 0}
        };
        int result = maxFlowService.fordFulkerson(graph, 0, 2);
        assertEquals(12, result);
    }

    /**
     * Tests a larger valid graph scenario with multiple augmenting paths.
     * <p>
     * <strong>Scenario:</strong> A 4x4 network with several potential paths for flow.
     * <strong>Premise:</strong> The network is valid and supports multiple paths.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The computed maximum flow is 20.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The maximum flow is correctly computed as 20.
     * <strong>Fail Condition:</strong> An incorrect flow value is computed.
     * </p>
     */
    @Test
    @DisplayName("Larger 4x4 graph returns correct maximum flow")
    public void testLargerGraphReturnsCorrectMaxFlow() {
        int[][] graph = {
                {0, 10, 10, 0},
                {0, 0, 5, 15},
                {0, 0, 0, 10},
                {0, 0, 0, 0}
        };
        int result = maxFlowService.fordFulkerson(graph, 0, 3);
        assertEquals(20, result);
    }
}
