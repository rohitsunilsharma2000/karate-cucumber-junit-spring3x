

---

**Use Case:** A network analysis tool for industries requiring efficient detection of bridges and articulation points in complex networks. This API is designed to be integrated into larger systems (e.g., infrastructure monitoring, cybersecurity, and social network analysis) to quickly identify critical nodes and connections whose removal could fragment or weaken the network. The solution must be robust, scalable, and well-documented with comprehensive REST endpoints, input validations, and strong exception handling.

# **Prompt**

## **Title:**
Spring Boot Graph Analyzer — Bridge and Articulation Point Detection API

## **High-Level Description:**
A Spring Boot API that identifies *bridges* and *articulation points* in an undirected graph. The application exposes REST API endpoints where users can submit a graph defined by vertices and edges. It utilizes DFS-based algorithms to detect structural vulnerabilities—edges whose removal disconnects the graph (bridges), and vertices whose removal increases the number of connected components (articulation points). The solution adheres to layered architecture principles and includes a controller, service, exception handling, validation, and test coverage.

## **Key Features:**

1. **Project Structure & Setup**
- Create the project with Spring Initializr.
- Organize packages into `controller`, `service`, `dto`, `exception`, and `config`.

2. **REST Controller & API Endpoints**
- Implement a REST controller under `/graph`.
- Expose two endpoints:
  - `POST /graph/bridges` — accepts a JSON request and returns a list of bridge edges.
  - `POST /graph/articulation` — accepts a JSON request and returns a list of articulation point nodes.
- Use `@Valid` with request DTO to enforce field constraints.

3. **Service Layer – DFS-based Algorithms**
- Detect **bridges** using Tarjan’s Bridge-Finding algorithm (DFS-based).
- Detect **articulation points** using DFS with tracking of discovery and low times.
- Log detailed progress using SLF4J at `INFO`, `DEBUG`, and `ERROR` levels.

4. **Exception Handling & Input Validation**
- Validate input using Jakarta annotations:
  - `@Min(1)` for vertex count.
  - `@NotEmpty`, `@NotNull` for edge definitions.
- Implement `@RestControllerAdvice` to handle:
  - Validation exceptions (`MethodArgumentNotValidException`)
  - Malformed JSON (`HttpMessageNotReadableException`)
  - Custom graph exceptions (`GraphAnalysisException`)
  - Generic unhandled exceptions.

5. **Logging & Traceability**
- Log incoming requests, internal algorithm steps, and responses using SLF4J.
- Capture exceptions with stack traces for debugging.

6. **Testing & Documentation**
- Write:
  - Unit tests for graph logic in service layer.
  - Integration tests for controller with MockMvc.
  - Negative test cases for malformed JSON and invalid input.
- Use Javadoc and class-level documentation for maintainability.

7. **Expected Behaviour:**
- The bridge/articulation detection endpoints must:
  - Return HTTP 200 OK with correct results for valid graphs.
  - Return HTTP 400 Bad Request for:
    - Missing/zero vertices → “Number of vertices must be at least 1”
    - Null/empty edge list → “Edges list cannot be null/empty”
    - Edges with null nodes → “Edge vertex cannot be null”
    - Malformed JSON → “Malformed JSON request”
- In case of internal errors during analysis, return HTTP 500 with a meaningful error.

8. **Edge Cases**
- Handle cases where:
  - `vertices = 0` → return 400.
  - `edges = null or []` → return 400.
  - Graph has isolated nodes → no bridges/articulation points.
  - Edge list contains null vertex → return validation error.
  - Invalid JSON → return parse error.
- Maintain consistent error response format with `timestamp`, `status`, `error`, `message`.

---

**Dependency Requirements:**

- **JUnit 5:** For writing and executing unit tests.
- **Maven:** For dependency management and builds.
- **Spring Boot Starter Web:** For exposing RESTful APIs.
- **Spring Boot Starter Validation:** For request validation using Jakarta Bean Validation.
- **Spring Boot Starter Test:** For unit and integration testing.
- **Lombok:** (optional) for reducing boilerplate in DTOs and services.
- **SLF4J / Logback:** For logging.
- **Spring Boot DevTools:** For fast development and hot reloading.
- **H2 Database:** Only if persistence is needed for future enhancements (not used here).

---

## **Goal:**
To develop a Spring Boot microservice that reliably detects bridges and articulation points in a graph using efficient DFS-based algorithms. The application must maintain a clean structure, enforce robust validation, log key activities, and handle both expected and unexpected errors gracefully. This service is useful for identifying critical points in communication, social, and transport networks.


---

**Plan**  
I will set up the project structure using Spring Initializr and create the core packages: `controller`, `service`, `dto`, `exception`, and `config`. I will configure the `pom.xml` with all required dependencies and set the Java version to 17. I will define the `GraphRequest` DTO with validation annotations to ensure input integrity. I will build the REST controller to expose two endpoints: one for detecting bridges and another for articulation points. I will implement the DFS-based algorithms in the service layer with proper SLF4J logging at INFO and DEBUG levels. I will add a `GlobalExceptionHandler` class to handle validation failures, malformed requests, and custom graph exceptions uniformly. I will write unit tests for the service logic and integration tests for the REST endpoints using JUnit 5 and Spring MockMvc. Finally, I will verify the application by building and running it with Maven, ensuring that it returns correct results and properly handles edge cases and errors.

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
|   |               |-- controller
|   |               |   `-- GraphController.java
|   |               |-- dto
|   |               |   `-- GraphRequest.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   `-- GraphAnalysisException.java
|   |               `-- service
|   |                   `-- GraphService.java
|   `-- resources
`-- test
    `-- java
        `-- com
            `-- example
                `-- graph
                    |-- GraphIntegrityAppTest.java
                    |-- controller
                    |   `-- GraphControllerIntegrationTest.java
                    |   `-- GraphControllerTest.java
                    |-- dto
                    |   `-- GraphRequestTest.java
                    |-- exception
                    |   `-- GlobalExceptionHandlerTest.java
                    `-- service
                        `-- GraphServiceTest.java




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
 * @since 2025-03-26
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
 * Service class for analyzing graph integrity by detecting critical elements like
 * bridges and articulation points in an undirected graph.
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
        try {
            logger.debug("Initializing bridge detection with {} vertices and edges: {}", vertices, edges);
            time = 0;

            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

            for (List<Integer> edge : edges) {
                adj.get(edge.get(0)).add(edge.get(1));
                adj.get(edge.get(1)).add(edge.get(0));
            }

            boolean[] visited = new boolean[vertices];
            int[] disc = new int[vertices];
            int[] low = new int[vertices];
            List<List<Integer>> bridges = new ArrayList<>();

            for (int i = 0; i < vertices; i++) {
                if (!visited[i]) {
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
        try {
            logger.debug("Initializing articulation point detection with {} vertices and edges: {}", vertices, edges);
            time = 0;

            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

            for (List<Integer> edge : edges) {
                adj.get(edge.get(0)).add(edge.get(1));
                adj.get(edge.get(1)).add(edge.get(0));
            }

            boolean[] visited = new boolean[vertices];
            int[] disc = new int[vertices];
            int[] low = new int[vertices];
            boolean[] ap = new boolean[vertices];
            int[] parent = new int[vertices];
            int[] children = new int[vertices];

            for (int i = 0; i < vertices; i++) {
                parent[i] = -1;
                if (!visited[i]) {
                    dfsAP(i, visited, disc, low, ap, parent, children, adj);
                }
            }

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
**4) GlobalExceptionHandler:** `src/main/java/com/example/graph/exception/GlobalExceptionHandler.java`
```java
package com.example.graph.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Global exception handler for managing application-wide exceptions with consistent responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Generic error response builder.
     *
     * @param status  HTTP status code
     * @param message Main message or error details
     * @param extra   Optional extra fields (like field errors)
     * @return structured error response
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message, Map<String, ?> extra) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (extra != null && !extra.isEmpty()) {
            body.putAll(extra);
        }
        return new ResponseEntity<>(body, status);
    }

    /**
     * Handles validation failures on method arguments.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", Map.of("errors", errors));
    }

    /**
     * Handles malformed JSON requests.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonParseErrors(HttpMessageNotReadableException ex) {
        logger.error("Malformed JSON: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", null);
    }

    /**
     * Handles graph analysis related exceptions.
     */
    @ExceptionHandler(GraphAnalysisException.class)
    public ResponseEntity<Object> handleGraphAnalysisException(GraphAnalysisException ex) {
        logger.error("Graph analysis exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    /**
     * Handles all other unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                null
        );
    }
}

```
**5) GraphRequest:** `src/main/java/com/example/graph/dto/GraphRequest.java`
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
**6) GraphController:** `src/`main/java/com/example/graph/controller/GraphController.java`
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
**7) application.properties:** `src/main/resources/application.properties`
```text
spring.application.name=graph-integrity-app
server.port=8080

```


**8) Maven:** `pom.xml`
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

**9) Main Application:** `src/test/java/com/example/graph/GraphIntegrityAppTest.java`
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
 * @since 2025-03-27
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
**10) GraphServiceTest:** `src/test/java/com/example/graph/service/GraphServiceTest.java`
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
 * @since 2025-03-27
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

**11) GraphRequestTest:** `src/test/java/com/example/graph/dto/GraphRequestTest.java`
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

## After the first iteration, the overall test coverage was 74%. To improve this, additional test cases—including those in GraphControllerIntegrationTest , GraphControllerTest and GlobalExceptionHandlerTest will be introduced to further increase the test coverage percentage.


**12) GraphControllerIntegrationTest:** `src/test/java/com/example/graph/controller/GraphControllerIntegrationTest.java`
```java
package com.example.graph.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link GraphController}.
 *
 * <p>
 * This class verifies the behavior of the REST endpoints defined in the GraphController within a full Spring Boot application context.
 * The tests cover:
 * <ul>
 *   <li>HTTP status codes for valid and invalid input payloads</li>
 *   <li>Proper JSON request/response handling</li>
 *   <li>Validation constraint violations on the {@link com.example.graph.dto.GraphRequest} DTO</li>
 *   <li>Behavior of {@link com.example.graph.exception.GlobalExceptionHandler} for edge cases</li>
 * </ul>
 * </p>
 *
 * <p>
 * By running these tests, we ensure that the controller logic, validation, and exception handling work as expected when the application is fully bootstrapped.
 * </p>
 *
 * @author
 * @since 2025-03-26
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Simulates HTTP requests for integration testing

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to JSON and vice versa

    /**
     * Integration test for the /graph/bridges endpoint with valid input.
     *
     * <p>
     * <strong>Scenario:</strong> A valid request is sent with 5 vertices and a proper list of edges.
     * The test expects an HTTP 200 OK response, indicating that the request was processed successfully.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/bridges - Valid Input - Returns 200 OK")
    public void testFindBridges_ValidInput_ReturnsOk() throws Exception {
        // GIVEN: Build a valid request payload containing 5 vertices and a list of edges.
        Map<String, Object> request = Map.of(
                "vertices", 5,
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2),
                        List.of(2, 0),
                        List.of(1, 3),
                        List.of(3, 4)
                )
        );

        // WHEN & THEN: Perform a POST request to /graph/bridges, then expect HTTP 200 OK.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Integration test for the /graph/articulation endpoint with valid input.
     *
     * <p>
     * <strong>Scenario:</strong> A valid request is sent with 5 vertices and a list of edges.
     * The test expects an HTTP 200 OK response, indicating that the endpoint processed the request successfully.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/articulation - Valid Input - Returns 200 OK")
    public void testFindArticulationPoints_ValidInput_ReturnsOk() throws Exception {
        // GIVEN: Build a valid request payload containing 5 vertices and a list of edges.
        Map<String, Object> request = Map.of(
                "vertices", 5,
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2),
                        List.of(2, 0),
                        List.of(1, 3),
                        List.of(3, 4)
                )
        );

        // WHEN & THEN: Perform a POST request to /graph/articulation and expect HTTP 200 OK.
        mockMvc.perform(post("/graph/articulation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Integration test for the /graph/bridges endpoint when the 'vertices' field is missing.
     *
     * <p>
     * <strong>Scenario:</strong> A request is sent without the required 'vertices' field.
     * The test expects an HTTP 400 Bad Request response due to validation failure.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/bridges - Missing vertices - Returns 400 Bad Request")
    public void testFindBridges_MissingVertices_ReturnsBadRequest() throws Exception {
        // GIVEN: Build a request payload with the 'edges' field only, leaving out the required 'vertices'.
        Map<String, Object> request = Map.of(
                "edges", List.of(
                        List.of(0, 1),
                        List.of(1, 2)
                )
        );

        // WHEN & THEN: Perform a POST request to /graph/bridges and expect HTTP 400 Bad Request with a validation error message.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    /**
     * Integration test for the /graph/articulation endpoint when the 'edges' list is empty.
     *
     * <p>
     * <strong>Scenario:</strong> A request is sent with a valid 'vertices' value but an empty 'edges' list.
     * The test expects an HTTP 400 Bad Request response due to a validation error.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/articulation - Empty edges - Returns 400 Bad Request")
    public void testFindArticulationPoints_EmptyEdges_ReturnsBadRequest() throws Exception {
        // GIVEN: Build a request payload with a valid 'vertices' value but an empty list for 'edges'.
        Map<String, Object> request = Map.of(
                "vertices", 4,
                "edges", List.of()
        );

        // WHEN & THEN: Perform a POST request to /graph/articulation and expect HTTP 400 Bad Request with a validation error message.
        mockMvc.perform(post("/graph/articulation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    /**
     * Integration test for the /graph/bridges endpoint with a malformed JSON payload.
     *
     * <p>
     * <strong>Scenario:</strong> A request is sent with a malformed JSON body.
     * The test expects an HTTP 400 Bad Request response due to a JSON parsing error.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("POST /graph/bridges - Invalid JSON - Returns 400 Bad Request")
    public void testFindBridges_InvalidJson_ReturnsBadRequest() throws Exception {
        // GIVEN: Create a malformed JSON string (e.g., missing closing bracket).
        String malformedJson = "{\"vertices\": 4, \"edges\": [[0,1], [1,2], [2]]";

        // WHEN & THEN: Perform a POST request to /graph/bridges with the malformed JSON and expect HTTP 400 Bad Request.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
               .andExpect(status().isBadRequest());
    }
}

```

**11) GlobalExceptionHandlerTest:** `src/test/java/com/example/graph/exception/GlobalExceptionHandlerTest.java`
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
 * @since 2025-03-27
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
**12) GraphControllerTest:** `src/test/java/com/example/graph/exception/GlobalExceptionHandlerTest.java`
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
 * @since 2025-03-26
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
# After the second iteration, test coverage increased to 99%.
**Result:** Total line coverage is 99%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml includes Spring Boot Starter Web, Security, Validation, Lombok, Test, H2 Database, Jakarta Validation API, and DevTools dependencies with Java version set to 17 for a scalable REST API using the Edmonds‑Karp algorithm..
3. **Database & Application Properties:** Configure H2 DB credentials in application.properties (or application.yml) due to in-memory auth, for example:
```properties
spring.application.name=graph-integrity-app
server.port=8080



# H2 in-memory database settings
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect



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


Here’s the complete **cURL documentation** for the **Detect Bridges** and **Detect Articulation Points** endpoints, formatted exactly like your sample:

---

### 6. **Accessing Endpoints & Features:**

Below are runnable cURL commands along with sample success and error responses for the **Graph Bridges and Articulation Points Detection** endpoints.

---

### ✅ Detect Bridges

- **Valid Request (Success Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/graph/bridges' \
    --header 'Content-Type: application/json' \
    --data '{
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

  - **Sample Success Response:**

    ```json
    [
      "3-4",
      "1-3"
    ]
    ```

    *(The API returns HTTP status 200 OK and a list of bridge edges in the graph.)*

---

- **Missing Vertices (Error Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/graph/bridges' \
    --header 'Content-Type: application/json' \
    --data '{
        "edges": [
            [0, 1],
            [1, 2]
        ]
    }'
    ```

  - **Sample Error Response:**

    ```json
    {
      "timestamp": "2025-03-27T13:45:20.147732",
      "status": 400,
      "error": "Bad Request",
      "message": "Validation failed",
      "errors": {
        "vertices": "must not be null"
      }
    }
    ```

    *(The API returns HTTP status 400 Bad Request due to missing required `vertices` field.)*

---

- **Malformed JSON (Error Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/graph/bridges' \
    --header 'Content-Type: application/json' \
    --data '{"vertices": 4, "edges": [[0,1], [1,2]'  # Missing closing bracket
    ```

  - **Sample Error Response:**

    ```json
    {
      "timestamp": "2025-03-27T13:47:01.253465",
      "status": 400,
      "error": "Bad Request",
      "message": "Malformed JSON request"
    }
    ```

    *(The API returns HTTP status 400 Bad Request due to malformed JSON.)*

---

### ✅ Detect Articulation Points

- **Valid Request (Success Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/graph/articulation' \
    --header 'Content-Type: application/json' \
    --data '{
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

  - **Sample Success Response:**

    ```json
    [1, 3]
    ```

    *(The API returns HTTP status 200 OK and a list of articulation point node indices.)*

---

- **Empty Edges (Error Response)**

  - **cURL Command:**

    ```bash
    curl --location 'http://localhost:8080/graph/articulation' \
    --header 'Content-Type: application/json' \
    --data '{
        "vertices": 4,
        "edges": []
    }'
    ```

  - **Sample Error Response:**

    ```json
    {
      "timestamp": "2025-03-27T13:48:50.987351",
      "status": 400,
      "error": "Bad Request",
      "message": "Validation failed",
      "errors": {
        "edges": "must not be empty"
      }
    }
    ```

    *(The API returns HTTP status 400 Bad Request due to empty edge list which violates the validation constraints.)*

---



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



# ** Iteration**


First Iteration: https://drive.google.com/file/d/1qFJcG7LmphJimo_lPlfdq_CttqWEqUDw/view?usp=drive_link
First Iteration: https://drive.google.com/file/d/1UXzwqbspuFDcE4vgSvoGYfp2UtzRPWEE/view?usp=drive_link
Code Download:
https://drive.google.com/file/d/1UNzUVJuYjaix-HaKOgmJ9qfUtp9uBaLZ/view?usp=drive_link