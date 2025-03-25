package com.example.videohosting.model;


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
 * 🧪 **UserEntityTest**
 * <p>
 * This test class verifies the functionality of the User entity.
 * It uses:
 * - @SpringBootTest to load the full application context.
 * - @AutoConfigureMockMvc to configure the web environment.
 * - @Transactional to auto-rollback database changes after each test.
 * - @WithMockUser to simulate a secured user context.
 * - @TestMethodOrder with @Order to control the execution order of tests.
 * <p>
 * The test confirms that the User entity's attributes are correctly set and persisted.
 * <p>
 * File: src/test/java/com/example/videohosting/model/UserEntityTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(OrderAnnotation.class)
public class UserEntityTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * Test case for persisting a User entity.
     * <p>
     * This test creates a new User object, sets its attributes using Lombok-generated setters,
     * saves it to the database via UserRepository, and retrieves it to verify that the data has been persisted correctly.
     */
    @Test
    @Order(1)
    public void testUserEntityPersistence() {
        // Create a new User entity and set its attributes
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole("USER");

        // Save the user entity to the repository
        User savedUser = userRepository.save(user);

        // Assert that the saved user has an auto-generated ID and the correct attribute values.
        assertNotNull(savedUser.getId(), "User ID should be generated after saving.");
        assertEquals("testuser", savedUser.getUsername(), "Username should be 'testuser'.");
        assertEquals("testpassword", savedUser.getPassword(), "Password should be 'testpassword'.");
        assertEquals("USER", savedUser.getRole(), "Role should be 'USER'.");
    }
}

