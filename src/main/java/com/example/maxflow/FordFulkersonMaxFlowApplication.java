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
