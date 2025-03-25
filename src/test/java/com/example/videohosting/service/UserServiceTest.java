package com.example.videohosting.service;


import com.example.videohosting.model.User;
import com.example.videohosting.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 🧪 **UserServiceTest**
 * <p>
 * This test class verifies the functionality of UserService.
 * It uses @SpringBootTest to load the full application context,
 *
 * @AutoConfigureMockMvc to configure the web environment,
 * @Transactional to auto-rollback DB changes after each test,
 * @WithMockUser to simulate a secured user context,
 * and @Order to control the execution order of tests.
 * <p>
 * File: src/test/java/com/example/videohosting/service/UserServiceTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@WithMockUser
@TestMethodOrder(OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Test case for saving a new user.
     * <p>
     * This test creates a new User object, saves it using UserService,
     * and asserts that the user is persisted with a generated ID and correct attributes.
     */
    @Test
    @Order(1)
    public void testSaveUser() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("secret");
        user.setRole("USER");

        User savedUser = userService.save(user);

        assertNotNull(savedUser.getId(), "User ID should be generated after saving.");
        assertEquals("john", savedUser.getUsername(), "Username should be 'john'.");
    }

    /**
     * Test case for retrieving a user by username.
     * <p>
     * This test first saves a new User and then retrieves it using the findByUsername method.
     * It verifies that the retrieved user's attributes match the expected values.
     */
    @Test
    @Order(2)
    public void testFindByUsername() {
        // Create and save a new user to later retrieve by username
        User user = new User();
        user.setUsername("alice");
        user.setPassword("password");
        user.setRole("USER");
        userService.save(user);

        User foundUser = userService.findByUsername("alice");

        assertNotNull(foundUser, "User should be found by username.");
        assertEquals("alice", foundUser.getUsername(), "Found user's username should be 'alice'.");
    }
}

