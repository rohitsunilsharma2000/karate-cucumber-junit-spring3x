**Metadata**

**Programming Language:** Java

**L1 Taxonomy:** Complete Implementations

**L2 Taxonomy:** Social Media Application

**Use Case:**  Implement the Ford-Fulkerson algorithm for maximum flow in a Spring Boot service to efficiently compute the maximum possible flow in a directed graph. This will help in optimizing resource allocation, network bandwidth management, and flow distribution in systems with multiple nodes and capacities. The service will expose REST endpoints for clients to submit graph data and retrieve maximum flow results. It will provide enhanced network analysis for resource allocation and system optimization.

# **Prompt**

## **Title:**
Spring Boot Maximum Flow Calculator — Ford‑Fulkerson Algorithm Implementation

## **High-Level Description:**
Develop a Spring Boot application that implements the Ford‑Fulkerson algorithm to compute the maximum flow in a network. The application exposes a REST API endpoint where clients can submit a directed graph (provided as an adjacency matrix) along with designated source and sink vertices. The service processes the request using a well-structured, layered architecture that includes a controller, service layer, and global exception handling. The solution emphasizes input validation, comprehensive logging, and robust error management to ensure data integrity and easy troubleshooting.

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


---
# **Complete Project Code**

**1) Project Structure:** A logical structure (typical Maven layout)

```
graph-flow-solver
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- maxflow
|   |               |-- FordFulkersonMaxFlowApplication.java
|   |               |-- config
|   |               |   `-- SecurityConfig.java
|   |               |-- controller
|   |               |   `-- MaxFlowController.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   `-- MaxFlowException.java
|   |               `-- service
|   |                   `-- MaxFlowService.java
|   `-- resources
`-- test
    `-- java
        `-- com
            `-- example
                `-- maxflow
                    |-- config
                    |   `-- SecurityConfigTest.java
                    |-- controller
                    |   `-- MaxFlowControllerIntegrationTest.java
                    |-- exception
                    |   `-- GlobalExceptionHandlerTest.java
                    `-- service
                        `-- MaxFlowServiceTest.java


```

**2) Main Application:** src/main/java/com/example/maxflow/FordFulkersonMaxFlowApplication.java
```java
package com.example.maxflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Ford-Fulkerson Max Flow Spring Boot application.
 * <p>
 * This class bootstraps the Spring Boot application by invoking the
 * {@link SpringApplication#run(Class, String...)} method. It initializes the Spring
 * application context and starts the embedded web server.
 * </p>
 */
@SpringBootApplication
public class FordFulkersonMaxFlowApplication {

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(FordFulkersonMaxFlowApplication.class, args);
    }
}

```
**3) MaxFlowService:** src/main/java/com/example/maxflow/service/MaxFlowService.java
```java
package com.example.maxflow.service;

import com.example.maxflow.exception.MaxFlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Service class providing functionality for calculating the maximum flow in a directed graph
 * using the Ford-Fulkerson algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This service implements the Ford-Fulkerson algorithm using breadth-first search (BFS)
 * to find augmenting paths. It includes comprehensive input validation to ensure data integrity,
 * custom exception handling to provide meaningful feedback when validations fail, and extensive logging
 * for traceability during development and troubleshooting.
 *
 * <p><strong>Input Validation and Data Integrity:</strong></p>
 * <ul>
 *     <li>Verifies that the provided graph is non-null and represented by a square matrix.</li>
 *     <li>Ensures that all capacities in the matrix are non-negative.</li>
 *     <li>Checks that the source and sink indices are within the valid range.</li>
 * </ul>
 *
 * <p><strong>Custom Exception Handling and Security:</strong></p>
 * <ul>
 *     <li>If any input validation fails (e.g., null graph, non-square matrix, negative capacity, or invalid indices),
 *     a {@link MaxFlowException} is thrown with an informative message.</li>
 *     <li>Input validations help to mitigate risks associated with processing malformed or malicious data.</li>
 * </ul>
 *
 * <p><strong>Logging and Traceability:</strong></p>
 * <ul>
 *     <li>Integrates logging across all application layers using multiple log levels (INFO, DEBUG, ERROR).</li>
 *     <li>Provides clear traceability of algorithm progress and validation errors for easier troubleshooting.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Inject and use the {@code MaxFlowService} to compute maximum flow using the {@link #fordFulkerson(int[][], int, int)} method.</li>
 *     <li>Ensure that the input graph is properly sanitized before calling the service to maintain security.</li>
 * </ul>
 */
@Service
public class MaxFlowService {

    private static final Logger logger = LoggerFactory.getLogger(MaxFlowService.class);

    /**
     * Calculates the maximum flow from the source to sink in the provided graph using the Ford-Fulkerson algorithm.
     *
     * <p><strong>Description:</strong></p>
     * This method computes the maximum flow of a network represented by a capacity matrix. It first performs
     * thorough input validation, then iteratively uses a breadth-first search (BFS) to identify augmenting paths,
     * adjusts the residual capacities, and accumulates the total flow until no further paths exist.
     *
     * <p><strong>Input Validation:</strong></p>
     * <ul>
     *     <li>Ensures the graph is non-null and the matrix is square.</li>
     *     <li>Verifies that all edge capacities are non-negative.</li>
     *     <li>Checks that the source and sink vertices are within valid bounds.</li>
     * </ul>
     *
     * <p><strong>Security Considerations:</strong></p>
     * <ul>
     *     <li>Input validations help to prevent potential exploits from malicious data inputs.</li>
     * </ul>
     *
     * @param graph  The capacity matrix representing the graph. Must be a non-null square matrix with non-negative integers.
     * @param source The index of the source vertex.
     * @param sink   The index of the sink vertex.
     * @return The maximum flow from the source to sink.
     * @throws MaxFlowException if the graph is null, not square, contains negative capacities, or if the source/sink indices are invalid.
     */
    public int fordFulkerson(int[][] graph, int source, int sink) {
        logger.info("Starting maximum flow calculation using Ford-Fulkerson algorithm.");

        // Validate the graph input
        if (graph == null) {
            logger.error("Graph cannot be null.");
            throw new MaxFlowException("Graph cannot be null.");
        }
        int n = graph.length;
        if (n == 0) {
            logger.error("Graph must have at least one vertex.");
            throw new MaxFlowException("Graph must have at least one vertex.");
        }
        // Ensure the matrix is square and all capacities are non-negative
        for (int i = 0; i < n; i++) {
            if (graph[i] == null || graph[i].length != n) {
                logger.error("Graph must be represented by a square matrix. Issue at row: {}", i);
                throw new MaxFlowException("Graph must be represented by a square matrix.");
            }
            for (int j = 0; j < n; j++) {
                if (graph[i][j] < 0) {
                    logger.error("Negative capacity detected at edge ({}, {}).", i, j);
                    throw new MaxFlowException("Negative capacity detected at edge (" + i + ", " + j + ").");
                }
            }
        }
        // Validate source and sink indices
        if (source < 0 || source >= n) {
            logger.error("Invalid source index: {}", source);
            throw new MaxFlowException("Invalid source index: " + source);
        }
        if (sink < 0 || sink >= n) {
            logger.error("Invalid sink index: {}", sink);
            throw new MaxFlowException("Invalid sink index: " + sink);
        }
        // If source equals sink, maximum flow is zero by definition
        if (source == sink) {
            logger.info("Source equals sink. Returning 0 as maximum flow.");
            return 0;
        }

        int u, v;
        // Create a residual graph and initialize it with capacities from the original graph
        int[][] rGraph = new int[n][n];
        for (u = 0; u < n; u++) {
            for (v = 0; v < n; v++) {
                rGraph[u][v] = graph[u][v];
            }
        }

        int[] parent = new int[n]; // Array to store the augmenting path
        int maxFlow = 0; // Initialize maximum flow to zero

        logger.debug("Entering main loop to find augmenting paths.");
        // Augment the flow while there is a path from source to sink
        while (bfs(rGraph, source, sink, parent)) {
            // Find the minimum residual capacity along the path found by BFS
            int pathFlow = Integer.MAX_VALUE;
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
            }
            logger.debug("Augmenting path found with flow: {}", pathFlow);

            // Update residual capacities for the edges and reverse edges along the path
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
            logger.info("Updated maximum flow: {}", maxFlow);
        }

        logger.info("Ford-Fulkerson algorithm completed. Total maximum flow: {}", maxFlow);
        return maxFlow;
    }

    /**
     * Performs a breadth-first search on the residual graph to find an augmenting path from source to sink.
     *
     * <p><strong>Description:</strong></p>
     * This helper method searches for an augmenting path in the residual graph. It updates the parent array to store
     * the path, and returns true if a path from source to sink exists, or false otherwise.
     *
     * @param rGraph The residual graph representing the available capacities.
     * @param s      The source vertex.
     * @param t      The sink vertex.
     * @param parent An array to store the path from source to sink.
     * @return {@code true} if an augmenting path exists; {@code false} otherwise.
     */
    private boolean bfs(int[][] rGraph, int s, int t, int[] parent) {
        int n = rGraph.length;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        logger.debug("Starting BFS for augmenting path search from source: {} to sink: {}.", s, t);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            logger.debug("Processing vertex: {}", u);
            for (int v = 0; v < n; v++) {
                if (!visited[v] && rGraph[u][v] > 0) {
                    parent[v] = u;
                    visited[v] = true;
                    logger.debug("Vertex {} visited via vertex {}.", v, u);
                    if (v == t) {
                        logger.debug("Sink {} reached during BFS.", t);
                        return true;
                    }
                    queue.add(v);
                }
            }
        }
        logger.debug("No augmenting path found in BFS.");
        return false;
    }
}

```
**4) MaxFlowException:** src/main/java/com/example/maxflow/exception/MaxFlowException.java
```java
package com.example.maxflow.exception;

/**
 * Custom exception for errors related to the maximum flow computation.
 * <p>
 * This exception is thrown when there is an issue during the execution
 * of the Ford-Fulkerson algorithm or related operations in the maximum flow calculation.
 * </p>
 */
public class MaxFlowException extends RuntimeException {

    /**
     * Constructs a new {@code MaxFlowException} with the specified detail message.
     *
     * @param message the detail message that provides more information about the exception.
     */
    public MaxFlowException(String message) {
        super(message);
    }
}

```
**5) GlobalExceptionHandler:** src/main/java/com/example/maxflow/exception/GlobalExceptionHandler.java
```java
package com.example.maxflow.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to catch and process exceptions thrown by controller methods.
 * <p>
 * This class is annotated with {@code @RestControllerAdvice} to intercept exceptions across the whole application.
 * It provides specific exception handlers for:
 * <ul>
 *   <li>{@link MaxFlowException} - Custom exception for max flow computation errors.</li>
 *   <li>{@link MethodArgumentNotValidException} - Validation errors when request arguments fail validation.</li>
 *   <li>{@link ConstraintViolationException} - Violations of bean validation constraints.</li>
 *   <li>{@code Exception} - Any other unexpected exceptions.</li>
 * </ul>
 * Each handler logs the error using different log levels and returns an appropriate {@code ResponseEntity} with a corresponding HTTP status code.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Logger instance for logging error messages.
   */
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles custom {@link MaxFlowException} thrown during max flow computation.
   *
   * @param ex the {@link MaxFlowException} instance
   * @return a {@link ResponseEntity} containing an error message map and a {@code BAD_REQUEST} status
   */
  @ExceptionHandler(MaxFlowException.class)
  public ResponseEntity<Map<String, String>> handleMaxFlowException(MaxFlowException ex) {
    logger.error("MaxFlowException: {}", ex.getMessage());
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link MethodArgumentNotValidException} thrown when request validation fails.
   *
   * @param ex the {@link MethodArgumentNotValidException} instance
   * @return a {@link ResponseEntity} containing a map of field-specific error messages and a {@code BAD_REQUEST} status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
    logger.error("Validation failure: {}", ex.getMessage());
    Map<String, String> errorMap = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(err ->
                                                           errorMap.put(err.getField(), err.getDefaultMessage())
    );
    return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link ConstraintViolationException} thrown when bean validation constraints are violated.
   *
   * @param ex the {@link ConstraintViolationException} instance
   * @return a {@link ResponseEntity} containing a map of constraint violation messages and a {@code BAD_REQUEST} status
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
    logger.error("Constraint violation: {}", ex.getMessage());
    Map<String, String> errorMap = new HashMap<>();
    ex.getConstraintViolations().forEach(cv ->
                                                 errorMap.put("field", cv.getMessage())
    );
    return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles all other unhandled exceptions that are not explicitly caught by other exception handlers.
   *
   * @param ex the generic {@link Exception} instance
   * @return a {@link ResponseEntity} containing a generic error message and an {@code INTERNAL_SERVER_ERROR} status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
    logger.error("Unhandled exception: {}", ex.getMessage(), ex);
    Map<String, String> error = new HashMap<>();
    error.put("error", "An unexpected error occurred.");
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

```
**6) MaxFlowController:** src/main/java/com/example/maxflow/controller/MaxFlowController.java
```java
package com.example.maxflow.controller;

import com.example.maxflow.exception.MaxFlowException;
import com.example.maxflow.service.MaxFlowService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for calculating the maximum flow in a flow network using the Ford-Fulkerson algorithm.
 * <p>
 * Enhancements:
 * <ul>
 *   <li>Input Validation and Data Integrity: Utilizes validation annotations to ensure incoming data meets required criteria.</li>
 *   <li>Logging and Traceability: Employs multiple log levels (INFO, DEBUG, ERROR) to provide detailed traceability.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/maxflow")
@Validated
public class MaxFlowController {

    /**
     * Logger instance for logging messages with various log levels.
     */
    private static final Logger logger = LoggerFactory.getLogger(MaxFlowController.class);

    /**
     * Service layer for performing the maximum flow calculation using the Ford-Fulkerson algorithm.
     */
    private final MaxFlowService maxFlowService;

    /**
     * Constructor for dependency injection of the MaxFlowService.
     *
     * @param maxFlowService The service responsible for calculating the maximum flow.
     */
    public MaxFlowController(MaxFlowService maxFlowService) {
        this.maxFlowService = maxFlowService;
    }

    /**
     * Calculates the maximum flow from a source to a sink in a directed graph.
     * <p>
     * This method receives a validated request payload containing the graph,
     * source index, and sink index. It logs the input details at INFO and DEBUG levels,
     * invokes the service layer to compute the maximum flow, and returns the result.
     * In case of validation errors or exceptions, appropriate HTTP error statuses are returned.
     * </p>
     *
     * @param request Validated request payload containing the graph, source, and sink indices.
     * @return {@link ResponseEntity} containing the computed maximum flow value or an error message.
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMaxFlow(@Valid @RequestBody MaxFlowRequest request) {
        logger.info("Received max flow calculation request from source={} to sink={}",
                    request.getSource(), request.getSink());
        logger.debug("Validating input graph and indices.");

        try {
            // Log the full graph details at DEBUG level for traceability.
            logger.debug("Input graph: {}", (Object) request.getGraph());

            // Calculate max flow using the service layer.
            int maxFlow = maxFlowService.fordFulkerson(request.getGraph(), request.getSource(), request.getSink());

            logger.info("Computed max flow successfully: {}", maxFlow);
            return ResponseEntity.ok(maxFlow);

        } catch (MaxFlowException e) {
            logger.error("MaxFlowException occurred during max flow computation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error while computing max flow: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred. Please try again later.");
        }
    }

    /**
     * Request Data Transfer Object (DTO) for max flow calculation.
     * <p>
     * This inner static class is used to encapsulate the request data required for
     * calculating the maximum flow. Input validation is enforced via annotations.
     * </p>
     *
     * Enhancements:
     * <ul>
     *   <li>Input Validation and Data Integrity: Enforced via {@link NotNull} and {@link Min} annotations.</li>
     * </ul>
     */
    public static class MaxFlowRequest {

        /**
         * Two-dimensional array representing the graph's adjacency matrix.
         * Must not be null.
         */
        @NotNull(message = "Graph must not be null.")
        private int[][] graph;

        /**
         * Index of the source vertex in the graph.
         * Must be a non-negative integer.
         */
        @Min(value = 0, message = "Source vertex index must be non-negative.")
        private int source;

        /**
         * Index of the sink vertex in the graph.
         * Must be a non-negative integer.
         */
        @Min(value = 0, message = "Sink vertex index must be non-negative.")
        private int sink;

        /**
         * Gets the graph represented as a two-dimensional array.
         *
         * @return The adjacency matrix of the graph.
         */
        public int[][] getGraph() {
            return graph;
        }

        /**
         * Sets the graph represented as a two-dimensional array.
         *
         * @param graph The adjacency matrix of the graph.
         */
        public void setGraph(int[][] graph) {
            this.graph = graph;
        }

        /**
         * Gets the source vertex index.
         *
         * @return The source index.
         */
        public int getSource() {
            return source;
        }

        /**
         * Sets the source vertex index.
         *
         * @param source The source index.
         */
        public void setSource(int source) {
            this.source = source;
        }

        /**
         * Gets the sink vertex index.
         *
         * @return The sink index.
         */
        public int getSink() {
            return sink;
        }

        /**
         * Sets the sink vertex index.
         *
         * @param sink The sink index.
         */
        public void setSink(int sink) {
            this.sink = sink;
        }
    }
}

```
**7) SecurityConfig:** src/main/java/com/example/graph/config/SecurityConfig.java
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
		<name>graph-flow-solver</name>
		<description>Implement the Ford‑Fulkerson algorithm for maximum flow in a Spring Boot service.</description>
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

**Result:** The line coverage is 33%

<a href="https://drive.google.com/file/d/17T-7mr7KN_EvCeBWwcAWk49zyBum0QAh/view?usp=drive_link">Iteration One</a>

**Plan:** The goal is to achieve >90% total code coverage and 95% total line coverage. To achieve this goal, will be writing tests for all models, dto, service, exception, event and controller packages.

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
<a href="https://drive.google.com/file/d/1QZ0EjCZ1Gh4ScQ8oYKdma_-nI0GZQN-I/view?usp=drive_link">Iteration Two </a>

**Result:** Total line coverage is 96%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml includes Spring Boot Starter Web, Security, Validation, Lombok, Test, H2 Database, Jakarta Validation API, and DevTools dependencies with Java version set to 17 for a scalable REST API using the Edmonds‑Karp algorithm..
3. **Database & Application Properties:** Configure H2 DB credentials in application.properties (or application.yml) due to in-memory auth, for example:
```properties
spring.application.name=graph-flow-solver
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

### 6. **Accessing Endpoints & Features:**

#### **Calculate Maximum Flow:**
- **POST /api/maxflow/calculate**
- **Description:**  
  Computes the maximum flow from a designated source to a sink in a directed graph using the Ford‑Fulkerson algorithm. The input graph is provided as an adjacency matrix along with the source and sink vertex indices.
- **Request Body Example:**
  ```json
  {
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
}
  ```
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

7. **Security**

- Our Spring Security configuration enables CORS with custom rules (e.g., allowing requests from localhost:3000), disables CSRF protection for API development, permits open access to all endpoints for testing purposes, enables HTTP Basic authentication for secured endpoints during testing, and configures in-memory authentication with a single test user.
  Here’s the **Time and Space Complexity** and **Conclusion** section tailored specifically for your **Graph Integrity App** that detects **bridges** and **articulation points** using DFS:



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

