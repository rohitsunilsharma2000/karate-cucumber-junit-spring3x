package com.example.graph.config;

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
 * Security configuration for the Notification Service Application.
 *
 * <p><strong>Overview:</strong></p>
 * This configuration sets up the security filter chain for the Notification Service. It:
 * <ul>
 *   <li>Enables CORS with custom rules to allow requests from development frontends (e.g., localhost:3000).</li>
 *   <li>Disables CSRF protection for API development (note: CSRF protection should be enabled in production).</li>
 *   <li>Permits open access to all endpoints for development and testing purposes.</li>
 *   <li>Enables HTTP Basic authentication for secured endpoints during testing.</li>
 *   <li>Configures in-memory authentication with a single test user.</li>
 * </ul>
 *
 * <p><strong>References:</strong></p>
 * <ul>
 *   <li>{@link SecurityFilterChain} is used to build and configure the filter chain.</li>
 *   <li>{@link CorsConfigurationSource} sets the CORS rules applied to all endpoints.</li>
 *   <li>{@link InMemoryUserDetailsManager} is used for in-memory user authentication.</li>
 * </ul>
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *   <li>Applies custom CORS rules allowing origins "http://localhost:3000" and "http://127.0.0.1:3000", with permitted methods GET, POST, PUT, DELETE, and OPTIONS.</li>
 *   <li>Disables CSRF protection to simplify API development.</li>
 *   <li>Allows open access to all endpoints ("/**" and "/api/**") for development purposes.</li>
 *   <li>Enables HTTP Basic authentication to secure endpoints during testing.</li>
 *   <li>Defines an in-memory user with username "user", password "password" (with no encoding), and role "USER".</li>
 * </ul>
 *
 * <p><strong>Error Conditions:</strong></p>
 * <ul>
 *   <li>If CORS is misconfigured, cross-origin requests may fail.</li>
 *   <li>Disabling CSRF in production environments can expose the application to CSRF attacks.</li>
 *   <li>If in-memory user details are misconfigured, authentication may fail, resulting in unauthorized access.</li>
 * </ul>
 *
 * <p><strong>Acceptable Values / Range:</strong></p>
 * <ul>
 *   <li><strong>Allowed Origins:</strong> "http://localhost:3000" and "http://127.0.0.1:3000"</li>
 *   <li><strong>Allowed Methods:</strong> GET, POST, PUT, DELETE, OPTIONS</li>
 *   <li><strong>User Credentials:</strong> Username "user", password "password" (with {noop} encoding), role "USER"</li>
 * </ul>
 *
 * <p><strong>Premise and Assertions:</strong></p>
 * <ul>
 *   <li>The configuration assumes a development or testing environment where open access is acceptable.</li>
 *   <li>It is expected that these settings will be revised for production to enforce stricter security controls.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> The SecurityFilterChain is correctly configured, permitting valid CORS requests, proper authentication,
 *       and open access as defined.</li>
 *   <li><strong>Fail:</strong> Unauthorized access attempts or misconfigured endpoints result in failure, typically yielding a 403 Forbidden response.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * <p><strong>Description:</strong></p>
     * This method sets up the security filter chain by:
     * <ul>
     *   <li>Enabling CORS with the defined custom configuration.</li>
     *   <li>Disabling CSRF protection for API development (ensure to secure this in production).</li>
     *   <li>Permitting all requests to endpoints ("/**" and "/api/**") for development purposes.</li>
     *   <li>Enabling HTTP Basic authentication to secure endpoints for testing.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If CORS settings are not applied correctly, cross-origin requests may fail.</li>
     *   <li>If CSRF is disabled in production, the application may be vulnerable to CSRF attacks.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> The filter chain builds successfully, and valid requests are processed with appropriate authentication.</li>
     *   <li><strong>Fail:</strong> Misconfiguration results in security exceptions or unauthorized access errors.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object provided by Spring Security.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors() // Enable CORS
                .and()
                .csrf().disable() // Disable CSRF for API development
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/**",      // Allow all paths
                                "/api/**"   // REST APIs
                        ).permitAll()
                        .anyRequest().permitAll() // Catch-all for any unlisted route
                )
                .httpBasic(); // Enable HTTP Basic authentication for testing

        return http.build();
    }

    /**
     * Defines the CORS configuration for the application.
     *
     * <p><strong>Description:</strong></p>
     * This method sets up CORS rules to allow requests from the development frontend running on
     * "http://localhost:3000" and "http://127.0.0.1:3000". It permits common HTTP methods and headers,
     * enables credentials, and sets the preflight cache duration.
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If the allowed origins or methods are misconfigured, legitimate cross-origin requests may be blocked.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> The CORS configuration is correctly applied to all endpoints.</li>
     *   <li><strong>Fail:</strong> Misconfigured CORS settings result in failed cross-origin requests.</li>
     * </ul>
     *
     * @return a {@link CorsConfigurationSource} containing the CORS settings.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply configuration to all endpoints
        return source;
    }

    /**
     * Defines an in-memory user for testing purposes.
     *
     * <p><strong>Description:</strong></p>
     * This method creates a simple in-memory user with the following details:
     * <ul>
     *   <li><strong>Username:</strong> "user"</li>
     *   <li><strong>Password:</strong> "password" (with {noop} to indicate no password encoding)</li>
     *   <li><strong>Role:</strong> "USER"</li>
     * </ul>
     * This user is intended for development and testing of authentication mechanisms.
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If the user details are misconfigured, authentication will fail, resulting in access errors.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> The in-memory user is correctly configured, allowing successful authentication.</li>
     *   <li><strong>Fail:</strong> Misconfiguration of user details leads to failed authentication.</li>
     * </ul>
     *
     * @return an instance of {@link UserDetailsService} containing the test user.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                               .password("{noop}password") // No encoding used for testing purposes
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }
}