package com.example.maxflow.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SecurityConfig}.
 * <p>
 * This class verifies the creation and correctness of beans related to the security configuration,
 * including the security filter chain, CORS configuration, and in-memory user details.
 * </p>
 */
@SpringBootTest
class SecurityConfigTest {

    /**
     * The security configuration instance to be tested.
     */
    @Autowired
    private SecurityConfig config;

    /**
     * The security filter chain bean configured by {@code SecurityConfig}.
     */
    @Autowired
    private SecurityFilterChain securityFilterChain;

    /**
     * The user details service that provides in-memory user details.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Test 1: Verifies that the security filter chain bean is loaded.
     * <p>
     * Ensures that the {@code SecurityFilterChain} is not {@code null},
     * indicating that the security filter chain has been properly configured.
     * </p>
     */
    @Test
    @DisplayName("Test 1: Security filter chain loads successfully")
    void testSecurityFilterChain() {
        assertNotNull(securityFilterChain, "SecurityFilterChain should not be null");
    }

    /**
     * Test 2: Validates the CORS configuration settings.
     * <p>
     * This test retrieves the {@code CorsConfigurationSource} from the security configuration,
     * simulates an HTTP request to retrieve the CORS configuration, and asserts that:
     * <ul>
     *   <li>The allowed origins include "http://localhost:3000" and "http://127.0.0.1:3000".</li>
     *   <li>The allowed methods include "GET" and "POST".</li>
     *   <li>Credentials are allowed in CORS requests.</li>
     * </ul>
     * </p>
     */
    @Test
    @DisplayName("Test 2: CORS configuration source is valid and matches expected setup")
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = config.corsConfigurationSource();
        assertNotNull(source, "CorsConfigurationSource should not be null");

        // Create a mock HTTP request to test CORS configuration retrieval
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        CorsConfiguration corsConfig = source.getCorsConfiguration(request);
        assertNotNull(corsConfig, "CorsConfiguration should not be null");

        List<String> allowedOrigins = corsConfig.getAllowedOrigins();
        assertTrue(allowedOrigins.contains("http://localhost:3000"), "Allowed origins should contain http://localhost:3000");
        assertTrue(allowedOrigins.contains("http://127.0.0.1:3000"), "Allowed origins should contain http://127.0.0.1:3000");

        List<String> allowedMethods = corsConfig.getAllowedMethods();
        assertTrue(allowedMethods.contains("GET"), "Allowed methods should contain GET");
        assertTrue(allowedMethods.contains("POST"), "Allowed methods should contain POST");
        assertTrue(corsConfig.getAllowCredentials(), "CORS configuration should allow credentials");
    }

    /**
     * Test 3: Confirms that the in-memory user details are configured properly.
     * <p>
     * This test asserts that the {@code UserDetailsService} bean is not {@code null}
     * and that an in-memory user with the username "user" can be successfully loaded.
     * </p>
     */
    @Test
    @DisplayName("Test 3: In-memory user details are configured properly")
    void testInMemoryUserDetails() {
        assertNotNull(userDetailsService, "UserDetailsService should not be null");
        assertDoesNotThrow(() -> userDetailsService.loadUserByUsername("user"), "User 'user' should be found");
    }
}
