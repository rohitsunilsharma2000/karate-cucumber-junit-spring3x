package com.example.userqueryhub.controller;

import com.example.userqueryhub.dto.UserDTO;
import com.example.userqueryhub.model.User;
import com.example.userqueryhub.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link com.example.userqueryhub.controller.UserController}.
 *
 * <p>
 * This class verifies the behavior of the UserController endpoints for:
 * <ul>
 *   <li>Creating a new user</li>
 *   <li>Retrieving a user by username</li>
 *   <li>Validating input for retrieving a user</li>
 * </ul>
 * The tests simulate HTTP requests using {@link MockMvc} and use Mockito to mock the {@link UserService}.
 * </p>
 *
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // Used to perform simulated HTTP requests

    @MockBean
    private UserService userService; // Mocked service to isolate controller tests

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to/from JSON

    /**
     * Tests that a valid user creation request returns HTTP 201 (Created)
     * along with the saved user's details.
     *
     * <p>
     * <strong>GIVEN:</strong> A valid {@link UserDTO} with username, email, and password.
     * <br>
     * <strong>WHEN:</strong> A POST request is sent to the user creation endpoint.
     * <br>
     * <strong>THEN:</strong> The response should be HTTP 201 and include the user's id, username, and email.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("Should create user and return 201")
    void testSaveUser() throws Exception {
        // GIVEN: Prepare a UserDTO for the request.
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("john_doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("securepassword");

        // AND: Prepare the expected saved User object.
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john_doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("securepassword");

        // WHEN: Configure the mocked userService to return the savedUser.
        when(userService.save(any(User.class))).thenReturn(savedUser);

        // THEN: Perform a POST request and expect a 201 Created status with correct JSON response.
        mockMvc.perform(post("/api/users/save")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.username").value("john_doe"))
               .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    /**
     * Tests that retrieving a user by a valid username returns HTTP 200 (OK)
     * along with the correct user details.
     *
     * <p>
     * <strong>GIVEN:</strong> A username for which a user exists.
     * <br>
     * <strong>WHEN:</strong> A GET request is sent to the user retrieval endpoint.
     * <br>
     * <strong>THEN:</strong> The response should be HTTP 200 with the user's id, username, and email.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("Should return user by username")
    void testGetUserByUsername() throws Exception {
        // GIVEN: A valid username.
        String username = "john_doe";

        // AND: Prepare the expected User object corresponding to the username.
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail("john@example.com");
        user.setPassword("securepassword");

        // WHEN: Configure the mocked userService to return the expected User.
        when(userService.getUserByUsername(username)).thenReturn(user);

        // THEN: Perform a GET request and expect a 200 OK status with the correct user details.
        mockMvc.perform(get("/api/users/username/{username}", username))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.username").value("john_doe"))
               .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    /**
     * Tests that a GET request with a blank username results in an error.
     *
     * <p>
     * <strong>GIVEN:</strong> A blank username (whitespace only).
     * <br>
     * <strong>WHEN:</strong> A GET request is sent to the user retrieval endpoint.
     * <br>
     * <strong>THEN:</strong> The response should indicate an error. Here, the test expects an internal server error,
     * though in a complete implementation, this might be handled as a 400 Bad Request.
     * </p>
     *
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    @DisplayName("Should fail validation for blank username")
    void testGetUserByUsername_Blank() throws Exception {
        // GIVEN: A blank username input.
        String blankUsername = " ";

        // WHEN & THEN: Perform a GET request with the blank username and expect an error status (Internal Server Error in this test).
        mockMvc.perform(get("/api/users/username/{username}", blankUsername))
               .andExpect(status().isInternalServerError());
    }
}
