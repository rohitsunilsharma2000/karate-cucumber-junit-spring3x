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
