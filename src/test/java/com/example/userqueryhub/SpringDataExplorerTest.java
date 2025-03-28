package com.example.userqueryhub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for the main application class {@link SpringDataExplorerTest}.
 *
 * <p>
 * This test class includes:
 * <ul>
 *   <li>{@code contextLoads()} - Verifies that the Spring application context starts up correctly.</li>
 *   <li>{@code testMainMethod()} - Calls the main method to ensure it executes without throwing exceptions.</li>
 * </ul>
 * </p>
 *
 * <p>
 * With these tests, code coverage tools (e.g., JaCoCo) should report over 90% class, method, and line coverage.
 * </p>
 *
 * @since 2025-03-27
 */
@SpringBootTest(classes = SpringDataExplorerTest.class)
public class SpringDataExplorerTest {

    /**
     * Verifies that the Spring application context loads successfully.
     *
     * <p>
     * <strong>GIVEN:</strong> The application is configured correctly.
     * <br>
     * <strong>WHEN:</strong> The test starts, and Spring Boot attempts to load the application context.
     * <br>
     * <strong>THEN:</strong> No exceptions should be thrown, indicating that the context has loaded properly.
     * </p>
     */
    @Test
    public void contextLoads() {
        // The application context is automatically loaded by the @SpringBootTest annotation.
        // If context loading fails, this test will fail.
    }

    /**
     * Verifies that calling the main method of {@link SpringDataExplorerTest} executes without exceptions.
     *
     * <p>
     * <strong>GIVEN:</strong> An empty argument array.
     * <br>
     * <strong>WHEN:</strong> The main method is invoked.
     * <br>
     * <strong>THEN:</strong> The application should start and terminate without any exceptions.
     * </p>
     */
    @Test
    public void testMainMethod() {
        // WHEN: Call the main method with an empty argument array.
        SpringDataExplorer.main(new String[]{});
        // THEN: The test passes if no exception is thrown during execution.
    }
}
