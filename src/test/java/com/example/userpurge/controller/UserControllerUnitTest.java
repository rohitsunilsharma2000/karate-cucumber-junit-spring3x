package com.example.userpurge.controller;

import com.example.userpurge.model.User;
import com.example.userpurge.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link UserController} ensuring that all endpoints function as expected.
 * <p>
 * These tests use Spring's {@link WebMvcTest} with {@link MockMvc} to simulate HTTP requests
 * and validate the responses returned by the controller methods. The tests also employ Mockito to
 * mock interactions with the {@link UserService}.
 * </p>
 *
 * <h3>Test Scenarios Covered:</h3>
 * <ul>
 *   <li>Retrieval of a user by ID when the user exists.</li>
 *   <li>Retrieval of all users.</li>
 *   <li>Creation of a new user with proper input validation.</li>
 *   <li>Updating an existing user and verifying the updates.</li>
 *   <li>Deletion of a user and verifying that the proper status code is returned.</li>
 * </ul>
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerUnitTest {

    // MockMvc is used to simulate HTTP requests and assert responses.
    @Autowired
    private MockMvc mockMvc;

    // Mock the UserService to isolate controller testing.
    @MockBean
    private UserService userService;

    // ObjectMapper to convert Java objects to JSON string and vice versa.
    private final ObjectMapper objectMapper = new ObjectMapper();

    // A sample user object used across multiple test cases.
    private User sampleUser;

    /**
     * Sets up a sample user before each test.
     * <p>
     * This method initializes the sample user with preset values for use in the tests.
     * </p>
     */
    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("testuser");
        sampleUser.setEmail("testuser@example.com");
    }

    /**
     * Tests the retrieval of a user by ID when the user exists.
     * <p>
     * Mocks the UserService to return the sample user and verifies that the controller
     * responds with the correct status code and user data.
     * </p>
     */
    @Test
    void testGetUserById_Success() throws Exception {
        // Mock the service call to return the sample user for ID 1.
        when(userService.getUserById(1L)).thenReturn(sampleUser);

        // Perform GET request to /api/users/1 and assert that the response is OK with expected user data.
        mockMvc.perform(get("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("testuser"));
    }

    /**
     * Tests the retrieval of all users.
     * <p>
     * Mocks the UserService to return a singleton list containing the sample user and verifies
     * that the response contains the expected data.
     * </p>
     */
    @Test
    void testGetAllUsers() throws Exception {
        // Mock the service call to return a list with the sample user.
        when(userService.findAll()).thenReturn(Collections.singletonList(sampleUser));

        // Perform GET request to /api/users and assert that the first user's username matches.
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    /**
     * Tests the creation of a new user.
     * <p>
     * Mocks the UserService to return the sample user upon creation.
     * Sends a POST request with the sample user JSON and verifies that the response status is CREATED
     * and that the returned user data matches the expected values.
     * </p>
     */
    @Test
    void testCreateUser() throws Exception {
        // Mock the service call to return the sample user when saving any User.
        when(userService.save(any(User.class))).thenReturn(sampleUser);

        // Convert sampleUser object to JSON.
        String userJson = objectMapper.writeValueAsString(sampleUser);

        // Perform POST request to /api/users with user JSON and verify response status and content.
        mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.username").value("testuser"));
    }

    /**
     * Tests updating an existing user's profile.
     * <p>
     * Mocks the UserService to return an updated user object and verifies that the controller
     * correctly processes the PUT request and returns the updated user.
     * </p>
     */
    @Test
    void testUpdateUser() throws Exception {
        // Create an updated user object with modified username and email.
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updated@example.com");

        // Mock the service call to return the updated user when updating user with ID 1.
        when(userService.updateUserProfile(eq(1L), any(User.class))).thenReturn(updatedUser);

        // Convert the updated user object to JSON.
        String userJson = objectMapper.writeValueAsString(updatedUser);

        // Perform PUT request to /api/users/1 with updated user JSON and verify response.
        mockMvc.perform(put("/api/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("updateduser"));
    }

    /**
     * Tests deletion of a user.
     * <p>
     * Mocks the UserService to do nothing on delete (simulating successful deletion) and verifies that
     * the DELETE request returns the correct HTTP status.
     * </p>
     */
    @Test
    void testDeleteUser() throws Exception {
        // Mock the deleteUser method to do nothing when called with user ID 1.
        Mockito.doNothing().when(userService).deleteUser(1L);

        // Perform DELETE request to /api/users/1 and assert that the response status is NO_CONTENT.
        mockMvc.perform(delete("/api/users/1"))
               .andExpect(status().isNoContent());
    }
}
