package com.example.userqueryhub.service;

import com.example.userqueryhub.exception.UserNotFoundException;
import com.example.userqueryhub.model.User;
import com.example.userqueryhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



/**
 * Service class for managing User operations in the UserPurge system.
 *
 * <p>
 * This service provides methods for retrieving, updating, saving, and deleting user information.
 * It ensures data integrity through repository checks and assumes input validation is handled at the controller layer.
 * Detailed logging is implemented for traceability, using multiple log levels (INFO, DEBUG, ERROR) to capture execution flow and error states.
 * Custom exception handling is demonstrated by throwing {@link UserNotFoundException} when a user is not found.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Logging:</strong> Uses INFO, DEBUG, and ERROR log levels to trace method calls, input parameters, and exception occurrences.</li>
 *   <li><strong>Data Integrity:</strong> Validates user existence before performing operations, throwing {@link UserNotFoundException} when necessary.</li>
 * </ul>
 *
 * <h3>Methods:</h3>
 * <ul>
 *   <li>{@link #save(User)}: Persists a user entity, logging the operation.</li>
 *   <li>{@link #getUserByUsername(String)}: Retrieves a user by username, logs the retrieval process, and throws {@link UserNotFoundException} if no user is found.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepo;

    /**
     * Saves a new or existing user.
     *
     * <p>
     * Persists the {@link User} entity to the database. Assumes that input validation has been performed beforehand.
     * Logs the operation at INFO level.
     * </p>
     *
     * @param user The {@link User} entity to be saved.
     * @return The saved {@link User} entity.
     */
    public User save(User user) {
        log.info("Saving user: {}", user);
        User savedUser = userRepo.save(user);
        log.debug("User saved successfully: {}", savedUser);
        return savedUser;
    }

    /**
     * Retrieves a user by username.
     *
     * <p>
     * Searches for a user in the repository using the provided username. Logs the attempt at DEBUG level.
     * If no user is found, logs an ERROR and throws a {@link UserNotFoundException}.
     * </p>
     *
     * @param username The username of the user to retrieve.
     * @return The {@link User} entity associated with the given username.
     * @throws UserNotFoundException if no user is found with the specified username.
     */
    public User getUserByUsername(String username) {
        log.debug("Attempting to retrieve user with username: {}", username);
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User with username '{}' not found", username);
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }
        log.info("User retrieved successfully: {}", user);
        return user;
    }

}
