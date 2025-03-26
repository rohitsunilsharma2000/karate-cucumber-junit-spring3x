package com.example.maxflow.controller;

import com.example.maxflow.controller.MaxFlowController.MaxFlowRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link MaxFlowController}.
 * <p>
 * This class tests the actual HTTP behavior of the <code>/api/maxflow/calculate</code> endpoint with various input conditions.
 * It covers multiple scenarios including:
 * <ul>
 *   <li>Valid flow calculation using a classic 6x6 network.</li>
 *   <li>Handling of a null graph input (expecting HTTP 400).</li>
 *   <li>Handling of an empty graph input (expecting HTTP 400).</li>
 *   <li>Validation of negative capacity values (expecting HTTP 400).</li>
 *   <li>Invalid source/sink index validation (expecting HTTP 400).</li>
 *   <li>Source equals sink case, which should return a max flow of 0.</li>
 * </ul>
 * </p>
 *
 * @since 2025-03-26
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MaxFlowControllerIntegrationTest {

    /**
     * The port where the application is running during tests.
     */
    @LocalServerPort
    private int port;

    /**
     * Template for performing REST calls in tests.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Constructs the URL for the max flow calculate endpoint.
     *
     * @return the complete URL as a String.
     */
    private String url() {
        return "http://localhost:" + port + "/api/maxflow/calculate";
    }

    /**
     * Test 1: Verifies that a valid 6x6 graph returns the expected max flow value (23).
     */
    @Test
    @DisplayName("Valid 6x6 graph returns max flow 23")
    void testValidMaxFlow() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(new int[][]{
                {0, 16, 13, 0, 0, 0},
                {0, 0, 10, 12, 0, 0},
                {0, 4, 0, 0, 14, 0},
                {0, 0, 9, 0, 0, 20},
                {0, 0, 0, 7, 0, 4},
                {0, 0, 0, 0, 0, 0}
        });
        request.setSource(0);
        request.setSink(5);

        ResponseEntity<Integer> response = restTemplate.postForEntity(url(), request, Integer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(23);
    }

    /**
     * Test 2: Verifies that providing a null graph returns a 400 Bad Request status.
     */
    @Test
    @DisplayName("Null graph returns 400 Bad Request")
    void testNullGraph() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(null);
        request.setSource(0);
        request.setSink(1);

        ResponseEntity<String> response = restTemplate.postForEntity(url(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Graph must not be null.");
    }

    /**
     * Test 3: Verifies that providing an empty graph returns a 400 Bad Request status.
     */
    @Test
    @DisplayName("Empty graph returns 400 Bad Request")
    void testEmptyGraph() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(new int[0][0]);
        request.setSource(0);
        request.setSink(1);

        ResponseEntity<String> response = restTemplate.postForEntity(url(), request, String.class);

        // Expected error message should indicate that the graph has no vertices.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Graph must have at least one vertex");
    }

    /**
     * Test 4: Verifies that a negative capacity in the graph returns a 400 Bad Request status.
     */
    @Test
    @DisplayName("Negative capacity returns 400 Bad Request")
    void testNegativeCapacity() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(new int[][]{
                {0, -5},
                {0, 0}
        });
        request.setSource(0);
        request.setSink(1);

        ResponseEntity<String> response = restTemplate.postForEntity(url(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Negative capacity detected");
    }

    /**
     * Test 5: Verifies that an invalid (negative) source index returns a 400 Bad Request status.
     */
    @Test
    @DisplayName("Invalid source index returns 400 Bad Request")
    void testInvalidSource() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(new int[][]{
                {0, 1},
                {0, 0}
        });
        request.setSource(-1); // Invalid index
        request.setSink(1);

        ResponseEntity<String> response = restTemplate.postForEntity(url(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Source vertex index must be non-negative.");
    }

    /**
     * Test 6: Verifies that an invalid sink index (out of bounds) returns a 400 Bad Request status.
     */
    @Test
    @DisplayName("Invalid sink index returns 400 Bad Request")
    void testInvalidSink() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(new int[][]{
                {0, 1},
                {0, 0}
        });
        request.setSource(0);
        request.setSink(3); // Out of bounds

        ResponseEntity<String> response = restTemplate.postForEntity(url(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid sink index");
    }

    /**
     * Test 7: Verifies that when the source equals the sink, the max flow returned is 0.
     */
    @Test
    @DisplayName("Source equals sink returns 0 flow")
    void testSourceEqualsSink() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(new int[][]{
                {0, 10},
                {0, 0}
        });
        request.setSource(0);
        request.setSink(0); // Same as source

        ResponseEntity<Integer> response = restTemplate.postForEntity(url(), request, Integer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(0);
    }
}
