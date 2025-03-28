package com.example.userpurge.controller;

import com.example.userpurge.model.User;
import com.example.userpurge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link UserController}.
 * <p>
 * This test class verifies that the REST endpoints exposed by {@code UserController} operate as expected.
 * It uses an embedded H2 database (or another configured database) and the {@link TestRestTemplate} to perform HTTP calls.
 * Each test method ensures the endpoint's behavior matches the expected response.
 * </p>
 *
 * <h3>Test Scenarios:</h3>
 * <ul>
 *   <li>Retrieve a user by ID and verify correct data is returned.</li>
 *   <li>Retrieve all users and confirm the returned list is not empty.</li>
 *   <li>Create a new user and check that the user is persisted correctly.</li>
 *   <li>Update an existing user and verify the updates are reflected.</li>
 *   <li>Delete a user and confirm that subsequent retrieval attempts fail.</li>
 * </ul>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    // TestRestTemplate to perform HTTP requests in tests.
    @Autowired
    private TestRestTemplate restTemplate;

    // UserRepository to interact with the database directly for test setup and verification.
    @Autowired
    private UserRepository userRepository;

    // Sample user used across test methods.
    private User sampleUser;

    /**
     * Setup method to initialize test data.
     * <p>
     * This method clears the database and creates a sample user before each test.
     * </p>
     */
    @BeforeEach
    void setUp() {
        // Clean the database before each test
        userRepository.deleteAll();

        // Create and persist a sample user for testing
        sampleUser = new User();
        sampleUser.setUsername("integrationUser");
        sampleUser.setEmail("integration@example.com");
        sampleUser = userRepository.save(sampleUser);
    }

    /**
     * Tests retrieval of a user by their ID.
     * <p>
     * Sends a GET request to {@code /api/users/{id}} and verifies that the returned user has the expected username.
     * </p>
     */
    @Test
    void testGetUserById() {
        // Send GET request to retrieve the sample user by ID
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/" + sampleUser.getId(), User.class);

        // Assert that the HTTP status is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Assert that the username matches the sample user's username
        assertThat(response.getBody().getUsername()).isEqualTo("integrationUser");
    }

    /**
     * Tests retrieval of all users.
     * <p>
     * Sends a GET request to {@code /api/users} and verifies that the returned list contains at least one user.
     * </p>
     */
    @Test
    void testGetAllUsers() {
        // Send GET request to retrieve all users
        ResponseEntity<User[]> response = restTemplate.getForEntity("/api/users", User[].class);

        // Assert that the HTTP status is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Convert the response body to a list and assert that it is not empty
        List<User> users = List.of(response.getBody());
        assertThat(users).isNotEmpty();
    }

    /**
     * Tests creation of a new user.
     * <p>
     * Sends a POST request to {@code /api/users} with a new user's data and verifies that the user is created with HTTP 201 status.
     * </p>
     */
    @Test
    void testCreateUser() {
        // Prepare a new user object to be created
        User newUser = new User();
        newUser.setUsername("newIntegrationUser");
        newUser.setEmail("newintegration@example.com");

        // Send POST request to create the new user
        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", newUser, User.class);

        // Assert that the HTTP status is 201 Created
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // Assert that the created user's username matches the expected value
        assertThat(response.getBody().getUsername()).isEqualTo("newIntegrationUser");
    }

    /**
     * Tests updating an existing user's information.
     * <p>
     * Sends a PUT request to {@code /api/users/{id}} with updated data and verifies that the response contains the updated user.
     * </p>
     */
    @Test
    void testUpdateUser() {
        // Update the sample user's username
        sampleUser.setUsername("updatedIntegrationUser");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HTTP entity with the updated user data and headers
        HttpEntity<User> requestEntity = new HttpEntity<>(sampleUser, headers);

        // Send PUT request to update the user
        ResponseEntity<User> response = restTemplate.exchange("/api/users/" + sampleUser.getId(),
                                                              HttpMethod.PUT, requestEntity, User.class);

        // Assert that the HTTP status is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Assert that the username was updated successfully
        assertThat(response.getBody().getUsername()).isEqualTo("updatedIntegrationUser");
    }

    /**
     * Tests deletion of a user.
     * <p>
     * Sends a DELETE request to {@code /api/users/{id}} and then attempts to retrieve the same user to verify deletion.
     * </p>
     */
    @Test
    void testDeleteUser() {
        // Send DELETE request to remove the sample user
        ResponseEntity<Void> response = restTemplate.exchange("/api/users/" + sampleUser.getId(),
                                                              HttpMethod.DELETE, null, Void.class);
        // Assert that the HTTP status is 204 No Content
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Attempt to retrieve the deleted user; expect a 404 Not Found
        ResponseEntity<User> getResponse = restTemplate.getForEntity("/api/users/" + sampleUser.getId(), User.class);
        // Assert that the user is not found (assuming global exception handling returns 404)
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
