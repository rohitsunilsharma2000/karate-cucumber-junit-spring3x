package com.example.userqueryhub.controller;

import com.example.userqueryhub.dto.UserDTO;
import com.example.userqueryhub.exception.UserNotFoundException;
import com.example.userqueryhub.model.User;
import com.example.userqueryhub.repository.UserRepository;
import com.example.userqueryhub.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link com.example.userqueryhub.controller.UserController}.
 *
 * <p>
 * This integration test class verifies the functionality of the UserController endpoints by simulating HTTP requests using {@link MockMvc}.
 * It tests both the user saving and retrieval endpoints to ensure that:
 * <ul>
 *   <li>User creation via {@code /api/users/save} persists the user and returns HTTP 201 with the correct user details.</li>
 *   <li>User retrieval via {@code /api/users/username/{username}} returns HTTP 200 with the correct data.</li>
 *   <li>Validation on user retrieval (e.g., blank username) is enforced.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Test Cases:</strong>
 * <ul>
 *   <li><strong>saveUserIntegrationTest:</strong> Valid user creation returns 201 and persisted user data.</li>
 *   <li><strong>getUserByUsernameIntegrationTest:</strong> Retrieval of an existing user by username returns 200 with correct details.</li>
 *   <li><strong>getUserByUsernameInvalidTest:</strong> A request with an invalid (blank) username results in an error (HTTP 400/500).</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Used to perform simulated HTTP requests

    @Autowired
    private UserRepository userRepository; // Repository used for data persistence in tests

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to/from JSON

    /**
     * Prepares the test environment by clearing the user repository to ensure a clean state before each test.
     */
    @BeforeEach
    public void setup() {
        // Delete all users to ensure tests run with a clean slate
        userRepository.deleteAll();
    }

    /**
     * Integration test for saving a valid user via the {@code /api/users/save} endpoint.
     *
     * <p>
     * <strong>GIVEN:</strong> A valid {@link UserDTO} with username, email, and password.
     * <br>
     * <strong>WHEN:</strong> A POST request is sent to the user creation endpoint.
     * <br>
     * <strong>THEN:</strong> The response should return HTTP 201 (Created) along with the persisted user details.
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */
    @Test
    public void saveUserIntegrationTest() throws Exception {
        // GIVEN: Create a valid UserDTO for the request.
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("securepassword");

        // WHEN & THEN: Perform a POST request to /api/users/save and expect HTTP 201.
        mockMvc.perform(post("/api/users/save")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists()) // Verify that an ID is returned
               .andExpect(jsonPath("$.username", is("testuser")))
               .andExpect(jsonPath("$.email", is("testuser@example.com")));
    }

    /**
     * Integration test for retrieving a user by username via the {@code /api/users/username/{username}} endpoint.
     *
     * <p>
     * <strong>GIVEN:</strong> A user has been saved with a specific username.
     * <br>
     * <strong>WHEN:</strong> A GET request is sent to retrieve the user by username.
     * <br>
     * <strong>THEN:</strong> The response should return HTTP 200 (OK) with the correct user details.
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */
    @Test
    public void getUserByUsernameIntegrationTest() throws Exception {
        // GIVEN: Create and save a user that can be retrieved later.
        User user = new User();
        user.setUsername("alice");
        user.setEmail("retrieve@example.com");
        user.setPassword("password");
        userRepository.save(user);

        // WHEN & THEN: Perform a GET request to /api/users/username/alice and expect HTTP 200 with the correct user details.
        mockMvc.perform(get("/api/users/username/{username}", "alice")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("alice")))
               .andExpect(jsonPath("$.email", is("retrieve@example.com")));
    }

    /**
     * Integration test for retrieving a user with an invalid (blank) username.
     *
     * <p>
     * <strong>GIVEN:</strong> A blank (whitespace-only) username is provided.
     * <br>
     * <strong>WHEN:</strong> A GET request is sent to the user retrieval endpoint.
     * <br>
     * <strong>THEN:</strong> The endpoint should reject the request with an HTTP 400 Bad Request (or internal error if not properly validated).
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */


    @Test
    public void getUserByUsernameNotFoundTest() throws Exception {
        // GIVEN
        String username = "non_existent_user";
        when(userService.getUserByUsername(username))
                .thenThrow(new UserNotFoundException("User with username '" + username + "' not found"));

        // WHEN & THEN
        mockMvc.perform(get("/api/users/username/{username}", username)
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(content().string("User with username 'non_existent_user' not found"));
    }



}
