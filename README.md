###  Configure `pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
		<modelVersion>4.0.0</modelVersion>
		<parent>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-parent</artifactId>
			<version>3.4.2</version>
			<relativePath/> <!-- lookup parent from repository -->
		</parent>
		<groupId>com.example</groupId>
		<artifactId>turingOnlineForumSystem</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<name>Turing online forum system</name>
		<description>
			Build a complete Spring Boot online forum with discussion threads, user moderation tools, private messaging, and community features.
		</description>
		<url/>
		<licenses>
			<license/>
		</licenses>
		<developers>
			<developer/>
		</developers>
		<scm>
			<connection/>
			<developerConnection/>
			<tag/>
			<url/>
		</scm>
		<properties>
			<java.version>17</java.version>
		</properties>
		<dependencies>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter</artifactId>
			</dependency>

			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-test</artifactId>
				<scope>test</scope>
			</dependency>

			<!-- Spring Boot Starter Web (Includes Spring Security by default) -->
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-web</artifactId>
			</dependency>
			<!-- Spring Security for authentication and authorization -->
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-security</artifactId>
			</dependency>


			<!-- Spring Boot Starter for Validation -->
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-validation</artifactId>
			</dependency>
			<!-- Lombok Dependency -->
			<dependency>
				<groupId>org.projectlombok</groupId>
				<artifactId>lombok</artifactId>
				<version>1.18.24</version>
				<scope>provided</scope>
			</dependency>



			<!-- For Jakarta Bean Validation -->
			<dependency>
				<groupId>jakarta.validation</groupId>
				<artifactId>jakarta.validation-api</artifactId>
				<version>3.0.2</version>
			</dependency>


			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-mail</artifactId>
			</dependency>

			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-thymeleaf</artifactId>
			</dependency>
			<!-- MySQL, Spring Boot 3.x -->
			<dependency>
				<groupId>com.mysql</groupId>
				<artifactId>mysql-connector-j</artifactId>
				<scope>runtime</scope>
			</dependency>

			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-data-jpa</artifactId>
			</dependency>

			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-devtools</artifactId>
				<version>3.4.1</version>
			</dependency>

			<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-websocket -->
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-websocket</artifactId>
				<version>3.4.0</version>
			</dependency>
			<dependency>
				<groupId>org.webjars</groupId>
				<artifactId>sockjs-client</artifactId>
				<version>1.5.1</version>
			</dependency>

			<dependency>
				<groupId>org.webjars</groupId>
				<artifactId>stomp-websocket</artifactId>
				<version>2.3.4</version>
			</dependency>

			<dependency>
				<groupId>org.webjars</groupId>
				<artifactId>bootstrap</artifactId>
				<version>5.1.3</version>
			</dependency>


		</dependencies>

		<build>
			<plugins>
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
				</plugin>
			</plugins>
		</build>

	</project>
```
###   `UserController`


```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing users in the Turing Online Forum System.
 * 
 * Provides endpoints for creating, retrieving, and updating user profiles.
 * All endpoints are accessible under the base path "/api/users".
 * 
 * Features:
 * - Create new users
 * - Fetch all users
 * - Fetch a specific user by ID
 * - Update user profile
 * 
 * Uses {@link UserService} for business logic and interacts with {@link User} entities.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Create a new user profile.
     *
     * @param user The {@link User} object to be created. Must contain valid data.
     * @return The created {@link User} object with an auto-generated ID and creation timestamp.
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        User saved = userService.save(user);
        log.info("Created user with ID {}", saved.getId());
        return saved;
    }

    /**
     * Retrieve all users from the system.
     *
     * @return A list of all {@link User} profiles available in the system.
     */
    @GetMapping
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userService.findAll();
    }

    /**
     * Retrieve a user by their unique ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The {@link User} object corresponding to the provided ID.
     * @throws RuntimeException if no user is found with the given ID.
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Fetching profile for user ID {}", id);

        return userService.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", id);
                    return new RuntimeException("User not found");
                });
    }

    /**
     * Update the profile of an existing user.
     *
     * @param id          The ID of the user to update.
     * @param updatedUser The updated user details.
     * @return A {@link ResponseEntity} containing the updated {@link User} object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("Updating profile for user ID {}", id);
        return ResponseEntity.ok(userService.updateUserProfile(id, updatedUser));
    }
}

```
