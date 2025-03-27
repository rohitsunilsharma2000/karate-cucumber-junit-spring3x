package com.example.userqueryhub.controller;

import com.example.userqueryhub.dto.UserDTO;
import com.example.userqueryhub.model.User;
import com.example.userqueryhub.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing users in the UserPurge system.
 *
 * <p>
 * This controller provides endpoints to save a user and retrieve a user by username. It leverages the
 * {@link UserService} to perform business logic and interact with the repository layer.
 * </p>
 *
 * <h3>Key Endpoints:</h3>
 * <ul>
 *   <li><strong>saveUser:</strong> Persists a new user entity in the system.</li>
 *   <li><strong>getUserByUsername:</strong> Retrieves a user entity by its username.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Persists a new user.
     *
     * <p>
     * Accepts a valid {@link UserDTO} containing user registration details, maps it to a {@link User} entity,
     * and persists the entity to the database.
     * </p>
     *
     * @param userDTO The data transfer object containing user registration details. Must be valid.
     * @return The persisted {@link User} entity with HTTP status 201 (Created).
     */
    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@Valid @RequestBody UserDTO userDTO) {
        // Mapping from DTO to entity (assuming matching field names).
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        User savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by username.
     *
     * <p>
     * This endpoint returns the {@link User} entity associated with the provided username.
     * If no matching user is found, the service layer should handle the error (e.g., by throwing an exception).
     * The username parameter is validated to ensure it is not blank.
     * </p>
     *
     * @param username The username of the user to retrieve. Must not be blank.
     * @return The {@link User} entity if found, with HTTP status 200 (OK).
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(
            @PathVariable("username") @NotBlank(message = "Username must not be blank") String username) {
        User user = userService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
