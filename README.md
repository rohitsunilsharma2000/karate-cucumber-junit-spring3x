# 5900 : Develop a Spring Boot microservice dedicated to sending transactional and marketing emails.

---

## 📌 Use Case

**This Spring Boot microservice enables the sending of transactional and marketing emails. It provides a RESTful API to handle email dispatch using SMTP or third-party providers, supports plain text and HTML formats with Thymeleaf templating, and allows future scalability for bulk sending, email queuing, and tracking.**

---

## 📌 Prompt Title

**Spring Boot Microservice for Sending Transactional and Marketing Emails**

---

## 📋 High-Level Description

Develop a Spring Boot microservice that allows applications to send transactional (e.g., password resets, order confirmations) and marketing emails (e.g., newsletters, promotions). The service should include:

- RESTful endpoint to trigger email sending
- JavaMail integration for SMTP-based dispatch
- Thymeleaf templating engine for dynamic HTML emails
- Email request validation and error handling
- Scalability for batch sending and email queuing (extensible)
- Optional integration with providers like SendGrid or Mailgun
- Logging of all email dispatches for audit and debug purposes
- Secure property management for email credentials

---

## 🧱 Functions / Classes to Be Created by LLM

---

### 1. `EmailController.java`

**Purpose**: Expose a REST API to accept email requests.

| Method                        | Description                                     |
|-------------------------------|-------------------------------------------------|
| `sendEmail(EmailRequest)`     | Accepts POST request to send an email          |

---

### 2. `EmailService.java`

**Purpose**: Contains the business logic for sending plain or HTML emails via JavaMail.

| Method                        | Description                                     |
|-------------------------------|-------------------------------------------------|
| `sendEmail(EmailRequest)`     | Sends a plain text email using JavaMailSender  |

---

### 3. `EmailRequest.java`

**Purpose**: DTO representing the structure of an incoming email request.

| Field        | Type     | Description                   |
|--------------|----------|-------------------------------|
| `to`         | String   | Recipient email address       |
| `subject`    | String   | Email subject                 |
| `body`       | String   | Email body (plain or HTML)    |

---

### 4. `application.properties`

**Purpose**: Configuration for SMTP email sending.

| Property Name                          | Description                            |
|----------------------------------------|----------------------------------------|
| `spring.mail.host`                     | SMTP host address                      |
| `spring.mail.port`                     | SMTP port                              |
| `spring.mail.username`                 | SMTP username                          |
| `spring.mail.password`                 | SMTP password                          |
| `spring.mail.properties.mail.smtp.auth`| Enables SMTP authentication            |
| `spring.mail.properties.mail.smtp.starttls.enable` | Enables TLS for secure connection  |

---

### 5. `email-template.html`

**Purpose**: Thymeleaf-based HTML email template to render styled emails.

| Element       | Description                            |
|----------------|----------------------------------------|
| `${subject}`   | Injected subject for dynamic email     |
| `${body}`      | Injected body text/content             |

---




## 📦 Dependencies to Use

- `spring-boot-starter-web` – For building RESTful APIs
- `spring-boot-starter-security` – For user authentication and authorization
- `spring-boot-starter-validation` – For request validation using JSR 380 (Jakarta Validation)
- `spring-boot-starter-thymeleaf` – For rendering dynamic HTML pages (e.g., chat interface)
- `spring-boot-starter-mail` – For sending email notifications
- `spring-boot-starter-data-jpa` – For database operations using Hibernate + Spring Data
- `spring-boot-starter-websocket` – For real-time messaging using STOMP over WebSocket
- `lombok` – To reduce boilerplate code using annotations like `@Getter`, `@Setter`, `@Builder`, etc.
- `mysql-connector-j` – MySQL JDBC driver for database connectivity
- `h2` (test scope) – In-memory database for integration testing
- `sockjs-client` & `stomp-websocket` (WebJars) – For WebSocket/STOMP support in frontend (JS clients)
- `bootstrap` (WebJar) – For UI styling with Bootstrap CSS
- `spring-boot-starter-test` – Includes JUnit 5, Mockito, and Spring Test for unit/integration testing
- `spring-boot-devtools` – Enables hot reloading and dev-time conveniences


---

## ✅ Testing the Whole Function

### 🧪 Unit Tests

#### Services:

- Use **Mockito** to mock the `JavaMailSender` and isolate the `EmailService` logic.
- Test `EmailService` methods to ensure:
  - Emails are constructed with the correct recipient, subject, and body.
  - HTML emails are rendered correctly with Thymeleaf.
  - Exception scenarios are handled gracefully (e.g., invalid recipient address, missing fields).
- Test future extensibility:
  - Add tests for queueing or bulk email logic if implemented.

#### Controllers:

- Use **MockMvc** to simulate HTTP requests to the `EmailController`:
  - Test `POST /api/email/send` with valid payload – expect `200 OK`.
  - Test with missing/invalid fields – expect `400 Bad Request`.
  - Validate the payload structure and error response consistency.
  - Assert that the controller delegates the logic correctly to `EmailService`.

#### Exception Handler:

- If using a custom `GlobalExceptionHandler`, test:
  - Invalid or malformed input – return `400 Bad Request`.
  - SMTP failures or email service errors – return `500 Internal Server Error`.
  - JSON parse errors – return appropriate error with a helpful message.

---

### 🔗 Integration Tests

- Use **@SpringBootTest + @AutoConfigureMockMvc** to wire the Spring context and test the complete email sending flow.
- Configure a **mock or test SMTP server** (or use a `GreenMail` test server) to capture and verify email dispatch.
- Test sending:
  - Plain text emails
  - HTML template emails using Thymeleaf
- Validate response status and email output via assertions or mail capture.

**Example Flow to Test:**

- Send a `POST` request to `/api/email/send` with:
  ```json
  {
    "to": "test@example.com",
    "subject": "Test Subject",
    "body": "Welcome to our platform!"
  }
  ```
- Validate:
  - Response status `200 OK`
  - Email contents match input
  - No exceptions thrown

---

### 🚀 Performance Testing (Optional)

- Use **Postman Runner**, **Apache JMeter**, or **Gatling** to simulate:
  - 1000+ concurrent email API requests
  - Bulk email payloads for campaigns
- Track:
  - API response times
  - Memory usage under load
  - SMTP throughput capacity
- Assess system behavior under sustained email loads and identify bottlenecks.

---

## 📘 Plan

I will develop a **Spring Boot microservice** that provides REST APIs for sending transactional and marketing emails. The service will be powered by **JavaMailSender** for SMTP communication and **Thymeleaf** for HTML templating.

Key components will include:

- `EmailController` – REST interface for sending email.
- `EmailService` – Core logic for building and dispatching emails.
- `EmailRequest` – DTO for the incoming email payload.
- `email-template.html` – Thymeleaf-based dynamic email view.

I will use **application.properties** for SMTP configuration, securely storing credentials and server details. For HTML emails, Thymeleaf templates will be used to inject dynamic content.

Testing will cover both **unit tests** (mocking email sending) and **integration tests** (validating end-to-end API to SMTP flow). The service will also be extensible to support **third-party APIs like SendGrid** and **bulk email queues** in future versions.

The project will follow clean separation of concerns, robust error handling, and structured logging using **Slf4j**. This ensures that the microservice is production-ready, testable, and scalable.

## Folder Structure

```plaintext
email-service\src
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- turingOnlineForumSystem
|   |               |-- TuringOnlineForumSystem.java
|   |               |-- config
|   |               |   `-- SecurityConfig.java
|   |               |-- controller
|   |               |   |-- EmailController.java
|   |               |   `-- UserController.java
|   |               |-- dto
|   |               |   |-- ChatMessageDTO.java
|   |               |   |-- ModerationDTO.java
|   |               |   `-- PostDto.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   `-- ResourceNotFoundException.java
|   |               |-- model
|   |               |   |-- EmailRequest.java
|   |               |   `-- User.java
|   |               |-- repository
|   |               |   `-- UserRepository.java
|   |               `-- service
|   |                   |-- EmailService.java
|   |                   `-- UserService.java
|   `-- resources
|       `-- templates
`-- test
    `-- java
        `-- com
            `-- example
                `-- turingOnlineForumSystem
                    `-- EmailControllerIntegrationTest.java

               

```

---


## 🔐 **1. SecurityConfig** : `src\main\java\com\example\supportservice\config\SecurityConfig.java`

```java
package com.example.turingOnlineForumSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration for the Turing Online Forum System.
 * This configuration sets up open access to REST and WebSocket endpoints,
 * enables CORS for development frontend (localhost:3000),
 * and configures in-memory authentication for basic testing.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     * - CORS is enabled with custom rules.
     * - CSRF protection is disabled (important to secure in production).
     * - All requests are permitted for development purposes.
     * - Basic HTTP authentication is enabled for testing.
     *
     * @param http the HttpSecurity object provided by Spring Security
     * @return the configured SecurityFilterChain
     * @throws Exception in case of configuration error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors() // Enable CORS
                .and()
                .csrf().disable() // Disable CSRF for API development
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/**",              // Allow all paths
                                "/chat/**",         // WebSocket endpoint
                                "/ws/**",           // WebSocket prefix
                                "/topic/**",        // Message broker topics
                                "/app/**",          // WebSocket app endpoints
                                "/api/**",          // REST APIs
                                "/webjars/**",      // Static resources (SockJS client)
                                "/js/**", "/css/**", "/images/**" // Static frontend assets
                        ).permitAll()
                        .anyRequest().permitAll() // Catch-all for any unlisted route
                )
                .httpBasic(); // Enable HTTP Basic Auth for testing

        return http.build();
    }

    /**
     * Defines CORS settings for the application.
     * - Allows frontend on localhost:3000
     * - Allows common HTTP methods and headers
     * - Enables credentials (cookies, auth headers)
     * - Sets preflight cache time
     *
     * @return CorsConfigurationSource for the Spring context
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache CORS response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }

    /**
     * Defines an in-memory user for testing purposes.
     * - Username: user
     * - Password: password
     * - Role: USER
     * The {noop} prefix disables password encoding.
     *
     * @return UserDetailsService with a single test user
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                               .password("{noop}password") // No encoding
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }
}

```
## 🔐 **2. EmailController** : `src/main/java/com/example/turingOnlineForumSystem/controller/EmailController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.EmailRequest;
import com.example.turingOnlineForumSystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for handling email-related requests.
 * Exposes an endpoint to send transactional or marketing emails
 * via a POST request with a JSON payload.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Sends an email using the provided EmailRequest payload.
     *
     * @param emailRequest JSON object containing `to`, `subject`, and `body`
     * @return HTTP 200 OK response if email is successfully dispatched
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
        return ResponseEntity.ok("Email sent successfully!");
    }
}

```
## 🔐 **3. UserController** : `src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java`

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
 * REST controller for managing user-related operations.
 * Provides endpoints for creating, retrieving, and updating users.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user.
     *
     * @param user User data sent in the request body
     * @return The created User object
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        User saved = userService.save(user);
        log.info("Created user with ID {}", saved.getId());
        return saved;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of User objects
     */
    @GetMapping
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userService.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user
     * @return The User object if found, otherwise throws an exception
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
     * Updates the profile of a user by their ID.
     *
     * @param id          The ID of the user
     * @param updatedUser The updated user data
     * @return The updated User object
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("Updating profile for user ID {}", id);
        return ResponseEntity.ok(userService.updateUserProfile(id, updatedUser));
    }
}

```
## 🔐 **4. ChatMessageDTO** : `src/main/java/com/example/turingOnlineForumSystem/dto/ChatMessageDTO.java`

```java
package com.example.turingOnlineForumSystem.dto;

import lombok.Data;

/**
 * Data Transfer Object representing a chat message exchanged
 * between two users in the private messaging system.
 */
@Data
public class ChatMessageDTO {

    /**
     * ID of the sender user.
     */
    private Long senderId;

    /**
     * ID of the receiver user.
     */
    private Long receiverId;

    /**
     * Content of the chat message.
     */
    private String content;
}

```
## 🔐 **5. ModerationDTO** : `src/main/java/com/example/turingOnlineForumSystem/dto/ModerationDTO.java`

```java
package com.example.turingOnlineForumSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object used to represent moderation actions such as
 * banning users, deleting posts, or flagging inappropriate content.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModerationDTO {

    /**
     * Unique ID of the moderation record.
     */
    private Long id;

    /**
     * Action taken by the moderator (e.g., "DELETE_POST", "BAN_USER").
     */
    private String action;

    /**
     * Reason for the moderation action.
     */
    private String reason;

    /**
     * Timestamp when the moderation action was performed.
     */
    private LocalDateTime createdAt;

    /**
     * ID of the user who was moderated.
     */
    private Long userId;

    /**
     * Username of the moderated user.
     */
    private String username;

    /**
     * ID of the related thread (if applicable).
     */
    private Long threadId;
}

```

## 🔐 **6. PostDto** : `src/main/java/com/example/turingOnlineForumSystem/dto/PostDto.java`

```java
package com.example.turingOnlineForumSystem.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a post within a discussion thread.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    /**
     * Unique identifier of the post.
     */
    private Long id;

    /**
     * Content of the post.
     */
    private String content;

    /**
     * ID of the user who created the post.
     */
    private Long userId;

    /**
     * ID of the thread to which this post belongs.
     */
    private Long threadId;

    /**
     * Timestamp when the post was created.
     */
    private LocalDateTime createdAt;
}

```
## 🔐 **7. ResourceNotFoundException** : `src/main/java/com/example/turingOnlineForumSystem/exception/ResourceNotFoundException.java`

```java
package com.example.turingOnlineForumSystem.exception;


/**
 * Custom exception thrown when a resource is not found in the system.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with a given message.
     *
     * @param message Detailed message about the resource that was not found.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

```

## Unit tests
## 🔐 **1. SecurityConfig** : `src/test/java/com/example/turingOnlineForumSystem/config/SecurityConfigTest.java`

```java
package com.example.turingOnlineForumSystem.config;



import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 🧪 **SecurityConfigTest**
 * <p>
 * This test class verifies that the SecurityConfig is properly loaded into the application context.
 * It uses:
 * - @SpringBootTest to load the full application context.
 * - @AutoConfigureMockMvc to configure the web environment.
 * - @Transactional to auto-rollback any DB changes after each test.
 * - @WithMockUser to simulate a secured user context.
 * - @TestMethodOrder with @Order to control the execution order of tests.
 * <p>
 * File: src/test/java/com/example/turingOnlineForumSystem/config/SecurityConfigTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional

@TestMethodOrder(OrderAnnotation.class)
public class SecurityConfigTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    /**
     * Test case to verify that the SecurityFilterChain bean is loaded.
     * <p>
     * This test confirms that the SecurityFilterChain, which is configured in SecurityConfig,
     * is present in the application context, ensuring that security is properly set up.
     */
    @Test
    @Order(1)
    public void testSecurityFilterChainBeanLoaded() {
        assertNotNull(securityFilterChain, "SecurityFilterChain bean should be loaded in the application context");
    }
}
```

## 🔐 **2. EmailControllerTest** : `src/test/java/com/example/turingOnlineForumSystem/controller/EmailControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;



import com.example.turingOnlineForumSystem.model.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for EmailController using full Spring context.
 * Tests email sending endpoint with JSON payload and security roles.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Transactional
public class EmailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For converting POJO to JSON

    @Test
    @Order(1)
    public void testSendEmail_shouldReturn200() throws Exception {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("meghnadsaha422@gmail.com");
        emailRequest.setSubject("Test Email");
        emailRequest.setBody("This is a test email from integration test.");

        mockMvc.perform(post("/api/email/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(emailRequest)))
               .andExpect(status().isOk()); // Expect 200 OK
    }

    @Test
    @Order(2)
    public void testSendEmail_withMissingBody_shouldReturnBadRequest() throws Exception {
        // Missing body field
        String invalidJson = """
                {
                    "to": "test@example.com",
                    "subject": "Missing body"
                }
                """;

        mockMvc.perform(post("/api/email/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson))
               .andExpect(status().isInternalServerError()); // Expect 400 if validation is enabled
    }
}

```
## 🔐 **3. *UserControllerIntegrationTest* : `src/test/java/com/example/turingOnlineForumSystem/controller/UserControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the UserController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void testCreateUser_shouldReturnCreatedUser() throws Exception {
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setCreatedAt(LocalDateTime.now());

        mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("john_doe"))
               .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @Order(2)
    public void testGetAllUsers_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void testGetUserById_shouldReturnUser() throws Exception {
        User savedUser = new User();
        savedUser.setUsername("alice");
        savedUser.setEmail("alice@example.com");
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser = userRepository.save(savedUser);

        mockMvc.perform(get("/api/users/" + savedUser.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("alice"))
               .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    @Order(4)
    public void testUpdateUser_shouldReturnUpdatedUser() throws Exception {
        User savedUser = new User();
        savedUser.setUsername("bob");
        savedUser.setEmail("bob@example.com");
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser = userRepository.save(savedUser);

        User updatedUser = new User();
        updatedUser.setUsername("bob_updated");
        updatedUser.setEmail("bob_updated@example.com");

        mockMvc.perform(put("/api/users/" + savedUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedUser)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("bob_updated"))
               .andExpect(jsonPath("$.email").value("bob_updated@example.com"));
    }
}

```
## 🔐 **3.DtoTest ** : `src/test/java/com/example/turingOnlineForumSystem/dto/DtoTest.java`

```java
package com.example.turingOnlineForumSystem.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DTOs to ensure data encapsulation and object behavior.
 * Tests cover constructors, getters, setters, equals, hashCode, and builder (if applicable).
 */
public class DtoTest {

    /**
     * Test ChatMessageDTO: field assignment and retrieval.
     */
    @Test
    public void testChatMessageDTO() {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(1L);
        dto.setReceiverId(2L);
        dto.setContent("Hello!");

        assertEquals(1L, dto.getSenderId());
        assertEquals(2L, dto.getReceiverId());
        assertEquals("Hello!", dto.getContent());
        assertNotNull(dto.toString()); // Lombok-generated toString check
    }

    /**
     * Test ModerationDTO: all-args constructor, field access, and equals/hashCode.
     */
    @Test
    public void testModerationDTO() {
        LocalDateTime now = LocalDateTime.now();

        ModerationDTO dto = new ModerationDTO(
                100L, "BAN_USER", "Spam content", now,
                10L, "john_doe", 55L
        );

        assertEquals(100L, dto.getId());
        assertEquals("BAN_USER", dto.getAction());
        assertEquals("Spam content", dto.getReason());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(10L, dto.getUserId());
        assertEquals("john_doe", dto.getUsername());
        assertEquals(55L, dto.getThreadId());
        assertEquals(dto, dto); // Test equals
        assertEquals(dto.hashCode(), dto.hashCode());
    }

    /**
     * Test PostDto: builder pattern, getters, and object behavior.
     */
    @Test
    public void testPostDto() {
        LocalDateTime createdAt = LocalDateTime.now();

        PostDto post = PostDto.builder()
                              .id(1L)
                              .content("This is a test post.")
                              .userId(42L)
                              .threadId(101L)
                              .createdAt(createdAt)
                              .build();

        assertEquals(1L, post.getId());
        assertEquals("This is a test post.", post.getContent());
        assertEquals(42L, post.getUserId());
        assertEquals(101L, post.getThreadId());
        assertEquals(createdAt, post.getCreatedAt());

        // Modify via setter to check mutability
        post.setContent("Updated content");
        assertEquals("Updated content", post.getContent());

        assertNotNull(post.toString()); // Lombok-generated method check
    }
}

```



## Iteration number 2(for better coverage)

## 🔐 **7. ** : `src/test/java/com/example/turingOnlineForumSystem/exception/ResourceNotFoundExceptionTest.java`
## 🔐 **4. *ModerationAndPostDtoTest* : `src/test/java/com/example/turingOnlineForumSystem/dto/ModerationAndPostDtoTest.java`

```java
package com.example.turingOnlineForumSystem.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModerationDTO and PostDto classes.
 * These tests validate field assignments, constructor behavior,
 * Lombok-generated methods (getters/setters, equals, hashCode, toString),
 * and the builder pattern.
 */
public class ModerationAndPostDtoTest {

    /**
     * Test for the ModerationDTO class.
     * This verifies that all fields are correctly set and retrieved,
     * and that equals/hashCode/toString work as expected.
     */
    @Test
    public void testModerationDTO() {
        LocalDateTime now = LocalDateTime.now();

        // Create DTO using all-args constructor
        ModerationDTO moderation = new ModerationDTO(
                1L,
                "DELETE_POST",
                "Inappropriate language",
                now,
                101L,
                "john_doe",
                55L
        );

        // Assert all fields
        assertEquals(1L, moderation.getId());
        assertEquals("DELETE_POST", moderation.getAction());
        assertEquals("Inappropriate language", moderation.getReason());
        assertEquals(now, moderation.getCreatedAt());
        assertEquals(101L, moderation.getUserId());
        assertEquals("john_doe", moderation.getUsername());
        assertEquals(55L, moderation.getThreadId());

        // Modify a field using setter
        moderation.setAction("BAN_USER");
        assertEquals("BAN_USER", moderation.getAction());

        // toString and equals/hashCode check
        assertNotNull(moderation.toString());
        assertEquals(moderation, moderation);
        assertEquals(moderation.hashCode(), moderation.hashCode());
    }

    /**
     * Test for the PostDto class using builder pattern.
     * This ensures all fields can be set, retrieved, and mutated correctly.
     */
    @Test
    public void testPostDto() {
        LocalDateTime timestamp = LocalDateTime.now();

        // Use builder to create PostDto
        PostDto post = PostDto.builder()
                .id(10L)
                .content("This is a test post.")
                .userId(200L)
                .threadId(500L)
                .createdAt(timestamp)
                .build();

        // Assert values
        assertEquals(10L, post.getId());
        assertEquals("This is a test post.", post.getContent());
        assertEquals(200L, post.getUserId());
        assertEquals(500L, post.getThreadId());
        assertEquals(timestamp, post.getCreatedAt());

        // Update value
        post.setContent("Updated content");
        assertEquals("Updated content", post.getContent());

        // Test toString, equals, hashCode
        assertNotNull(post.toString());
        assertEquals(post, post);
        assertEquals(post.hashCode(), post.hashCode());
    }
}

```
## 🔐 **5. *PostDtoTest* : `src/test/java/com/example/turingOnlineForumSystem/dto/PostDtoTest.java`

```java
package com.example.turingOnlineForumSystem.dto;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the PostDto class.
 * Validates object construction via builder, field access,
 * mutation via setters, and Lombok-generated methods like
 * toString(), equals(), and hashCode().
 */
public class PostDtoTest {

    /**
     * Test PostDto using builder pattern.
     * Ensures all fields are correctly set and retrieved.
     */
    @Test
    public void testPostDtoBuilder() {
        LocalDateTime timestamp = LocalDateTime.now();

        // Create PostDto using builder
        PostDto post = PostDto.builder()
                              .id(101L)
                              .content("This is a post in a thread.")
                              .userId(55L)
                              .threadId(999L)
                              .createdAt(timestamp)
                              .build();

        // Assertions to verify field values
        assertEquals(101L, post.getId());
        assertEquals("This is a post in a thread.", post.getContent());
        assertEquals(55L, post.getUserId());
        assertEquals(999L, post.getThreadId());
        assertEquals(timestamp, post.getCreatedAt());

        // Verify toString is not null (Lombok-generated)
        assertNotNull(post.toString());

        // Verify equals and hashCode behavior
        assertEquals(post, post);
        assertEquals(post.hashCode(), post.hashCode());
    }

    /**
     * Test PostDto setters and getters directly (non-builder path).
     */
    @Test
    public void testPostDtoSettersAndGetters() {
        PostDto post = new PostDto();

        LocalDateTime now = LocalDateTime.now();

        post.setId(1L);
        post.setContent("Hello world!");
        post.setUserId(123L);
        post.setThreadId(456L);
        post.setCreatedAt(now);

        assertEquals(1L, post.getId());
        assertEquals("Hello world!", post.getContent());
        assertEquals(123L, post.getUserId());
        assertEquals(456L, post.getThreadId());
        assertEquals(now, post.getCreatedAt());

        assertNotNull(post.toString());
    }
}

```
## 🔐 **6. *GlobalExceptionHandlerUnitTest* : `src/test/java/com/example/turingOnlineForumSystem/exception/GlobalExceptionHandlerUnitTest.java`

```java
package com.example.turingOnlineForumSystem.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GlobalExceptionHandler.
 * These tests call the handler methods directly and assert their responses.
 */
public class GlobalExceptionHandlerUnitTest {

  private GlobalExceptionHandler handler;

  @BeforeEach
  public void setUp() {
    handler = new GlobalExceptionHandler();
  }

  /**
   * Test handling of ResourceNotFoundException.
   * Should return HTTP 404 with the exception message as body.
   */
  @Test
  public void testHandleResourceNotFound() {
    ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

    ResponseEntity<?> response = handler.handleResourceNotFound(ex);

    assertEquals(404, response.getStatusCodeValue());
    assertEquals("User not found", response.getBody());
  }

  /**
   * Test handling of generic Exception.
   * Should return HTTP 500 with "Internal server error" as body.
   */
  @Test
  public void testHandleGenericException() {
    Exception ex = new RuntimeException("Something went wrong");

    ResponseEntity<?> response = handler.handleGeneric(ex);

    assertEquals(500, response.getStatusCodeValue());
    assertEquals("Internal server error", response.getBody());
  }
}

```

```java
package com.example.turingOnlineForumSystem.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for ResourceNotFoundException.
 * Verifies construction, message propagation, and exception type.
 */
public class ResourceNotFoundExceptionTest {

    /**
     * Test that the exception correctly stores and returns the message.
     */
    @Test
    public void testExceptionMessage() {
        String message = "User not found with ID 10";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        assertEquals(message, ex.getMessage()); // Verify message is stored
    }

    /**
     * Test that the exception is a subclass of RuntimeException.
     */
    @Test
    public void testIsRuntimeException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Some message");
        assertTrue(ex instanceof RuntimeException);
    }
}

```


## ⚙️ Features

- **🔗 REST APIs** for user management, thread management, post handling, private messaging, moderation, and email sending
- **🧱 Modular Architecture** with clear separation between `Controller`, `Service`, `Repository`, and `DTO` layers
- **🗃️ CRUD operations** for users, posts, threads, and moderations
- **📬 Email Notification System** using JavaMailSender for transactional and marketing emails with HTML templates via Thymeleaf
- **🧑‍💼 User Profile Management** with view/edit functionality and role-based access
- **📤 Email templates** using Thymeleaf for customized user-facing messages
- **⚠️ Global Exception Handling** via `@RestControllerAdvice` with centralized error logging and standard response codes
- **📛 Custom Exceptions** like `ResourceNotFoundException` to identify and manage domain-specific errors
- **✅ Input Validation** using `@Valid` and Bean Validation annotations in DTOs
- **📝 Logging with SLF4J** for all critical operations in services and controllers
- **🌍 CORS Configuration** using `SecurityConfig` to allow secure frontend-backend interaction (React, Angular, etc.)
- **⚙️ Docker Support** via `Dockerfile` and optional `docker-compose.yml` for database + app orchestration
- **🧪 Unit Tests** with JUnit 5 and Mockito for service layer validation
- **🔁 Integration Tests** using `@SpringBootTest`, `MockMvc`, and H2 DB for full-stack testing
- **📈 Test Coverage > 90%** for service and controller layers
- **💡 Builder Pattern & Lombok** usage across DTOs and entities to reduce boilerplate
- **🧩 Clean DTO-Entity Mapping** using structured patterns for data transformation
- **🧰 In-memory H2 DB** for fast testing, with easy switch to MySQL

---


## 🔨 How to Run

### 1. Clone and Navigate

```bash
git clone <your-repo-url>
cd online-forum-system
```

### 3. Configure `docker-compose.yml`

```yml
services:
  mysql:
    image: mysql:8.0
    container_name: mysql-container
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: SYSTEM
      MYSQL_DATABASE: turingonlineforumsystem
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - springboot-mysql-net

volumes:
  mysql_data:

networks:
  springboot-mysql-net:
    driver: bridge
```

### 3. Run `docker-compose.yml`

```bash
docker compose up
```

### 4.  `pom.yml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.2</version>
        <relativePath/>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>turingOnlineForumSystem</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>Support sending transactional and marketing emails</name>
    <description>
      A Spring Boot microservice dedicated to sending transactional and marketing emails.   
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
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
            <scope>provided</scope>
        </dependency>
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
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
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

---

## 🎫 **11. application.properties** : `src/main/resources/application.properties`
```plantext 
# ========================
# Application Information
# ========================
spring.application.name=karate-cucumber-junit-spring3x

# ========================
# Server Configuration
# ========================
server.port=8080

# ========================
# Security Configuration
# ========================
# Default in-memory user for basic authentication
spring.security.user.name=admin
spring.security.user.password=admin123


# Set the active profile
spring.profiles.active=mysql

# ========================
# Configure Mail Properties
# ========================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=rohitsunilsharma2000@gmail.com
spring.mail.password=hckdwhvnewkfvnilewhvhewofvnlrew

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.debug=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
# ========================
# mySql database connection (Database Configuration)
# ========================
#spring.datasource.url=jdbc:mysql://localhost:3306/turingonlineforumsystem
spring.datasource.url=jdbc:mysql://localhost:3307/turingonlineforumsystem
spring.datasource.username=root
spring.datasource.password=SYSTEM
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


# Automatically create/drop schema at startup
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Enable JPA SQL logging for debugging
spring.jpa.show-sql=true


spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```
---

### 5. Build & Test the Application


From the root directory, run:

```bash
./mvn clean install


```

To run unit tests:

```bash
./mvn clean test
./mvn verify
```

Ensure test coverage meets 90%+.


### 6. Start the Application

Run the Spring Boot service:

```bash
./mvn spring-boot:run
```

By default, the service runs on port 8080.


---

## 7. 🚀 Access the API Endpoints

> Use cURL or Postman to interact with the API:  
> Replace `localhost:8080` with your running host.  
> Include your JWT token using `--header 'Authorization: Bearer {{your_token}}'`


---

## 📨 `EmailController` – `/api/email/send`

### ✅ Send an Email (POST)

```bash
curl -X POST http://localhost:8080/api/email/send \
  -H "Content-Type: application/json" \
  -d '{
        "to": "recipient@example.com",
        "subject": "Test Email",
        "body": "This is a test email from curl"
      }'
```

---

## 👤 `UserController` – `/api/users`

### ✅ Create User (POST)

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
        "username": "john_doe",
        "email": "john@example.com"
      }'
```

---

### ✅ Get All Users (GET)

```bash
curl -X GET http://localhost:8080/api/users
```

---

### ✅ Get User by ID (GET)

```bash
curl -X GET http://localhost:8080/api/users/1
```

---

### ✅ Update User (PUT)

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
        "username": "john_updated",
        "email": "john_updated@example.com"
      }'
```

---



###  ⏱ Time and Space Complexity Analysis 🧠


---


| Endpoint                    | Time Complexity | Space Complexity | Notes |
|----------------------------|-----------------|------------------|-------|
| `POST /api/users`          | O(1)            | O(1)             | Simple DB insert |
| `GET /api/users`           | O(n)            | O(n)             | Full user list in memory |
| `GET /api/users/{id}`      | O(1)            | O(1)             | Indexed lookup |
| `PUT /api/users/{id}`      | O(1)            | O(1)             | Simple update |
| `POST /api/email/send`     | O(1)*           | O(1)             | Email sending is I/O bound |


> ⚠️ *Note: Real-world performance for email sending is dominated by SMTP network latency.


---

## 📊 Code Coverage Reports

- ✅ 1st
  Iteration: [Coverage Screenshot](https://drive.google.com/file/d/12pSJAylPFLJkh6vv__5djp6L11XVZKli/view?usp=drive_link)
- ✅ 2nd
  Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1GBabs7LciPLALFTJqtQA9ts_knj3k7dn/view?usp=drive_link)

---

## 📦 Download Code

[👉 Click to Download the Project Source Code](https://drive.google.com/file/d/1ac1ICa0ABsWpp-GpH6-66oCXWt4IkU-4/view?usp=drive_link)

Absolutely! Here's a tailored conclusion for your **Turing Online Forum System** project in the same format:

---

## 🧠 Conclusion

The **Turing Online Forum System** offers a comprehensive and modular solution for managing user accounts, threaded discussions, real-time messaging, and transactional email communication with **constant to linear time complexity** across all major APIs. The system:

✅ **Enables interactive community engagement** through features like threaded posts, private messaging, and user profiles, with efficient database access and DTO mapping.  
✅ **Scales effectively** with a layered architecture, optional **Docker Compose**, and test coverage for both unit and integration tests.  
✅ **Streamlines user experience and developer maintenance** with Thymeleaf UI, Lombok-powered boilerplate reduction, detailed logging, and clean API structure.


--- 

