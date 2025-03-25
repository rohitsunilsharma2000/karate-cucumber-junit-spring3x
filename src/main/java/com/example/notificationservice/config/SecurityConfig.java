package com.example.notificationservice.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration for the Turing Online Forum System.
 * This configuration sets up open access to REST and WebSocket endpoints,
 * enables CORS for development frontend (localhost:3000),
 * and configures in-memory authentication for basic testing.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     * - CORS is enabled with custom rules.
     * - CSRF protection is disabled (important to secure in production).
     * - All requests are permitted for development purposes.
     * - Basic HTTP authentication is enabled for testing.
     *
     * @param http the HttpSecurity object provided by Spring Security
     * @return the configured SecurityFilterChain
     * @throws Exception in case of configuration error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors() // Enable CORS
                .and()
                .csrf().disable() // Disable CSRF for API development
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/**",              // Allow all paths
                                "/chat/**",         // WebSocket endpoint
                                "/ws/**",           // WebSocket prefix
                                "/topic/**",        // Message broker topics
                                "/app/**",          // WebSocket app endpoints
                                "/api/**",          // REST APIs
                                "/webjars/**",      // Static resources (SockJS client)
                                "/js/**", "/css/**", "/images/**" // Static frontend assets
                        ).permitAll()
                        .anyRequest().permitAll() // Catch-all for any unlisted route
                )
                .httpBasic(); // Enable HTTP Basic Auth for testing

        return http.build();
    }

    /**
     * Defines CORS settings for the application.
     * - Allows frontend on localhost:3000
     * - Allows common HTTP methods and headers
     * - Enables credentials (cookies, auth headers)
     * - Sets preflight cache time
     *
     * @return CorsConfigurationSource for the Spring context
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache CORS response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }

    /**
     * Defines an in-memory user for testing purposes.
     * - Username: user
     * - Password: password
     * - Role: USER
     * The {noop} prefix disables password encoding.
     *
     * @return UserDetailsService with a single test user
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                               .password("{noop}password") // No encoding
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }
}
