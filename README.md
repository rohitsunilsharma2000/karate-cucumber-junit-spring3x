



**Use Case:** A network analysis tool for industries requiring efficient detection of bridges and articulation points in complex networks. This API is designed to be integrated into larger systems (e.g., infrastructure monitoring, cybersecurity, and social network analysis) to quickly identify critical nodes and connections whose removal could fragment or weaken the network. The solution must be robust, scalable, and well-documented with comprehensive REST endpoints, input validations, and strong exception handling.
Here’s your reformatted and properly indented prompt, matching the indentation and structure style of the previous "Social Media Platform" format:



# **Prompt**

## **Title:**
Spring Boot Graph Analyzer — Bridge and Articulation Point Detection API

## **High-Level Description:**  
You need to build a Spring Boot application that exposes REST endpoints to analyze undirected graphs and detect:

- **Bridges**: Edges whose removal disconnects the graph.
- **Articulation Points**: Vertices whose removal increases the number of connected components.

This API receives a graph represented as vertices and edges, uses DFS-based algorithms for structural analysis, and returns the results in a clean format. The solution follows layered architecture, uses validation, exception handling, and includes full test coverage.



## **Key Features**

### 1. **Project Structure & Setup**
- Create the project using Spring Initializr.
- Organize packages as:
    - `controller`
    - `service`
    - `dto`
    - `exception`
    - `config`

### 2. **REST Controller & API Endpoints**
- Create a REST controller under `/graph`.
- Expose the following endpoints:
    - `POST /graph/bridges` → Returns a list of bridge edges.
    - `POST /graph/articulation` → Returns a list of articulation points.
- Validate requests using `@Valid` on the input DTO.

### 3. **Service Layer – DFS-Based Algorithms**
- Implement:
    - **Tarjan’s Algorithm** to find bridges.
    - **Low-link value tracking** to find articulation points.
- Ensure the service:
    - Uses efficient DFS with visited, discovery, and low arrays.
    - Logs computation at `INFO`, `DEBUG`, and `ERROR` levels.

### 4. **Exception Handling & Input Validation**
- Validate using Jakarta Bean Validation:
    - `@Min(1)` for vertex count.
    - `@NotEmpty`, `@NotNull` for edge lists.
    - `GraphRequestValidator` for cross-field edge validator.
- Add custom validation:
    - Edge must contain exactly 2 vertices.
    - Vertex indices must be within bounds `[0, vertices-1]`.
- Use `@RestControllerAdvice` to handle:
    - `MethodArgumentNotValidException`
    - `HttpMessageNotReadableException`
    - `MissingServletRequestParameterException`
    - `GraphAnalysisException`
    - Generic `Exception`

### 5. **Logging & Traceability**
- Log:
    - Incoming requests and payloads.
    - Step-by-step DFS traversal logic.
    - Final results.
    - Exceptions and their stack traces.

### 6. **Testing & Documentation**
- Write:
    - **Unit Tests** for service logic (bridge/articulation detection).
    - **Integration Tests** for REST endpoints with `MockMvc`.
    - **Negative Tests** for invalid requests and malformed JSON.
- Document:
    - All public methods with Javadoc.
    - Test classes with premise, assertions, pass/fail notes.

### 7. **Expected Behavior**
- Return:
    - `200 OK` for valid inputs.
    - `400 Bad Request` for:
        - `vertices <= 0`
        - `edges == null || edges.isEmpty()`
        - `null` edge nodes
        - malformed JSON
        - out-of-bound vertex indices
    - `500 Internal Server Error` for unexpected logic errors.

### 8. **Edge Case Handling**
- `vertices = 0` → return 400.
- `edges = null or []` → return 400.
- Self-loops (e.g., `[0, 0]`) → valid, must not crash.
- Duplicate edges → should be handled gracefully.
- Out-of-bound vertex indices → validation error.
- Isolated nodes → no bridges or articulation points.
- Maintain uniform error format with:
    - `timestamp`
    - `status`
    - `error`
    - `message`



## **Dependency Requirements**

- **Spring Boot Starter Web**: REST API support
- **Spring Boot Starter Validation**: Jakarta Bean validation
- **Spring Boot Starter Test**: JUnit 5, Mockito
- **Lombok**: (Optional) to reduce boilerplate
- **SLF4J / Logback**: Logging
- **Spring Boot DevTools**: For hot reload
- **JUnit 5**: For testing
- **Maven**: Dependency management



## **Goal**
To build a scalable, robust, and clean Spring Boot service for analyzing graph vulnerabilities through bridge and articulation point detection. The system must follow clean architecture, provide helpful logging and error messages, and be thoroughly tested for correctness and reliability across edge cases.



## **Plan**
 
I will set up the project structure using Spring Initializr and create the core packages: `controller`, `service`, `dto`, `exception`, and `config`. I will configure the `pom.xml` with all required dependencies and set the Java version to 17. I will define the `GraphRequest` DTO with validation annotations to ensure input integrity. I will build the REST controller to expose two endpoints: one for detecting bridges and another for articulation points. I will implement the DFS-based algorithms in the service layer with proper SLF4J logging at INFO and DEBUG levels. I will add a `GlobalExceptionHandler` class to handle validation failures, malformed requests, and custom graph exceptions uniformly. I will write unit tests for the service logic and integration tests for the REST endpoints using JUnit 5 and Spring MockMvc. Finally, I will verify the application by building and running it with Maven, ensuring that it returns correct results and properly handles edge cases and errors.


# **Complete Project Code**

**1) Project Structure:** A logical structure (typical Maven layout)

```
graph-integrity-app
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- graph
|   |               |-- GraphIntegrityApp.java
|   |               |-- controller
|   |               |   `-- GraphController.java
|   |               |-- dto
|   |               |   `-- GraphRequest.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   `-- GraphAnalysisException.java
|   |               |-- service
|   |               |   `-- GraphService.java
|   |               `-- validation
|   |                   |-- GraphRequestValidator.java
|   |                   `-- ValidGraphRequest.java
|   `-- resources
`-- test
    `-- java
        `-- com
            `-- example
                `-- graph
                    |-- GraphIntegrityAppTest.java
                    |-- controller
                    |   |-- GraphControllerIntegrationTest.java
                    |   `-- GraphControllerTest.java
                    |-- dto
                    |   `-- GraphRequestTest.java
                    |-- exception
                    |   `-- GlobalExceptionHandlerTest.java
                    |-- service
                    |   `-- GraphServiceTest.java
                    `-- validation
                        `-- GraphRequestValidatorTest.java





```

**2) Main Application:** `src/main/java/com/example/graph/GraphIntegrityApp.java`
```java
package com.example.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Graph Integrity Analysis Application.
 *
 * <p>
 * This class bootstraps the Spring Boot application, initializing all required
 * configurations, components, and services to enable graph analysis operations such as
 * detecting bridges and articulation points.
 * </p>
 *
 * <p>
 * Logging is enabled to trace application startup lifecycle events.
 * </p>
 *
 * <p>
 * <strong>Responsibilities:</strong>
 * <ul>
 *   <li>Launch the Spring Boot application context.</li>
 *   <li>Trigger component scanning and auto-configuration.</li>
 *   <li>Log important lifecycle events like startup and shutdown.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Pass/Fail Conditions:</strong>
 * <ul>
 *   <li><strong>Pass:</strong> The Spring context loads successfully, all beans are initialized, and no exceptions occur during startup.</li>
 *   <li><strong>Fail:</strong> Any misconfiguration or runtime issue during bean initialization will cause startup failure.</li>
 * </ul>
 * </p>
 *
 * @author 
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class GraphIntegrityApp {

  /**
   * Main method that starts the Spring Boot application.
   *
   * @param args Command-line arguments passed to the application.
   */
  public static void main(String[] args) {
    SpringApplication.run(GraphIntegrityApp.class, args);
  }
}

```
**3) GraphService:** `src/main/java/com/example/graph/service/GraphService.java`
```java
package com.example.graph.service;

import com.example.graph.exception.GraphAnalysisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for analyzing graph structures by detecting bridges and articulation points
 * in an undirected graph using DFS-based algorithms.
 *
 * <p>
 * Premise: The graph is undirected, with nodes labeled 0 to vertices - 1. Input is expected
 * to be validated before invoking service methods.
 * </p>
 */
@Service
public class GraphService {

    private static final Logger logger = LoggerFactory.getLogger(GraphService.class);
    private int time; // Global counter for discovery time

    /**
     * Detects all bridges (critical edges) in an undirected graph.
     *
     * <p><strong>Premise:</strong></p>
     * Graph is connected or disconnected. DFS will traverse all components.
     *
     * <p><strong>Algorithm:</strong></p>
     * Tarjan's Bridge-Finding Algorithm using DFS timestamps.
     *
     * <p><strong>Time Complexity:</strong> O(V + E)</p>
     * <p><strong>Space Complexity:</strong> O(V + E)</p>
     *
     * @param vertices number of vertices (0-indexed)
     * @param edges    list of undirected edges, each represented by a list of two integers
     * @return list of bridges formatted as strings (e.g., "1-3")
     *
     * <p><strong>Pass:</strong> If input is valid and bridges are correctly detected.</p>
     * <p><strong>Fail:</strong> If internal exceptions occur or invalid edges crash the logic.</p>
     */
    public List<String> findBridges(int vertices, List<List<Integer>> edges) {
        try {
            logger.debug("Initializing bridge detection with {} vertices and edges: {}", vertices, edges);
            time = 0;

            // Create adjacency list
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

            // Build undirected graph
            for (List<Integer> edge : edges) {
                adj.get(edge.get(0)).add(edge.get(1));
                adj.get(edge.get(1)).add(edge.get(0));
            }
            logger.info("Adjacency list built successfully.");
            boolean[] visited = new boolean[vertices];
            int[] disc = new int[vertices]; // Discovery times
            int[] low = new int[vertices];  // Lowest reachable vertex from subtree
            List<List<Integer>> bridges = new ArrayList<>();

            // Run DFS from unvisited nodes (handles disconnected graphs)
            for (int i = 0; i < vertices; i++) {
                if (!visited[i]) {
                    logger.debug("Starting DFS from vertex {}", i);
                    dfsBridge(i, -1, visited, disc, low, bridges, adj);
                }
            }

            List<String> result = bridges.stream().map(b -> b.get(0) + "-" + b.get(1)).toList();
            logger.info("Total bridges found: {}", result.size());
            return result;

        } catch (Exception e) {
            logger.error("Error occurred while finding bridges", e);
            throw new GraphAnalysisException("Something went wrong while detecting bridges in the graph.", e);
        }
    }

    /**
     * DFS helper for bridge detection using Tarjan’s algorithm.
     *
     * @param u        current node
     * @param parent   parent node in DFS tree
     * @param visited  visited marker
     * @param disc     discovery time array
     * @param low      low-link values
     * @param bridges  result list of bridges
     * @param adj      adjacency list of graph
     */
    private void dfsBridge(int u, int parent, boolean[] visited, int[] disc, int[] low,
                           List<List<Integer>> bridges, List<List<Integer>> adj) {
        visited[u] = true;
        disc[u] = low[u] = ++time;
        logger.debug("Visiting node {}: disc = {}, low = {}", u, disc[u], low[u]);

        for (int v : adj.get(u)) {
            if (!visited[v]) {
                logger.debug("Tree edge found from {} to {}", u, v);
                dfsBridge(v, u, visited, disc, low, bridges, adj);
                low[u] = Math.min(low[u], low[v]);
                logger.debug("Backtracking to {}: updated low[{}] = {}", u, u, low[u]);

                // A bridge exists if subtree cannot reach an ancestor of u
                if (low[v] > disc[u]) {
                    logger.debug("Bridge found between {} and {}", u, v);
                    bridges.add(List.of(u, v));
                }
            } else if (v != parent) {
                logger.debug("Back edge found from {} to {}", u, v);
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    /**
     * Detects all articulation points (cut vertices) in an undirected graph.
     *
     * <p><strong>Premise:</strong></p>
     * Articulation points are vertices that, if removed, increase the number of connected components.
     *
     * <p><strong>Algorithm:</strong></p>
     * DFS-based approach with Tarjan’s low-link technique.
     *
     * <p><strong>Time Complexity:</strong> O(V + E)</p>
     * <p><strong>Space Complexity:</strong> O(V + E)</p>
     *
     * @param vertices the number of vertices in the graph
     * @param edges    list of undirected edges
     * @return list of articulation point indices
     *
     * <p><strong>Pass:</strong> If valid articulation points are returned.</p>
     * <p><strong>Fail:</strong> If any error occurs or invalid edge indices are provided.</p>
     */
    public List<Integer> findArticulationPoints(int vertices, List<List<Integer>> edges) {
        try {
            logger.debug("Initializing articulation point detection with {} vertices and edges: {}", vertices, edges);
            time = 0;

            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

            for (List<Integer> edge : edges) {
                adj.get(edge.get(0)).add(edge.get(1));
                adj.get(edge.get(1)).add(edge.get(0));
            }

            logger.info("Adjacency list built successfully for articulation point detection.");

            boolean[] visited = new boolean[vertices];
            int[] disc = new int[vertices];
            int[] low = new int[vertices];
            boolean[] ap = new boolean[vertices]; // Flags for articulation points
            int[] parent = new int[vertices];
            int[] children = new int[vertices];

            // Initialize parent values and start DFS
            for (int i = 0; i < vertices; i++) {
                parent[i] = -1;
                if (!visited[i]) {
                    logger.debug("Starting DFS at articulation detection from node {}", i);
                    dfsAP(i, visited, disc, low, ap, parent, children, adj);
                }
            }

            // Collect articulation points from flags
            List<Integer> articulationPoints = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                if (ap[i]) articulationPoints.add(i);
            }

            logger.info("Total articulation points found: {}", articulationPoints.size());
            return articulationPoints;

        } catch (Exception e) {
            logger.error("Error occurred while finding articulation points", e);
            throw new GraphAnalysisException("Something went wrong while detecting articulation points in the graph.", e);
        }
    }

    /**
     * DFS helper to detect articulation points.
     *
     * @param u        current vertex
     * @param visited  array to track visited nodes
     * @param disc     discovery times
     * @param low      low values
     * @param ap       articulation point flags
     * @param parent   parent array
     * @param children children count for root node
     * @param adj      adjacency list
     */
    private void dfsAP(int u, boolean[] visited, int[] disc, int[] low, boolean[] ap,
                       int[] parent, int[] children, List<List<Integer>> adj) {
        visited[u] = true;
        disc[u] = low[u] = ++time;
        logger.debug("Visiting node {}: disc = {}, low = {}", u, disc[u], low[u]);

        for (int v : adj.get(u)) {
            if (!visited[v]) {
                logger.debug("Tree edge found from {} to {}", u, v);
                parent[v] = u;
                children[u]++;
                dfsAP(v, visited, disc, low, ap, parent, children, adj);
                low[u] = Math.min(low[u], low[v]);

                // Case 1: u is root and has 2+ children
                if (parent[u] == -1 && children[u] > 1) {
                    logger.debug("Articulation point found at root {}", u);
                    ap[u] = true;
                }

                // Case 2: u is not root and child v can't reach an ancestor of u
                if (parent[u] != -1 && low[v] >= disc[u]) {
                    logger.debug("Articulation point found at {}", u);
                    ap[u] = true;
                }

            } else if (v != parent[u]) {
                logger.debug("Back edge found from {} to {}", u, v);
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }
}

```
**4) GraphAnalysisException:** `src/main/java/com/example/graph/exception/GraphAnalysisException.java`
```java
package com.example.graph.exception;


/**
 * Exception thrown when an error occurs during graph analysis,
 * such as detecting bridges or articulation points.
 */
public class GraphAnalysisException extends RuntimeException {

    public GraphAnalysisException(String message) {
        super(message);
    }

    public GraphAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}

```
**5) GlobalExceptionHandler:** `src/main/java/com/example/graph/exception/GlobalExceptionHandler.java`
```java
package com.example.graph.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Global exception handler for managing application-wide exceptions in a consistent and structured manner.
 *
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li>Catches and handles known and unexpected exceptions thrown across all controller layers.</li>
 *   <li>Returns standardized error responses with appropriate HTTP status codes and informative messages.</li>
 * </ul>
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Handles validation errors, malformed JSON input, and missing request parameters.</li>
 *   <li>Captures application-specific and generic exceptions.</li>
 *   <li>Includes timestamp, HTTP status, error message, and optional field-level errors in responses.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Logger instance to log all exception details
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Builds a structured error response body with optional extra fields.
     *
     * @param status  The HTTP status to be returned (e.g., 400, 500).
     * @param message A human-readable message explaining the error.
     * @param extra   Additional fields to be included in the response (e.g., field validation errors).
     * @return A {@link ResponseEntity} containing the error response body.
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message, Map<String, ?> extra) {
        Map<String, Object> body = new LinkedHashMap<>(); // Maintain field order
        body.put("timestamp", LocalDateTime.now());       // Timestamp for error tracking
        body.put("status", status.value());               // HTTP status code
        body.put("error", status.getReasonPhrase());      // HTTP status description
        body.put("message", message);                     // Main error message

        // Merge any additional error details (e.g., field-level validation errors)
        if (extra != null && !extra.isEmpty()) {
            body.putAll(extra);
        }

        return new ResponseEntity<>(body, status); // Return complete response
    }

    /**
     * Handles validation errors triggered by `@Valid` annotations on method arguments.
     *
     * <p>Occurs when DTO validation constraints are violated (e.g., blank fields, invalid size).</p>
     *
     * @param ex The exception containing binding result with field errors.
     * @return A response with HTTP 400 and a map of field errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());

        // Extract field errors and build a key-value map
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // Return error response with detailed field-level messages
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", Map.of("errors", errors));
    }

    /**
     * Handles cases where the request body contains malformed JSON or cannot be parsed.
     *
     * @param ex The exception with parsing details.
     * @return A response with HTTP 400 and a message indicating malformed JSON.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonParseErrors(HttpMessageNotReadableException ex) {
        logger.error("Malformed JSON: {}", ex.getMessage());

        // Inform client that the JSON input is invalid
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", null);
    }

    /**
     * Handles custom exceptions thrown during graph-related computations or algorithms.
     *
     * <p>Used when a graph analysis algorithm fails (e.g., invalid topology, null edges).</p>
     *
     * @param ex The {@link GraphAnalysisException} instance containing the error detail.
     * @return A response with HTTP 500 and the specific error message.
     */
    @ExceptionHandler(GraphAnalysisException.class)
    public ResponseEntity<Object> handleGraphAnalysisException(GraphAnalysisException ex) {
        logger.error("Graph analysis exception: {}", ex.getMessage());

        // Return internal server error with graph-specific message
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    /**
     * Handles missing required query or request parameters.
     *
     * <p>Occurs when a required @RequestParam is not supplied in the request.</p>
     *
     * @param ex The exception indicating the missing parameter.
     * @return A response with HTTP 400 and a message describing the missing parameter.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
        logger.error("Missing request parameter: {}", ex.getMessage());

        // Notify client that a required parameter is missing
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    /**
     * Handles all uncaught exceptions that do not match any specific handler above.
     *
     * <p>Acts as a fallback for unexpected or runtime exceptions.</p>
     *
     * @param ex The exception that was thrown.
     * @return A response with HTTP 500 and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: ", ex); // Log full stack trace for debugging

        // Return generic internal error to avoid exposing internal logic to client
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                null
        );
    }
}

```
**6) GraphRequest:** `src/main/java/com/example/graph/dto/GraphRequest.java`
```java
package com.example.graph.dto;

import com.example.graph.validation.ValidGraphRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the input request for graph operations.
 *
 * <p>
 * This class is used to receive input from clients via REST APIs for detecting
 * bridges and articulation points in an undirected graph. It ensures that
 * the data is well-formed and adheres to expected constraints before being
 * processed by the service layer.
 * </p>
 *
 * <p><strong>Structure:</strong></p>
 * <ul>
 *   <li><code>vertices</code>: Total number of nodes in the graph (must be ≥ 1)</li>
 *   <li><code>edges</code>: List of edges, where each edge is represented as a pair of connected vertices</li>
 * </ul>
 *
 * <p><strong>Validation Constraints:</strong></p>
 * <ul>
 *   <li><code>@Min(1)</code>: Ensures the number of vertices is at least 1.</li>
 *   <li><code>@NotNull</code> and <code>@NotEmpty</code>: Ensure the edges list is not null or empty.</li>
 *   <li><code>@ValidGraphRequest</code>: Ensures that each edge contains exactly 2 valid vertex indices (0 ≤ vertex < vertices).</li>
 * </ul>
 *
 * <p><strong>Usage Example (JSON):</strong></p>
 * <pre>
 * {
 *   "vertices": 5,
 *   "edges": [[0, 1], [1, 2], [2, 0], [1, 3], [3, 4]]
 * }
 * </pre>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> All required fields are provided and meet the validation criteria.</li>
 *   <li><strong>Fail:</strong> Any field is missing, null, malformed, or violates a validation rule.</li>
 * </ul>
 *
 * @author
 */
@Getter
@Setter
@ValidGraphRequest // Class-level validation: checks edges have valid size (2) and vertex bounds [0, vertices-1]
public class GraphRequest {

    /**
     * The number of vertices in the graph.
     * Must be greater than or equal to 1.
     */
    @Min(value = 1, message = "Number of vertices must be at least 1")
    private int vertices;

    /**
     * The list of undirected edges in the graph.
     * Each edge is represented as a list of exactly two integers: [u, v].
     *
     * <ul>
     *   <li><code>@NotNull</code>: Edges list must not be null.</li>
     *   <li><code>@NotEmpty</code>: Edges list must contain at least one edge.</li>
     *   <li><code>@NotNull</code> on inner list items: Each vertex index in each edge must be non-null.</li>
     * </ul>
     */
    @NotNull(message = "Edges list cannot be null")
    @NotEmpty(message = "Edges list cannot be empty")
    private List<List<@NotNull(message = "Edge vertex cannot be null") Integer>> edges;

}

```
**7) GraphController:** `src/main/java/com/example/graph/controller/GraphController.java`
```java
package com.example.graph.controller;

import com.example.graph.dto.GraphRequest;
import com.example.graph.service.GraphService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for graph analysis operations.
 *
 * <p><strong>Overview:</strong></p>
 * This controller exposes REST endpoints to analyze undirected graphs and detect:
 * <ul>
 *   <li><strong>Bridge Edges:</strong> Edges whose removal increases the number of connected components.</li>
 *   <li><strong>Articulation Points:</strong> Vertices whose removal increases the number of connected components.</li>
 * </ul>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Receives graph data from clients as JSON input.</li>
 *   <li>Validates the input structure using Jakarta Bean Validation annotations.</li>
 *   <li>Delegates processing to the {@link GraphService}.</li>
 *   <li>Returns analysis results in a user-friendly format.</li>
 * </ul>
 *
 * <p><strong>Base URL:</strong> <code>/graph</code></p>
 * <p><strong>Validation Handling:</strong> Managed globally via {@link com.example.graph.exception.GlobalExceptionHandler}</p>
 * <p><strong>Logging:</strong> Uses SLF4J to trace request and response data.</p>
 *
 * @author
 */
@RestController
@RequestMapping("/graph")
public class GraphController {

    // Logger to track request and response flow
    private static final Logger logger = LoggerFactory.getLogger(GraphController.class);

    // GraphService is injected via constructor
    private final GraphService graphService;

    /**
     * Constructor-based dependency injection for GraphService.
     *
     * @param graphService The service that implements graph algorithms.
     */
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    /**
     * POST endpoint to identify all bridge edges in an undirected graph.
     *
     * <p><strong>Bridge edges</strong> are those whose removal would increase the number of connected components.</p>
     *
     * @param request A valid {@link GraphRequest} object containing vertex and edge data.
     * @return A list of bridge edges in the format ["1-2", "2-3"], representing critical edges.
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Returns list of bridges if graph is valid.</li>
     *   <li><strong>Fail:</strong> Returns 400 if validation fails, 500 if internal graph error occurs.</li>
     * </ul>
     */
    @PostMapping("/bridges")
    public List<String> getBridges(@RequestBody @Valid GraphRequest request) {
        // Log the input graph for traceability
        logger.info("Received request to find bridges with vertices: {} and edges: {}",
                    request.getVertices(), request.getEdges());

        // Delegate bridge-finding logic to the service layer
        List<String> bridges = graphService.findBridges(request.getVertices(), request.getEdges());

        // Log the result of the bridge detection
        logger.info("Bridges found: {}", bridges);

        return bridges;
    }

    /**
     * POST endpoint to identify all articulation points (critical vertices) in the graph.
     *
     * <p><strong>Articulation points</strong> are vertices whose removal would increase the number of connected components.</p>
     *
     * @param request A valid {@link GraphRequest} object containing vertex and edge data.
     * @return A list of vertex indices representing articulation points.
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Returns list of articulation point indices.</li>
     *   <li><strong>Fail:</strong> Returns 400 for validation issues, 500 for graph analysis failures.</li>
     * </ul>
     */
    @PostMapping("/articulation")
    public List<Integer> getArticulationPoints(@RequestBody @Valid GraphRequest request) {
        // Log incoming request for audit/debugging
        logger.info("Received request to find articulation points with vertices: {} and edges: {}",
                    request.getVertices(), request.getEdges());

        // Perform articulation point analysis
        List<Integer> articulationPoints = graphService.findArticulationPoints(
                request.getVertices(), request.getEdges());

        // Log the output result
        logger.info("Articulation points found: {}", articulationPoints);

        return articulationPoints;
    }
}

```
**8) GraphRequestValidator:** `src/main/java/com/example/graph/validation/GraphRequestValidator.java`
```javapackage com.example.graph.validation;

import com.example.graph.dto.GraphRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Custom validator for {@link GraphRequest}.
 *
 * <p>
 * Ensures that:
 * <ul>
 *   <li>The number of vertices is ≥ 1 (already enforced via annotation).</li>
 *   <li>Each edge is a non-null list of exactly two integers.</li>
 *   <li>Each vertex index in an edge is within the valid range: [0, vertices - 1].</li>
 * </ul>
 * </p>
 */
public class GraphRequestValidator implements ConstraintValidator<ValidGraphRequest, GraphRequest> {

    private static final Logger logger = LoggerFactory.getLogger(GraphRequestValidator.class);

    @Override
    public boolean isValid(GraphRequest request, ConstraintValidatorContext context) {
        // Null request — let other validators handle it
        if (request == null) {
            logger.warn("GraphRequest is null. Skipping custom validation.");
            return true;
        }

        logger.debug("Starting validation for GraphRequest: vertices={}, edges={}",
                     request.getVertices(), request.getEdges());

        int vertices = request.getVertices();
        List<List<Integer>> edges = request.getEdges();

        // Ensure edge list is not null or empty
        if (edges == null || edges.isEmpty()) {
            logger.error("Validation failed: Edges list is null or empty.");
            return false;
        }

        int edgeIndex = 0;
        for (List<Integer> edge : edges) {
            logger.debug("Validating edge[{}]: {}", edgeIndex, edge);

            // Check if the edge itself is null
            if (edge == null) {
                logger.error("Validation failed: Edge[{}] is null.", edgeIndex);
                return false;
            }

            // Each edge must contain exactly 2 vertices
            if (edge.size() != 2) {
                logger.error("Validation failed: Edge[{}] does not contain exactly two vertices.", edgeIndex);
                return false;
            }

            int vertexIndex = 0;
            for (Integer vertex : edge) {
                if (vertex == null) {
                    logger.error("Validation failed: Vertex in edge[{}][{}] is null.", edgeIndex, vertexIndex);
                    return false;
                }
                if (vertex < 0 || vertex >= vertices) {
                    logger.error("Validation failed: Vertex in edge[{}][{}] = {} is out of bounds (0 to {}).",
                                 edgeIndex, vertexIndex, vertex, vertices - 1);
                    return false;
                }
                logger.debug("Vertex[{}] in edge[{}] is valid: {}", vertexIndex, edgeIndex, vertex);
                vertexIndex++;
            }

            edgeIndex++;
        }

        logger.info("GraphRequest validation passed.");
        return true;
    }
}


```
**9) ValidGraphRequest:** `src/main/resources/validation/ValidGraphRequest.java`
```javapackage com.example.graph.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom constraint annotation for validating a {@link com.example.graph.dto.GraphRequest}.
 *
 * <p><strong>Purpose:</strong></p>
 * Ensures that:
 * <ul>
 *   <li>Each edge in the graph contains exactly two vertices.</li>
 *   <li>Each vertex index is non-null and within the range [0, vertices - 1].</li>
 * </ul>
 *
 * <p><strong>How it works:</strong></p>
 * This annotation delegates validation to the {@link GraphRequestValidator} class,
 * which implements the logic for checking structure and range of vertex indices.
 *
 * <p><strong>Usage:</strong></p>
 * <pre>
 * {@code
 * @ValidGraphRequest
 * public class GraphRequest {
 *     ...
 * }
 * }
 * </pre>
 *
 * <p><strong>Target:</strong> Classes (i.e., DTOs)</p>
 * <p><strong>Retention:</strong> Runtime, required for runtime validation.</p>
 *
 * @see com.example.graph.validation.GraphRequestValidator
 * @see jakarta.validation.ConstraintValidator
 */
@Documented
@Constraint(validatedBy = GraphRequestValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGraphRequest {

    /**
     * Default error message if validation fails.
     */
    String message() default "Invalid graph request: edge vertices must be within range and non-negative.";

    /**
     * Allows specification of validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payloads can be used by clients to assign custom payload objects to a constraint.
     */
    Class<? extends Payload>[] payload() default {};
}


```
**10) application.properties:** `src/main/resources/application.properties`
```properties
spring.application.name=graph-integrity-app
server.port=8080
```

**11) Maven:** `pom.xml`
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
  <artifactId>graph</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>graph-integrity-app</name>
  <description>Build a Spring Boot application that detects bridges and articulation points in a graph.</description>
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

**12) Main Application:** `src/test/java/com/example/graph/GraphIntegrityAppTest.java`
```java
package com.example.graph;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit and integration test for {@link GraphIntegrityAppTest}.
 *
 * <p>
 * <strong>Purpose:</strong> Ensures that the Spring Boot application context loads successfully and that the main method can be invoked without errors.
 * </p>
 *
 * <p>
 * This test verifies that the application's configuration and bean definitions are correct and that no exceptions occur during context initialization.
 * </p>
 *
 */
@SpringBootTest
public class GraphIntegrityAppTest {

    /**
     * Tests that the Spring Boot application context loads without throwing any exceptions.
     *
     * <p>
     * <strong>GIVEN:</strong> The Spring Boot application is configured properly.
     * <br>
     * <strong>WHEN:</strong> The application context is started.
     * <br>
     * <strong>THEN:</strong> No exceptions are thrown, indicating that the application has started successfully.
     * </p>
     */
    @Test
    public void contextLoads() {
        // WHEN & THEN: Assert that no exception is thrown when loading the Spring Boot application context.
        assertDoesNotThrow(() -> {
            // The SpringBootTest annotation will automatically load the application context.
            // If context initialization fails, this assertion will fail.
        }, "Spring Boot context should load without throwing exceptions");
    }
}

```
**13) GraphServiceTest:** `src/test/java/com/example/graph/service/GraphServiceTest.java`
```java
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

```

**14) GraphRequestTest:** `src/test/java/com/example/graph/dto/GraphRequestTest.java`
```java
package com.example.graph.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link GraphRequest}.
 *
 * <p>
 * This class contains unit tests to verify that the validation annotations applied to
 * the GraphRequest DTO work as expected using Jakarta Bean Validation.
 * </p>
 *
 * <p>
 * <strong>Validation Rules Tested:</strong>
 * <ul>
 *   <li>{@code @Min(1)} on {@code vertices}</li>
 *   <li>{@code @NotNull} on {@code edges} and inner vertex values</li>
 *   <li>{@code @NotEmpty} on {@code edges}</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 */
public class GraphRequestTest {

    /**
     * A shared Validator instance for performing constraint validation.
     */
    private static Validator validator;

    /**
     * Sets up the validator instance before all test cases run.
     * This uses the Jakarta ValidatorFactory to build a validator.
     */
    @BeforeAll
    public static void setupValidatorInstance() {
        // Build the default ValidatorFactory and retrieve the Validator instance.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests a valid {@link GraphRequest} with 3 vertices and valid edges.
     *
     * <p>
     * <strong>Expectation:</strong> No constraint violations should occur.
     * </p>
     */
    @Test
    public void testValidGraphRequest_NoViolations() {
        // GIVEN: A valid GraphRequest with vertices set to 3 and a non-empty list of edges.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        // WHEN: Validate the request using the shared validator instance.
        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);

        // THEN: Assert that there are no constraint violations.
        assertTrue(violations.isEmpty(), "There should be no validation errors for a valid input");
    }

    /**
     * Tests {@code vertices = 0}, which violates {@code @Min(1)}.
     *
     * <p>
     * <strong>Expectation:</strong> Validation should fail on the 'vertices' field.
     * </p>
     */
    @Test
    public void testInvalidGraphRequest_ZeroVertices_ShouldFailMinValidation() {
        // GIVEN: A GraphRequest with vertices set to 0 (invalid) and a valid edges list.
        GraphRequest request = new GraphRequest();
        request.setVertices(0);
        request.setEdges(List.of(List.of(0, 1)));

        // WHEN: Validate the request.
        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);

        // THEN: Assert that validation fails and a violation is reported on the 'vertices' field.
        assertFalse(violations.isEmpty(), "Validation should fail for vertices < 1");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("vertices")),
                   "Expected violation on the 'vertices' field");
    }

    /**
     * Tests with {@code edges = null}, violating {@code @NotNull}.
     *
     * <p>
     * <strong>Expectation:</strong> Validation should fail on the 'edges' field.
     * </p>
     */
    @Test
    public void testInvalidGraphRequest_NullEdges_ShouldFailNotNullValidation() {
        // GIVEN: A GraphRequest with a valid vertices value but with edges set to null.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(null);

        // WHEN: Validate the request.
        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);

        // THEN: Assert that validation fails and a violation is reported on the 'edges' field.
        assertFalse(violations.isEmpty(), "Validation should fail for null edges list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edges")),
                   "Expected violation on the 'edges' field");
    }

    /**
     * Tests with an empty list of edges, violating {@code @NotEmpty}.
     *
     * <p>
     * <strong>Expectation:</strong> Validation should fail on the 'edges' field.
     * </p>
     */
    @Test
    public void testInvalidGraphRequest_EmptyEdgesList_ShouldFailNotEmptyValidation() {
        // GIVEN: A GraphRequest with vertices set to a valid value but with an empty edges list.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(new ArrayList<>()); // Empty list

        // WHEN: Validate the request.
        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);

        // THEN: Assert that validation fails and a violation is reported on the 'edges' field.
        assertFalse(violations.isEmpty(), "Validation should fail for empty edges list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edges")),
                   "Expected violation on the 'edges' field");
    }
}

```
## After the first iteration, the overall test coverage was 88%. To improve this, additional test cases—including those in GraphControllerIntegrationTest , GraphControllerTest , GlobalExceptionHandlerTest and GraphControllerTest will be introduced to further increase the test coverage percentage.


**15) GraphControllerIntegrationTest:** `src/test/java/com/example/graph/controller/GraphControllerIntegrationTest.java`
```java

package com.example.graph.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link GraphController}, specifically for edge case scenarios.
 *
 * <p>This class uses parameterized tests to verify that the controller behaves as expected when provided with special
 * or boundary graph inputs, such as:</p>
 * <ul>
 *   <li>A graph with a single vertex and no edges</li>
 *   <li>A graph with a self-loop edge</li>
 *   <li>A fully connected graph (clique) which has no articulation points or bridges</li>
 *   <li>A simple path graph where intermediate nodes are articulation points</li>
 *   <li>A star-shaped graph with one central articulation point</li>
 *   <li>A graph with an out-of-bounds vertex reference</li>
 *   <li>A graph with duplicate edges</li>
 * </ul>
 *
 * <p>Each test validates both the HTTP response status and, where applicable, the expected response body.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // For sending simulated HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // For converting request maps to JSON

    /**
     * Represents a single test case used in the parameterized test.
     */
    static class GraphTestCase {
        final String name;               // Description of the test
        final Map<String, Object> request;  // The graph input request
        final String endpoint;          // The controller endpoint to call
        final int expectedStatus;       // Expected HTTP status
        final String expectedJson;      // Expected JSON response (can be null if only checking status)

        GraphTestCase(String name, Map<String, Object> request, String endpoint, int expectedStatus, String expectedJson) {
            this.name = name;
            this.request = request;
            this.endpoint = endpoint;
            this.expectedStatus = expectedStatus;
            this.expectedJson = expectedJson;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Supplies the edge case scenarios for the parameterized test.
     */
    static Stream<GraphTestCase> graphEdgeCasesProvider() {
        return Stream.of(

                // A graph with a single vertex and no edges. Should return 200 with empty response.
                new GraphTestCase(
                        "Single vertex, no edges (via self-loop)",
                        Map.of("vertices", 1, "edges", List.of(List.of(0, 0))), // Added self-loop to avoid empty edge list
                        "/graph/bridges",
                        200,
                        "[]" // Still expecting no bridges
                ),


                // A self-loop edge should be ignored in bridge detection.
                new GraphTestCase(
                        "Self-loop edge",
                        Map.of("vertices", 1, "edges", List.of(List.of(0, 0))),
                        "/graph/bridges",
                        200,
                        "[]"
                ),

                // Fully connected graph with 4 nodes. No articulation points should be detected.
                new GraphTestCase(
                        "Fully connected graph",
                        Map.of("vertices", 4, "edges", List.of(
                                List.of(0, 1), List.of(0, 2), List.of(0, 3),
                                List.of(1, 2), List.of(1, 3), List.of(2, 3))),
                        "/graph/articulation",
                        200,
                        "[]"
                ),

                // A line graph: nodes 1, 2, 3 are articulation points.
                new GraphTestCase(
                        "Simple path with articulation points",
                        Map.of("vertices", 5, "edges", List.of(
                                List.of(0, 1), List.of(1, 2), List.of(2, 3), List.of(3, 4))),
                        "/graph/articulation",
                        200,
                        "[1,2,3]"
                ),

                // A star-shaped graph: only the center node (0) is an articulation point.
                new GraphTestCase(
                        "Star topology with central articulation point",
                        Map.of("vertices", 5, "edges", List.of(
                                List.of(0, 1), List.of(0, 2), List.of(0, 3), List.of(0, 4))),
                        "/graph/articulation",
                        200,
                        "[0]"
                ),

                // An edge includes a vertex index (4) that is outside the valid range [0, 2].
                new GraphTestCase(
                        "Edge with out-of-bound vertex",
                        Map.of("vertices", 3, "edges", List.of(List.of(0, 4))),
                        "/graph/bridges",
                        400,
                        null // Validation should fail, so no need to verify response body
                ),

                // Duplicate edges should not break the algorithm or appear more than once.
                new GraphTestCase(
                        "Duplicate edges",
                        Map.of("vertices", 3, "edges", List.of(
                                List.of(0, 1), List.of(1, 2), List.of(0, 1))),
                        "/graph/bridges",
                        200,
                        "[\"1-2\",\"0-1\"]"
                )
        );
    }

    /**
     * Executes a parameterized integration test for each graph edge case.
     *
     * <p>Each test performs a POST request to the specified endpoint using the request payload,
     * then validates the HTTP status code and optionally the JSON response body.</p>
     *
     * @param testCase The test case input and expected result
     * @throws Exception if request processing fails
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("graphEdgeCasesProvider")
    @DisplayName("Parameterized edge case test for /graph endpoints")
    void testGraphEdgeCases(GraphTestCase testCase) throws Exception {
        // Perform the POST request to the specified endpoint with given input
        var resultActions = mockMvc.perform(post(testCase.endpoint)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(testCase.request)))
                                   .andExpect(status().is(testCase.expectedStatus)); // Assert expected HTTP status

        // If expected JSON is provided, assert the response body matches exactly
        if (testCase.expectedJson != null) {
            resultActions
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // Ensure response is JSON-compatible
                    .andExpect(content().encoding("UTF-8")) // Optional: Assert encoding is UTF-8 (default for JSON)
                    .andExpect(header().string("Content-Type", "application/json")) // Check exact header
                    .andExpect(content().json(testCase.expectedJson, true)); // Strict JSON equality (no extra/missing fields)
                  }
    }
}

```
**16) GlobalExceptionHandlerTest:** `src/test/java/com/example/graph/exception/GlobalExceptionHandlerTest.java`
```java
package com.example.graph.exception;

import com.example.graph.dto.GraphRequest;
import com.example.graph.controller.GraphController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link GlobalExceptionHandler} within the context of {@link GraphController}.
 *
 * <p>
 * This test class verifies that application exceptions are correctly mapped to standardized HTTP responses.
 * The tests validate:
 * <ul>
 *     <li>Validation failures return 400 Bad Request with detailed error messages</li>
 *     <li>Malformed JSON input results in 400 Bad Request with a specific error message</li>
 *     <li>Custom exceptions (e.g., {@link GraphAnalysisException}) return 500 Internal Server Error with the exception's message</li>
 *     <li>Unhandled exceptions return a generic 500 Internal Server Error with fallback messaging</li>
 * </ul>
 * </p>
 *
 */
@WebMvcTest(controllers = GraphController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {GraphController.class, GlobalExceptionHandler.class})
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc; // Used to simulate HTTP requests in integration tests

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to JSON strings for request payloads

    @MockBean
    private com.example.graph.service.GraphService graphService; // Mocks the GraphService dependency

    /**
     * Verifies that validation errors on a {@link GraphRequest} result in a structured 400 Bad Request.
     *
     * <p>
     * <strong>Scenario:</strong> An invalid GraphRequest (with missing required fields) is sent.
     * The endpoint should return HTTP 400 along with a message indicating that validation failed and include error details.
     * </p>
     *
     * @throws Exception if request processing fails.
     */
    @Test
    @DisplayName("Should return 400 for invalid GraphRequest (validation error)")
    void testHandleValidationErrors() throws Exception {
        // GIVEN: Create an invalid GraphRequest instance with missing required fields.
        GraphRequest invalidRequest = new GraphRequest(); // Required fields are not set

        // WHEN & THEN: Perform a POST request to /graph/bridges and expect HTTP 400 with a validation error message.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"))
               .andExpect(jsonPath("$.errors").exists());
    }

    /**
     * Verifies that malformed JSON input triggers a 400 Bad Request response.
     *
     * <p>
     * <strong>Scenario:</strong> A POST request is sent with improperly formatted JSON (e.g., missing closing brackets).
     * The endpoint should detect the JSON parsing error and return HTTP 400 with a message indicating a malformed JSON request.
     * </p>
     *
     * @throws Exception if request processing fails.
     */
    @Test
    @DisplayName("Should return 400 for malformed JSON input")
    void testHandleJsonParseErrors() throws Exception {
        // GIVEN: Define a malformed JSON string (missing closing bracket).
        String malformedJson = "{\"vertices\": 5, \"edges\": [[0,1], [1,2]"; // Malformed JSON

        // WHEN & THEN: Perform a POST request to /graph/bridges using the malformed JSON and expect HTTP 400.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    /**
     * Verifies that a custom {@link GraphAnalysisException} thrown by the service layer is mapped to a 500 Internal Server Error.
     *
     * <p>
     * <strong>Scenario:</strong> The GraphService throws a GraphAnalysisException when processing a valid GraphRequest.
     * The endpoint should catch the exception and return HTTP 500 with the exception's message.
     * </p>
     *
     * @throws Exception if request processing fails.
     */
    @Test
    @DisplayName("Should return 500 for custom GraphAnalysisException")
    void testHandleGraphAnalysisException() throws Exception {
        // GIVEN: Create a valid GraphRequest with proper vertices and edges.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        // AND: Configure the mocked GraphService to throw a GraphAnalysisException when findBridges is called.
        doThrow(new GraphAnalysisException("Failed to analyze graph"))
                .when(graphService).findBridges(request.getVertices(), request.getEdges());

        // WHEN & THEN: Perform a POST request to /graph/bridges and expect HTTP 500 with the exception's message.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").value("Failed to analyze graph"));
    }

    /**
     * Verifies that unhandled runtime exceptions are caught and result in a generic 500 Internal Server Error.
     *
     * <p>
     * <strong>Scenario:</strong> The GraphService throws a generic RuntimeException when processing a valid GraphRequest.
     * The endpoint should return HTTP 500 with a generic error message indicating that an unexpected error occurred.
     * </p>
     *
     * @throws Exception if request processing fails.
     */
    @Test
    @DisplayName("Should return 500 for unexpected exception")
    void testHandleGenericException() throws Exception {
        // GIVEN: Create a valid GraphRequest with proper vertices and edges.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        // AND: Configure the mocked GraphService to throw a RuntimeException when findBridges is called.
        doThrow(new RuntimeException("Unexpected error"))
                .when(graphService).findBridges(request.getVertices(), request.getEdges());

        // WHEN & THEN: Perform a POST request to /graph/bridges and expect HTTP 500 with a generic error message.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").value("An unexpected error occurred. Please try again later."));
    }
}

```
**17) GraphControllerTest:** `src/test/java/com/example/graph/exception/GlobalExceptionHandlerTest.java`
```java
package com.example.graph.controller;

import com.example.graph.dto.GraphRequest;
import com.example.graph.service.GraphService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link com.example.graph.controller.GraphController} class.
 *
 * <p>
 * This class tests the REST endpoints exposed by the GraphController, which are responsible for:
 * <ul>
 *   <li>Detecting bridge edges in an undirected graph using the /graph/bridges endpoint.</li>
 *   <li>Detecting articulation points in an undirected graph using the /graph/articulation endpoint.</li>
 * </ul>
 * The tests use Spring Boot's {@code @WebMvcTest} to initialize the controller in isolation,
 * along with {@code MockMvc} for simulating HTTP requests and Mockito for mocking the service layer.
 * </p>
 *
 * <p>
 * <strong>Test Cases Covered:</strong>
 * <ul>
 *   <li><strong>Valid Request for Bridges:</strong> Ensures that a valid GraphRequest returns the expected list of bridge edges.</li>
 *   <li><strong>Valid Request for Articulation Points:</strong> Ensures that a valid GraphRequest returns the expected list of articulation point indices.</li>
 *   <li><strong>Validation Failure:</strong> Ensures that requests missing required fields (e.g., vertices) result in a 400 Bad Request with a proper error message.</li>
 * </ul>
 * </p>
 *
 * @author
 */
@WebMvcTest(GraphController.class)
public class GraphControllerTest {

    @Autowired
    private MockMvc mockMvc; // Used to perform HTTP requests in tests

    @MockBean
    private GraphService graphService; // Mocking GraphService dependency to isolate controller tests

    @Autowired
    private ObjectMapper objectMapper; // Used to convert Java objects to JSON strings

    /**
     * Tests the /graph/bridges endpoint with a valid GraphRequest.
     *
     * <p>
     * <strong>Scenario:</strong>
     * A valid request with 5 vertices and a list of edges is sent.
     * The GraphService is expected to return a list of bridge edges (e.g., "3-4" and "1-3").
     * </p>
     *
     * @throws Exception if the request processing fails.
     */
    @Test
    @DisplayName("Test getBridges endpoint - valid request")
    public void testGetBridges_ValidRequest() throws Exception {
        // Given: Define vertices, edges, and create a valid GraphRequest object.
        int vertices = 5;
        List<List<Integer>> edges = Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(1, 2),
                Arrays.asList(2, 0),
                Arrays.asList(1, 3),
                Arrays.asList(3, 4)
        );
        GraphRequest request = new GraphRequest();
        request.setVertices(vertices);
        request.setEdges(edges);

        // Expected response from the service layer
        List<String> bridges = Arrays.asList("3-4", "1-3");

        // When: Mock the GraphService behavior for findBridges.
        Mockito.when(graphService.findBridges(vertices, edges)).thenReturn(bridges);

        // Then: Perform POST request to /graph/bridges and validate the response.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$", containsInAnyOrder("3-4", "1-3")));
    }

    /**
     * Tests the /graph/articulation endpoint with a valid GraphRequest.
     *
     * <p>
     * <strong>Scenario:</strong>
     * A valid request with 5 vertices and a list of edges is sent.
     * The GraphService is expected to return a list of articulation point indices (e.g., 1 and 3).
     * </p>
     *
     * @throws Exception if the request processing fails.
     */
    @Test
    @DisplayName("Test getArticulationPoints endpoint - valid request")
    public void testGetArticulationPoints_ValidRequest() throws Exception {
        // Given: Define vertices, edges, and create a valid GraphRequest object.
        int vertices = 5;
        List<List<Integer>> edges = Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(1, 2),
                Arrays.asList(2, 0),
                Arrays.asList(1, 3),
                Arrays.asList(3, 4)
        );
        GraphRequest request = new GraphRequest();
        request.setVertices(vertices);
        request.setEdges(edges);

        // Expected response from the service layer
        List<Integer> articulationPoints = Arrays.asList(1, 3);

        // When: Mock the GraphService behavior for findArticulationPoints.
        Mockito.when(graphService.findArticulationPoints(vertices, edges)).thenReturn(articulationPoints);

        // Then: Perform POST request to /graph/articulation and validate the response.
        mockMvc.perform(post("/graph/articulation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0]").value(1))
               .andExpect(jsonPath("$[1]").value(3));
    }

    /**
     * Tests the /graph/bridges endpoint for a validation failure scenario.
     *
     * <p>
     * <strong>Scenario:</strong>
     * A request with missing vertices (null) is sent to trigger a validation error.
     * The expected response is a 400 Bad Request with a validation error message.
     * </p>
     *
     * @throws Exception if the request processing fails.
     */
    @Test
    @DisplayName("Test getBridges endpoint - validation failure (missing vertices)")
    public void testGetBridges_ValidationFailure_MissingVertices() throws Exception {
        // Given: Create a GraphRequest with missing vertices to simulate a validation error.
        GraphRequest request = new GraphRequest();
        // vertices not set (null), only edges are provided.
        request.setEdges(Collections.singletonList(Arrays.asList(0, 1)));

        // Then: Perform POST request and validate that a 400 status and validation message is returned.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}

```
**18) GraphControllerTest:** `src/test/java/com/example/graph/exception/GlobalExceptionHandlerTest.java`
```java
package com.example.graph.validation;

import com.example.graph.dto.GraphRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link GraphRequestValidator}.
 *
 * <p><strong>Premise:</strong> The validator enforces that:
 * <ul>
 *     <li>Each edge must contain exactly 2 vertices</li>
 *     <li>Each vertex must be in range [0, vertices - 1]</li>
 *     <li>Vertices must not be null or negative</li>
 * </ul>
 *
 * <p><strong>Assertions:</strong> The validation result matches expected validity.
 *
 * <p><strong>Pass/Fail Conditions:</strong>
 * <ul>
 *     <li><strong>Pass:</strong> All edge lists are valid and vertices are within bounds.</li>
 *     <li><strong>Fail:</strong> If any edge is null, malformed, or references invalid vertex indices.</li>
 * </ul>
 */
class GraphRequestValidatorTest {


    // Create a validator instance and mock context for testing
    private final GraphRequestValidator validator = new GraphRequestValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    @DisplayName("Valid graph with correct edge format and in-range vertices")
    void testValidGraph () {
        // Setup: valid graph with 4 vertices and 2 well-formed edges
        GraphRequest request = new GraphRequest();
        request.setVertices(4);
        request.setEdges(List.of(List.of(0 , 1), List.of(2 , 3)));

        // Expect the validator to return true
        assertTrue(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge contains more than two vertices")
    void testEdgeWithTooManyVertices () {
        // Setup: edge has 3 vertices instead of 2
        GraphRequest request = new GraphRequest();
        request.setVertices(4);
        request.setEdges(List.of(List.of(0 , 1 , 2)));

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge contains only one vertex")
    void testEdgeWithOneVertex () {
        // Setup: edge has only 1 vertex
        GraphRequest request = new GraphRequest();
        request.setVertices(4);
        request.setEdges(List.of(List.of(0)));

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge contains null vertex")
    void testEdgeWithNullVertex () {
        // Setup: one vertex in the edge is null
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        List<List<Integer>> edges = new ArrayList<>();
        edges.add(Arrays.asList(0 , null));
        request.setEdges(edges);

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Vertex index out of bounds (negative)")
    void testVertexNegative () {
        // Setup: one vertex is negative
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(-1 , 1)));

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Vertex index out of bounds (greater than max)")
    void testVertexTooLarge () {
        // Setup: one vertex index exceeds the allowed maximum
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0 , 4))); // Max index should be 2

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge list is null")
    void testEdgesListNull () {
        // Setup: edge list itself is null
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(null);

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge list is empty")
    void testEdgesListEmpty () {
        // Setup: edge list is empty
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of());

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge object is null in list")
    void testEdgeNullInList () {
        // Setup: edge list contains a null edge object
        GraphRequest request = new GraphRequest();
        request.setVertices(3);

        List<List<Integer>> edges = new ArrayList<>();
        edges.add(null);
        request.setEdges(edges);

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("GraphRequest is null")
    void testNullGraphRequest () {
        // Setup: null input request
        // Expect true, as per convention (Bean Validation spec handles nulls as valid unless explicitly disallowed)
        assertTrue(validator.isValid(null, context));
    }
}

```
# After the second iteration, test coverage increased to 99%.
**Result:** Total line coverage is 99%


# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml contains the necessary Spring Boot dependencies (e.g., Spring Web, Spring Boot Starter Test,  etc.). Confirm that the Java version is set to 17 (or adjust accordingly) and any needed plugins  are present.
3. ** Application Properties:** Configure `server.port` in application.properties (or application.yml), for example:
```properties
spring.application.name=graph-integrity-app
server.port=8080
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

Below are runnable cURL commands along with sample success and error responses for the **Graph Bridges and Articulation Points Detection** endpoints.




Detect Bridges – Valid Request
```bash
curl -X POST http://localhost:8080/graph/bridges \
  -H "Content-Type: application/json" \
  -d '{"vertices":5,"edges":[[0,1],[1,2],[2,0],[1,3],[3,4]]}'

```



 Missing Vertices
```bash
curl -X POST http://localhost:8080/graph/bridges \
  -H "Content-Type: application/json" \
  -d '{"edges":[[0,1],[1,2]]}'
```



Malformed JSON
```bash
curl -X POST http://localhost:8080/graph/bridges \
  -H "Content-Type: application/json" \
  -d '{"vertices":4,"edges":[[0,1],[1,2]'
```



Detect Articulation Points – Valid Request
```bash
curl -X POST http://localhost:8080/graph/articulation \
  -H "Content-Type: application/json" \
  -d '{"vertices":5,"edges":[[0,1],[1,2],[2,0],[1,3],[3,4]]}'
```



Empty Edges
```bash
curl -X POST http://localhost:8080/graph/articulation \
  -H "Content-Type: application/json" \
  -d '{"vertices":4,"edges":[]}'
```




# **Time and Space Complexity:**

- **Time Complexity:**  
  The detection of both bridges and articulation points in an undirected graph is performed using **Depth-First Search (DFS)**, resulting in:
  - **O(V + E)** per DFS traversal,  
    where **V** is the number of vertices and **E** is the number of edges.
  - This complexity is optimal for sparse and dense graphs alike, as it guarantees linear time processing relative to input size.

- **Space Complexity:**
  - **O(V + E)** for storing the adjacency list.
  - **O(V)** for auxiliary arrays like `disc[]`, `low[]`, `visited[]`, `ap[]`, `parent[]`, etc.
  - Therefore, total space complexity is **O(V + E)**.



# **Conclusion**

The **Graph Integrity App**, built with **Spring Boot**, delivers efficient and scalable detection of graph **bridges** and **articulation points** through a REST API interface. Leveraging the optimal **DFS-based Tarjan's algorithm**, the system achieves linear-time performance **(O(V + E))**, which is ideal for real-time graph integrity validation in network topologies, dependency chains, or infrastructure graphs.

The solution is further strengthened with:
- **Robust input validation** using Jakarta Bean Validation,
- **Comprehensive logging** and error handling,
- **Unit and integration testing** with JUnit and MockMvc.

This makes the application suitable for academic, enterprise, and analytical use cases that demand both **correctness** and **performance** in graph structural analysis.



# ** Iteration**


First Iteration: https://drive.google.com/file/d/1k5aWK_9uDu8GKvd5dCHPl24LrUv3JI5i/view?usp=drive_link
First Iteration: https://drive.google.com/file/d/1FoUCCNkvL8b-V59H3SjpqOGzpAY5UeZj/view?usp=drive_link
Code Download:https://drive.google.com/file/d/13r91CiKdWESVm379HkGiZJ9XDSP7Wdbe/view?usp=drive_link
