package com.example.videohosting.exception;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 🧪 **GlobalExceptionHandlerDirectTest**
 *
 * This test class directly invokes the GlobalExceptionHandler methods to validate their behavior.
 * It uses:
 *   - @SpringBootTest to load the full application context.
 *   - @Transactional to auto-rollback database changes after each test.
 *   - @WithMockUser(username = "john@example.com", roles = {"ADMIN"}) to simulate a secured admin user.
 *   - @TestMethodOrder with @Order to control the execution order of tests.
 *
 * Note: This test does not use any controller endpoints.
 *
 * File: src/test/java/com/example/videohosting/exception/GlobalExceptionHandlerDirectTest.java
 */
@SpringBootTest
@Transactional
//@WithMockUser(username = "john@example.com", roles = {"ADMIN"})
@TestMethodOrder(OrderAnnotation.class)
public class GlobalExceptionHandlerDirectTest {

    // Autowire the GlobalExceptionHandler bean from the context.
    @org.springframework.beans.factory.annotation.Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    // Create a mocked WebRequest for testing purposes.
    private final WebRequest webRequest = Mockito.mock(WebRequest.class);

    public GlobalExceptionHandlerDirectTest() {
        // Stub the getDescription method to return a dummy URI.
        Mockito.when(webRequest.getDescription(false)).thenReturn("uri=/test");
    }

    /**
     * Test handling of ResourceNotFoundException.
     *
     * This test calls handleResourceNotFoundException with a ResourceNotFoundException
     * and verifies that the returned ResponseEntity contains the expected error message and HTTP status 404.
     */
    @Test
    @Order(1)
    public void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Test resource not found");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleDoctorAlreadyExistsException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be 404 NOT FOUND");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Test resource not found", response.getBody().get("message"), "Error message should match");
    }

    /**
     * Test handling of AccessDeniedException.
     *
     * This test calls handleAccessDeniedException with a simulated AccessDeniedException
     * and verifies that the returned ResponseEntity contains an error message starting with "Access Denied:" and HTTP status 403.
     */
    @Test
    @Order(2)
    public void testHandleAccessDeniedException() {
        Exception ex = new org.springframework.security.access.AccessDeniedException("Test access denied");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAccessDeniedException(ex, webRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Status should be 403 FORBIDDEN");
        assertNotNull(response.getBody(), "Response body should not be null");
        String message = (String) response.getBody().get("message");
        assertTrue(message.startsWith("Access Denied:"), "Error message should start with 'Access Denied:'");
    }

    /**
     * Test handling of HttpClientErrorException.
     *
     * This test creates a HttpClientErrorException with a 400 status and calls the handler.
     * It verifies that the returned ResponseEntity contains the expected error message and HTTP status 400.
     */
    @Test
    @Order(3)
    public void testHandleHttpClientErrorException() {
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Test client error");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleHttpClientErrorException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be 400 BAD REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Bad Request", response.getBody().get("message"), "Error message should be the status reason phrase");
    }

    /**
     * Test handling of HttpServerErrorException.
     *
     * This test creates a HttpServerErrorException with a 500 status and calls the handler.
     * It verifies that the returned ResponseEntity contains the expected error message and HTTP status 500.
     */
    @Test
    @Order(4)
    public void testHandleHttpServerErrorException() {
        HttpServerErrorException ex = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Test server error");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleHttpServerErrorException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Status should be 500 INTERNAL SERVER ERROR");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Internal Server Error", response.getBody().get("message"), "Error message should be the status reason phrase");
    }
}
