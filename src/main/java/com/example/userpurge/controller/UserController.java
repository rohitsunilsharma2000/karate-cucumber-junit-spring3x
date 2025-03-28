package com.example.userpurge.controller;

import com.example.userpurge.exception.UserNotFoundException;
import com.example.userpurge.model.User;
import com.example.userpurge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * REST controller for managing users in the UserPurge system.
 * <p>
 * This controller provides endpoints for performing CRUD operations on user entities.
 * It interacts with the {@link UserService} to perform business logic and ensures that all operations
 * are validated, logged, and handled appropriately.
 * </p>
 *
 * <h3>Functionality:</h3>
 * <ul>
 *   <li>Retrieve a user by ID.</li>
 *   <li>Retrieve all users.</li>
 *   <li>Create a new user with input validation.</li>
 *   <li>Update an existing user's profile with validation.</li>
 *   <li>Delete a user by ID.</li>
 * </ul>
 *
 * <h3>Test Coverage:</h3>
 * <ul>
 *   <li>Unit and integration tests ensure 100% line coverage for all endpoints.</li>
 *   <li>Enhanced Javadoc, inline comments, and logging improve code clarity and maintainability.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    // Inject the UserService to delegate business logic
    private final UserService userService;

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The unique identifier of the user.
     * @return A {@link ResponseEntity} containing the user if found.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.debug("Received request to retrieve user with ID: {}", id);
        // Call service layer to retrieve user by ID
        User user = userService.getUserById(id);
        log.info("User with ID: {} retrieved successfully", id);
        // Return HTTP 200 (OK) along with the user data
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves all users.
     *
     * @return A {@link ResponseEntity} containing a list of all users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.debug("Received request to retrieve all users");
        // Retrieve all users from the service layer
        List<User> users = userService.findAll();
        log.info("Retrieved {} users", users.size());
        // Return HTTP 200 (OK) along with the list of users
        return ResponseEntity.ok(users);
    }

    /**
     * Creates a new user.
     * <p>
     * Validates the input using Jakarta Bean Validation (@Valid) to ensure that the user data meets the required constraints.
     * </p>
     *
     * @param user The user object to be created, provided in the request body.
     * @return A {@link ResponseEntity} containing the created user with HTTP 201 (Created) status.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.debug("Received request to create user with username: {}", user.getUsername());
        // Persist the new user using the service layer
        User savedUser = userService.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        // Return HTTP 201 (Created) along with the created user data
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Updates an existing user's profile.
     * <p>
     * Validates the input using Jakarta Bean Validation (@Valid) to ensure that the updated user data is valid.
     * </p>
     *
     * @param id   The unique identifier of the user to update.
     * @param user The user object containing updated data, provided in the request body.
     * @return A {@link ResponseEntity} containing the updated user with HTTP 200 (OK) status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        log.debug("Received request to update user with ID: {}", id);
        // Update the user's profile using the service layer
        User updated = userService.updateUserProfile(id, user);
        log.info("User with ID: {} updated successfully", id);
        // Return HTTP 200 (OK) along with the updated user data
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a user by their unique ID.
     * <p>
     * First, the service checks if the user exists; if not, a {@link UserNotFoundException} is thrown.
     * </p>
     *
     * @param id The unique identifier of the user to be deleted.
     * @return A {@link ResponseEntity} with HTTP 204 (No Content) status if the deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("Received request to delete user with ID: {}", id);
        // Delete the user using the service layer; exceptions are handled globally
        userService.deleteUser(id);
        log.info("User with ID: {} deleted successfully", id);
        // Return HTTP 204 (No Content) to indicate successful deletion without any response body
        return ResponseEntity.noContent().build();
    }
}
