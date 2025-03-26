package com.example.maxflow.controller;

import com.example.maxflow.model.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test class for {@code MaxFlowController}.
 *
 * <p><strong>Overview:</strong></p>
 * This class contains integration tests to verify the behavior of the
 * {@code /api/maxflow/calculate} endpoint. The tests simulate HTTP requests using
 * {@link TestRestTemplate} in a running Spring Boot context.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Constructs various {@link Graph} scenarios to validate maximum flow computation.</li>
 *     <li>Verifies correct HTTP responses, ensuring proper handling of valid and invalid inputs.</li>
 *     <li>Confirms that expected maximum flow values are returned under different conditions.</li>
 * </ul>
 *
 * <p><strong>Acceptable Values / Constraints:</strong></p>
 * <ul>
 *     <li>{@link Graph} must be well-formed with valid vertices and capacity mappings.</li>
 *     <li>Source and sink parameters must be within the valid range of vertex indices.</li>
 * </ul>
 *
 * <p><strong>Error Conditions:</strong></p>
 * <ul>
 *     <li>Invalid inputs (e.g., non-existent sink vertex) should result in proper error responses.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> The endpoint returns the correct maximum flow and HTTP status 200 when provided with valid input.</li>
 *     <li><strong>Fail:</strong> The endpoint returns an incorrect flow or HTTP status (e.g., 404 or 400) for invalid inputs.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MaxFlowControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // ----------------------------
    //  Original Tests
    // ----------------------------

    /**
     * Test 1: Valid graph input should return the correct maximum flow.
     *
     * <p><strong>Scenario:</strong> A graph with two paths from source (0) to sink (3) is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 15.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 15.
     * <strong>Fail Condition:</strong> The computed max flow is not 15 or the response status is not 200.
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=3", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(15, response.getBody());
    }

    /**
     * Test 2: Graph with no path from source to sink should return max flow of 0.
     *
     * <p><strong>Scenario:</strong> A graph where vertex 2 is unreachable from vertex 0.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 0.
     * <strong>Fail Condition:</strong> A non-zero max flow is computed.
     * </p>
     */
    @Test
    @DisplayName("Test 2: No Path - Max Flow 0")
    void testCalculateMaxFlow_NoPath() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 10);
        // No edge from 1 to 2 means no path from 0 to 2.

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=2", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody());
    }

    /**
     * Test 3: Invalid sink index should return an error.
     *
     * <p><strong>Scenario:</strong> A graph with an invalid sink index (3 in a 3-vertex graph).
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status indicates an error (NOT_FOUND).</li>
     * </ul>
     * <strong>Pass Condition:</strong> Error response is returned.
     * <strong>Fail Condition:</strong> Request does not return an error.
     * </p>
     */
    @Test
    @DisplayName("Test 3: Invalid Sink - Error Response")
    void testCalculateMaxFlow_InvalidSink() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=3", request, String.class);

        // Assuming the controller returns 404 for invalid sink
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ----------------------------
    //  Additional Integration Tests
    // ----------------------------

    /**
     * Test 4: Classic graph example.
     *
     * <p><strong>Scenario:</strong> A classic max flow example graph is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 23.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 23.
     * <strong>Fail Condition:</strong> The computed max flow is incorrect.
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=5", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(23, response.getBody());
    }

    /**
     * Test 5: Graph with cycle.
     *
     * <p><strong>Scenario:</strong> A graph with a cycle is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 8 (bottleneck on path 0->1->3).</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 8.
     * <strong>Fail Condition:</strong> The computed max flow is incorrect.
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=3", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(8, response.getBody());
    }

    /**
     * Test 7: Graph with a single edge.
     *
     * <p><strong>Scenario:</strong> A simple graph with one edge from 0 to 1 is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 100.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 100.
     * <strong>Fail Condition:</strong> The computed max flow is incorrect.
     * </p>
     */
    @Test
    @DisplayName("Test 7: Single Edge Graph - Max Flow 100")
    void testCalculateMaxFlow_SingleEdgeGraph() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, 100);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=1", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100, response.getBody());
    }

    /**
     * Test 8: Disconnected graph where some vertices are isolated.
     *
     * <p><strong>Scenario:</strong> A graph with isolated vertices is provided.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 0 because the sink is unreachable.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 0.
     * <strong>Fail Condition:</strong> The computed max flow is not 0.
     * </p>
     */
    @Test
    @DisplayName("Test 8: Disconnected Graph - Max Flow 0")
    void testCalculateMaxFlow_DisconnectedGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        // Vertices 2 and 3 remain disconnected.

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=3", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    /**
     * Test 9: Graph with multiple paths.
     *
     * <p><strong>Scenario:</strong> A graph with multiple distinct paths from source to sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 15.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 15.
     * <strong>Fail Condition:</strong> The computed max flow is incorrect.
     * </p>
     */
    @Test
    @DisplayName("Test 9: Multiple Paths - Max Flow 15")
    void testCalculateMaxFlow_MultiplePaths() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 10);
        graph.addEdge(2, 3, 5);
        graph.addEdge(1, 4, 2);
        graph.addEdge(4, 3, 2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=3", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(15, response.getBody());
    }

    /**
     * Test 10: Graph with a zero capacity edge.
     *
     * <p><strong>Scenario:</strong> A graph where one edge has zero capacity.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 0.
     * <strong>Fail Condition:</strong> The computed max flow is not 0.
     * </p>
     */
    @Test
    @DisplayName("Test 10: Zero Capacity Edge - Max Flow 0")
    void testCalculateMaxFlow_ZeroCapacityEdge() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 0);
        graph.addEdge(1, 2, 10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=2", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    /**
     * Test 11: Graph with a reverse edge only.
     *
     * <p><strong>Scenario:</strong> A graph that only contains a reverse edge, resulting in no forward path.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 0.
     * <strong>Fail Condition:</strong> The computed max flow is not 0.
     * </p>
     */
    @Test
    @DisplayName("Test 11: Reverse Edge Only - Max Flow 0")
    void testCalculateMaxFlow_ReverseEdgeGraph() {
        Graph graph = new Graph(3);
        // Only reverse edge exists: 1 -> 0, so there is no valid forward path.
        graph.addEdge(1, 0, 10);
        graph.addEdge(1, 2, 10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=2", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    /**
     * Test 12: Graph with a self-loop.
     *
     * <p><strong>Scenario:</strong> A graph where a vertex has an edge to itself.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The self-loop does not affect the flow from source to sink; expected max flow is 5.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 5.
     * <strong>Fail Condition:</strong> The computed max flow is incorrect.
     * </p>
     */
    @Test
    @DisplayName("Test 12: Self-Loop Graph - Max Flow 5")
    void testCalculateMaxFlow_SelfLoopGraph() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 0, 10);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 5);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=2", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody());
    }

    /**
     * Test 13: Large graph test to assess performance.
     *
     * <p><strong>Scenario:</strong> A chain graph with 10 vertices and capacity 5 between consecutive vertices.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 5.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 5.
     * <strong>Fail Condition:</strong> The computed max flow is not 5.
     * </p>
     */
    @Test
    @DisplayName("Test 13: Large Chain Graph - Max Flow 5")
    void testCalculateMaxFlow_LargeGraph() {
        int vertices = 10;
        Graph graph = new Graph(vertices);
        // Create a chain from 0 -> 1 -> ... -> 9 with capacity 5
        for (int i = 0; i < vertices - 1; i++) {
            graph.addEdge(i, i + 1, 5);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=9", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody());
    }

    /**
     * Test 14: Graph with all edges of zero capacity.
     *
     * <p><strong>Scenario:</strong> A graph where every edge has zero capacity.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 0.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 0.
     * <strong>Fail Condition:</strong> The computed max flow is non-zero.
     * </p>
     */
    @Test
    @DisplayName("Test 14: All Zero Capacity Edges - Max Flow 0")
    void testCalculateMaxFlow_AllZeroEdges() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 0);
        graph.addEdge(0, 2, 0);
        graph.addEdge(1, 3, 0);
        graph.addEdge(2, 3, 0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=3", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    /**
     * Test 15: Graph with varying capacities.
     *
     * <p><strong>Scenario:</strong> A graph with edges of different capacities.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 10.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 10.
     * <strong>Fail Condition:</strong> The computed max flow is incorrect.
     * </p>
     */
    @Test
    @DisplayName("Test 15: Varying Capacities - Max Flow 10")
    void testCalculateMaxFlow_VaryingCapacities() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 20);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 15);
        graph.addEdge(3, 4, 10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=4", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody());
    }

    /**
     * Test 16: Graph with multiple disjoint paths.
     *
     * <p><strong>Scenario:</strong> A graph that contains two disjoint paths from source to sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 10 (sum of both disjoint paths).</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 10.
     * <strong>Fail Condition:</strong> The computed max flow is not 10.
     * </p>
     */
    @Test
    @DisplayName("Test 16: Multiple Disjoint Paths - Max Flow 10")
    void testCalculateMaxFlow_MultipleDisjointPaths() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 5, 5);
        graph.addEdge(4, 5, 5);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=5", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody());
    }

    /**
     * Test 17: Graph with parallel paths.
     *
     * <p><strong>Scenario:</strong> A graph with two parallel paths from source to sink.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow equals 20.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 20.
     * <strong>Fail Condition:</strong> The computed max flow is not 20.
     * </p>
     */
    @Test
    @DisplayName("Test 17: Parallel Paths - Max Flow 20")
    void testCalculateMaxFlow_ParallelPaths() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 4, 10);
        graph.addEdge(2, 4, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(3, 4, 5);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=4", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(20, response.getBody());
    }

    /**
     * Test 18: Graph with alternate augmenting paths.
     *
     * <p><strong>Scenario:</strong> A graph with multiple augmenting paths with different bottlenecks.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>HTTP status is 200 (OK).</li>
     *     <li>The returned max flow is determined by the bottleneck on the alternate path; expected value is 10.</li>
     * </ul>
     * <strong>Pass Condition:</strong> The computed max flow is 10.
     * <strong>Fail Condition:</strong> The computed max flow is incorrect.
     * </p>
     */
    @Test
    @DisplayName("Test 18: Alternate Augmenting Paths - Max Flow 10")
    void testCalculateMaxFlow_AlternatePath() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 8);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 15);
        graph.addEdge(3, 4, 10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Graph> request = new HttpEntity<>(graph, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/maxflow/calculate?source=0&sink=4", request, Integer.class);

        // Expected value for this test case is assumed to be 10.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody());
    }
}
