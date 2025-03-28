package com.example.colorsched;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



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
public class GraphSlotterSystem {

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(GraphSlotterSystem.class, args);
    }
}