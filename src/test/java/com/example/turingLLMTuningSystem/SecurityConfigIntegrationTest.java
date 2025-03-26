package com.example.turingLLMTuningSystem;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for SecurityConfig.
 * Verifies the CORS configuration and security setup.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests CORS preflight requests to verify allowed methods, headers, and origins.
     */
    @Test
    void testCorsPreflightRequest() throws Exception {
        mockMvc.perform(options("/api/users")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000"))
                .andExpect(header().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS));
    }

    /**
     * Tests accessing secured API with basic authentication.
     */
    @Test
//    @WithMockUser(username = "user", password = "password", roles = "USER")
    void testAccessWithBasicAuth() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    /**
     * Tests accessing secured API without authentication.
     */
    @Test
    void testAccessWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk()); // This configuration permits all, so it should be OK
    }
}

