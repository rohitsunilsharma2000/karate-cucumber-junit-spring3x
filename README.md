**Metadata**

**Programming Language:** Java

**L1 Taxonomy:** Complete Implementations

**L2 Taxonomy:** Social Media Application

**Use Case:**  
When a client requests a user by username (e.g., "alice"), the application leverages a derived query (findByUsername("alice")) to locate and return the user details. If the user is not found, the service throws a UserNotFoundException and logs the error for troubleshooting.
Below is an example documentation following the provided format for an application that uses a derived query to locate a user entity via:

```java
userRepository.findByUsername("alice");
```

---

# **Prompt**

## **Title:**
Spring Boot User Query Service — Derived Query Lookup

## **High-Level Description:**
Develop a Spring Boot application that incorporates a service layer for retrieving user entities from a relational database using Spring Data JPA derived queries. The service leverages the repository method `userRepository.findByUsername("alice")` to perform a lookup based on a specific field (username). The solution is designed with a layered architecture, featuring a well-organized separation between controllers, services, and repositories. It emphasizes robust input validation, detailed logging for traceability, and effective error handling to ensure reliable data retrieval and maintainability.

## **Key Features:**
1. **Project Structure & Setup**
    - Create a Spring Boot project using Spring Initializr with dependencies such as Spring Web, Spring Data JPA, and an in-memory database (H2) or MySQL for development and testing.
    - Organize the project into distinct packages: `controller`, `service`, `repository`, `dto`, `model`, and `exception`.

2. **Service Layer – User Query**
    - Encapsulate the user lookup logic in a dedicated service class.
    - Use a derived query method in the repository (`findByUsername`) to retrieve user details based on the username field.
    - Map the retrieved entity to a DTO if necessary for data transfer.
    - Incorporate comprehensive logging (using SLF4J) at multiple levels (INFO, DEBUG, ERROR) to capture the execution flow and highlight key events during the lookup process.

3. **Exception Handling & Input Validation**
    - Implement input validation at the controller or service layer to ensure that only valid usernames are processed.
    - Use global exception handling to manage custom exceptions (e.g., `UserNotFoundException`) and provide clear error messages when no matching user is found.
    - Validate that the username is not blank and matches expected patterns before invoking the repository method.

4. **Logging & Traceability**
    - Use SLF4J for detailed logging throughout the lookup process.
    - Log critical events such as:
        - Receipt of a user lookup request.
        - Invocation of the derived query method (`findByUsername`).
        - Successful retrieval of the user entity.
        - Error conditions, such as when the user is not found.
    - Ensure that logs create an audit trail that is useful for debugging and production monitoring.

5. **Testing & Documentation**
    - Write comprehensive unit and integration tests for the user lookup functionality, ensuring that both valid and invalid scenarios are covered.
    - Use testing frameworks like JUnit 5 and Mockito to simulate repository behavior.
    - Document all code with detailed Javadoc comments to facilitate future maintenance and enhancements.
    - Validate logging and exception management through targeted tests to confirm the robustness of the user query process.

## **Dependency Requirements:**
- **JUnit 5:** For unit and integration testing.
- **Maven:** For dependency management and build automation.
- **Spring Boot Starter Web:** For creating REST endpoints and handling HTTP requests.
- **Spring Boot Starter Data JPA:** For ORM and repository support.
- **Spring Boot Starter Validation:** For implementing robust input validation.
- **Lombok:** To reduce boilerplate code in entity and DTO classes.
- **Spring Boot Starter Test:** For unit and integration testing (includes JUnit 5, Mockito, etc.).
- **Spring Boot DevTools:** For enhanced development productivity with automatic restarts and live reload.

## **Goal:**
To build a scalable and maintainable Spring Boot service that efficiently retrieves user entities from a database using derived queries via `userRepository.findByUsername("alice")`. The solution will ensure high data integrity by enforcing stringent input validation, maintain comprehensive traceability through detailed logging, and implement robust error handling to manage cases where the user is not found. This design will result in a reliable user query process that is easy to maintain and extend as the application evolves.


---
# **Complete Project Code**

**1) Project Structure:** A logical structure (typical Maven layout)

```
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- userqueryhub
|   |               |-- SpringDataExplorer.java
|   |               |-- controller
|   |               |   `-- UserController.java
|   |               |-- dto
|   |               |   `-- UserDTO.java
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
                `-- userqueryhub
                    |-- SpringDataExplorerTest.java
                    |-- controller
                    |   `-- UserControllerIntegrationTest.java
                    |-- exception
                    |   `-- UserNotFoundExceptionTest.java
                    `-- service
                        `-- UserServiceTest.java



```

**2) Main Application:** src/main/java/com/example/userqueryhub/UserPurgeSystem.java
```java
package com.example.userqueryhub;

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
 *    - Built using Spring Boot with essential dependencies such as Spring Data JPA and an embedded database (H2) for development and testing.
 *    - Organized into clearly defined packages: {@code model}, {@code repository}, {@code service}, and {@code exception}.
 *
 * 2. **Service Layer – User Deletion**
 *    - Implements user deletion by first retrieving the user entity and then calling {@code userRepository.delete(user)}.
 *    - Includes validation checks to ensure the user exists before attempting deletion, preventing unintended errors.
 *
 * 3. **Exception Handling & Input Validation**
 *    - Utilizes custom exceptions (e.g., {@link com.example.userqueryhub.exception.UserNotFoundException}) to handle cases where a user is not found.
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
public class SpringDataExplorer {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataExplorer.class, args);
    }
}

```

**3) UserService:** src/main/java/com/example/userqueryhub/service/UserService.java
```java
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

```

**4) UserRepository:** src/main/java/com/example/userqueryhub/repository/UserRepository.java
```java
package com.example.userqueryhub.repository;

import com.example.userqueryhub.model.User;
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
    User findByUsername(String username);
}

```

**5) User:** src/main/java/com/example/userqueryhub/model/User.java
```java
package com.example.userqueryhub.model;

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
    private String password;


}

```

**6) UserNotFoundException:** src/main/java/com/example/userqueryhub/exception/UserNotFoundException.java
```java
package com.example.userqueryhub.exception;

/**
 * Custom exception thrown when a user resource is not found in the system.
 * <p>
 * This exception is used throughout the UserPurge application to indicate that a requested user
 * entity could not be located in the database. It extends {@link RuntimeException} to provide an unchecked exception,
 * simplifying error propagation in the service layer.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Clear Messaging:</strong> Accepts a detailed message describing the resource that was not found.</li>
 *   <li><strong>Runtime Exception:</strong> Inherits from {@code RuntimeException} to avoid mandatory try-catch blocks,
 *       allowing the exception to be handled globally by a dedicated exception handler.</li>
 *   <li><strong>Integration with Global Exception Handling:</strong> Designed to work with the application's global exception handling framework,
 *       providing clear error responses to clients when a user is not found.</li>
 * </ul>
 *
 * <p>
 * Use this exception in scenarios where the user entity retrieval fails, ensuring that consumers of the service are
 * informed about the absence of the expected resource in a consistent manner.
 * </p>
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code UserNotFoundException} with the specified detail message.
     *
     * @param message Detailed message about the user that was not found.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}

```
**7) GlobalExceptionHandler:** src/main/java/com/example/userqueryhub/exception/GlobalExceptionHandler.java
```java
package com.example.userqueryhub.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the UserQueryHub application.
 *
 * <p>
 * This class intercepts exceptions thrown across the whole application and provides meaningful HTTP responses.
 * It specifically handles {@link UserNotFoundException} and any generic exceptions, ensuring that errors are logged
 * and appropriate HTTP status codes and messages are returned to the client.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Centralized Error Handling:</strong> Captures exceptions from all controllers in the application.</li>
 *   <li><strong>Custom Exception Handling:</strong> Provides specific handling for {@link UserNotFoundException}.</li>
 *   <li><strong>Logging:</strong> Uses SLF4J to log error details at the error level for debugging and production monitoring.</li>
 *   <li><strong>HTTP Response:</strong> Returns a ResponseEntity with an appropriate HTTP status code and error message.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles {@link UserNotFoundException} exceptions.
     *
     * <p>
     * Logs the error message at the error level and returns a ResponseEntity with HTTP status 404 (Not Found)
     * and the exception's message in the response body.
     * </p>
     *
     * @param ex the {@link UserNotFoundException} thrown when a user is not found.
     * @return a ResponseEntity containing the error message and a 404 status.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(UserNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Handles all generic exceptions not explicitly handled by other exception handlers.
     *
     * <p>
     * Logs the exception stack trace at the error level and returns a ResponseEntity with HTTP status 500 (Internal Server Error)
     * and a generic error message in the response body.
     * </p>
     *
     * @param ex the exception that was thrown.
     * @return a ResponseEntity containing a generic error message and a 500 status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("Unhandled error: ", ex);
        return ResponseEntity.status(500).body("Internal server error");
    }
}

```
**8) UserDTO:** src/main/java/com/example/userqueryhub/dto/UserDTO.java
```java
package com.example.userqueryhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for transferring user data between layers.
 *
 * <p>
 * This DTO is used to encapsulate user details in the UserPurge system. It includes fields for the username,
 * email, and password, along with validation constraints to ensure data integrity and consistency.
 * </p>
 *
 * <h3>Validation Constraints:</h3>
 * <ul>
 *   <li><strong>username:</strong> Must not be blank.</li>
 *   <li><strong>email:</strong> Must be a valid email format and not blank.</li>
 *   <li><strong>password:</strong> Must not be blank; additional security constraints can be applied as needed.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;
}

```

**9) UserController:** src/main/java/com/example/userqueryhub/controller/UserController.java
```java
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

```

**7) Maven:** pom.xml
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
		<artifactId>userqueryhub</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<name>DataFieldFinder</name>
		<description>Use userRepository.findByUsername("alice") to perform a derived query based on a field.</description>
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

# **Unit Tests (JUnit 5 + Mockito)**


**8) SpringDataExplorerTest:** src/test/java/com/example/userqueryhub/SpringDataExplorerTest.java
```java
package com.example.userqueryhub;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for the main application class {@link SpringDataExplorerTest}.
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
@SpringBootTest(classes = SpringDataExplorerTest.class)
public class SpringDataExplorerTest {

    /**
     * Verifies that the Spring application context loads successfully.
     * If the context fails to load, the test will fail.
     */
    @Test
    public void contextLoads() {
        // The application context is automatically loaded by the @SpringBootTest annotation.
    }

    /**
     * Verifies that calling the main method of {@link SpringDataExplorerTest} executes without exceptions.
     */
    @Test
    public void testMainMethod() {
        // Call the main method with an empty argument array.
        SpringDataExplorer.main(new String[]{});
        // Test passes if no exception is thrown.
    }
}

```
**9) UserNotFoundExceptionTest:** src/test/java/com/example/userpurge/exception/UserNotFoundExceptionTest.java
```java
package com.example.userqueryhub.exception;

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

**Result:** The line coverage is 48%

<a href="https://drive.google.com/file/d/1KzBln2wAVTaKX_JUy0kbTP0vfko0Qhl2/view?usp=drive_link">Iteration One</a>

**Plan:** The goal is to achieve >90% total code coverage and 95% total line coverage. To achieve this goal, will be writing tests for all models, dto, service, exception, event and controller packages.
**10) UserServiceTest:** src/test/java/com/example/userpurge/service/UserServiceTest.java
```java
package com.example.userqueryhub.service;

import com.example.userqueryhub.exception.UserNotFoundException;
import com.example.userqueryhub.model.User;
import com.example.userqueryhub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserService} covering user save and retrieval by username.
 *
 * <p>
 * This test class verifies the behavior of the {@code save} and {@code getUserByUsername} methods in the
 * {@code UserService}. The repository is mocked using Mockito to isolate the service logic.
 * </p>
 *
 * <h3>Test Cases:</h3>
 * <ul>
 *   <li><strong>testSaveUser:</strong> Verifies that a user is correctly saved and returned.</li>
 *   <li><strong>testGetUserByUsernameFound:</strong> Verifies that an existing user is retrieved successfully by username.</li>
 *   <li><strong>testGetUserByUsernameNotFound:</strong> Verifies that a {@link UserNotFoundException} is thrown when the user is not found.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    private User user;

    /**
     * Initializes a sample user before each test.
     */
    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("securepassword");
    }

    /**
     * Tests that the {@code save} method persists a user correctly.
     *
     * <p>
     * The test mocks the repository's {@code save} method to return the same user instance, and then asserts that the
     * returned user matches the input.
     * </p>
     */
    @Test
    public void testSaveUser() {
        when(userRepo.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("testuser@example.com");
    }

    /**
     * Tests that the {@code getUserByUsername} method retrieves an existing user.
     *
     * <p>
     * The test mocks the repository's {@code findByUsername} method to return a sample user when a valid username is provided.
     * The returned user is then validated for correctness.
     * </p>
     */
    @Test
    public void testGetUserByUsernameFound() {
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        User foundUser = userService.getUserByUsername("testuser");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("testuser@example.com");
    }

    /**
     * Tests that the {@code getUserByUsername} method throws a {@link UserNotFoundException}
     * when the specified username is not found.
     *
     * <p>
     * The test mocks the repository's {@code findByUsername} method to return {@code null} for a non-existing username,
     * and verifies that a {@link UserNotFoundException} is thrown.
     * </p>
     */
    @Test
    public void testGetUserByUsernameNotFound() {
        when(userRepo.findByUsername("nonexistent")).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                                                       () -> userService.getUserByUsername("nonexistent"),
                                                       "Expected getUserByUsername to throw, but it didn't");

        assertThat(exception.getMessage()).isEqualTo("User with username 'nonexistent' not found");
    }
}

```
**11) UserControllerIntegrationTest:** src/test/java/com/example/userpurge/controller/UserControllerIntegrationTest.java
```java
package com.example.userqueryhub.controller;


import com.example.userqueryhub.dto.UserDTO;
import com.example.userqueryhub.model.User;
import com.example.userqueryhub.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link UserController}.
 *
 * <p>
 * This integration test class verifies the functionality of the {@link UserController}
 * endpoints by simulating HTTP requests using {@link MockMvc}. It tests both the user saving
 * and retrieval endpoints, ensuring that the controller interacts correctly with the service layer,
 * input validation is enforced, and data persistence behaves as expected.
 * </p>
 *
 * <h3>Test Cases:</h3>
 * <ul>
 *   <li><strong>saveUserIntegrationTest:</strong> Verifies that a valid user is successfully saved
 *   and persisted, returning HTTP 201.</li>
 *   <li><strong>getUserByUsernameIntegrationTest:</strong> Verifies that an existing user can be
 *   retrieved by username, returning HTTP 200 with the correct user data.</li>
 *   <li><strong>getUserByUsernameInvalidTest:</strong> Verifies that a request with an invalid (blank)
 *   username is rejected with an HTTP 400 Bad Request.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Prepares the test environment by clearing the user repository to ensure a clean state.
     */
    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    /**
     * Tests the saving of a valid user via the {@code /api/users/save} endpoint.
     *
     * <p>
     * This test verifies that providing a valid {@link UserDTO} results in the user being persisted
     * and a HTTP 201 status code being returned, along with the user details.
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */
    @Test
    public void saveUserIntegrationTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("securepassword");

        mockMvc.perform(post("/api/users/save")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.username", is("testuser")))
               .andExpect(jsonPath("$.email", is("testuser@example.com")));
    }

    /**
     * Tests retrieving a user by username via the {@code /api/users/username/{username}} endpoint.
     *
     * <p>
     * This test verifies that after saving a user, the user can be retrieved by their username,
     * resulting in a HTTP 200 status code and correct user details.
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */
    @Test
    public void getUserByUsernameIntegrationTest() throws Exception {
        // First, save a user to be retrieved later.
        User user = new User();
        user.setUsername("alice");
        user.setEmail("retrieve@example.com");
        user.setPassword("password");
        userRepository.save(user);

        mockMvc.perform(get("/api/users/username/{username}", "alice")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("alice")))
               .andExpect(jsonPath("$.email", is("retrieve@example.com")));
    }

    /**
     * Tests that an invalid request to retrieve a user by username with a whitespace username is handled correctly.
     *
     * <p>
     * This test verifies that if a username consisting of only whitespace is provided, the endpoint returns an HTTP 400 Bad Request,
     * ensuring that validation on the {@link UserController} is effective.
     * </p>
     *
     * @throws Exception if the HTTP request fails.
     */
    @Test
    public void getUserByUsernameInvalidTest() throws Exception {
        // Passing a whitespace string to trigger @NotBlank validation
        mockMvc.perform(get("/api/users/username/{username}", " ")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());
    }
}


```
**12) GlobalExceptionHandlerTest:** src/test/java/com/example/userpurge/exception/GlobalExceptionHandlerTest.java
```java
package com.example.userqueryhub.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.userqueryhub.exception.GlobalExceptionHandler;
import com.example.userqueryhub.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for {@link GlobalExceptionHandler} to ensure full code coverage.
 *
 * <p>
 * This test class simulates exceptions to verify that the GlobalExceptionHandler correctly processes:
 * <ul>
 *   <li>{@link UserNotFoundException} - returning a 404 Not Found response.</li>
 *   <li>Generic {@link Exception} - returning a 500 Internal Server Error response.</li>
 * </ul>
 * Each test case asserts that the returned ResponseEntity has the expected HTTP status code and message.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-27
 */
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests that the {@link GlobalExceptionHandler#handleResourceNotFound(UserNotFoundException)}
     * method returns a ResponseEntity with HTTP status 404 and the correct error message.
     */
    @Test
    public void testHandleResourceNotFound() {
        String errorMessage = "User not found";
        UserNotFoundException ex = new UserNotFoundException(errorMessage);

        ResponseEntity<?> response = globalExceptionHandler.handleResourceNotFound(ex);

        assertEquals(404, response.getStatusCodeValue(), "Expected status code 404 for UserNotFoundException");
        assertEquals(errorMessage, response.getBody(), "Expected response body to match the exception message");
    }

    /**
     * Tests that the {@link GlobalExceptionHandler#handleGeneric(Exception)} method returns
     * a ResponseEntity with HTTP status 500 and a generic error message.
     */
    @Test
    public void testHandleGeneric() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<?> response = globalExceptionHandler.handleGeneric(ex);

        assertEquals(500, response.getStatusCodeValue(), "Expected status code 500 for generic exceptions");
        assertEquals("Internal server error", response.getBody(), "Expected generic error message in response body");
    }
}

```
<a href="https://drive.google.com/file/d/1r2BNKyngnAiFPDFBLCbdVmXuh-oMJjYe/view?usp=drive_link">Iteration Two </a>

**Result:** Total line coverage is 100%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml includes Spring Boot Starter Web, Security, Validation, Lombok, Test, H2 Database, Jakarta Validation API, and DevTools dependencies with Java version set to 17 for a scalable REST API using the Edmonds‑Karp algorithm..
3. **Database & Application Properties:** Configure H2 DB credentials in application.properties (or application.yml) due to in-memory auth, for example:
```properties

spring.application.name=DataFieldFinder

server.port=8080

# mySql database connection (Database Configuration)
spring.datasource.url=jdbc:mysql://localhost:3306/DataFieldFinder
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

6. **Accessing Endpoints & Features:** REST Endpoints typically follow the pattern:

- **User:**
    - **List All Users:**
      ```bash
      curl -X GET http://localhost:8080/api/users
      ```
    - **Create User:**
      ```bash
      curl -X POST http://localhost:8080/api/users \
        -H "Content-Type: application/json" \
        -d '{"username": "alice", "password": "secret"}'
      ```
    - **Retrieve User by ID:**
      ```bash
      curl -X GET http://localhost:8080/api/users/1
      ```
# **Time and Space Complexity:**

- **Time Complexity:**  
  The user lookup operation using a derived query (`userRepository.findByUsername("alice")`) is generally executed in constant time **O(1)**, assuming that the username field is properly indexed.
    - **Lookup:** The query efficiently locates the user based on the indexed username, resulting in constant-time performance.
    - **Additional Overhead:** While underlying database operations may add slight overhead, the overall complexity remains close to **O(1)**.

- **Space Complexity:**
    - The operation uses minimal space as it only retrieves a single user entity into memory.
    - Memory usage is largely dependent on the size of the user object, but generally remains negligible, hence **O(1)**.
    - Any additional space overhead is managed by the database and the Spring Data JPA framework internally.

---

# **Conclusion**

The **User Query Service**, built with **Spring Boot** and leveraging Spring Data JPA, offers an efficient and robust solution for retrieving user entities from a database using a derived query method such as `userRepository.findByUsername("alice")`. This solution is designed with a layered architecture that emphasizes:

- **Efficient Performance:**  
  The lookup operation is highly performant with constant time complexity due to proper indexing and optimized database queries.

- **Robust Input Validation & Error Handling:**  
  The service ensures that valid data is processed while gracefully handling cases where the user is not found by throwing a custom `UserNotFoundException`.

- **Comprehensive Logging:**  
  Detailed logging through SLF4J provides complete traceability of operations, aiding in debugging and ensuring reliable production monitoring.

Overall, this approach delivers a scalable, maintainable, and performant solution for user data management, making it well-suited for enterprise applications that require efficient and reliable user retrieval processes.

Iteration One:https://drive.google.com/file/d/1KzBln2wAVTaKX_JUy0kbTP0vfko0Qhl2/view?usp=drive_link
Iteration Two: https://drive.google.com/file/d/1r2BNKyngnAiFPDFBLCbdVmXuh-oMJjYe/view?usp=drive_link
Download Code: https://drive.google.com/file/d/1idhigvauZ8bnUpQH4_rnfIdVRrDkbDk0/view?usp=drive_link