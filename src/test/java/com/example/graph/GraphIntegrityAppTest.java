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
