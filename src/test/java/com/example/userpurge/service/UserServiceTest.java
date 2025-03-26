package com.example.userpurge.service;

import com.example.userpurge.exception.UserNotFoundException;
import com.example.userpurge.model.User;
import com.example.userpurge.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit test class for {@link UserService} ensuring 100% line coverage.
 * <p>
 * Tests are executed in order:
 * <ol>
 *   <li>Retrieve user by valid and invalid ID.</li>
 *   <li>Update user profile.</li>
 *   <li>Find by ID (found and not found scenarios).</li>
 *   <li>Retrieve all users.</li>
 *   <li>Save a user.</li>
 *   <li>Delete a user (successful and failure cases).</li>
 *   <li>Test the {@link User} entity getters and setters.</li>
 * </ol>
 * </p>
 *
 * <h3>Key Test Scenarios:</h3>
 * <ul>
 *   <li><strong>Get User By ID (Success):</strong> Verify that a valid user is retrieved.</li>
 *   <li><strong>Get User By ID (Not Found):</strong> Ensure that a {@link UserNotFoundException} is thrown when the user is not found.</li>
 *   <li><strong>Update User Profile:</strong> Confirm that the user's profile details (username and email) are updated correctly.</li>
 *   <li><strong>Find By ID:</strong> Validate both scenarios where the user exists and does not exist.</li>
 *   <li><strong>Find All Users:</strong> Ensure that a list of users is returned.</li>
 *   <li><strong>Save User:</strong> Confirm that saving a user returns a non-null user with expected properties.</li>
 *   <li><strong>Delete User (Success):</strong> Verify that deletion is successful when the user exists.</li>
 *   <li><strong>Delete User (Not Found):</strong> Ensure that a {@link UserNotFoundException} is thrown when trying to delete a non-existent user.</li>
 *   <li><strong>User Entity Getters and Setters:</strong> Increase the coverage of the {@link User} model by testing its getters and setters.</li>
 * </ul>
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 999L;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(VALID_ID);
        sampleUser.setUsername("testuser");
        sampleUser.setEmail("testuser@example.com");
    }

    /**
     * Test getUserById for a valid user.
     * <p>
     * This test verifies that the service correctly retrieves a user when a valid ID is provided.
     * </p>
     */
    @Test
    @Order(1)
    void testGetUserById_Success() {
        when(userRepo.findById(VALID_ID)).thenReturn(Optional.of(sampleUser));

        User user = userService.getUserById(VALID_ID);
        assertNotNull(user, "The retrieved user should not be null.");
        assertEquals("testuser", user.getUsername(), "The username should match the expected value.");
        verify(userRepo, times(1)).findById(VALID_ID);
    }

    /**
     * Test getUserById when the user is not found.
     * <p>
     * This test ensures that a {@link UserNotFoundException} is thrown if the user with the specified ID does not exist.
     * </p>
     */
    @Test
    @Order(2)
    void testGetUserById_NotFound() {
        when(userRepo.findById(INVALID_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(INVALID_ID),
                                           "Expected UserNotFoundException when user is not found.");
        assertTrue(exception.getMessage().contains("User not found with ID: " + INVALID_ID),
                   "The exception message should indicate that the user was not found.");
        verify(userRepo, times(1)).findById(INVALID_ID);
    }

    /**
     * Test updateUserProfile to update username and email.
     * <p>
     * This test confirms that the service can update the user's profile details.
     * </p>
     */
    @Test
    @Order(3)
    void testUpdateUserProfile() {
        when(userRepo.findById(VALID_ID)).thenReturn(Optional.of(sampleUser));
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedInfo = new User();
        updatedInfo.setUsername("updateduser");
        updatedInfo.setEmail("updated@example.com");

        User result = userService.updateUserProfile(VALID_ID, updatedInfo);
        assertEquals("updateduser", result.getUsername(), "The username should be updated successfully.");
        assertEquals("updated@example.com", result.getEmail(), "The email should be updated successfully.");
        verify(userRepo, times(1)).findById(VALID_ID);
        verify(userRepo, times(1)).save(sampleUser);
    }

    /**
     * Test findById when the user exists.
     * <p>
     * This test verifies that the service returns an Optional containing the user when found.
     * </p>
     */
    @Test
    @Order(4)
    void testFindById_Found() {
        when(userRepo.findById(VALID_ID)).thenReturn(Optional.of(sampleUser));
        Optional<User> userOpt = userService.findById(VALID_ID);
        assertTrue(userOpt.isPresent(), "The Optional should contain a user.");
        assertEquals(sampleUser, userOpt.get(), "The returned user should match the sample user.");
        verify(userRepo, times(1)).findById(VALID_ID);
    }

    /**
     * Test findById when the user does not exist.
     * <p>
     * This test verifies that the service returns an empty Optional when the user is not found.
     * </p>
     */
    @Test
    @Order(5)
    void testFindById_NotFound() {
        when(userRepo.findById(INVALID_ID)).thenReturn(Optional.empty());
        Optional<User> userOpt = userService.findById(INVALID_ID);
        assertFalse(userOpt.isPresent(), "The Optional should be empty when no user is found.");
        verify(userRepo, times(1)).findById(INVALID_ID);
    }

    /**
     * Test findAll returns a list of users.
     * <p>
     * This test verifies that the service retrieves a list of users correctly.
     * </p>
     */
    @Test
    @Order(6)
    void testFindAll() {
        List<User> userList = new ArrayList<>();
        userList.add(sampleUser);
        when(userRepo.findAll()).thenReturn(userList);

        List<User> result = userService.findAll();
        assertEquals(1, result.size(), "The returned list should contain one user.");
        assertEquals(sampleUser, result.get(0), "The user in the list should match the sample user.");
        verify(userRepo, times(1)).findAll();
    }

    /**
     * Test save persists a user.
     * <p>
     * This test verifies that a user is successfully saved and returned.
     * </p>
     */
    @Test
    @Order(7)
    void testSave() {
        when(userRepo.save(any(User.class))).thenReturn(sampleUser);

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");

        User result = userService.save(newUser);
        assertNotNull(result, "The saved user should not be null.");
        verify(userRepo, times(1)).save(newUser);
    }

    /**
     * Test deleteUser successfully deletes an existing user.
     * <p>
     * This test verifies that the service deletes the user when the user exists.
     * </p>
     */
    @Test
    @Order(8)
    void testDeleteUser_Success() {
        when(userRepo.existsById(VALID_ID)).thenReturn(true);
        doNothing().when(userRepo).deleteById(VALID_ID);

        assertDoesNotThrow(() -> userService.deleteUser(VALID_ID),
                           "Deletion should occur without throwing an exception.");
        verify(userRepo, times(1)).existsById(VALID_ID);
        verify(userRepo, times(1)).deleteById(VALID_ID);
    }

    /**
     * Test deleteUser when the user does not exist.
     * <p>
     * This test verifies that attempting to delete a non-existent user results in a {@link UserNotFoundException}.
     * </p>
     */
    @Test
    @Order(9)
    void testDeleteUser_NotFound() {
        when(userRepo.existsById(INVALID_ID)).thenReturn(false);

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(INVALID_ID),
                                           "Expected a UserNotFoundException when deleting a non-existent user.");
        assertTrue(exception.getMessage().contains("User not found with id " + INVALID_ID),
                   "The exception message should indicate that the user was not found.");
        verify(userRepo, times(1)).existsById(INVALID_ID);
    }

    /**
     * Test the getters and setters of the {@link User} entity.
     * <p>
     * This test ensures that the User model's getter and setter methods work as expected,
     * increasing its code coverage to 100%.
     * </p>
     */
    @Test
    @Order(10)
    void testUserEntityGettersAndSetters() {
        User user = new User();
        Long id = 2L;
        String username = "anotheruser";
        String email = "another@example.com";

        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);

        assertEquals(id, user.getId(), "The user ID should match the value set.");
        assertEquals(username, user.getUsername(), "The username should match the value set.");
        assertEquals(email, user.getEmail(), "The email should match the value set.");
    }
}
