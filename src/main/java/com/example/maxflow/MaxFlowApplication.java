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
