package com.example.colorsched;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This test verifies that the application context loads correctly
 * and that the main() method can be called without errors.
 */
@SpringBootTest
class GraphSlotterSystemTest {

    /**
     * ✅ Test: Ensures that the Spring application context loads without exceptions.
     * This validates that all beans and configurations are set up correctly.
     */
    @Test
    @DisplayName("Application context should load successfully")
    void contextLoads() {
        // Context load is automatically verified by @SpringBootTest
    }

    /**
     * ✅ Test: Verifies that the main method can be called without throwing any exceptions.
     */
    @Test
    @DisplayName("main() method should start the Spring Boot application without error")
    void testMainMethodRunsSuccessfully() {
        String[] args = {};
        GraphSlotterSystem.main(args); // This should run without throwing exceptions
    }
}
