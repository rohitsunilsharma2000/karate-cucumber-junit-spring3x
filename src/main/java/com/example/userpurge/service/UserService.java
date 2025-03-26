package com.example.userpurge.service;

import com.example.userpurge.exception.UserNotFoundException;
import com.example.userpurge.model.User;
import com.example.userpurge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing User operations in the Turing LLM Tuning System.
 * <p>
 * This service provides methods for retrieving, updating, saving, and deleting user information.
 * It ensures data integrity through repository checks and assumes input validation is handled at the controller layer.
 * Detailed logging is implemented for traceability, using multiple log levels (INFO, DEBUG, ERROR) to capture execution flow and error states.
 * Custom exception handling is demonstrated by throwing {@link UserNotFoundException} when a user is not found.
 * </p>
 *
 * <h3>Input Validation and Data Integrity:</h3>
 * <ul>
 *   <li>Controller-level validations (e.g., using {@code @Valid} annotations) should ensure that only valid data reaches this service.</li>
 *   <li>Data integrity is maintained by verifying the existence of entities (e.g., checking if a user exists before deletion or update).</li>
 * </ul>
 *
 * <h3>Logging and Traceability:</h3>
 * <ul>
 *   <li>INFO level logs mark high-level operations (e.g., successful updates or deletions).</li>
 *   <li>DEBUG level logs can be used for detailed tracing of inputs and method execution (e.g., logging input parameters).</li>
 *   <li>ERROR level logs capture exceptions and error conditions.</li>
 * </ul>
 *
 * <h3>Custom Exception Handling:</h3>
 * <ul>
 *   <li>The custom exception {@link UserNotFoundException} is thrown to signal that a user was not found, providing clearer error messaging for consumers of this service.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-01-28
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepo;

    /**
     * Retrieves a user by their ID.
     * <p>
     * Uses the repository to find a user and throws a {@link UserNotFoundException} if the user is not found.
     * Logs the operation at DEBUG level for traceability.
     * </p>
     *
     * @param id The unique identifier of the user.
     * @return The found {@link User} entity.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    public User getUserById(Long id) {
        log.debug("Attempting to retrieve user with ID: {}", id);
        return userRepo.findById(id)
                       .orElseThrow(() -> {
                           log.error("User not found with ID: {}", id);
                           return new UserNotFoundException("User not found with ID: " + id);
                       });
    }

    /**
     * Updates the profile of an existing user.
     * <p>
     * Retrieves the current user, updates the username and email, and saves the updated entity.
     * Logs the update process at INFO level.
     * </p>
     *
     * @param id          The unique identifier of the user to update.
     * @param updatedUser A {@link User} entity containing updated profile information.
     * @return The updated {@link User} entity.
     */
    public User updateUserProfile(Long id, User updatedUser) {
        log.info("Updating profile for user with ID: {}", id);
        User existing = getUserById(id);
        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        User savedUser = userRepo.save(existing);
        log.debug("User with ID: {} updated successfully.", id);
        return savedUser;
    }

    /**
     * Finds a user by ID.
     * <p>
     * This method wraps the repository's findById call.
     * </p>
     *
     * @param id The unique identifier of the user.
     * @return An {@link Optional} containing the {@link User} if found, or empty otherwise.
     */
    public Optional<User> findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        return userRepo.findById(id);
    }

    /**
     * Retrieves all users.
     * <p>
     * Returns a list of all {@link User} entities in the system.
     * </p>
     *
     * @return A list of {@link User} entities.
     */
    public List<User> findAll() {
        log.debug("Retrieving all users from the database.");
        return userRepo.findAll();
    }

    /**
     * Saves a new or existing user.
     * <p>
     * Persists the {@link User} entity to the database. Assumes that input validation has been performed beforehand.
     * </p>
     *
     * @param user The {@link User} entity to be saved.
     * @return The saved {@link User} entity.
     */
    public User save(User user) {
        log.info("Saving user: {}", user);
        return userRepo.save(user);
    }

    /**
     * Deletes a user by their ID.
     * <p>
     * Checks if the user exists before deletion. If the user does not exist, a {@link UserNotFoundException} is thrown.
     * Uses {@code deleteById} for efficient deletion.
     * </p>
     *
     * @param userId The unique identifier of the user to be deleted.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    public void deleteUser(Long userId) {
        log.info("Attempting to delete user with ID: {}", userId);
        if (userRepo.existsById(userId)) {
            userRepo.deleteById(userId);
            log.debug("User with ID: {} deleted successfully.", userId);
        } else {
            log.error("Failed to delete - user not found with ID: {}", userId);
            throw new UserNotFoundException("User not found with id " + userId);
        }
    }
}
