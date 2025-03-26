package com.example.turingLLMTuningSystem;


import com.example.turingLLMTuningSystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for GlobalExceptionHandler.
 * Validates exception handling for different HTTP error scenarios.
 */
@WebMvcTest
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    //    @MockBean
    private TestController testController; // Dummy controller to trigger exceptions

    /**
     * Tests handling of HttpClientErrorException (4xx errors).
     */
    @Test
    void testHandleHttpClientErrorException() throws Exception {
        when(testController.trigger()).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    /**
     * Tests handling of HttpServerErrorException (5xx errors).
     */
    @Test
    void testHandleHttpServerErrorException() throws Exception {
        when(testController.trigger()).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/test"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    /**
     * Tests handling of AccessDeniedException.
     */
    @Test
    void testHandleAccessDeniedException() throws Exception {
        when(testController.trigger()).thenThrow(new AccessDeniedException("Not Authorized"));

        mockMvc.perform(get("/test"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Access Denied: Not Authorized"));
    }

    /**
     * Tests handling of ResourceNotFoundException.
     */
    @Test
    void testHandleResourceNotFoundException() throws Exception {
        when(testController.trigger()).thenThrow(new ResourceNotFoundException("Resource not found"));

        mockMvc.perform(get("/test"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    // Dummy controller interface for testing purposes
    interface TestController {
        String trigger();
    }
}

