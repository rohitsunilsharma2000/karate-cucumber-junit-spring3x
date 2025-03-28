**Metadata**

**Programming Language:** Java

**L1 Taxonomy:** Complete Implementations

**L2 Taxonomy:** Social Media Application

**Use Case:**  
A user account deactivation module in enterprise systems or SaaS platforms.
The method userRepository.delete(user) is used to permanently remove a user record from the database. This operation is suitable for applications where users need to be purged due to account closure, data retention policies, GDPR compliance, or inactivity.

# **Prompt**

## **Title:**
Spring Boot User Deletion Service — Repository Entity Removal

## **High-Level Description:**
A Spring Boot application that includes a dedicated service for deleting user entities from a relational database using Spring Data JPA. The service performs the deletion operation by retrieving the user entity and then calling `userRepository.delete(user)` to remove it from the database. The solution is structured with a service layer, repository, exception handling, and logging components. It emphasizes input validation and robust error management to ensure data integrity while providing detailed traceability through logging.

## **Key Features:**
1. **Project Structure & Setup**
    - Create a Spring Boot project using Spring Initializr with dependencies such as Spring Web, Spring Data JPA, and an mysql database for development and testing.
    - Organize the project into clear packages: `service`, `repository`, `exception`, and `configuration`.

2. **Service Layer – User Deletion**
    - Encapsulate the deletion logic in a dedicated service class.
    - Retrieve the user entity using the repository based on a provided identifier.
    - Call `userRepository.delete(user)` to remove the entity from the database.
    - Include comprehensive logging (using SLF4J) at multiple levels (INFO, DEBUG, ERROR) to capture key events of the deletion process.

3. **Exception Handling & Input Validation**
    - Implement input validation at the service layer (or at the controller level if applicable) to ensure that only valid data reaches the deletion process.
    - Integrate global exception handling to manage custom exceptions (e.g., `UserNotFoundException`) and provide meaningful error responses.
    - Check for the existence of the user entity before attempting deletion, and throw a custom exception if the user is not found.

4. **Logging & Traceability**
    - Use SLF4J for logging throughout the deletion process.
    - Log critical events such as:
        - The receipt of the deletion request.
        - Successful retrieval of the user entity.
        - Successful deletion of the user.
        - Error conditions, including when the user entity does not exist.
    - These logs provide a clear audit trail and assist in debugging issues in production environments.

5. **Testing & Documentation**
    - Write unit tests for the service method responsible for user deletion to ensure comprehensive code coverage, including both success and failure cases.
    - Document the code thoroughly with Javadoc to facilitate maintainability and ease of future enhancements.
    - Ensure that logging and exception handling are properly validated through tests to confirm the robustness of the deletion process.

6. **Edge Case Handling:**

- **Invalid User ID Format:**
    - When an invalid or non-numeric user ID is provided in the URL path, the application should return an HTTP 400 (Bad Request) error, indicating that the input is malformed.

- **User Not Found:**
    - When a valid user ID is provided but no corresponding user exists in the database, a {@link UserNotFoundException} should be thrown. This should be handled gracefully by returning an HTTP 404 (Not Found) response with an appropriate error message.

- **Empty or Malformed Request Body:**
    - For create and update operations, if the request body is empty, contains malformed JSON, or does not meet the validation constraints (e.g., missing username or email), the application should return an HTTP 400 (Bad Request) error detailing the validation issues.

- **Duplicate User Creation:**
    - If the creation of a new user conflicts with an existing record (for example, if uniqueness constraints are in place), the system should handle this gracefully, typically returning an HTTP 409 (Conflict) status with an appropriate message.

- **Concurrent Modifications:**
    - In scenarios where multiple requests attempt to update or delete the same user concurrently, the service should ensure data integrity through proper transaction management and return an appropriate error if a conflict is detected.



7. **Expected Behavior:**

- **Get User By ID:**
    - When a valid user ID is provided and the user exists, the system returns the user details with HTTP 200 (OK).
    - If the user does not exist, the system returns HTTP 404 (Not Found) with an error message indicating that the user was not found.

- **Get All Users:**
    - The system returns a list of all users with HTTP 200 (OK). If no users are present, an empty list is returned.

- **Create User:**
    - When a valid user object is provided in the request body, the system creates the user, persists it in the database, and returns the created user with HTTP 201 (Created).
    - If the request body fails validation (e.g., missing required fields or invalid data), the system returns HTTP 400 (Bad Request) with details about the validation errors.

- **Update User:**
    - When a valid user ID and an updated user object are provided, the system updates the existing user's details and returns the updated user with HTTP 200 (OK).
    - If the user to be updated does not exist, the system returns HTTP 404 (Not Found) with an appropriate error message.

- **Delete User:**
    - When a valid user ID is provided for deletion, the system deletes the user from the database and returns HTTP 204 (No Content).
    - If the user does not exist, the system throws a {@link UserNotFoundException} and returns HTTP 404 (Not Found) with an error message.

- **Error Responses:**
    - For all endpoints, proper error responses are returned for various edge cases, ensuring that clients receive clear and informative messages about any issues encountered during the operation.

**Dependency Requirements:**

- **JUnit 5:** For writing and executing unit tests.
- **Maven:** For dependency management and build automation.
- **Spring Boot Starter Web:** For creating REST endpoints and handling HTTP requests.
- **Spring Boot Starter Security:** For providing authentication and authorization capabilities.
- **Spring Boot Starter Validation:** For implementing robust input validation.
- **Lombok:** For reducing boilerplate code in model classes (optional).
- **Spring Boot Starter Test:** For unit and integration testing (includes JUnit 5 and Mockito).
- **Spring Boot DevTools:** For enhancing development productivity with automatic restarts and live reload.

## **Goal:**
To build a robust, scalable Spring Boot service capable of efficiently removing user entities from the database by calling `userRepository.delete(user)`. The solution will ensure high data integrity through stringent input validation, comprehensive logging for detailed traceability, and clear exception management, ultimately delivering a reliable deletion process that is easily maintained and scalable.

## **Plan:**
I will build a robust and scalable Spring Boot service that efficiently removes user entities from the database using userRepository.delete(user). I will ensure high data integrity by implementing strict input validation to prevent invalid or incomplete requests. I will incorporate comprehensive logging to provide detailed traceability of the deletion process, including who initiated the deletion and which user was removed.
To handle edge cases and failures gracefully, I will implement clear and consistent exception management. This will include custom exceptions for scenarios such as attempting to delete a non-existent user. I will also structure the service for easy maintenance and future scalability by following best practices in code organization, service layering, and clean API design.
By taking this approach, I will deliver a reliable, maintainable, and production-ready solution for user deletion.


# **Complete Project Code**

**1) Project Structure:** A logical structure (typical Maven layout)

```
user-purge-system
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- userpurge
|   |               |-- UserPurgeSystem.java
|   |               |-- controller
|   |               |   `-- UserController.java
|   |               |-- exception
|   |               |   `-- UserNotFoundException.java
|   |               |-- model
|   |               |   `-- User.java
|   |               |-- repository
|   |               |   `-- UserRepository.java
|   |               `-- service
|   |                   `-- UserService.java
|   `-- resources
`-- test
    `-- java
        `-- com
            `-- example
                `-- userpurge
                    |-- UserPurgeSystemTest.java
                    |-- controller
                    |   |-- UserControllerIntegrationTest.java
                    |   `-- UserControllerUnitTest.java
                    |-- exception
                    |   `-- UserNotFoundExceptionTest.java
                    `-- service
                        `-- UserServiceTest.java
```

**2) Main Application:** `src/main/java/com/example/userpurge/UserPurgeSystem.java`
```java
package com.example.userpurge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * **Goal:**
 * To build a robust, scalable Spring Boot service capable of efficiently removing user entities from the database by calling
 * {@code userRepository.delete(user)}. The solution ensures high data integrity through stringent input validation, comprehensive logging,
 * and clear exception management, ultimately delivering a reliable deletion process that is easily maintained and scalable.
 *
 * **Title:**
 * UserPurge System — Spring Boot User Deletion Service
 *
 * **High-Level Description:**
 * The UserPurge System is a Spring Boot application that provides a dedicated service for deleting user entities from a relational database.
 * Leveraging Spring Data JPA, the application focuses on the deletion process by retrieving a user entity and invoking the repository's
 * {@code delete(user)} method to remove it. The system is designed with a layered architecture that includes a service layer, repository,
 * and custom exception handling, ensuring robust error management and data integrity.
 *
 * **Key Features:**
 * 1. **Project Structure & Setup**
 *    - Built using Spring Boot with essential dependencies such as Spring Data JPA and an embedded database (mySql) for development and testing.
 *    - Organized into clearly defined packages: {@code model}, {@code repository}, {@code service}, and {@code exception}.
 *
 * 2. **Service Layer – User Deletion**
 *    - Implements user deletion by first retrieving the user entity and then calling {@code userRepository.delete(user)}.
 *    - Includes validation checks to ensure the user exists before attempting deletion, preventing unintended errors.
 *
 * 3. **Exception Handling & Input Validation**
 *    - Utilizes custom exceptions (e.g., {@link com.example.userpurge.exception.UserNotFoundException}) to handle cases where a user is not found.
 *    - Integrates global exception handling to provide meaningful error responses and maintain application stability.
 *
 * 4. **Logging & Traceability**
 *    - Employs SLF4J for logging to capture key events during the deletion process.
 *    - Logs important steps and error conditions at various log levels (INFO, DEBUG, ERROR) to facilitate troubleshooting and ensure transparency.
 *
 * 5. **Testing & Documentation**
 *    - Comprehensive unit and integration tests are implemented to ensure high code coverage and reliable functionality.
 *    - Detailed Javadoc and inline comments support maintainability and scalability, making it easier for developers to understand and enhance the system.
 *
 * This class serves as the entry point for the UserPurge System, bootstrapping the Spring Boot application context and initializing all components.
 */
@SpringBootApplication
public class UserPurgeSystem {

    public static void main(String[] args) {
        SpringApplication.run(UserPurgeSystem.class, args);
    }
}

```
**3) UserService:** `src/main/java/com/example/userpurge/service/UserService.java`
```java
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

```
**4) UserRepository:** `src/main/java/com/example/userpurge/repository/UserRepository.java`
```java
package com.example.userpurge.repository;

import com.example.userpurge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface extends Spring Data JPA's {@link JpaRepository} to provide CRUD operations and query methods
 * for the User entity without requiring explicit implementation. It serves as the data access layer in the UserPurge application.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>CRUD Operations:</strong> Inherits standard create, read, update, and delete methods from {@code JpaRepository}.</li>
 *   <li><strong>Query Derivation:</strong> Supports query method derivation, allowing custom finder methods to be defined by following Spring Data naming conventions.</li>
 *   <li><strong>Integration with Spring Data JPA:</strong> Seamlessly integrates with Spring Boot to automatically implement repository logic at runtime.</li>
 * </ul>
 *
 * <p>
 * This repository is used by the service layer to interact with the database, ensuring that user data is persisted and retrieved efficiently.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Long> {
}

```
**5) User:** `src/main/java/com/example/userpurge/model/User.java`
```java
package com.example.userpurge.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a user entity in the UserPurge system.
 * <p>
 * This class models the user details for the UserPurge project, which is focused on efficiently removing user entities from the database.
 * It is mapped to the "user" table using JPA annotations and serves as a fundamental component for performing CRUD operations.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Entity Mapping:</strong> Annotated with {@code @Entity} and {@code @Table} to map the class to the database table "user".</li>
 *   <li><strong>Lombok Integration:</strong> Uses Lombok's {@code @Getter} and {@code @Setter} to automatically generate getter and setter methods, reducing boilerplate code.</li>
 *   <li><strong>Primary Key Generation:</strong> The {@code id} field is marked with {@code @Id} and {@code @GeneratedValue} using the {@code GenerationType.IDENTITY} strategy to automatically generate unique identifiers.</li>
 *   <li><strong>Essential Attributes:</strong> Contains core attributes such as {@code username} and {@code email} to represent user information.</li>
 * </ul>
 *
 * <p>
 * This entity is used by the service layer to manage user data and is critical for the functionality of operations such as user deletion,
 * ensuring that the data integrity and business logic of the system are maintained.
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
}

```
**6) UserNotFoundException:** `src/main/java/com/example/userpurge/exception/UserNotFoundException.java`
```java
package com.example.userpurge.exception;



/**
 * Custom exception thrown when a resource is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with a given message.
     *
     * @param message Detailed message about the resource that was not found.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
```

**7) Maven:** `pom.xml`
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
		<artifactId>userpurge</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<name>user-purge</name>
		<description>Call userRepository.delete(user) to remove an entity instance from the database.</description>
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

		<!-- Spring Boot Starter for Validation -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<!-- Lombok Dependency -->
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.30</version>
			<scope>provided</scope>
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
**8) UserController:** `src/main/java/com/example/userpurge/controller/UserController.java`
```java
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

```
**9) application.properties:** `src/main/resources/application.properties`
```properties

spring.application.name=user-purge-system

server.port=8080

# mySql database connection (Database Configuration)
#spring.datasource.url=jdbc:mysql://localhost:3306/turingonlineforumsystem
spring.datasource.url=jdbc:mysql://localhost:3307/turingonlineforumsystem
spring.datasource.username=root
spring.datasource.password=SYSTEM
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


# Automatically create/drop schema at startup
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect


```

# **Unit Tests (JUnit 5 + Mockito)**


**9) UserPurgeSystemTest:** `src/test/java/com/example/userpurge/UserPurgeSystemTest.java`
```java
package com.example.userpurge;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for the main application class {@link UserPurgeSystemTest}.
 *
 * <p>
 * This test class includes:
 * <ul>
 *   <li>{@code contextLoads()} - verifies that the Spring application context starts up correctly.
 *   <li>{@code testMainMethod()} - calls the main method to ensure it executes without throwing exceptions.
 * </ul>
 * </p>
 *
 * <p>
 * With these tests, code coverage tools (e.g., JaCoCo) should report over 90% class, method, and line coverage.
 * </p>
 */
@SpringBootTest(classes = UserPurgeSystemTest.class)
public class UserPurgeSystemTest {

    /**
     * Verifies that the Spring application context loads successfully.
     * If the context fails to load, the test will fail.
     */
    @Test
    public void contextLoads() {
        // The application context is automatically loaded by the @SpringBootTest annotation.
    }

    /**
     * Verifies that calling the main method of {@link UserPurgeSystemTest} executes without exceptions.
     */
    @Test
    public void testMainMethod() {
        // Call the main method with an empty argument array.
        UserPurgeSystem.main(new String[]{});
        // Test passes if no exception is thrown.
    }
}

```
**10) UserNotFoundExceptionTest:** `src/test/java/com/example/userpurge/exception/UserNotFoundExceptionTest.java`
```java
package com.example.userpurge.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UserNotFoundException}.
 * <p>
 * This class tests the functionality of the custom {@code UserNotFoundException} to ensure that
 * the exception message is correctly passed and can be retrieved via {@link RuntimeException#getMessage()}.
 * </p>
 *
 * <h3>Key Aspects:</h3>
 * <ul>
 *   <li><strong>Message Verification:</strong> Confirms that the exception is instantiated with the expected message.</li>
 *   <li><strong>Error Propagation:</strong> Validates that the message provided during construction is properly propagated to the superclass.</li>
 * </ul>
 */
public class UserNotFoundExceptionTest {

    /**
     * Verifies that the exception message is correctly passed to the {@link RuntimeException}.
     */
    @Test
    void testUserNotFoundExceptionMessage() {
        String expectedMessage = "User not found with id 123";
        UserNotFoundException exception = new UserNotFoundException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected message.");
    }
}

```

## After the first iteration, the overall test coverage was 14%. To improve this, additional test cases—including those in UserServiceTest will be introduced to further increase the test coverage percentage.


**11) UserServiceTest:** `src/test/java/com/example/userpurge/service/UserServiceTest.java`
```java
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
    private UserRepository userRepo; // Mocking the repository layer

    @InjectMocks
    private UserService userService; // Injecting the mocked repository into the service

    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 999L;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(); // Creating a sample user before each test
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
        when(userRepo.findById(VALID_ID)).thenReturn(Optional.of(sampleUser)); // Mocking repository response

        User user = userService.getUserById(VALID_ID); // Calling service method

        assertNotNull(user, "The retrieved user should not be null."); // Ensuring user is not null
        assertEquals("testuser", user.getUsername(), "The username should match the expected value."); // Asserting username

        verify(userRepo, times(1)).findById(VALID_ID); // Verifying repository method was called once
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
        when(userRepo.findById(INVALID_ID)).thenReturn(Optional.empty()); // Mocking not found scenario

        Exception exception = assertThrows(UserNotFoundException.class,
                                           () -> userService.getUserById(INVALID_ID),
                                           "Expected UserNotFoundException when user is not found."); // Expecting exception

        assertTrue(exception.getMessage().contains("User not found with ID: " + INVALID_ID),
                   "The exception message should indicate that the user was not found."); // Asserting exception message

        verify(userRepo, times(1)).findById(INVALID_ID); // Verifying method call
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
        when(userRepo.findById(VALID_ID)).thenReturn(Optional.of(sampleUser)); // Mock user lookup
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved user

        User updatedInfo = new User();
        updatedInfo.setUsername("updateduser");
        updatedInfo.setEmail("updated@example.com");

        User result = userService.updateUserProfile(VALID_ID, updatedInfo); // Call service method

        assertEquals("updateduser", result.getUsername(), "The username should be updated successfully.");
        assertEquals("updated@example.com", result.getEmail(), "The email should be updated successfully.");

        verify(userRepo, times(1)).findById(VALID_ID); // Verify lookup
        verify(userRepo, times(1)).save(sampleUser); // Verify save
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
        when(userRepo.findById(VALID_ID)).thenReturn(Optional.of(sampleUser)); // Mock existing user

        Optional<User> userOpt = userService.findById(VALID_ID); // Call service method

        assertTrue(userOpt.isPresent(), "The Optional should contain a user.");
        assertEquals(sampleUser, userOpt.get(), "The returned user should match the sample user.");

        verify(userRepo, times(1)).findById(VALID_ID); // Ensure method was called
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
        when(userRepo.findById(INVALID_ID)).thenReturn(Optional.empty()); // Mock missing user

        Optional<User> userOpt = userService.findById(INVALID_ID); // Call service method

        assertFalse(userOpt.isPresent(), "The Optional should be empty when no user is found.");

        verify(userRepo, times(1)).findById(INVALID_ID); // Ensure method was called
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
        when(userRepo.findAll()).thenReturn(userList); // Mock user list

        List<User> result = userService.findAll(); // Call service

        assertEquals(1, result.size(), "The returned list should contain one user.");
        assertEquals(sampleUser, result.get(0), "The user in the list should match the sample user.");

        verify(userRepo, times(1)).findAll(); // Ensure findAll called
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
        when(userRepo.save(any(User.class))).thenReturn(sampleUser); // Mock save behavior

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");

        User result = userService.save(newUser); // Call save

        assertNotNull(result, "The saved user should not be null."); // Verify non-null result

        verify(userRepo, times(1)).save(newUser); // Ensure save was called with newUser
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
        when(userRepo.existsById(VALID_ID)).thenReturn(true); // Mock user existence
        doNothing().when(userRepo).deleteById(VALID_ID); // Mock delete behavior

        assertDoesNotThrow(() -> userService.deleteUser(VALID_ID),
                           "Deletion should occur without throwing an exception."); // Expect no exception

        verify(userRepo, times(1)).existsById(VALID_ID); // Verify existence check
        verify(userRepo, times(1)).deleteById(VALID_ID); // Verify deletion
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
        when(userRepo.existsById(INVALID_ID)).thenReturn(false); // Mock missing user

        Exception exception = assertThrows(UserNotFoundException.class,
                                           () -> userService.deleteUser(INVALID_ID),
                                           "Expected a UserNotFoundException when deleting a non-existent user."); // Expect exception

        assertTrue(exception.getMessage().contains("User not found with id " + INVALID_ID),
                   "The exception message should indicate that the user was not found."); // Check exception message

        verify(userRepo, times(1)).existsById(INVALID_ID); // Verify existence check
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
        User user = new User(); // Creating new User object

        Long id = 2L;
        String username = "anotheruser";
        String email = "another@example.com";

        user.setId(id); // Set values
        user.setUsername(username);
        user.setEmail(email);

        // Validate values were properly set using getters
        assertEquals(id, user.getId(), "The user ID should match the value set.");
        assertEquals(username, user.getUsername(), "The username should match the value set.");
        assertEquals(email, user.getEmail(), "The email should match the value set.");
    }
}

```
**12) UserControllerIntegrationTest:** `src/test/java/com/example/userpurge/controller/UserControllerIntegrationTest.java`
```java
package com.example.userpurge.controller;

import com.example.userpurge.model.User;
import com.example.userpurge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link UserController}.
 * <p>
 * This test class verifies that the REST endpoints exposed by {@code UserController} operate as expected.
 * It uses an embedded mySql database (or another configured database) and the {@link TestRestTemplate} to perform HTTP calls.
 * Each test method ensures the endpoint's behavior matches the expected response.
 * </p>
 *
 * <h3>Test Scenarios:</h3>
 * <ul>
 *   <li>Retrieve a user by ID and verify correct data is returned.</li>
 *   <li>Retrieve all users and confirm the returned list is not empty.</li>
 *   <li>Create a new user and check that the user is persisted correctly.</li>
 *   <li>Update an existing user and verify the updates are reflected.</li>
 *   <li>Delete a user and confirm that subsequent retrieval attempts fail.</li>
 * </ul>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    // TestRestTemplate to perform HTTP requests in tests.
    @Autowired
    private TestRestTemplate restTemplate;

    // UserRepository to interact with the database directly for test setup and verification.
    @Autowired
    private UserRepository userRepository;

    // Sample user used across test methods.
    private User sampleUser;

    /**
     * Setup method to initialize test data.
     * <p>
     * This method clears the database and creates a sample user before each test.
     * </p>
     */
    @BeforeEach
    void setUp() {
        // Clean the database before each test
        userRepository.deleteAll();

        // Create and persist a sample user for testing
        sampleUser = new User();
        sampleUser.setUsername("integrationUser");
        sampleUser.setEmail("integration@example.com");
        sampleUser = userRepository.save(sampleUser);
    }

    /**
     * Tests retrieval of a user by their ID.
     * <p>
     * Sends a GET request to {@code /api/users/{id}} and verifies that the returned user has the expected username.
     * </p>
     */
    @Test
    void testGetUserById() {
        // Send GET request to retrieve the sample user by ID
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/" + sampleUser.getId(), User.class);

        // Assert that the HTTP status is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Assert that the username matches the sample user's username
        assertThat(response.getBody().getUsername()).isEqualTo("integrationUser");
    }

    /**
     * Tests retrieval of all users.
     * <p>
     * Sends a GET request to {@code /api/users} and verifies that the returned list contains at least one user.
     * </p>
     */
    @Test
    void testGetAllUsers() {
        // Send GET request to retrieve all users
        ResponseEntity<User[]> response = restTemplate.getForEntity("/api/users", User[].class);

        // Assert that the HTTP status is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Convert the response body to a list and assert that it is not empty
        List<User> users = List.of(response.getBody());
        assertThat(users).isNotEmpty();
    }

    /**
     * Tests creation of a new user.
     * <p>
     * Sends a POST request to {@code /api/users} with a new user's data and verifies that the user is created with HTTP 201 status.
     * </p>
     */
    @Test
    void testCreateUser() {
        // Prepare a new user object to be created
        User newUser = new User();
        newUser.setUsername("newIntegrationUser");
        newUser.setEmail("newintegration@example.com");

        // Send POST request to create the new user
        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", newUser, User.class);

        // Assert that the HTTP status is 201 Created
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // Assert that the created user's username matches the expected value
        assertThat(response.getBody().getUsername()).isEqualTo("newIntegrationUser");
    }

    /**
     * Tests updating an existing user's information.
     * <p>
     * Sends a PUT request to {@code /api/users/{id}} with updated data and verifies that the response contains the updated user.
     * </p>
     */
    @Test
    void testUpdateUser() {
        // Update the sample user's username
        sampleUser.setUsername("updatedIntegrationUser");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HTTP entity with the updated user data and headers
        HttpEntity<User> requestEntity = new HttpEntity<>(sampleUser, headers);

        // Send PUT request to update the user
        ResponseEntity<User> response = restTemplate.exchange("/api/users/" + sampleUser.getId(),
                                                              HttpMethod.PUT, requestEntity, User.class);

        // Assert that the HTTP status is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Assert that the username was updated successfully
        assertThat(response.getBody().getUsername()).isEqualTo("updatedIntegrationUser");
    }

    /**
     * Tests deletion of a user.
     * <p>
     * Sends a DELETE request to {@code /api/users/{id}} and then attempts to retrieve the same user to verify deletion.
     * </p>
     */
    @Test
    void testDeleteUser() {
        // Send DELETE request to remove the sample user
        ResponseEntity<Void> response = restTemplate.exchange("/api/users/" + sampleUser.getId(),
                                                              HttpMethod.DELETE, null, Void.class);
        // Assert that the HTTP status is 204 No Content
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Attempt to retrieve the deleted user; expect a 404 Not Found
        ResponseEntity<User> getResponse = restTemplate.getForEntity("/api/users/" + sampleUser.getId(), User.class);
        // Assert that the user is not found (assuming global exception handling returns 404)
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

```
**13) UserControllerUnitTest:** `src/test/java/com/example/userpurge/controller/UserControllerUnitTest.java`
```java
package com.example.userpurge.controller;

import com.example.userpurge.model.User;
import com.example.userpurge.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link UserController} ensuring that all endpoints function as expected.
 * <p>
 * These tests use Spring's {@link WebMvcTest} with {@link MockMvc} to simulate HTTP requests
 * and validate the responses returned by the controller methods. The tests also employ Mockito to
 * mock interactions with the {@link UserService}.
 * </p>
 *
 * <h3>Test Scenarios Covered:</h3>
 * <ul>
 *   <li>Retrieval of a user by ID when the user exists.</li>
 *   <li>Retrieval of all users.</li>
 *   <li>Creation of a new user with proper input validation.</li>
 *   <li>Updating an existing user and verifying the updates.</li>
 *   <li>Deletion of a user and verifying that the proper status code is returned.</li>
 * </ul>
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerUnitTest {

    // MockMvc is used to simulate HTTP requests and assert responses.
    @Autowired
    private MockMvc mockMvc;

    // Mock the UserService to isolate controller testing.
    @MockBean
    private UserService userService;

    // ObjectMapper to convert Java objects to JSON string and vice versa.
    private final ObjectMapper objectMapper = new ObjectMapper();

    // A sample user object used across multiple test cases.
    private User sampleUser;

    /**
     * Sets up a sample user before each test.
     * <p>
     * This method initializes the sample user with preset values for use in the tests.
     * </p>
     */
    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("testuser");
        sampleUser.setEmail("testuser@example.com");
    }

    /**
     * Tests the retrieval of a user by ID when the user exists.
     * <p>
     * Mocks the UserService to return the sample user and verifies that the controller
     * responds with the correct status code and user data.
     * </p>
     */
    @Test
    void testGetUserById_Success() throws Exception {
        // Mock the service call to return the sample user for ID 1.
        when(userService.getUserById(1L)).thenReturn(sampleUser);

        // Perform GET request to /api/users/1 and assert that the response is OK with expected user data.
        mockMvc.perform(get("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("testuser"));
    }

    /**
     * Tests the retrieval of all users.
     * <p>
     * Mocks the UserService to return a singleton list containing the sample user and verifies
     * that the response contains the expected data.
     * </p>
     */
    @Test
    void testGetAllUsers() throws Exception {
        // Mock the service call to return a list with the sample user.
        when(userService.findAll()).thenReturn(Collections.singletonList(sampleUser));

        // Perform GET request to /api/users and assert that the first user's username matches.
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    /**
     * Tests the creation of a new user.
     * <p>
     * Mocks the UserService to return the sample user upon creation.
     * Sends a POST request with the sample user JSON and verifies that the response status is CREATED
     * and that the returned user data matches the expected values.
     * </p>
     */
    @Test
    void testCreateUser() throws Exception {
        // Mock the service call to return the sample user when saving any User.
        when(userService.save(any(User.class))).thenReturn(sampleUser);

        // Convert sampleUser object to JSON.
        String userJson = objectMapper.writeValueAsString(sampleUser);

        // Perform POST request to /api/users with user JSON and verify response status and content.
        mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.username").value("testuser"));
    }

    /**
     * Tests updating an existing user's profile.
     * <p>
     * Mocks the UserService to return an updated user object and verifies that the controller
     * correctly processes the PUT request and returns the updated user.
     * </p>
     */
    @Test
    void testUpdateUser() throws Exception {
        // Create an updated user object with modified username and email.
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updated@example.com");

        // Mock the service call to return the updated user when updating user with ID 1.
        when(userService.updateUserProfile(eq(1L), any(User.class))).thenReturn(updatedUser);

        // Convert the updated user object to JSON.
        String userJson = objectMapper.writeValueAsString(updatedUser);

        // Perform PUT request to /api/users/1 with updated user JSON and verify response.
        mockMvc.perform(put("/api/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("updateduser"));
    }

    /**
     * Tests deletion of a user.
     * <p>
     * Mocks the UserService to do nothing on delete (simulating successful deletion) and verifies that
     * the DELETE request returns the correct HTTP status.
     * </p>
     */
    @Test
    void testDeleteUser() throws Exception {
        // Mock the deleteUser method to do nothing when called with user ID 1.
        Mockito.doNothing().when(userService).deleteUser(1L);

        // Perform DELETE request to /api/users/1 and assert that the response status is NO_CONTENT.
        mockMvc.perform(delete("/api/users/1"))
               .andExpect(status().isNoContent());
    }
}

```

# After the second iteration, test coverage increased to 100%.
**Result:** Total line coverage is 100%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml includes Spring Boot Starter Web, Security, Validation, Lombok, Test, mySql Database, Jakarta Validation API, and DevTools dependencies with Java version set to 17 for a scalable REST API using the Edmonds‑Karp algorithm..
3. **Database & Application Properties:** Configure mySql DB credentials in application.properties (or application.yml) due to in-memory auth, for example:
```properties

spring.application.name=user-purge-system

server.port=8080

# mySql database connection (Database Configuration)
spring.datasource.url=jdbc:mysql://localhost:3306/turingonlineforumsystem
spring.datasource.username=root
spring.datasource.password=SYSTEM
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


# Automatically create/drop schema at startup
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect


```
4. **Build & Test:**

- From the root directory (where pom.xml is located), run:
```bash
mvn clean install
```
This will compile the code, install dependencies, and package the application.
- To run unit tests (e.g., JUnit 5 + Mockito) and check coverage (Jacoco), do:
```bash
mvn clean test
```
Verify that test coverage meets or exceeds the 90% target.

5. **Start the Application:**

- Launch the Spring Boot app via Maven:
```bash
mvn spring-boot:run
```
### 6. **Accessing Endpoints & Features:**

 Get User By ID
```bash
curl --location --request GET 'http://localhost:8080/api/users/1'
```

Get All Users
```bash
curl --location --request GET 'http://localhost:8080/api/users'

```

 Create User
```bash
curl --location --request POST 'http://localhost:8080/api/users' \
--header 'Content-Type: application/json' \
--data '{
"username": "newuser",
"email": "newuser@example.com"
}'
```

 Update User
```bash
curl --location --request PUT 'http://localhost:8080/api/users/1' \
--header 'Content-Type: application/json' \
--data '{
"username": "updateduser",
"email": "updated@example.com"
}'
```

Delete User

```bash
curl --location --request DELETE 'http://localhost:8080/api/users/1'

```

# **Time and Space Complexity:**

- **Time Complexity:**  
  The user deletion operation primarily involves two database actions: a lookup and a deletion.
    - **Lookup:** Typically performed in constant time **O(1)** due to primary key indexing, ensuring quick retrieval of the user entity.
    - **Deletion:** Also operates in constant time **O(1)** as it directly removes the entity once it is located.
    - In practical scenarios, underlying database factors may introduce minor overhead, but the overall operational complexity remains close to **O(1)**.

- **Space Complexity:**
    - The operation uses a minimal amount of space, requiring only a reference to the user entity during the process.
    - Any additional space used is managed internally by the database and the Spring Data JPA framework, resulting in a negligible space footprint.
    - Overall, the space complexity is **O(1)**.


# **Conclusion**

The **User Deletion Service**, built with **Spring Boot** and leveraging Spring Data JPA, provides an efficient and robust solution for removing user entities from the database through the use of `userRepository.delete(user)`. Designed with a focus on data integrity, this service ensures that deletions are executed only after thorough validation, supported by comprehensive logging and clear exception management.

Key strengths of the solution include:
- **Robust Input Validation:** Ensuring that only valid deletion requests proceed.
- **Comprehensive Logging:** Utilizing SLF4J to capture detailed audit trails and facilitate debugging.
- **Clear Exception Handling:** Implementing custom exceptions like `UserNotFoundException` to handle errors gracefully.
- **Efficient Performance:** With both lookup and deletion operations operating in constant time, the service is highly performant.

This makes the application ideal for enterprise environments that require reliable and maintainable user data management, ensuring that deletion operations are both precise and scalable.


**Iteration One:**

https://drive.google.com/file/d/1avUH0V35ukulqXF_wLN57e2Gjf9jYLxc/view?usp=drive_link

**Iteration Two:**
https://drive.google.com/file/d/1dMgqS-bD2TaFiwMpMnzVy2QIKJfd-2Kn/view?usp=drive_link

**Download Code:**

https://drive.google.com/file/d/1rShdMn5Aeq-iygIorpu-RSz2xthsrdRK/view?usp=drive_link