package com.example.notificationservice.config;



import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 🧪 **SecurityConfigTest**
 * <p>
 * This test class verifies that the SecurityConfig is properly loaded into the application context.
 * It uses:
 * - @SpringBootTest to load the full application context.
 * - @AutoConfigureMockMvc to configure the web environment.
 * - @Transactional to auto-rollback any DB changes after each test.
 * - @WithMockUser to simulate a secured user context.
 * - @TestMethodOrder with @Order to control the execution order of tests.
 * <p>
 * File: src/test/java/com/example/turingOnlineForumSystem/config/SecurityConfigTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc

@TestMethodOrder(OrderAnnotation.class)
public class SecurityConfigTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    /**
     * Test case to verify that the SecurityFilterChain bean is loaded.
     * <p>
     * This test confirms that the SecurityFilterChain, which is configured in SecurityConfig,
     * is present in the application context, ensuring that security is properly set up.
     */
    @Test
    @Order(1)
    public void testSecurityFilterChainBeanLoaded() {
        assertNotNull(securityFilterChain, "SecurityFilterChain bean should be loaded in the application context");
    }
}