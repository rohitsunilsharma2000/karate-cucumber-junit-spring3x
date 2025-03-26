package com.example.graph.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SecurityConfig}.
 *
 * <p><strong>Purpose:</strong> To validate the correctness of security-related bean configurations
 * such as the CORS policy, security filter chain, and user authentication setup.</p>
 */
@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    /**
     * Validates the presence and construction of the {@link SecurityFilterChain} bean.
     *
     * <p><strong>Expected:</strong> Bean is not null and accepts configuration without throwing errors.</p>
     */
    @Test
    public void testSecurityFilterChainBeanExists() throws Exception {
        SecurityFilterChain filterChain = context.getBean(SecurityFilterChain.class);
        assertNotNull(filterChain, "SecurityFilterChain bean should be present in context");
    }

    /**
     * Validates the {@link CorsConfigurationSource} settings to ensure CORS rules are applied correctly.
     *
     * <p><strong>Checks:</strong></p>
     * <ul>
     *     <li>Allowed origins include expected dev URLs</li>
     *     <li>Allowed methods include common HTTP verbs</li>
     *     <li>All headers are allowed</li>
     * </ul>
     */
    @Test
    public void testCorsConfigurationSourceSettings() {
        CorsConfigurationSource source = context.getBean(CorsConfigurationSource.class);
        assertNotNull(source);

        UrlBasedCorsConfigurationSource configSource = (UrlBasedCorsConfigurationSource) source;
        CorsConfiguration config = configSource.getCorsConfigurations().get("/**");

        assertNotNull(config);
        assertTrue(config.getAllowedOrigins().contains("http://localhost:3000"));
        assertTrue(config.getAllowedOrigins().contains("http://127.0.0.1:3000"));
        assertTrue(config.getAllowedMethods().containsAll(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")));
        assertTrue(config.getAllowedHeaders().contains("*"));
        assertTrue(config.getAllowCredentials());
        assertEquals(3600L, config.getMaxAge());
    }

    /**
     * Validates that the in-memory user defined in {@link SecurityConfig#userDetailsService()} is correctly configured.
     *
     * <p><strong>Checks:</strong></p>
     * <ul>
     *     <li>User has username "user"</li>
     *     <li>User has password "password" (no encoding)</li>
     *     <li>User has role "USER"</li>
     * </ul>
     */
    @Test
    public void testInMemoryUserDetailsServiceConfiguration() {
        UserDetailsService userDetailsService = context.getBean(UserDetailsService.class);
        assertNotNull(userDetailsService);

        var user = userDetailsService.loadUserByUsername("user");
        assertEquals("user", user.getUsername());
        assertEquals("{noop}password", user.getPassword());
        assertTrue(user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
}