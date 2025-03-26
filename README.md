
---

**L2 Taxonomy:** Graph Algorithms Application

**Use Case:**  
A network analysis tool for industries requiring efficient computation of maximum flow in complex networks. This API is designed to be integrated into larger systems (e.g., transportation, telecommunications, and supply chain applications) to quickly calculate the maximum flow through a network. The solution must be robust, scalable, and well-documented with comprehensive REST endpoints, input validations, and strong exception handling.

# **Prompt**

**Title:** Spring Boot Max Flow API — Efficient Network Flow Computation

**High-Level Description:**  
Develop a Spring Boot REST API that computes the maximum flow in a network using the Edmonds‑Karp algorithm. The API accepts a JSON representation of a directed graph with vertices and edge capacities, validates the input data, and returns the computed maximum flow from a given source to a sink vertex. The solution should integrate robust input validation, custom exception handling, comprehensive REST endpoints, unit/integration testing.

**Key Features:**

1. **Project Structure & Setup**
    - Create a new Spring Boot project (e.g., via Spring Initializer).
    - Organize the project into layers (model, service, controller, exception, configuration, and tests).

2. **Input Validation**
    - Validate that the input graph is non-null and contains at least one vertex.
    - Ensure that all vertex indices (for source and sink) are within the valid range.
    - Check that all edge capacities are non-negative.
    - Return appropriate error responses when validations fail.

3. **Entity Model**
    - Implement a `Graph` model that encapsulates the number of vertices and an adjacency matrix for edge capacities.
    - Use Java collections (e.g., HashMap) to represent the graph structure.

4. **Service Layer**
    - Develop a `MaxFlowService` that implements the Edmonds‑Karp algorithm.
    - Include robust input validations and logging.
    - Throw custom exceptions (e.g., `MaxFlowException`) for invalid inputs.
    - Optimize the algorithm for performance and scalability.

5. **Controller Layer**
    - Create a `MaxFlowController` that exposes a REST endpoint (e.g., `/api/maxflow/calculate`).
    - Accept a JSON representation of the graph and query parameters for the source and sink vertices.
    - Invoke the service layer to compute the maximum flow and return the result as a JSON response.

6. **Exception Handling**
    - Develop custom exception classes such as `MaxFlowException`.
    - Implement a `GlobalExceptionHandler` using `@ControllerAdvice` to centralize error handling.
    - Ensure that error responses include timestamp, message, status code, error reason, and request path.

7. **Testing**
    - Create comprehensive unit tests (e.g., `MaxFlowServiceTest`) covering various graph scenarios:
        - Valid inputs, edge cases, and error conditions.
    - Develop integration tests (e.g., `MaxFlowControllerIntegrationTest`) using TestRestTemplate/MockMvc to simulate REST calls and validate API behavior.



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

**Goal:**  
Develop a Spring Boot REST API that computes the maximum flow in networks using the Edmonds‑Karp algorithm. The application must ensure robust input validation, strong exception handling, and comprehensive testing, making it a reliable backend component for network analysis and integration into larger systems requiring efficient flow computations.
---
# **Complete Project Code**

**1) Project Structure:** A logical structure (typical Maven layout)

```
edmonds‑karp-app/
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- maxflow
|   |               |-- MaxFlowApplication.java
|   |               |-- config
|   |               |   `-- SecurityConfig.java
|   |               |-- controller
|   |               |   `-- MaxFlowController.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   `-- MaxFlowException.java
|   |               |-- model
|   |               |   `-- Graph.java
|   |               `-- service
|   |                   `-- MaxFlowService.java
|   `-- resources
`-- test
    `-- java
        `-- com
            `-- example
                `-- maxflow
                    |-- MaxFlowApplicationTest.java
                    |-- config
                    |   `-- SecurityConfigTest.java
                    |-- controller
                    |   `-- MaxFlowControllerIntegrationTest.java
                    |-- exception
                    |   `-- GlobalExceptionHandlerTest.java
                    `-- service
                        `-- MaxFlowServiceTest.java


```

**2) Main Application:** src/main/java/com/example/maxflow/MaxFlowApplication.java
```java
package com.example.maxflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the MaxFlowApplication.
 *
 * <p><strong>Overview:</strong></p>
 * This Spring Boot application serves as a REST API that computes the maximum flow in a network using the
 * Edmonds‑Karp algorithm. The application is designed to be integrated into systems requiring efficient
 * network flow computations and provides robust input validation, security, and comprehensive error handling.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Run this class to start the Spring Boot application.</li>
 *     <li>Endpoints (e.g., /api/maxflow/calculate) are exposed for computing maximum flow based on JSON graph input.</li>
 * </ul>
 *
 * <p><strong>Technical Details:</strong></p>
 * <ul>
 *     <li>Uses Spring Boot for rapid application development and dependency management.</li>
 *     <li>Integrates Spring Security for authentication and authorization during development and testing.</li>
 *     <li>Leverages input validation and custom exception handling to ensure robust API behavior.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@SpringBootApplication
public class MaxFlowApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MaxFlowApplication.class, args);
    }
}

```
**3) MaxFlowService:** src/main/java/com/example/maxflow/service/MaxFlowService.java
```java
package com.example.maxflow.service;

import com.example.maxflow.exception.MaxFlowException;
import com.example.maxflow.model.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Service class providing functionality for calculating the maximum flow in a directed graph
 * using the Edmonds-Karp algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This service implements the Edmonds-Karp algorithm, which is an implementation of the Ford-Fulkerson
 * method. The algorithm repeatedly searches for augmenting paths in a flow network using a breadth-first search (BFS)
 * and updates the flow until no further augmenting paths can be found.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Create an instance of {@code MaxFlowService} and invoke {@link #edmondsKarp(Graph, int, int)}
 *         with a properly defined {@link Graph}, a source vertex, and a sink vertex.</li>
 *     <li>The {@link Graph} must include the number of vertices and an adjacency mapping that represents the capacities.
 *         All capacities must be non-negative.</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *     <li>The graph must not be null and must have at least one vertex.</li>
 *     <li>The source and sink indices must be valid and within the range of the graph's vertices.</li>
 *     <li>All edge capacities in the graph must be non-negative.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> Successfully computes the maximum flow from the source to the sink when
 *         provided with a valid graph and valid vertex indices.</li>
 *     <li><strong>Fail:</strong> Throws a {@link MaxFlowException} if the graph is null, if the vertex count is invalid,
 *         if any capacity is negative, or if the source/sink indices are out of bounds.</li>
 * </ul>
 *
 * @see Graph
 */
@Service
public class MaxFlowService {

    private static final Logger logger = LoggerFactory.getLogger(MaxFlowService.class);

    /**
     * Calculates the maximum flow from a specified source to sink in the provided graph using the Edmonds-Karp algorithm.
     *
     * <p><strong>Description:</strong></p>
     * This method initializes capacity and flow matrices based on the provided {@link Graph}. It then employs
     * a breadth-first search (BFS) to identify augmenting paths in the residual graph. For each found path, the method
     * computes the bottleneck capacity (i.e., the minimum residual capacity along the path), updates the flow accordingly,
     * and accumulates the total maximum flow. The process repeats until no augmenting path exists.
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *     <li><strong>graph</strong> (<em>{@link Graph}</em>): The directed graph representing the flow network,
     *         including vertices and capacity mappings. Must be non-null, with at least one vertex and non-negative edge capacities.</li>
     *     <li><strong>source</strong> (<em>int</em>): The index of the source vertex where the flow originates.</li>
     *     <li><strong>sink</strong> (<em>int</em>): The index of the sink vertex where the flow is collected.</li>
     * </ul>
     *
     * <p><strong>Return Value:</strong></p>
     * Returns an integer representing the maximum flow from the source to the sink.
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *     <li><strong>Pass:</strong> The method correctly computes the maximum flow when provided with valid inputs.</li>
     *     <li><strong>Fail:</strong> Throws a {@link MaxFlowException} if the graph is null, has no vertices, contains negative capacities,
     *         or if the provided source/sink indices are invalid.</li>
     * </ul>
     *
     * @param graph  The directed graph containing the vertices and capacity mappings.
     * @param source The index of the source vertex.
     * @param sink   The index of the sink vertex.
     * @return The maximum flow from the source to the sink.
     * @throws MaxFlowException if the graph is null, has no vertices, contains negative capacities, or if source/sink indices are invalid.
     */
    public int edmondsKarp(Graph graph, int source, int sink) {
        // Validate graph and vertices count
        if (graph == null) {
            throw new MaxFlowException("Graph cannot be null.");
        }
        int vertices = graph.getVertices();
        if (vertices <= 0) {
            throw new MaxFlowException("Graph must have at least one vertex.");
        }
        if (source < 0 || source >= vertices) {
            throw new MaxFlowException("Invalid source vertex: " + source);
        }
        if (sink < 0 || sink >= vertices) {
            throw new MaxFlowException("Invalid sink vertex: " + sink);
        }
        // Early exit if source equals sink
        if (source == sink) {
            logger.info("Source equals sink; returning 0 flow.");
            return 0;
        }

        // Validate that all capacities are non-negative
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : graph.getAdjMatrix().entrySet()) {
            int u = entry.getKey();
            for (Map.Entry<Integer, Integer> edge : entry.getValue().entrySet()) {
                if (edge.getValue() < 0) {
                    throw new MaxFlowException("Negative capacity from vertex " + u + " to vertex " + edge.getKey());
                }
            }
        }

        logger.info("Starting Edmonds-Karp algorithm from source {} to sink {}.", source, sink);

        int maxFlow = 0;
        int[][] capacity = new int[vertices][vertices];
        int[][] flow = new int[vertices][vertices];

        // Initialize the capacity matrix using the graph's adjacency matrix
        for (int u : graph.getAdjMatrix().keySet()) {
            for (int v : graph.getAdjMatrix().get(u).keySet()) {
                capacity[u][v] = graph.getCapacity(u, v);
            }
        }

        // Find augmenting paths repeatedly until no such path exists
        while (true) {
            int[] parent = new int[vertices];
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(source);
            visited[source] = true;

            // BFS to find an augmenting path in the residual graph
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v = 0; v < vertices; v++) {
                    if (!visited[v] && capacity[u][v] > flow[u][v]) {
                        parent[v] = u;
                        visited[v] = true;
                        queue.add(v);
                    }
                }
            }

            // If no augmenting path is found, terminate the loop
            if (!visited[sink]) {
                logger.debug("No augmenting path found; terminating algorithm.");
                break;
            }

            // Determine the bottleneck capacity along the found path
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, capacity[u][v] - flow[u][v]);
            }

            // Update flows along the path and adjust reverse flows for residual capacity
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                flow[u][v] += pathFlow;
                flow[v][u] -= pathFlow;
            }

            maxFlow += pathFlow;
            logger.debug("Found augmenting path with flow {}; updated max flow is {}.", pathFlow, maxFlow);
        }

        logger.info("Edmonds-Karp algorithm completed. Maximum flow computed: {}.", maxFlow);
        return maxFlow;
    }
}

```

**4) Graph:** src/main/java/com/example/maxflow/model/Graph.java
```java
package com.example.maxflow.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a directed graph used for calculating the maximum flow in a network.
 * <p>
 * This model captures the structure of the graph using an adjacency matrix implemented as a map of maps.
 * Each key in the outer map represents a vertex, and the corresponding inner map holds the adjacent vertices
 * and the capacity of the edge connecting them.
 * </p>
 *
 * <p><b>Usage:</b></p>
 * <ul>
 *     <li>Create an instance by specifying the total number of vertices.</li>
 *     <li>Add edges between vertices along with their capacities using the {@link #addEdge(int, int, int)} method.</li>
 *     <li>Retrieve the capacity of an edge with {@link #getCapacity(int, int)}.</li>
 * </ul>
 *
 * <p><b>Constraints:</b></p>
 * <ul>
 *     <li>The number of vertices must be a positive integer.</li>
 *     <li>Edges are directed; an edge from vertex A to vertex B does not imply an edge from B to A.</li>
 *     <li>Edge capacities must be non-negative integers.</li>
 * </ul>
 *
 * <p><b>Pass/Fail Conditions:</b></p>
 * <ul>
 *     <li><b>Pass:</b> Successfully initializes the graph with the specified number of vertices and correctly adds edges with their capacities.</li>
 *     <li><b>Fail:</b> Fails to add an edge if the specified source or destination vertices are not valid (i.e., outside the range of 0 to vertices - 1).</li>
 * </ul>
 */
public class Graph {

    /**
     * The total number of vertices in the graph.
     */
    @Min(value = 1, message = "There must be at least one vertex in the graph.")
    private final int vertices;

    /**
     * Adjacency matrix representing the graph.
     * <p>
     * Each key in the outer map represents a vertex. The inner map contains pairs where the key is the destination vertex
     * and the value is the capacity of the edge from the source vertex (the key in the outer map) to that destination.
     * </p>
     */
    @NotNull(message = "Adjacency matrix must not be null.")
    private final Map<Integer, Map<Integer, Integer>> adjMatrix;

    /**
     * Constructs a new {@code Graph} with the specified number of vertices.
     * <p>
     * Initializes an empty adjacency matrix where each vertex is associated with an empty map.
     * </p>
     *
     * @param vertices the total number of vertices in the graph; must be a positive integer.
     */
    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjMatrix = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjMatrix.put(i, new HashMap<>());
        }
    }

    /**
     * Adds a directed edge from the specified source vertex to the destination vertex with the given capacity.
     * <p>
     * If an edge already exists, its capacity is updated to the new value.
     * </p>
     *
     * @param source      the source vertex of the edge.
     * @param destination the destination vertex of the edge.
     * @param capacity    the capacity of the edge; must be non-negative.
     */
    public void addEdge(int source, int destination, int capacity) {
        // Consider adding validations for source and destination range if needed.
        // Here, we assume the calling code ensures that indices are valid.
        adjMatrix.get(source).put(destination, capacity);
    }

    /**
     * Retrieves the capacity of the edge from the specified source vertex to the destination vertex.
     * <p>
     * If the edge does not exist, returns 0.
     * </p>
     *
     * @param source      the source vertex of the edge.
     * @param destination the destination vertex of the edge.
     * @return the capacity of the edge, or 0 if no edge exists.
     */
    public int getCapacity(int source, int destination) {
        return adjMatrix.get(source).getOrDefault(destination, 0);
    }

    /**
     * Returns the entire adjacency matrix representing the graph.
     *
     * @return the adjacency matrix of the graph.
     */
    public Map<Integer, Map<Integer, Integer>> getAdjMatrix() {
        return adjMatrix;
    }

    /**
     * Returns the total number of vertices in the graph.
     *
     * @return the number of vertices.
     */
    public int getVertices() {
        return vertices;
    }
}

```

**5) GlobalExceptionHandler:** src/main/java/com/example/maxflow/exception/GlobalExceptionHandler.java
```java
package com.example.maxflow.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that intercepts exceptions thrown by controllers
 * and returns a standardized error response.
 *
 * <p><strong>Overview:</strong></p>
 * This class uses Spring's {@code @ControllerAdvice} annotation to globally handle exceptions.
 * In particular, it intercepts {@link MaxFlowException} and builds a structured JSON error response.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Any controller throwing a {@link MaxFlowException} will have its exception intercepted by this handler.</li>
 *     <li>The error response includes a timestamp, error message, HTTP status code, error reason, and the request path.</li>
 * </ul>
 *
 * <p><strong>Error Response Format:</strong></p>
 * <pre>
 * {
 *   "timestamp": "Date object",
 *   "message": "Error message",
 *   "status": HTTP status code (e.g., 404),
 *   "error": "Reason phrase (e.g., Not Found)",
 *   "path": "Request URI (e.g., uri=/api/maxflow/calculate)"
 * }
 * </pre>
 *
 * @see MaxFlowException
 * @version 1.0
 * @since 2025-03-26
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Helper method to build the error response.
     *
     * @param message the error message
     * @param status  the HTTP status to be returned
     * @param request the current web request
     * @return a {@link ResponseEntity} containing the error details as a JSON map
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("path", request.getDescription(false)); // e.g., "uri=/api/..."
        return new ResponseEntity<>(errorDetails, status);
    }

    /**
     * Handles {@link MaxFlowException} thrown by controllers.
     *
     * <p><strong>Description:</strong></p>
     * This method intercepts any {@link MaxFlowException} and returns a standardized error response
     * with HTTP status 404 (Not Found). It logs the error message and builds the error response using
     * {@link #buildErrorResponse(String, HttpStatus, WebRequest)}.
     *
     * @param ex      the {@link MaxFlowException} that was thrown
     * @param request the current web request
     * @return a {@link ResponseEntity} with error details and HTTP status 404 (Not Found)
     */
    @ExceptionHandler(MaxFlowException.class)
    public ResponseEntity<Map<String, Object>> handleMaxFlowException(MaxFlowException ex, WebRequest request) {
        log.error("MaxFlowException occurred: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }
}

```
**6) MaxFlowException:** src/main/java/com/example/maxflow/exception/MaxFlowException.java
```java
package com.example.maxflow.exception;




/**
 * Custom exception for errors related to the maximum flow computation.
 */
public class MaxFlowException extends RuntimeException {

    /**
     * Constructs a new {@code MaxFlowException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public MaxFlowException(String message) {
        super(message);
    }
}

```
**7) MaxFlowController:** src/main/java/com/example/maxflow/controller/MaxFlowController.java
```java
package com.example.maxflow.controller;

import com.example.maxflow.model.Graph;
import com.example.maxflow.service.MaxFlowService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for calculating the maximum flow in a flow network using the Edmonds-Karp algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This controller exposes an endpoint to calculate the maximum flow from a given source to a sink vertex.
 * It leverages {@link MaxFlowService} for computation, and validates input using JSR-380 annotations.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *   <li>POST a JSON representation of a graph along with query parameters specifying source and sink.</li>
 *   <li>The API returns the maximum flow as an integer with HTTP 200 (OK) status if the input is valid.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><b>Pass:</b> Valid graph input returns the correct max flow.</li>
 *   <li><b>Fail:</b> Invalid input (e.g., negative vertices, invalid indices) results in an error response.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/maxflow")
public class MaxFlowController {

    private final MaxFlowService maxFlowService;

    /**
     * Constructs a new {@code MaxFlowController} and initializes the {@link MaxFlowService}.
     */
    public MaxFlowController() {
        this.maxFlowService = new MaxFlowService();
    }

    /**
     * Calculates the maximum flow from the specified source to sink in the provided graph.
     *
     * <p><strong>Description:</strong></p>
     * This endpoint accepts a graph (validated using JSR-380 annotations) in JSON format, along with source and sink query parameters.
     * It delegates the computation to {@link MaxFlowService} which processes the graph using the Edmonds-Karp algorithm.
     *
     * @param graph  the flow network represented as a {@link Graph} object; validated for correctness.
     * @param source the index of the source vertex.
     * @param sink   the index of the sink vertex.
     * @return the maximum flow from the source to the sink.
     */
    @PostMapping("/calculate")
    public int calculateMaxFlow(@Valid @RequestBody Graph graph,
                                @RequestParam int source,
                                @RequestParam int sink) {
        return maxFlowService.edmondsKarp(graph, source, sink);
    }
}

```
**8) SecurityConfig:** src/main/java/com/example/maxflow/config/SecurityConfig.java
```java
package com.example.maxflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration for the Notification Service Application.
 *
 * <p><strong>Overview:</strong></p>
 * This configuration sets up the security filter chain for the Notification Service. It:
 * <ul>
 *   <li>Enables CORS with custom rules to allow requests from development frontends (e.g., localhost:3000).</li>
 *   <li>Disables CSRF protection for API development (note: CSRF protection should be enabled in production).</li>
 *   <li>Permits open access to all endpoints for development and testing purposes.</li>
 *   <li>Enables HTTP Basic authentication for secured endpoints during testing.</li>
 *   <li>Configures in-memory authentication with a single test user.</li>
 * </ul>
 *
 * <p><strong>References:</strong></p>
 * <ul>
 *   <li>{@link SecurityFilterChain} is used to build and configure the filter chain.</li>
 *   <li>{@link CorsConfigurationSource} sets the CORS rules applied to all endpoints.</li>
 *   <li>{@link InMemoryUserDetailsManager} is used for in-memory user authentication.</li>
 * </ul>
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *   <li>Applies custom CORS rules allowing origins "http://localhost:3000" and "http://127.0.0.1:3000", with permitted methods GET, POST, PUT, DELETE, and OPTIONS.</li>
 *   <li>Disables CSRF protection to simplify API development.</li>
 *   <li>Allows open access to all endpoints ("/**" and "/api/**") for development purposes.</li>
 *   <li>Enables HTTP Basic authentication to secure endpoints during testing.</li>
 *   <li>Defines an in-memory user with username "user", password "password" (with no encoding), and role "USER".</li>
 * </ul>
 *
 * <p><strong>Error Conditions:</strong></p>
 * <ul>
 *   <li>If CORS is misconfigured, cross-origin requests may fail.</li>
 *   <li>Disabling CSRF in production environments can expose the application to CSRF attacks.</li>
 *   <li>If in-memory user details are misconfigured, authentication may fail, resulting in unauthorized access.</li>
 * </ul>
 *
 * <p><strong>Acceptable Values / Range:</strong></p>
 * <ul>
 *   <li><strong>Allowed Origins:</strong> "http://localhost:3000" and "http://127.0.0.1:3000"</li>
 *   <li><strong>Allowed Methods:</strong> GET, POST, PUT, DELETE, OPTIONS</li>
 *   <li><strong>User Credentials:</strong> Username "user", password "password" (with {noop} encoding), role "USER"</li>
 * </ul>
 *
 * <p><strong>Premise and Assertions:</strong></p>
 * <ul>
 *   <li>The configuration assumes a development or testing environment where open access is acceptable.</li>
 *   <li>It is expected that these settings will be revised for production to enforce stricter security controls.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> The SecurityFilterChain is correctly configured, permitting valid CORS requests, proper authentication,
 *       and open access as defined.</li>
 *   <li><strong>Fail:</strong> Unauthorized access attempts or misconfigured endpoints result in failure, typically yielding a 403 Forbidden response.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * <p><strong>Description:</strong></p>
     * This method sets up the security filter chain by:
     * <ul>
     *   <li>Enabling CORS with the defined custom configuration.</li>
     *   <li>Disabling CSRF protection for API development (ensure to secure this in production).</li>
     *   <li>Permitting all requests to endpoints ("/**" and "/api/**") for development purposes.</li>
     *   <li>Enabling HTTP Basic authentication to secure endpoints for testing.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If CORS settings are not applied correctly, cross-origin requests may fail.</li>
     *   <li>If CSRF is disabled in production, the application may be vulnerable to CSRF attacks.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> The filter chain builds successfully, and valid requests are processed with appropriate authentication.</li>
     *   <li><strong>Fail:</strong> Misconfiguration results in security exceptions or unauthorized access errors.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object provided by Spring Security.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors() // Enable CORS
                .and()
                .csrf().disable() // Disable CSRF for API development
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/**",      // Allow all paths
                                "/api/**"   // REST APIs
                        ).permitAll()
                        .anyRequest().permitAll() // Catch-all for any unlisted route
                )
                .httpBasic(); // Enable HTTP Basic authentication for testing

        return http.build();
    }

    /**
     * Defines the CORS configuration for the application.
     *
     * <p><strong>Description:</strong></p>
     * This method sets up CORS rules to allow requests from the development frontend running on
     * "http://localhost:3000" and "http://127.0.0.1:3000". It permits common HTTP methods and headers,
     * enables credentials, and sets the preflight cache duration.
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If the allowed origins or methods are misconfigured, legitimate cross-origin requests may be blocked.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> The CORS configuration is correctly applied to all endpoints.</li>
     *   <li><strong>Fail:</strong> Misconfigured CORS settings result in failed cross-origin requests.</li>
     * </ul>
     *
     * @return a {@link CorsConfigurationSource} containing the CORS settings.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply configuration to all endpoints
        return source;
    }

    /**
     * Defines an in-memory user for testing purposes.
     *
     * <p><strong>Description:</strong></p>
     * This method creates a simple in-memory user with the following details:
     * <ul>
     *   <li><strong>Username:</strong> "user"</li>
     *   <li><strong>Password:</strong> "password" (with {noop} to indicate no password encoding)</li>
     *   <li><strong>Role:</strong> "USER"</li>
     * </ul>
     * This user is intended for development and testing of authentication mechanisms.
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If the user details are misconfigured, authentication will fail, resulting in access errors.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> The in-memory user is correctly configured, allowing successful authentication.</li>
     *   <li><strong>Fail:</strong> Misconfiguration of user details leads to failed authentication.</li>
     * </ul>
     *
     * @return an instance of {@link UserDetailsService} containing the test user.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                               .password("{noop}password") // No encoding used for testing purposes
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }
}
```



**8) Maven:** pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
		<modelVersion>4.0.0</modelVersion>
		<parent>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-parent</artifactId>
			<version>3.4.2</version>
			<relativePath/> <!-- lookup parent from repository -->
		</parent>
		<groupId>com.example</groupId>
		<artifactId>maxflow</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<name>edmonds‑karp-app</name>
		<description>Develop a Spring Boot REST API that uses the Edmonds‑Karp algorithm to compute max flow in networks.</description>
		<url/>
		<licenses>
			<license/>
		</licenses>
		<developers>
			<developer/>
		</developers>
		<scm>
			<connection/>
			<developerConnection/>
			<tag/>
			<url/>
		</scm>
		<properties>
			<java.version>17</java.version>
		</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<!-- Spring Boot Starter Web (Includes Spring Security by default) -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<!-- Spring Security for authentication and authorization -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<!-- Spring Boot Starter Web (Includes Spring Security by default) -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<!-- Spring Boot Starter for Validation -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<!-- Lombok Dependency -->
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.30</version>
			<scope>provided</scope>
		</dependency>
		<!-- For Jakarta Bean Validation -->
		<dependency>
			<groupId>jakarta.validation</groupId>
			<artifactId>jakarta.validation-api</artifactId>
			<version>3.0.2</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<version>3.4.1</version>
		</dependency>


		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>

	</dependencies>

		<build>
			<plugins>
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
				</plugin>
			</plugins>
		</build>

	</project>
```


# **Unit Tests (JUnit 5 + Mockito)**

**9) Main Application:** src/test/java/com/example/maxflow/MaxFlowApplicationTest.java
```java
package com.example.maxflow;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for the main application class {@link MaxFlowApplication}.
 *
 * <p>
 * This test class includes:
 * <ul>
 *   <li>{@code contextLoads()} - verifies that the Spring application context starts up correctly.
 *   <li>{@code testMainMethod()} - calls the main method to ensure it executes without throwing exceptions.
 * </ul>
 * </p>
 *
 * <p>
 * With these tests, code coverage tools (e.g., JaCoCo) should report over 90% class, method, and line coverage.
 * </p>
 */
@SpringBootTest(classes = MaxFlowApplication.class)
public class MaxFlowApplicationTest {

    /**
     * Verifies that the Spring application context loads successfully.
     * If the context fails to load, the test will fail.
     */
    @Test
    public void contextLoads() {
        // The application context is automatically loaded by the @SpringBootTest annotation.
    }

    /**
     * Verifies that calling the main method of {@link MaxFlowApplication} executes without exceptions.
     */
    @Test
    public void testMainMethod() {
        // Call the main method with an empty argument array.
        MaxFlowApplication.main(new String[]{});
        // Test passes if no exception is thrown.
    }
}

```
**10) MaxFlowServiceTest:** src/test/java/com/example/maxflow/service/MaxFlowServiceTest.java
```java
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

```
**11) GlobalExceptionHandlerTest:** src/test/java/com/example/maxflow/exception/GlobalExceptionHandlerTest.java
```java
package com.example.maxflow.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GlobalExceptionHandler} without using a dummy controller.
 *
 * <p><strong>Overview:</strong></p>
 * This test class directly invokes the exception handler method to verify that when a {@link MaxFlowException}
 * is thrown, the {@link GlobalExceptionHandler} returns a standardized error response containing the proper
 * error message, HTTP status, timestamp, error reason, and request path.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>A {@link WebRequest} is mocked to provide a sample request description.</li>
 *     <li>The {@code handleMaxFlowException} method is invoked with a sample exception and the mocked request.</li>
 *     <li>The returned {@link ResponseEntity} is verified to contain all required fields with expected values.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> The error response contains a non-null timestamp, the expected error message,
 *         status code 404, error reason "Not Found", and the correct request path.</li>
 *     <li><strong>Fail:</strong> Any missing or incorrect fields in the error response.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests that the global exception handler correctly builds the error response when a {@link MaxFlowException} is thrown.
     *
     * <p><strong>Scenario:</strong> The exception handler is invoked directly with a {@link MaxFlowException} and a mocked {@link WebRequest}.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The HTTP status in the response is 404 (Not Found).</li>
     *     <li>The "message" field equals "Test exception".</li>
     *     <li>The "status" field equals 404.</li>
     *     <li>The "error" field equals "Not Found".</li>
     *     <li>The "timestamp" field is non-null and an instance of {@link Date}.</li>
     *     <li>The "path" field contains the mocked URI.</li>
     * </ul>
     * <strong>Pass Condition:</strong> All assertions pass.
     * <strong>Fail Condition:</strong> Any assertion fails.
     * </p>
     */
    @Test
    @DisplayName("handleMaxFlowException returns proper error response")
    void testHandleMaxFlowException() {
        // Create a sample MaxFlowException
        MaxFlowException ex = new MaxFlowException("Test exception");

        // Create a mock WebRequest that returns a sample request description
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/test");

        // Invoke the exception handler
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleMaxFlowException(ex, mockRequest);

        // Assert that the status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Get the response body and verify all required fields
        Map<String, Object> body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertEquals("Test exception", body.get("message"), "Error message should match");
        assertEquals(404, body.get("status"), "Status code should be 404");
        assertEquals("Not Found", body.get("error"), "Error reason should be 'Not Found'");
        assertTrue(body.get("timestamp") instanceof Date, "Timestamp should be a Date instance");
        assertEquals("uri=/test", body.get("path"), "Path should match the mocked request description");
    }
}

```
**12) MaxFlowControllerIntegrationTest:** src/test/java/com/example/maxflow/controller/MaxFlowControllerIntegrationTest.java
```java
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

```
**13) SecurityConfigTest:** src/test/java/com/example/maxflow/config/SecurityConfigTest.java
```java
package com.example.maxflow.config;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SecurityConfig}.
 *
 * <p><strong>Overview:</strong></p>
 * These tests verify that security-related beans such as {@link SecurityFilterChain},
 * {@link UserDetailsService}, and {@link CorsConfigurationSource} are correctly configured.
 *
 * <p><strong>Scope:</strong></p>
 * <ul>
 *   <li>Bean instantiation</li>
 *   <li>Basic configuration assertions</li>
 * </ul>
 */
@SpringBootTest
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired

    private  SecurityConfig config;
    @Autowired
    private SecurityFilterChain securityFilterChain;
    /**
     * Test 1: Verifies that the security filter chain is created successfully.
     */
    @Test
    @DisplayName("Test 1: Security filter chain loads")
    void testSecurityFilterChain() {
        assertNotNull(securityFilterChain);
    }


    /**
     * Test 2: Verifies that the CORS configuration source allows expected origins and methods.
     */

    @Test
    @DisplayName("Test 2: CORS configuration is defined correctly")
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = config.corsConfigurationSource();
        assertNotNull(source);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var cors = source.getCorsConfiguration(request);

        assertNotNull(cors);
        assertTrue(cors.getAllowedOrigins().contains("http://localhost:3000"));
        assertTrue(cors.getAllowedMethods().contains("GET"));
        assertTrue(cors.getAllowCredentials());
    }


    /**
     * Test 3: Verifies that the in-memory user is configured with username 'user'.
     */
    @Test
    @DisplayName("Test 3: In-memory user setup")
    void testUserDetailsService() {
        UserDetailsService userDetailsService = config.userDetailsService();
        assertNotNull(userDetailsService);
        assertDoesNotThrow(() -> userDetailsService.loadUserByUsername("user"));
    }
}

```


**Result:** The line coverage is 39%

<a href="https://drive.google.com/file/d/14sXbGAy92H4HovkZAdDXrlx8FGyzOOE_/view?usp=drive_link">Iteration One</a>

**Plan:** The goal is to achieve >90% total code coverage and 95% total line coverage. To achieve this goal, will be writing tests for all models, dto, service, exception, event and controller packages.


<a href="https://drive.google.com/file/d/1G3UUKaedD_BzGLIADnRRLIpgmY2yH6qi/view?usp=drive_link">Iteration Two </a>

**Result:** Total line coverage is 95%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml includes Spring Boot Starter Web, Security, Validation, Lombok, Test, H2 Database, Jakarta Validation API, and DevTools dependencies with Java version set to 17 for a scalable REST API using the Edmonds‑Karp algorithm..
3. **Database & Application Properties:** Configure H2 DB credentials in application.properties (or application.yml) due to in-memory auth, for example:
```properties
spring.application.name=MaxFlowApplication
server.port=8080

# Default in-memory user for basic authentication
spring.security.user.name=admin
spring.security.user.password=admin123

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
6.**Accessing Endpoints & Features:** REST Endpoints typically follow the pattern:
- **Max Flow:**
    - **POST /api/maxflow/calculate** (calculate max flow, e.g., `{"vertices":4,"adjMatrix":{"0":{"1":10,"2":5},"1":{"3":10},"2":{"3":5},"3":{}}}` with query parameters `source=0` and `sink=3`)
9. **Security**

  - Our Spring Security configuration enables CORS with custom rules (e.g., allowing requests from localhost:3000), disables CSRF protection for API development, permits open access to all endpoints for testing purposes, enables HTTP Basic authentication for secured endpoints during testing, and configures in-memory authentication with a single test user.
  - 
# **Time and Space Complexity:**

- **Time Complexity:** The Edmonds‑Karp algorithm runs in **O(V * E²)**, where **V** is the number of vertices and **E** is the number of edges; this complexity is optimal for sparse networks and scales well with moderate graph sizes.
- **Space Complexity:** The algorithm uses **O(V²)** space to store capacity and flow matrices, ensuring that memory usage remains manageable for typical use cases.

# **Conclusion**

A well-designed REST API built with Spring Boot and employing the Edmonds‑Karp algorithm can efficiently compute maximum flow in networks, balancing the algorithm's inherent **O(V * E²)** time complexity with practical performance optimizations, robust input validation, and comprehensive testing to ensure scalability and reliability in network analysis applications.
