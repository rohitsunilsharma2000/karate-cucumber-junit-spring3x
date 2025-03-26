**Metadata**

**Programming Language:** Java

**L1 Taxonomy:** Complete Implementations

**L2 Taxonomy:** Social Media Application

**Use Case:**  Graph Integrity Analysis Platform for validating critical connectivity in infrastructure, network systems, and dependency graphs.

Organizations need to identify vulnerabilities in their graph-based systems—such as communication networks, software component dependencies, or transport grids. This application detects bridges (critical edges) and articulation points (critical nodes) that, if removed, would disconnect the graph, potentially disrupting system integrity.


**Use Case Type:** Core

**Task Difficulty:** Hard

# **Prompt**

**Title:** Spring Boot Graph Analysis Service — Bridge & Articulation Point Detection

**High-Level Description:**  
You need to build a Spring Boot application that analyzes undirected graphs to detect **bridges** (critical edges) and **articulation points** (critical vertices). The application provides REST endpoints to receive input in the form of vertex and edge lists and responds with the results of the analysis. The solution includes input validation, service layer logic using DFS traversal, structured logging, exception handling, and security configuration. It is designed to be modular, testable, and extendable for more graph algorithms in the future.

---

**Key Features:**

### 1. **Project Structure & Setup**

- Use Spring Initializr (https://start.spring.io/) to generate the project.


### 2. **Graph Input DTO & Validation**

- `GraphRequest.java`: DTO used in REST API input
- Fields:
   - `int vertices` (must be ≥ 1)
   - `List<List<Integer>> edges` (cannot be null or empty)
- Uses annotations like `@Min`, `@NotNull`, and `@NotEmpty` for validation.



### 3. **Service Layer: DFS-based Graph Analysis**

- `GraphService.java` implements:
   - `findBridges(int vertices, List<List<Integer>> edges)`
   - `findArticulationPoints(int vertices, List<List<Integer>> edges)`
- Depth First Search (DFS) used with discovery/low time tracking.
- Logs every DFS step using SLF4J with levels: `INFO`, `DEBUG`.



### 4. **Controller Layer: Graph Endpoints**

- `GraphController.java` defines two REST endpoints:
   - `POST /graph/bridges` → returns a list of bridges (`["1-3", "3-4"]`)
   - `POST /graph/articulation` → returns a list of articulation points (`[1, 3]`)
- Input validated with `@Valid`.
- Logs each request and result.



### 5. **Security Configuration**

- `SecurityConfig.java`:
   - Disables CSRF (for dev)
   - Enables CORS for localhost:3000
   - Allows all endpoints (`/**`)
   - Adds basic HTTP authentication
   - Defines in-memory user: `user:password` (no encoding)



### 6. **Exception Handling**

- `MaxFlowException.java` for custom graph errors.
- Recommended: Implement `@ControllerAdvice` for centralized error handling (e.g., validation errors, bad input).



### 7. **Testing**

- ✅ `GraphServiceTest`  
  Tests core DFS logic for bridges and articulation points.

- ✅ `GraphControllerIntegrationTest`  
  Tests REST endpoints using `MockMvc`, covering:
   - Valid requests
   - Invalid (validation-failing) requests

- ✅ `GraphRequestTest`  
  JUnit + Validator test of `GraphRequest` constraints.

- ✅ `SecurityConfigTest`  
  Verifies:
   - CORS config
   - UserDetailsService creation
   - Security filter chain setup

- ✅ `MaxFlowExceptionTest`  
  Checks proper exception message propagation.

- ✅ `GraphApplicationTest` and `JohnsonApplicationTest`  
  Confirms Spring Boot context loads and `main()` executes without issues.


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


### **Goal:**
A fully testable and modular Spring Boot microservice capable of detecting graph bridges and articulation points via REST APIs, complete with security, logging, and input validation. Ideal for integration in larger platforms needing graph-based analytics or structural diagnostics.

---
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
|   |               |-- config
|   |               |   `-- SecurityConfig.java
|   |               |-- controller
|   |               |   `-- GraphController.java
|   |               |-- dto
|   |               |   `-- GraphRequest.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   `-- MaxFlowException.java
|   |               `-- service
|   |                   `-- GraphService.java
|   `-- resources
`-- test
    `-- java
        `-- com
            `-- example
                `-- graph
                    |-- GraphIntegrityAppTest.java
                    |-- config
                    |   `-- SecurityConfigTest.java
                    |-- controller
                    |   `-- GraphControllerIntegrationTest.java
                    |-- dto
                    |   `-- GraphRequestTest.java
                    |-- exception
                    |   |-- GraphServiceTest.java
                    |   `-- MaxFlowExceptionTest.java
                    `-- service
                        `-- GraphServiceTest.java


```

**2) Main Application:** src/main/java/com/example/graph/GraphIntegrityApp.java
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
 * @since 2025-03-26
 */
@SpringBootApplication
public class GraphIntegrityApp {
    private static final Logger logger = LoggerFactory.getLogger(GraphIntegrityApp.class);

    /**
     * The main method that launches the Graph Integrity Application.
     *
     * <p>
     * Logs startup initiation and completion.
     * </p>
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        logger.info("Starting Graph Application...");
        SpringApplication.run(GraphIntegrityApp.class, args);
        logger.info("Graph Application started successfully.");
    }
}

```
**3) GraphService:** src/main/java/com/example/graph/service/GraphService.java
```java
package com.example.graph.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for analyzing graph integrity by detecting critical elements like
 * bridges and articulation points in an undirected graph.
 *
 * <p>
 * This service implements Depth First Search (DFS)-based algorithms for:
 * <ul>
 *   <li><strong>Bridge Detection:</strong> Identifying edges which, if removed, would increase the number of connected components.</li>
 *   <li><strong>Articulation Point Detection:</strong> Identifying vertices which, if removed, would increase the number of connected components.</li>
 * </ul>
 * </p>
 *
 * <p>
 * It supports the analysis of any undirected graph represented by an edge list and a number of vertices.
 * </p>
 *
 * <p><strong>Logging:</strong> SLF4J is used to log method executions, DFS traversal details, and outcomes at DEBUG and INFO levels.</p>
 *
 * @author
 * @since 2025-03-26
 */
@Service
public class GraphService {
    private static final Logger logger = LoggerFactory.getLogger(GraphService.class);
    private int time;

    /**
     * Detects all bridges in an undirected graph.
     *
     * @param vertices the number of vertices in the graph (indexed from 0 to vertices - 1)
     * @param edges    a list of undirected edges, where each edge is a list of two integers
     * @return a list of string representations of bridge edges (e.g., "1-3")
     */
    public List<String> findBridges(int vertices, List<List<Integer>> edges) {
        logger.debug("Initializing bridge detection with {} vertices and edges: {}", vertices, edges);
        time = 0;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

        for (List<Integer> edge : edges) {
            adj.get(edge.get(0)).add(edge.get(1));
            adj.get(edge.get(1)).add(edge.get(0));
        }
        logger.debug("Adjacency list created: {}", adj);

        boolean[] visited = new boolean[vertices];
        int[] disc = new int[vertices];
        int[] low = new int[vertices];
        List<List<Integer>> bridges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                logger.debug("Starting DFS for bridge detection at vertex {}", i);
                dfsBridge(i, -1, visited, disc, low, bridges, adj);
            }
        }

        List<String> result = bridges.stream().map(b -> b.get(0) + "-" + b.get(1)).toList();
        logger.info("Total bridges found: {}", result.size());
        return result;
    }

    /**
     * DFS utility for bridge detection.
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
                logger.debug("After visiting {}: updated low[{}] = {}", v, u, low[u]);
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
     * Detects all articulation points in an undirected graph.
     *
     * @param vertices the number of vertices in the graph
     * @param edges    the list of undirected edges
     * @return a list of articulation point vertex indices
     */
    public List<Integer> findArticulationPoints(int vertices, List<List<Integer>> edges) {
        logger.debug("Initializing articulation point detection with {} vertices and edges: {}", vertices, edges);
        time = 0;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

        for (List<Integer> edge : edges) {
            adj.get(edge.get(0)).add(edge.get(1));
            adj.get(edge.get(1)).add(edge.get(0));
        }
        logger.debug("Adjacency list created: {}", adj);

        boolean[] visited = new boolean[vertices];
        int[] disc = new int[vertices];
        int[] low = new int[vertices];
        boolean[] ap = new boolean[vertices];
        int[] parent = new int[vertices];
        int[] children = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            parent[i] = -1;
            if (!visited[i]) {
                logger.debug("Starting DFS for articulation point detection at vertex {}", i);
                dfsAP(i, visited, disc, low, ap, parent, children, adj);
            }
        }

        List<Integer> articulationPoints = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            if (ap[i]) articulationPoints.add(i);
        }

        logger.info("Total articulation points found: {}", articulationPoints.size());
        return articulationPoints;
    }

    /**
     * DFS utility for articulation point detection.
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
                logger.debug("After visiting {}: updated low[{}] = {}", v, u, low[u]);

                if ((parent[u] == -1 && children[u] > 1) ||
                        (parent[u] != -1 && low[v] >= disc[u])) {
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

**4) GlobalExceptionHandler:** src/main/java/com/example/maxflow/exception/GlobalExceptionHandler.java
```java
package com.example.graph.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Graph Application.
 *
 * <p>
 * This class centralizes exception handling for all controllers, ensuring that errors are
 * consistently formatted, logged, and returned with appropriate HTTP status codes.
 * </p>
 *
 * <p><strong>Key Responsibilities:</strong></p>
 * <ul>
 *   <li>Handle validation errors arising from invalid request payloads.</li>
 *   <li>Catch and log unexpected runtime exceptions.</li>
 *   <li>Ensure secure and meaningful error responses are sent to clients.</li>
 * </ul>
 *
 * <p><strong>Registered Exception Handlers:</strong></p>
 * <ul>
 *   <li>{@link MethodArgumentNotValidException}: Thrown when a `@Valid` annotated DTO fails validation.</li>
 *   <li>{@link Exception}: Catch-all handler for any uncaught runtime exception.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> The application returns structured and meaningful error responses for invalid or exceptional conditions.</li>
 *   <li><strong>Fail:</strong> Errors are returned as raw stack traces or unformatted messages, leaking implementation details or failing to inform clients.</li>
 * </ul>
 *
 * @author
 * @since 2025-03-26
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors from failed `@Valid` DTOs.
     *
     * <p>
     * Aggregates all validation errors and returns them in a structured format with HTTP 400.
     * </p>
     *
     * @param ex the MethodArgumentNotValidException thrown by Spring
     * @return ResponseEntity containing field-specific error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        logger.error("Validation error occurred: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches any unexpected or unhandled exception.
     *
     * <p>
     * Logs the full stack trace for debugging and returns a generic error message to the client.
     * </p>
     *
     * @param ex the unexpected exception
     * @return ResponseEntity with HTTP 500 and a general error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

```
**5) GraphRequest:** src/main/java/com/example/graph/dto/GraphRequest.java
```java
package com.example.graph.dto;

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
 * bridges and articulation points in an undirected graph.
 * It includes validation constraints to ensure data integrity.
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
 *   <li><code>@NotNull</code> and <code>@NotEmpty</code>: Ensure the edges list is non-null and not empty.</li>
 *   <li>Each edge vertex must also be non-null.</li>
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
 *   <li><strong>Fail:</strong> Any field is missing, null, or does not satisfy validation constraints.</li>
 * </ul>
 *
 * @author
 * @since 2025-03-26
 */
@Getter
@Setter
public class GraphRequest {

  /**
   * The number of vertices in the graph.
   * Must be greater than or equal to 1.
   */
  @Min(value = 1, message = "Number of vertices must be at least 1")
  private int vertices;

  /**
   * The list of undirected edges.
   * Each edge is represented as a list of two integers [u, v].
   * Cannot be null or empty.
   */
  @NotNull(message = "Edges list cannot be null")
  @NotEmpty(message = "Edges list cannot be empty")
  private List<List<@NotNull(message = "Edge vertex cannot be null") Integer>> edges;

}

```
**6) GraphController:** src/main/java/com/example/graph/controller/GraphController.java
```java
package com.example.graph.controller;

import com.example.graph.dto.GraphRequest;
import com.example.graph.service.GraphService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for graph analysis operations.
 *
 * <p>
 * Provides HTTP POST endpoints to analyze undirected graphs and determine:
 * <ul>
 *   <li>Bridge Edges – Edges whose removal increases the number of connected components.</li>
 *   <li>Articulation Points – Vertices whose removal increases the number of connected components.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Validates incoming requests using Jakarta Bean Validation and delegates processing to {@link GraphService}.
 * </p>
 *
 * <p><strong>Base URL:</strong> <code>/graph</code></p>
 *
 * <p><strong>Logging:</strong> Uses SLF4J to log request input and analysis results.</p>
 *
 * <p><strong>Security:</strong> If enabled, these endpoints may be protected by HTTP Basic Auth or other Spring Security configurations.</p>
 *
 * <p><strong>Validation Failures:</strong> Handled by {@link com.example.graph.exception.GlobalExceptionHandler}</p>
 *
 * @author
 * @since 2025-03-26
 */
@RestController
@RequestMapping("/graph")
public class GraphController {

    private static final Logger logger = LoggerFactory.getLogger(GraphController.class);

    @Autowired
    private GraphService graphService;

    /**
     * Endpoint to detect all bridges (critical edges) in the graph.
     *
     * @param request A valid {@link GraphRequest} containing vertices and edges.
     * @return A list of string representations of bridge edges (e.g., "1-3", "3-4").
     */
    @PostMapping("/bridges")
    public List<String> getBridges(@RequestBody @Valid GraphRequest request) {
        logger.info("Received request to find bridges with vertices: {} and edges: {}", request.getVertices(), request.getEdges());
        List<String> bridges = graphService.findBridges(request.getVertices(), request.getEdges());
        logger.info("Bridges found: {}", bridges);
        return bridges;
    }

    /**
     * Endpoint to detect all articulation points (critical vertices) in the graph.
     *
     * @param request A valid {@link GraphRequest} containing vertices and edges.
     * @return A list of articulation point vertex indices.
     */
    @PostMapping("/articulation")
    public List<Integer> getArticulationPoints(@RequestBody @Valid GraphRequest request) {
        logger.info("Received request to find articulation points with vertices: {} and edges: {}", request.getVertices(), request.getEdges());
        List<Integer> articulationPoints = graphService.findArticulationPoints(request.getVertices(), request.getEdges());
        logger.info("Articulation points found: {}", articulationPoints);
        return articulationPoints;
    }
}

```
**7) SecurityConfig:** src/main/java/com/example/graph/config/SecurityConfig.java
```java
package com.example.graph.config;

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
		<artifactId>graph</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<name>graph-integrity-app</name>
		<description>Build a Spring Boot application that detects bridges and articulation points in a graph. Sure! Here's the complete documentation in your requested format for the Bridges and Articulation Points Detection project using Spring Boot</description>
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
package com.example.graph;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit and integration test for {@link GraphIntegrityAppTest}.
 *
 * <p><strong>Purpose:</strong> Ensures that the Spring Boot application context loads successfully
 * and the main method can be invoked without errors.</p>
 */
@SpringBootTest
public class GraphIntegrityAppTest {

    /**
     * Tests that the Spring Boot application context loads without throwing any exceptions.
     *
     * <p><strong>Expectation:</strong> The application should start and initialize the context successfully.</p>
     */
    @Test
    public void contextLoads() {
        // If this fails, Spring Boot context loading failed
    }


}

```
**10) GraphServiceTest:** src/test/java/com/example/graph/service/GraphServiceTest.java
```java
package com.example.graph.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link GraphService} class.
 *
 * <p>This test suite validates the correctness of the graph analysis algorithms, specifically:</p>
 * <ul>
 *   <li>{@code findBridges(int, List<List<Integer>>)}</li>
 *   <li>{@code findArticulationPoints(int, List<List<Integer>>)}</li>
 * </ul>
 *
 * <p>Each test case checks either:</p>
 * <ul>
 *   <li>The detection of correct graph elements (bridges or articulation points).</li>
 *   <li>The return of empty results when no such critical elements exist.</li>
 * </ul>
 *
 * <p><strong>Note:</strong> The service is instantiated manually to test in isolation, without reliance on Spring context or dependencies.</p>
 *
 * @author
 * @since 2025-03-26
 */
@SpringBootTest
public class GraphServiceTest {

    private final GraphService graphService = new GraphService();

    /**
     * Verifies that the bridge detection algorithm returns the correct set of bridge edges.
     * <pre>
     * Graph:
     *   0 - 1 - 3 - 4
     *    \  |
     *      2
     * </pre>
     * Expected bridges: 1-3 and 3-4
     */
    @Test
    public void testFindBridges_WithValidGraph_ReturnsCorrectBridges() {
        int vertices = 5;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0),
                List.of(1, 3),
                List.of(3, 4)
        );

        List<String> bridges = graphService.findBridges(vertices, edges);
        assertEquals(2, bridges.size());
        assertTrue(bridges.contains("1-3") || bridges.contains("3-1"));
        assertTrue(bridges.contains("3-4") || bridges.contains("4-3"));
    }

    /**
     * Verifies that articulation points are correctly identified for a non-trivial graph.
     * Expected articulation points: vertex 1 and vertex 3
     */
    @Test
    public void testFindArticulationPoints_WithValidGraph_ReturnsCorrectPoints() {
        int vertices = 5;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0),
                List.of(1, 3),
                List.of(3, 4)
        );

        List<Integer> points = graphService.findArticulationPoints(vertices, edges);
        assertEquals(2, points.size());
        assertTrue(points.contains(1));
        assertTrue(points.contains(3));
    }

    /**
     * Verifies that the bridge detection algorithm returns an empty list
     * when there are no bridges in a cyclic graph.
     */
    @Test
    public void testFindBridges_WithNoBridgeGraph_ReturnsEmptyList() {
        int vertices = 3;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0)
        );

        List<String> bridges = graphService.findBridges(vertices, edges);
        assertTrue(bridges.isEmpty());
    }

    /**
     * Verifies that the articulation point detection algorithm returns an empty list
     * when all nodes are part of a strongly connected component.
     */
    @Test
    public void testFindArticulationPoints_WithNoArticulationPointGraph_ReturnsEmptyList() {
        int vertices = 3;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0)
        );

        List<Integer> points = graphService.findArticulationPoints(vertices, edges);
        assertTrue(points.isEmpty());
    }
}

```

**11) GraphRequestTest:** src/test/java/com/example/graph/dto/GraphRequestTest.java
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
 * <p><strong>Validation Rules Tested:</strong></p>
 * <ul>
 *   <li>{@code @Min(1)} on {@code vertices}</li>
 *   <li>{@code @NotNull} on {@code edges} and inner vertex values</li>
 *   <li>{@code @NotEmpty} on {@code edges}</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
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
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests a valid {@link GraphRequest} with 3 vertices and valid edges.
     *
     * <p><strong>Expectation:</strong> No constraint violations should occur.</p>
     */
    @Test
    public void testValidGraphRequest_NoViolations() {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "There should be no validation errors for a valid input");
    }

    /**
     * Tests {@code vertices = 0}, which violates {@code @Min(1)}.
     *
     * <p><strong>Expectation:</strong> Validation should fail on the 'vertices' field.</p>
     */
    @Test
    public void testInvalidGraphRequest_ZeroVertices_ShouldFailMinValidation() {
        GraphRequest request = new GraphRequest();
        request.setVertices(0);
        request.setEdges(List.of(List.of(0, 1)));

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Validation should fail for vertices < 1");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("vertices")));
    }

    /**
     * Tests with {@code edges = null}, violating {@code @NotNull}.
     *
     * <p><strong>Expectation:</strong> Validation should fail on the 'edges' field.</p>
     */
    @Test
    public void testInvalidGraphRequest_NullEdges_ShouldFailNotNullValidation() {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(null);

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Validation should fail for null edges list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edges")));
    }

    /**
     * Tests with an empty list of edges, violating {@code @NotEmpty}.
     *
     * <p><strong>Expectation:</strong> Validation should fail on the 'edges' field.</p>
     */
    @Test
    public void testInvalidGraphRequest_EmptyEdgesList_ShouldFailNotEmptyValidation() {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(new ArrayList<>());

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Validation should fail for empty edges list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edges")));
    }

   
}

```
**12) GraphControllerIntegrationTest:** src/test/java/com/example/graph/controller/GraphControllerIntegrationTest.java
```java
package com.example.graph.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link GraphController}.
 *
 * <p>
 * This class verifies the actual behavior of the graph-related REST endpoints by launching
 * the Spring Boot context and executing HTTP requests through {@link MockMvc}.
 * </p>
 *
 * <p>
 * <strong>Target Endpoints:</strong>
 * <ul>
 *     <li>POST /graph/bridges</li>
 *     <li>POST /graph/articulation</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Focus:</strong>
 * Validates end-to-end request processing, including:
 * <ul>
 *     <li>JSON input parsing</li>
 *     <li>Validation constraints</li>
 *     <li>Controller and service layer interaction</li>
 *     <li>HTTP response status codes</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerIntegrationTest {

    /**
     * Injected MockMvc to simulate HTTP requests to controller endpoints.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Injected ObjectMapper for serializing request bodies to JSON format.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Verifies that a valid request to /graph/bridges returns a 200 OK response.
     *
     * <p><strong>Scenario:</strong> Valid graph with 5 vertices and known bridges.</p>
     * <p><strong>Pass:</strong> Returns HTTP 200.</p>
     * <p><strong>Fail:</strong> Any non-200 status indicates a failure in controller validation or service logic.</p>
     */
    @Test
    public void testGetBridges_ValidRequest_ReturnsBridges() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "vertices", 5,
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2),
                        List.of(2, 0),
                        List.of(1, 3),
                        List.of(3, 4)
                )
        );

        mockMvc.perform(post("/graph/bridges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    /**
     * Verifies that a valid request to /graph/articulation returns a 200 OK response.
     *
     * <p><strong>Scenario:</strong> Valid graph input that includes articulation points.</p>
     * <p><strong>Pass:</strong> HTTP 200 response indicating successful processing.</p>
     * <p><strong>Fail:</strong> Any non-200 status.</p>
     */
    @Test
    public void testGetArticulationPoints_ValidRequest_ReturnsPoints() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "vertices", 5,
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2),
                        List.of(2, 0),
                        List.of(1, 3),
                        List.of(3, 4)
                )
        );

        mockMvc.perform(post("/graph/articulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    /**
     * Tests missing "vertices" field in request to /graph/bridges, expecting a 400 Bad Request.
     *
     * <p><strong>Scenario:</strong> Input validation fails due to missing required field.</p>
     * <p><strong>Pass:</strong> HTTP 400 is returned by validation framework.</p>
     * <p><strong>Fail:</strong> If status is not 400, validation isn't working as expected.</p>
     */
    @Test
    public void testGetBridges_InvalidRequest_MissingVertices() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2)
                )
        );

        mockMvc.perform(post("/graph/bridges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests empty edges list for /graph/articulation, expecting 400 Bad Request.
     *
     * <p><strong>Scenario:</strong> Edge list is empty, violating @NotEmpty constraint.</p>
     * <p><strong>Pass:</strong> HTTP 400 is returned due to validation failure.</p>
     * <p><strong>Fail:</strong> Any other status implies a missing or incorrect validation rule.</p>
     */
    @Test
    public void testGetArticulationPoints_InvalidRequest_EmptyEdges() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "vertices", 3,
                "edges", List.of()
        );

        mockMvc.perform(post("/graph/articulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }
}

```
**13) SecurityConfigTest:** src/test/java/com/example/graph/config/SecurityConfigTest.java
```java
package com.example.graph.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SecurityConfig}.
 *
 * <p><strong>Purpose:</strong> To validate the correctness of security-related bean configurations
 * such as the CORS policy, security filter chain, and user authentication setup.</p>
 */
@SpringBootTest
public class SecurityConfigTest {

  @Autowired
  private ApplicationContext context;

  /**
   * Validates the presence and construction of the {@link SecurityFilterChain} bean.
   *
   * <p><strong>Expected:</strong> Bean is not null and accepts configuration without throwing errors.</p>
   */
  @Test
  public void testSecurityFilterChainBeanExists() throws Exception {
    SecurityFilterChain filterChain = context.getBean(SecurityFilterChain.class);
    assertNotNull(filterChain, "SecurityFilterChain bean should be present in context");
  }


  /**
   * Validates that the in-memory user defined in {@link SecurityConfig#userDetailsService()} is correctly configured.
   *
   * <p><strong>Checks:</strong></p>
   * <ul>
   *     <li>User has username "user"</li>
   *     <li>User has password "password" (no encoding)</li>
   *     <li>User has role "USER"</li>
   * </ul>
   */
  @Test
  public void testInMemoryUserDetailsServiceConfiguration() {
    UserDetailsService userDetailsService = context.getBean(UserDetailsService.class);
    assertNotNull(userDetailsService);

    var user = userDetailsService.loadUserByUsername("user");
    assertEquals("user", user.getUsername());
    assertEquals("{noop}password", user.getPassword());
    assertTrue(user.getAuthorities().stream()
                   .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
  }
}
```

**Result:** The line coverage is 68%

<a href="https://drive.google.com/file/d/1MrtHA1YwcW4SxyBZvBxSQSKkAkQ4Cp2x/view?usp=drive_link">Iteration One</a>

**Plan:** The goal is to achieve >90% total code coverage and 95% total line coverage. To achieve this goal, will be writing tests for all models, dto, service, exception, event and controller packages.


<a href="https://drive.google.com/file/d/1CSHrykxvcxkZayc4Q1VI4S373grtF8AL/view?usp=drive_link">Iteration Two </a>

**Result:** Total line coverage is 96%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml includes Spring Boot Starter Web, Security, Validation, Lombok, Test, H2 Database, Jakarta Validation API, and DevTools dependencies with Java version set to 17 for a scalable REST API using the Edmonds‑Karp algorithm..
3. **Database & Application Properties:** Configure H2 DB credentials in application.properties (or application.yml) due to in-memory auth, for example:
```properties
spring.application.name=graph-integrity-app
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


### 6. **Accessing Endpoints & Features:**
REST Endpoints typically follow the pattern:

---

- **Detect Bridges:**
  - **POST /graph/bridges**
  - **Description:** Detects all bridges (critical edges) in an undirected graph.
  - **Request Body Example:**
    ```json
    {
      "vertices": 5,
      "edges": [
        [0, 1],
        [1, 2],
        [2, 0],
        [1, 3],
        [3, 4]
      ]
    }
    ```
  - **cURL Command:**
    ```bash
    curl -X POST http://localhost:8080/graph/bridges \
    -H "Content-Type: application/json" \
    -d '{
      "vertices": 5,
      "edges": [
        [0, 1],
        [1, 2],
        [2, 0],
        [1, 3],
        [3, 4]
      ]
    }'
    ```

---

- **Detect Articulation Points:**
  - **POST /graph/articulation**
  - **Description:** Detects all articulation points (critical nodes) in an undirected graph.
  - **Request Body Example:**
    ```json
    {
      "vertices": 5,
      "edges": [
        [0, 1],
        [1, 2],
        [2, 0],
        [1, 3],
        [3, 4]
      ]
    }
    ```
  - **cURL Command:**
    ```bash
    curl -X POST http://localhost:8080/graph/articulation \
    -H "Content-Type: application/json" \
    -d '{
      "vertices": 5,
      "edges": [
        [0, 1],
        [1, 2],
        [2, 0],
        [1, 3],
        [3, 4]
      ]
    }'
    ```


7. **Security**

- Our Spring Security configuration enables CORS with custom rules (e.g., allowing requests from localhost:3000), disables CSRF protection for API development, permits open access to all endpoints for testing purposes, enables HTTP Basic authentication for secured endpoints during testing, and configures in-memory authentication with a single test user.
  Here’s the **Time and Space Complexity** and **Conclusion** section tailored specifically for your **Graph Integrity App** that detects **bridges** and **articulation points** using DFS:



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

---

# **Conclusion**

The **Graph Integrity App**, built with **Spring Boot**, delivers efficient and scalable detection of graph **bridges** and **articulation points** through a REST API interface. Leveraging the optimal **DFS-based Tarjan's algorithm**, the system achieves linear-time performance **(O(V + E))**, which is ideal for real-time graph integrity validation in network topologies, dependency chains, or infrastructure graphs.

The solution is further strengthened with:
- **Robust input validation** using Jakarta Bean Validation,
- **Comprehensive logging** and error handling,
- **Unit and integration testing** with JUnit and MockMvc.

This makes the application suitable for academic, enterprise, and analytical use cases that demand both **correctness** and **performance** in graph structural analysis.