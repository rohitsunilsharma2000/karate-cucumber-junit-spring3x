package com.example.graph;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit and integration test for {@link GraphApplication}.
 *
 * <p><strong>Purpose:</strong> Ensures that the Spring Boot application context loads successfully
 * and the main method can be invoked without errors.</p>
 */
@SpringBootTest
public class GraphApplicationTest {

    /**
     * Tests that the Spring Boot application context loads without throwing any exceptions.
     *
     * <p><strong>Expectation:</strong> The application should start and initialize the context successfully.</p>
     */
    @Test
    public void contextLoads() {
        // If this fails, Spring Boot context loading failed
    }

    /**
     * Tests that the main method of {@link GraphApplication} executes without throwing any exceptions.
     *
     * <p><strong>Scenario:</strong> Simulates the entry point invocation during application bootstrapping.</p>
     *
     * <p><strong>Expectation:</strong> Application starts cleanly with logs, no exceptions thrown.</p>
     */
    @Test
    public void testMainMethodExecution() {
        assertDoesNotThrow(() -> GraphApplication.main(new String[]{}),
                "Main method should execute without throwing exceptions");
    }
}
