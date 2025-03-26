package com.example.turingLLMTuningSystem;


import com.example.turingLLMTuningSystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for ResourceNotFoundException handling.
 * Ensures correct HTTP status and response structure when a resource is not found.
 */
@WebMvcTest
class ResourceNotFoundExceptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    //    @MockBean
    private DummyResourceController dummyResourceController;

    /**
     * Test handling ResourceNotFoundException thrown by the controller.
     */
    @Test
    void testResourceNotFoundException() throws Exception {
        String errorMessage = "Resource with ID 1 not found";

        when(dummyResourceController.getResourceById(1L)).thenThrow(new ResourceNotFoundException(errorMessage));

        mockMvc.perform(get("/api/dummy-resources/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    // Dummy controller interface for testing purposes
    interface DummyResourceController {
        String getResourceById(Long id);
    }
}
