
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
 *
 * <p>Verifies the creation and correctness of beans related to security configuration.</p>
 */
@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig config;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Test 1: Verifies that the security filter chain bean is loaded.
     */
    @Test
    @DisplayName("Test 1: Security filter chain loads successfully")
    void testSecurityFilterChain() {
        assertNotNull(securityFilterChain, "SecurityFilterChain should not be null");
    }

    /**
     * Test 2: Validates the CORS configuration settings.
     */
    @Test
    @DisplayName("Test 2: CORS configuration source is valid and matches expected setup")
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = config.corsConfigurationSource();
        assertNotNull(source, "CorsConfigurationSource should not be null");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        CorsConfiguration corsConfig = source.getCorsConfiguration(request);
        assertNotNull(corsConfig, "CorsConfiguration should not be null");

        List<String> allowedOrigins = corsConfig.getAllowedOrigins();
        assertTrue(allowedOrigins.contains("http://localhost:3000"));
        assertTrue(allowedOrigins.contains("http://127.0.0.1:3000"));

        List<String> allowedMethods = corsConfig.getAllowedMethods();
        assertTrue(allowedMethods.contains("GET"));
        assertTrue(allowedMethods.contains("POST"));
        assertTrue(corsConfig.getAllowCredentials());
    }

    /**
     * Test 3: Confirms that the in-memory user exists.
     */
    @Test
    @DisplayName("Test 3: In-memory user details are configured properly")
    void testInMemoryUserDetails() {
        assertNotNull(userDetailsService, "UserDetailsService should not be null");
        assertDoesNotThrow(() -> userDetailsService.loadUserByUsername("user"), "User 'user' should be found");
    }
}
