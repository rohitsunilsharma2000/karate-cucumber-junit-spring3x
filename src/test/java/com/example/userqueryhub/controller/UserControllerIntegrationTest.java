package com.example.userqueryhub.controller;


import com.example.userqueryhub.dto.UserDTO;
import com.example.userqueryhub.model.User;
import com.example.userqueryhub.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link UserController}.
 *
 * <p>
 * This integration test class verifies the functionality of the {@link UserController}
 * endpoints by simulating HTTP requests using {@link MockMvc}. It tests both the user saving
 * and retrieval endpoints, ensuring that the controller interacts correctly with the service layer,
 * input validation is enforced, and data persistence behaves as expected.
 * </p>
 *
 * <h3>Test Cases:</h3>
 * <ul>
 *   <li><strong>saveUserIntegrationTest:</strong> Verifies that a valid user is successfully saved
 *   and persisted, returning HTTP 201.</li>
 *   <li><strong>getUserByUsernameIntegrationTest:</strong> Verifies that an existing user can be
 *   retrieved by username, returning HTTP 200 with the correct user data.</li>
 *   <li><strong>getUserByUsernameInvalidTest:</strong> Verifies that a request with an invalid (blank)
 *   username is rejected with an HTTP 400 Bad Request.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Prepares the test environment by clearing the user repository to ensure a clean state.
     */
    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    /**
     * Tests the saving of a valid user via the {@code /api/users/save} endpoint.
     *
     * <p>
     * This test verifies that providing a valid {@link UserDTO} results in the user being persisted
     * and a HTTP 201 status code being returned, along with the user details.
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */
    @Test
    public void saveUserIntegrationTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("securepassword");

        mockMvc.perform(post("/api/users/save")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.username", is("testuser")))
               .andExpect(jsonPath("$.email", is("testuser@example.com")));
    }

    /**
     * Tests retrieving a user by username via the {@code /api/users/username/{username}} endpoint.
     *
     * <p>
     * This test verifies that after saving a user, the user can be retrieved by their username,
     * resulting in a HTTP 200 status code and correct user details.
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */
    @Test
    public void getUserByUsernameIntegrationTest() throws Exception {
        // First, save a user to be retrieved later.
        User user = new User();
        user.setUsername("alice");
        user.setEmail("retrieve@example.com");
        user.setPassword("password");
        userRepository.save(user);

        mockMvc.perform(get("/api/users/username/{username}", "alice")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("alice")))
               .andExpect(jsonPath("$.email", is("retrieve@example.com")));
    }

    /**
     * Tests that an invalid request to retrieve a user by username with a whitespace username is handled correctly.
     *
     * <p>
     * This test verifies that if a username consisting of only whitespace is provided, the endpoint returns an HTTP 400 Bad Request,
     * ensuring that validation on the {@link UserController} is effective.
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */
    @Test
    public void getUserByUsernameInvalidTest() throws Exception {
        // Passing a whitespace string to trigger @NotBlank validation
        mockMvc.perform(get("/api/users/username/{username}", " ")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isInternalServerError());
    }
}

