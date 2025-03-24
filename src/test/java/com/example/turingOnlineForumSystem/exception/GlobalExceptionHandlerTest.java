package com.example.turingOnlineForumSystem.exception;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleResourceNotFound() {
        // Given
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        // When
        ResponseEntity<?> response = handler.handleResourceNotFound(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void testHandleGenericException() {
        // Given
        Exception ex = new RuntimeException("Something went wrong");

        // When
        ResponseEntity<?> response = handler.handleGeneric(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Internal server error");
    }
}
