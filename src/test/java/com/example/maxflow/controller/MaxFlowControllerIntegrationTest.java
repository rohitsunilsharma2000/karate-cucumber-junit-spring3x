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
 *
 * <p>
 * This class tests the actual HTTP behavior of the /api/maxflow/calculate endpoint with various input conditions.
 * </p>
 *
 * <p><strong>Coverage:</strong></p>
 * <ul>
 *     <li>Valid flow calculation (classic 6x6 network).</li>
 *     <li>Null graph input (HTTP 400).</li>
 *     <li>Empty graph input (HTTP 400).</li>
 *     <li>Negative capacity validation.</li>
 *     <li>Invalid source/sink index handling.</li>
 *     <li>Source equals sink case.</li>
 * </ul>
 *
 * @since 2025-03-26
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MaxFlowControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url() {
        return "http://localhost:" + port + "/api/maxflow/calculate";
    }

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

    @Test
    @DisplayName("Empty graph returns 400 Bad Request")
    void testEmptyGraph() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(new int[0][0]);
        request.setSource(0);
        request.setSink(1);

        ResponseEntity<String> response = restTemplate.postForEntity(url(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Graph must have at least one vertex");
    }

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

    @Test
    @DisplayName("Invalid source index returns 400 Bad Request")
    void testInvalidSource() {
        MaxFlowRequest request = new MaxFlowRequest();
        request.setGraph(new int[][]{
                {0, 1},
                {0, 0}
        });
        request.setSource(-1); // Invalid
        request.setSink(1);

        ResponseEntity<String> response = restTemplate.postForEntity(url(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Source vertex index must be non-negative.");
    }

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
