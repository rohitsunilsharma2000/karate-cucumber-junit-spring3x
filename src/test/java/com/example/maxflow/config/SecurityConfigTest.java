package com.example.maxflow.config;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SecurityConfig}.
 *
 * <p><strong>Overview:</strong></p>
 * These tests verify that security-related beans such as {@link SecurityFilterChain},
 * {@link UserDetailsService}, and {@link CorsConfigurationSource} are correctly configured.
 *
 * <p><strong>Scope:</strong></p>
 * <ul>
 *   <li>Bean instantiation</li>
 *   <li>Basic configuration assertions</li>
 * </ul>
 */
@SpringBootTest
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired

    private  SecurityConfig config;
    @Autowired
    private SecurityFilterChain securityFilterChain;
    /**
     * Test 1: Verifies that the security filter chain is created successfully.
     */
    @Test
    @DisplayName("Test 1: Security filter chain loads")
    void testSecurityFilterChain() {
        assertNotNull(securityFilterChain);
    }


    /**
     * Test 2: Verifies that the CORS configuration source allows expected origins and methods.
     */

    @Test
    @DisplayName("Test 2: CORS configuration is defined correctly")
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = config.corsConfigurationSource();
        assertNotNull(source);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var cors = source.getCorsConfiguration(request);

        assertNotNull(cors);
        assertTrue(cors.getAllowedOrigins().contains("http://localhost:3000"));
        assertTrue(cors.getAllowedMethods().contains("GET"));
        assertTrue(cors.getAllowCredentials());
    }


    /**
     * Test 3: Verifies that the in-memory user is configured with username 'user'.
     */
    @Test
    @DisplayName("Test 3: In-memory user setup")
    void testUserDetailsService() {
        UserDetailsService userDetailsService = config.userDetailsService();
        assertNotNull(userDetailsService);
        assertDoesNotThrow(() -> userDetailsService.loadUserByUsername("user"));
    }
}
