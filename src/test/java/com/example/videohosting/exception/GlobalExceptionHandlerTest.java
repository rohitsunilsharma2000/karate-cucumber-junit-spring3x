package com.example.videohosting.exception;


import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 🧪 **GlobalExceptionHandlerTest**
 * <p>
 * This test class validates the functionality of GlobalExceptionHandler.
 * It uses:
 * - @SpringBootTest to load the full application context.
 * - @AutoConfigureMockMvc to configure the web environment.
 * - @Transactional to auto-rollback DB changes after each test.
 * - @WithMockUser to simulate a secured user context.
 * - @TestMethodOrder with @Order to control the execution order of tests.
 * <p>
 * A dummy controller (DummyExceptionController) is defined here to trigger exceptions for testing.
 * <p>
 * File: src/test/java/com/example/videohosting/exception/GlobalExceptionHandlerTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional

@TestMethodOrder(OrderAnnotation.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test case for ResourceNotFoundException handling.
     * <p>
     * This test performs a GET request to trigger a ResourceNotFoundException and verifies
     * that the response contains the expected error message and HTTP status 404.
     */
    @Test
    @Order(1)
    public void testHandleResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/api/test/resourceNotFound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Test resource not found"));
    }

    /**
     * Test case for AccessDeniedException handling.
     * <p>
     * This test performs a GET request to trigger an AccessDeniedException and verifies
     * that the response contains the expected error message and HTTP status 403.
     */
    @Test
    @Order(2)
    public void testHandleAccessDeniedException() throws Exception {
        mockMvc.perform(get("/api/test/accessDenied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(containsString("Access Denied:")));
    }

    /**
     * Test case for HttpClientErrorException handling.
     * <p>
     * This test performs a GET request to trigger a HttpClientErrorException and verifies
     * that the response contains the expected error message and HTTP status 400.
     */
    @Test
    @Order(3)
    public void testHandleHttpClientErrorException() throws Exception {
        mockMvc.perform(get("/api/test/clientError"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Test client error"));
    }

    /**
     * Test case for HttpServerErrorException handling.
     * <p>
     * This test performs a GET request to trigger a HttpServerErrorException and verifies
     * that the response contains the expected error message and HTTP status 500.
     */
    @Test
    @Order(4)
    public void testHandleHttpServerErrorException() throws Exception {
        mockMvc.perform(get("/api/test/serverError"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Test server error"));
    }

    /**
     * Dummy controller to trigger various exceptions.
     */
    @RestController
    @RequestMapping("/api/test")
    public static class DummyExceptionController {

        @GetMapping("/resourceNotFound")
        public void resourceNotFound() {
            throw new ResourceNotFoundException("Test resource not found");
        }

        @GetMapping("/accessDenied")
        public void accessDenied() {
            throw new org.springframework.security.access.AccessDeniedException("Test access denied");
        }

        @GetMapping("/clientError")
        public void clientError() {
            throw new org.springframework.web.client.HttpClientErrorException(HttpStatus.BAD_REQUEST, "Test client error");
        }

        @GetMapping("/serverError")
        public void serverError() {
            throw new org.springframework.web.client.HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Test server error");
        }
    }
}

