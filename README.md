**Metadata**

**Programming Language:** Java

**L1 Taxonomy:** Complete Implementations

**L2 Taxonomy:** Social Media Application

**Use Case:**  Implement the Ford-Fulkerson algorithm for maximum flow in a Spring Boot service to efficiently compute the maximum possible flow in a directed graph. This will help in optimizing resource allocation, network bandwidth management, and flow distribution in systems with multiple nodes and capacities. The service will expose REST endpoints for clients to submit graph data and retrieve maximum flow results. It will provide enhanced network analysis for resource allocation and system optimization.

# **Prompt**

## **Title:**
Spring Boot Maximum Flow Calculator — Ford‑Fulkerson Algorithm Implementation

## **High-Level Description:**
A Spring Boot API  that implements the Ford‑Fulkerson algorithm to compute the maximum flow in a network. The application exposes a REST API endpoint where clients can submit a directed graph (provided as an adjacency matrix) along with designated source and sink vertices. The service processes the request using a well-structured, layered architecture that includes a controller, service layer, and global exception handling. The solution emphasizes input validation, comprehensive logging, and robust error management to ensure data integrity and easy troubleshooting.

## **Key Features:**
1. **Project Structure & Setup**
  - Create a Spring Boot project using Spring Initializr.
  - Organize the project into clear packages: controller, service, exception, and configuration.

2. **REST Controller & API Endpoints**
  - Implement a REST controller that exposes the `/api/maxflow/calculate` endpoint.
  - Accepts a JSON payload containing the graph (adjacency matrix), source, and sink.
  - Validates input using Jakarta validation annotations (`@NotNull`, `@Min`).

3. **Service Layer – Ford‑Fulkerson Algorithm**
  - Encapsulate the algorithm logic in a dedicated service class.
  - Compute the maximum flow by processing the network graph using the Ford‑Fulkerson method.
  - Log key events at various levels (INFO, DEBUG, ERROR) to ensure traceability.

4. **Exception Handling & Input Validation**
  - Implement a global exception handler to catch and handle custom exceptions (e.g., `MaxFlowException`) and validation errors.
  - Return meaningful HTTP error responses for invalid inputs or processing errors.

5. **Logging & Traceability**
  - Use SLF4J for logging.
  - Incorporate multiple log levels to provide detailed insights into the application's behavior and facilitate debugging.

6. **Testing & Documentation**
  - Write unit tests for individual components and integration tests for the complete REST endpoint.
  - Document code and endpoints thoroughly with Javadoc to ensure maintainability.

7. **Expected Behaivour:**
  - The Maximum Flow Calculator endpoint must be designed to correctly process valid requests and return a success response with the computed maximum flow. For example, when a valid directed graph (provided as an adjacency matrix) along with appropriate source and sink indices is submitted, the endpoint should compute and return the maximum flow value with an HTTP status of 200 OK.
  - In addition, the endpoint must handle invalid inputs by returning clear and meaningful error messages along with an HTTP status of 400 Bad Request. Specific cases include:
  - When the graph is null, the service should return an error message stating "Graph must not be null."
  - When any negative capacity is detected within the graph, the service should return an error message stating "Negative capacity detected in the graph."

8. **Edge cases**
- Verify that the graph is not empty. If the graph array has no vertices, return an error indicating that the graph must contain at least one vertex.
- Validate that both the source and sink indices are within the bounds of the provided graph. If either index is out of range, return an error stating that the index is invalid.
- Check for any negative capacity values within the graph. Since the algorithm requires non-negative capacities, any negative value should trigger an error response.
- Handle the case where the source equals the sink by returning a flow of 0, as no valid flow can exist between the same node.
- Ensure that the graph is represented as a square matrix. If the number of rows does not equal the number of columns, return an error indicating that the graph's structure is invalid.

---


**Dependency Requirements:**

- **JUnit 5:** For writing and executing unit tests.
- **Maven:** For dependency management and build automation.
- **Spring Boot Starter Web:** For creating REST endpoints and handling HTTP requests.
- **Spring Boot Starter Security:** For providing authentication and authorization capabilities.
- **Spring Boot Starter Validation:** For implementing robust input validation.
- **Lombok:** For reducing boilerplate code in model classes (optional).
- **Spring Boot Starter Test:** For unit and integration testing (includes JUnit 5 and Mockito).
- **H2 Database:** For in-memory database support during testing.
- **Jakarta Validation API:** For supporting Jakarta Bean Validation.
- **Spring Boot DevTools:** For enhancing development productivity with automatic restarts and live reload.


## **Goal:**
To build a robust, scalable Spring Boot service capable of efficiently computing maximum flow using the Ford‑Fulkerson algorithm. The solution will ensure high data integrity through stringent input validation, comprehensive logging for debugging, and clear exception management, ultimately delivering a reliable service that can be easily maintained and scaled.

## **Plan:**
I will set up the project structure using Spring Initializr and create the necessary packages. I will configure the pom.xml with all required dependencies and set the Java version to 17. I will configure the application properties for an H2 in-memory database and basic authentication. I will develop the REST controller to expose the maximum flow endpoint with strict input validation. I will implement the Ford‑Fulkerson algorithm in the service layer and integrate comprehensive logging using SLF4J. I will establish a global exception handler to manage errors clearly. I will write unit and integration tests using JUnit 5 and Mockito, then build and run the application with Maven to verify its functionality.

---


# **Unit Tests (JUnit 5 + Mockito)**

**19) MaxFlowServiceTest:** src/test/java/com/example/maxflow/service/MaxFlowServiceTest.java
```java
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

```
**20) GlobalExceptionHandlerTest:** src/test/java/com/example/maxflow/exception/GlobalExceptionHandlerTest.java
```java
package com.example.maxflow.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * <p>
 * This class verifies that all custom exception handlers return the proper HTTP responses and error messages.
 * It covers the handling of:
 * <ul>
 *   <li>Custom {@link MaxFlowException}.</li>
 *   <li>{@link ConstraintViolationException} for bean validation errors.</li>
 *   <li>Generic exceptions.</li>
 * </ul>
 * </p>
 */
class GlobalExceptionHandlerTest {

    /**
     * Instance of the global exception handler used for testing.
     */
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Test 1: Validates handling of a custom {@link MaxFlowException}.
     * <p>
     * Expects a 400 BAD_REQUEST status and an error message containing the exception message.
     * </p>
     */
    @Test
    @DisplayName("Test 1: Handle MaxFlowException - should return 400 BAD_REQUEST with error message")
    void testHandleMaxFlowException() {
        MaxFlowException ex = new MaxFlowException("Invalid source index");
        ResponseEntity<Map<String, String>> response = handler.handleMaxFlowException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("Invalid source index"));
    }

    /**
     * Test 2: Validates handling of {@link ConstraintViolationException} with one violation.
     * <p>
     * Mocks a constraint violation and ensures that the returned error map correctly contains the violation message.
     * </p>
     */
    @Test
    @DisplayName("Test 2: Handle ConstraintViolationException - should map field violations correctly")
    void testHandleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("must not be null");

        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("graph");
        when(violation.getPropertyPath()).thenReturn(mockPath);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);
        ResponseEntity<Map<String, String>> response = handler.handleConstraintViolation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("must not be null", response.getBody().get("field"));
    }

    /**
     * Test 3: Validates the generic exception handler.
     * <p>
     * Ensures that any unhandled exception returns a 500 INTERNAL_SERVER_ERROR status and a generic error message.
     * </p>
     */
    @Test
    @DisplayName("Test 3: Handle generic Exception - should return 500 INTERNAL_SERVER_ERROR")
    void testHandleOtherExceptions() {
        Exception ex = new RuntimeException("Unexpected failure");
        ResponseEntity<Map<String, String>> response = handler.handleOtherExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody().get("error"));
    }
}

```


## After the first iteration, the overall test coverage was 33%. To improve this, additional test cases—including those in GlobalExceptionHandlerTest, MaxFlowControllerIntegrationTest, and SecurityConfigTest—will be introduced to further increase the test coverage percentage.


**21) MaxFlowControllerIntegrationTest:** src/test/java/com/example/maxflow/controller/MaxFlowControllerIntegrationTest.java
```java
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

```
**22) MaxFlowControllerIntegrationTest:** src/test/java/com/example/maxflow/config/MaxFlowControllerIntegrationTest.java
```java
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

```


**Result:** Total line coverage is 96%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml contains the necessary Spring Boot dependencies (e.g., Spring Web, Spring Boot Starter Test, H2 Driver etc.). Confirm that the Java version is set to 17 (or adjust accordingly) and any needed plugins (e.g., the Maven Surefire plugin for tests etc.) are present.
3. **Database & Application Properties:** Configure h2 db credentials in application.properties (or application.yml), for example:
```properties
spring.application.name=MaxFlowApplication
server.port=8080
# H2 in-memory database settings
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Enable the H2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

```
4. **Build & Test:**

- From the root directory (where pom.xml is located), run:
```bash
mvn clean install
```
This will compile the code, install dependencies, and package the application.
- To run unit tests (e.g., JUnit 5 + Mockito) and check coverage (Jacoco), do:
```bash
mvn clean test
```
Verify that test coverage meets or exceeds the 90% target.
5. **Start the Application:**

- Launch the Spring Boot app via Maven:
```bash
mvn spring-boot:run
```
- By default, it starts on port 8080 (unless configured otherwise).
### 6. **Accessing Endpoints & Features:**

Below are runnable cURL commands along with sample success and error responses for the Maximum Flow Calculator endpoint.

---

- **Valid Request (Success Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/api/maxflow/calculate' \
    --header 'Content-Type: application/json' \
    --data '{
        "graph": [
          [0, 16, 13, 0, 0, 0],
          [0, 0, 10, 12, 0, 0],
          [0, 4, 0, 0, 14, 0],
          [0, 0, 9, 0, 0, 20],
          [0, 0, 0, 7, 0, 4],
          [0, 0, 0, 0, 0, 0]
        ],
        "source": 0,
        "sink": 5
    }'
    ```

  - **Sample Success Response:**

    ```json
    {
      "maxFlow": 23
    }
    ```

    *(The API returns HTTP status 200 OK and the computed maximum flow value.)*

---

- **Null Graph Request (Error Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/api/maxflow/calculate' \
    --header 'Content-Type: application/json' \
    --data '{
        "graph": null,
        "source": 0,
        "sink": 1
    }'
    ```

  - **Sample Error Response:**

    ```json
    {
      "error": "Graph must not be null."
    }
    ```

    *(The API returns HTTP status 400 Bad Request with an error message indicating that the graph cannot be null.)*

---

- **Negative Capacity Request (Error Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/api/maxflow/calculate' \
    --header 'Content-Type: application/json' \
    --data '{
        "graph": [
          [0, -5],
          [0, 0]
        ],
        "source": 0,
        "sink": 1
    }'
    ```

  - **Sample Error Response:**

    ```json
    {
      "error": "Negative capacity detected in the graph."
    }
    ```

    *(The API returns HTTP status 400 Bad Request with an error message indicating that a negative capacity was detected.)*

---

These cURL commands can be run from a terminal to test the API endpoints. The success and error responses provided above represent expected outcomes based on valid and invalid inputs.

# **Time and Space Complexity:**

- **Time Complexity:**  
  The Ford‑Fulkerson algorithm's time complexity depends on the method used to find augmenting paths. In the worst-case scenario using a DFS-based approach:
  - **O(E * f)**, where **E** is the number of edges and **f** is the maximum flow value.
  - This can become less efficient if the maximum flow value is very high, as the number of augmenting path iterations may increase.

- **Space Complexity:**
  - **O(V + E)** for storing the graph as an adjacency matrix (or list) and the corresponding residual network.
  - Additional space is used for storing arrays and data structures required during the DFS traversal.
  - Therefore, the overall space complexity is **O(V + E)**.


---

# **Conclusion**

The **Maximum Flow Calculator**, built with **Spring Boot**, delivers efficient computation of network capacities using the **Ford‑Fulkerson algorithm** through a REST API interface. Leveraging a DFS-based approach to find augmenting paths, the system is designed to handle diverse network flow scenarios, making it ideal for real-time analysis in logistics, telecommunications, and data flow management systems.

The solution is further strengthened with:
- **Robust input validation** using Jakarta Bean Validation to ensure data integrity,
- **Comprehensive logging and traceability** using SLF4J for detailed debugging and monitoring,
- **Centralized error handling** via a global exception handler to manage and report issues effectively,
- **Thorough testing** with unit and integration tests to ensure reliability and performance.

This makes the application suitable for academic, enterprise, and operational use cases that demand both **accuracy** and **efficiency** in maximum flow computation and network capacity analysis..




# **Unit Tests (JUnit 5 + Mockito)**

**19) MaxFlowServiceTest:** `src/test/java/com/example/maxflow/service/MaxFlowServiceTest.java`
```java
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

```
**20) GlobalExceptionHandlerTest:** `src/test/java/com/example/maxflow/exception/GlobalExceptionHandlerTest.java`
```java
package com.example.maxflow.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * <p>
 * This class verifies that all custom exception handlers return the proper HTTP responses and error messages.
 * It covers the handling of:
 * <ul>
 *   <li>Custom {@link MaxFlowException}.</li>
 *   <li>{@link ConstraintViolationException} for bean validation errors.</li>
 *   <li>Generic exceptions.</li>
 * </ul>
 * </p>
 */
class GlobalExceptionHandlerTest {

    /**
     * Instance of the global exception handler used for testing.
     */
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Test 1: Validates handling of a custom {@link MaxFlowException}.
     * <p>
     * Expects a 400 BAD_REQUEST status and an error message containing the exception message.
     * </p>
     */
    @Test
    @DisplayName("Test 1: Handle MaxFlowException - should return 400 BAD_REQUEST with error message")
    void testHandleMaxFlowException() {
        MaxFlowException ex = new MaxFlowException("Invalid source index");
        ResponseEntity<Map<String, String>> response = handler.handleMaxFlowException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("Invalid source index"));
    }

    /**
     * Test 2: Validates handling of {@link ConstraintViolationException} with one violation.
     * <p>
     * Mocks a constraint violation and ensures that the returned error map correctly contains the violation message.
     * </p>
     */
    @Test
    @DisplayName("Test 2: Handle ConstraintViolationException - should map field violations correctly")
    void testHandleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("must not be null");

        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("graph");
        when(violation.getPropertyPath()).thenReturn(mockPath);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);
        ResponseEntity<Map<String, String>> response = handler.handleConstraintViolation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("must not be null", response.getBody().get("field"));
    }

    /**
     * Test 3: Validates the generic exception handler.
     * <p>
     * Ensures that any unhandled exception returns a 500 INTERNAL_SERVER_ERROR status and a generic error message.
     * </p>
     */
    @Test
    @DisplayName("Test 3: Handle generic Exception - should return 500 INTERNAL_SERVER_ERROR")
    void testHandleOtherExceptions() {
        Exception ex = new RuntimeException("Unexpected failure");
        ResponseEntity<Map<String, String>> response = handler.handleOtherExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody().get("error"));
    }
}

```


### After the first iteration, the overall test coverage was 33%. To improve this, additional test cases—including those in GlobalExceptionHandlerTest, MaxFlowControllerIntegrationTest will be introduced to further increase the test coverage percentage. 


**21) MaxFlowControllerIntegrationTest:** `src/test/java/com/example/maxflow/controller/MaxFlowControllerIntegrationTest.java`
```java
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

```
**22) MaxFlowControllerIntegrationTest:** `src/test/java/com/example/maxflow/config/MaxFlowControllerIntegrationTest.java`
```java
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

```
<a href="https://drive.google.com/file/d/1QZ0EjCZ1Gh4ScQ8oYKdma_-nI0GZQN-I/view?usp=drive_link">Iteration Two </a>

**Result:** Total line coverage is 96%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml contains the necessary Spring Boot dependencies (e.g., Spring Web, Spring Boot Starter Test, H2 Driver etc.). Confirm that the Java version is set to 17 (or adjust accordingly) and any needed plugins (e.g., the Maven Surefire plugin for tests etc.) are present.
3. **Database & Application Properties:** Configure h2 db credentials in application.properties (or application.yml), for example:
```properties
spring.application.name=MaxFlowApplication
server.port=8080



# H2 in-memory database settings
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Enable the H2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

```
4. **Build & Test:**

- From the root directory (where pom.xml is located), run:
```bash
mvn clean install
```
This will compile the code, install dependencies, and package the application.
- To run unit tests (e.g., JUnit 5 + Mockito) and check coverage (Jacoco), do:
```bash
mvn clean test
```
Verify that test coverage meets or exceeds the 90% target.
5. **Start the Application:**

- Launch the Spring Boot app via Maven:
```bash
mvn spring-boot:run
```
- By default, it starts on port 8080 (unless configured otherwise).
### 6. **Accessing Endpoints & Features:**

Below are runnable cURL commands along with sample success and error responses for the Maximum Flow Calculator endpoint.

---

- **Valid Request (Success Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/api/maxflow/calculate' \
    --header 'Content-Type: application/json' \
    --data '{
        "graph": [
          [0, 16, 13, 0, 0, 0],
          [0, 0, 10, 12, 0, 0],
          [0, 4, 0, 0, 14, 0],
          [0, 0, 9, 0, 0, 20],
          [0, 0, 0, 7, 0, 4],
          [0, 0, 0, 0, 0, 0]
        ],
        "source": 0,
        "sink": 5
    }'
    ```

  - **Sample Success Response:**

    ```json
    {
      "maxFlow": 23
    }
    ```

    *(The API returns HTTP status 200 OK and the computed maximum flow value.)*

---

- **Null Graph Request (Error Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/api/maxflow/calculate' \
    --header 'Content-Type: application/json' \
    --data '{
        "graph": null,
        "source": 0,
        "sink": 1
    }'
    ```

  - **Sample Error Response:**

    ```json
    {
      "error": "Graph must not be null."
    }
    ```

    *(The API returns HTTP status 400 Bad Request with an error message indicating that the graph cannot be null.)*

---

- **Negative Capacity Request (Error Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/api/maxflow/calculate' \
    --header 'Content-Type: application/json' \
    --data '{
        "graph": [
          [0, -5],
          [0, 0]
        ],
        "source": 0,
        "sink": 1
    }'
    ```

  - **Sample Error Response:**

    ```json
    {
      "error": "Negative capacity detected in the graph."
    }
    ```

    *(The API returns HTTP status 400 Bad Request with an error message indicating that a negative capacity was detected.)*

---

These cURL commands can be run from a terminal to test the API endpoints. The success and error responses provided above represent expected outcomes based on valid and invalid inputs.

# **Time and Space Complexity:**

- **Time Complexity:**  
  The Ford‑Fulkerson algorithm's time complexity depends on the method used to find augmenting paths. In the worst-case scenario using a DFS-based approach:
  - **O(E * f)**, where **E** is the number of edges and **f** is the maximum flow value.
  - This can become less efficient if the maximum flow value is very high, as the number of augmenting path iterations may increase.

- **Space Complexity:**
  - **O(V + E)** for storing the graph as an adjacency matrix (or list) and the corresponding residual network.
  - Additional space is used for storing arrays and data structures required during the DFS traversal.
  - Therefore, the overall space complexity is **O(V + E)**.


---

# **Conclusion**

The **Maximum Flow Calculator**, built with **Spring Boot**, delivers efficient computation of network capacities using the **Ford‑Fulkerson algorithm** through a REST API interface. Leveraging a DFS-based approach to find augmenting paths, the system is designed to handle diverse network flow scenarios, making it ideal for real-time analysis in logistics, telecommunications, and data flow management systems.

The solution is further strengthened with:
- **Robust input validation** using Jakarta Bean Validation to ensure data integrity,
- **Comprehensive logging and traceability** using SLF4J for detailed debugging and monitoring,
- **Centralized error handling** via a global exception handler to manage and report issues effectively,
- **Thorough testing** with unit and integration tests to ensure reliability and performance.

This makes the application suitable for academic, enterprise, and operational use cases that demand both **accuracy** and **efficiency** in maximum flow computation and network capacity analysis..
