# Spring Boot Modular Caching Service with Redis

---



## 📌 Use Case
**This online forum system shall be used in community-driven applications where users interact through threads, posts, private messages, and social features. The system provides tools for discussion management, real-time messaging, moderation, user engagement, and content discovery, making it ideal for:**


## 📌 Prompt Title  
**Spring Boot Online Forum System with Moderation, Messaging, and Community Features**

## 📋 High-Level Description

Develop a full-featured online forum system using Spring Boot that supports:

- User registration and profile management
- Thread creation and discussion posts
- User moderation (ban/delete post/thread)
- Real-time private messaging with WebSocket
- Notification system for events like new messages or replies
- Follow system for users
- Search functionality for threads and users


---

## 🧱 Functions / Classes to Be Created by LLM

---

### 1. `UserController.java`

**Purpose**: Handle user creation, retrieval, and profile updates.

| Method                         | Description                              |
|--------------------------------|------------------------------------------|
| `createUser(User)`             | Creates a new user                       |
| `getAllUsers()`                | Returns list of all users                |
| `getUserById(Long id)`         | Fetch user details by ID                 |
| `updateUser(Long, User)`       | Updates user's profile                   |

---

### 2. `ThreadController.java`

**Purpose**: Manage discussion threads.

| Method                         | Description                              |
|--------------------------------|------------------------------------------|
| `createThreads(Threads)`       | Creates a new discussion thread          |
| `getThreads(Long id)`          | Gets thread by ID                        |
| `getAllThreadss()`             | Lists all threads                        |
| `updateThreads(Long, Threads)` | Updates thread title/content             |
| `deleteThreads(Long)`          | Deletes thread by ID                     |

---

### 3. `PostController.java`

**Purpose**: Manage replies/posts in threads.

| Method                             | Description                              |
|------------------------------------|------------------------------------------|
| `createPost(Long threadId, Post)`  | Create a post under a thread             |
| `getPostsByThread(Long threadId)`  | Get all posts for a thread               |

---

### 4. `MessagingController.java`

**Purpose**: Real-time and historical private messaging.

| Method                                    | Description                              |
|-------------------------------------------|------------------------------------------|
| `sendMessage(ChatMessageDTO)`             | Sends message via WebSocket              |
| `getHistory(Long senderId, Long receiverId)` | Fetches chat history between users     |
| `chatPage(Long userId, Model model)`      | Loads Thymeleaf UI page for chat         |

---

### 5. `ModerationController.java`

**Purpose**: Handle moderation actions like deletion and banning.

| Method                                     | Description                              |
|-------------------------------------------|------------------------------------------|
| `deleteThread(Long, Long, String)`        | Delete a thread with moderator ID        |
| `deletePost(Long, Long, String)`          | Delete a post with moderator ID          |
| `banUser(Long, String)`                   | Ban a user with reason                   |
| `getModerationHistory(Long)`              | Get user's moderation history            |

---

### 6. `NotificationController.java`

**Purpose**: Manage user notifications.

| Method                                   | Description                              |
|------------------------------------------|------------------------------------------|
| `getUserNotifications(Long)`             | Get all notifications for a user         |
| `markNotificationAsRead(Long)`           | Mark a notification as read              |
| `deleteNotification(Long)`               | Delete a notification by ID              |

---

### 7. `FollowController.java`

**Purpose**: Social feature to follow other users.

| Method                                        | Description                              |
|-----------------------------------------------|------------------------------------------|
| `followUser(Long followerId, Long followingId)` | Follower follows another user           |
| `getFollowing(Long userId)`                   | List of users the given user is following|

---

### 8. `SearchController.java`

**Purpose**: Search feature for users and threads.

| Method                              | Description                              |
|-------------------------------------|------------------------------------------|
| `searchUsers(String q)`             | Search users by partial username         |
| `searchThreads(String q)`           | Search threads by title/content          |

---

### 9. `EmailController.java`

**Purpose**: Send emails via SMTP.

| Method                             | Description                              |
|------------------------------------|------------------------------------------|
| `sendEmail(EmailRequest)`          | Sends a plain text email                 |

---

### 10. `AuthController.java`

**Purpose**: JWT-based authentication & profile endpoints.

| Method                         | Description                              |
|--------------------------------|------------------------------------------|
| `register(RegisterRequest)`   | Registers a user                         |
| `login(LoginRequest)`         | Authenticates and returns JWT            |
| `profile(Principal)`          | Gets logged-in user's profile            |
| `updateProfile(Principal, UserDto)` | Updates logged-in user's profile       |

---

### 11. `MessagingService.java`

**Purpose**: Business logic for messaging.

| Method                                   | Description                              |
|------------------------------------------|------------------------------------------|
| `sendMessage(ChatMessageDTO)`           | Saves message and triggers notification  |
| `getChatHistory(Long, Long)`            | Returns chat messages between 2 users    |

---

### 12. `ModerationService.java`

**Purpose**: Handle thread/post deletion and user banning.

| Method                                | Description                              |
|---------------------------------------|------------------------------------------|
| `deleteThread(Long, Long, String)`    | Deletes a thread with logging            |
| `deletePost(Long, Long, String)`      | Deletes a post and logs moderation       |
| `banUser(Long, String)`               | Bans a user                              |
| `getModerationHistory(Long)`          | Returns user's moderation logs           |

---

### 13. `NotificationService.java`

**Purpose**: Business logic for notifications.

| Method                                 | Description                              |
|----------------------------------------|------------------------------------------|
| `sendNotification(Long, String)`      | Sends notification to a user             |
| `sendNotification(User, String)`      | Overloaded version to send using object  |
| `getNotificationsForUser(Long)`       | Returns notifications for user           |
| `markAsRead(Long)`                    | Marks notification as read               |
| `deleteNotification(Long)`            | Deletes notification                     |

---

### 14. `PostService.java`

**Purpose**: Business logic for posts.

| Method                                      | Description                              |
|---------------------------------------------|------------------------------------------|
| `createPost(Post, Long threadId)`          | Creates a post under a thread            |
| `getPostsByThread(Long threadId)`          | Returns posts as PostDto list            |
| `deletePostsByThread(Long threadId)`       | Deletes all posts of a thread            |

---

### 15. `ThreadService.java`

**Purpose**: Thread operations including timestamps.

| Method                              | Description                              |
|-------------------------------------|------------------------------------------|
| `createThread(Threads)`            | Saves a new thread                       |
| `updateThread(Long, Threads)`      | Updates thread by ID                     |
| `deleteThread(Long)`               | Deletes thread by ID                     |
| `getThread(Long)`                  | Fetch single thread                      |
| `getAllThreads()`                  | Get all threads                          |

---

### 16. `UserService.java`

**Purpose**: User profile and registration logic.

| Method                          | Description                              |
|----------------------------------|------------------------------------------|
| `getUserById(Long)`             | Get user by ID                           |
| `updateUserProfile(Long, User)` | Update user's profile                    |
| `findById(Long)`                | Optional fetch by ID                     |
| `findAll()`                     | Fetch all users                          |
| `save(User)`                    | Create/save new user                     |

---

### 17. `GlobalExceptionHandler.java`

**Purpose**: Catch and map exceptions globally.

| Method                                   | Description                              |
|------------------------------------------|------------------------------------------|
| `handleResourceNotFound(...)`           | Returns 404 for missing resources        |
| `handleGenericException(...)`           | Catches generic exceptions               |

---

### 18. `WebSocketConfig.java`

**Purpose**: WebSocket configuration for chat.

| Method                            | Description                              |
|-----------------------------------|------------------------------------------|
| `registerStompEndpoints()`        | Define `/chat` WebSocket endpoint        |
| `configureMessageBroker()`        | Enable message broker `/topic`, `/app`   |

---

### 19. `SecurityConfig.java`

**Purpose**: Open endpoints for development (override later).

| Method                          | Description                              |
|----------------------------------|------------------------------------------|
| `filterChain(HttpSecurity)`     | Disable CSRF, allow all URLs (initial)   |
| `corsConfigurationSource()`     | Allow CORS from localhosts               |

---

### 20. `TuringOnlineForumSystemApplication.java`

**Purpose**: Main Spring Boot application class.

| Method                       | Description                              |
|------------------------------|------------------------------------------|
| `main(String[] args)`        | Bootstraps Spring Boot app               |

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
- Mock database repositories (UserRepository, ThreadRepository, etc.) using **Mockito**.
- Test service methods in:
  - `UserService` – CRUD operations and validations
  - `PostService` – Post creation and retrieval
  - `ThreadService` – Thread operations
  - `NotificationService` – Notification logic
  - `MessagingService` – Message sending & chat history
  - `ModerationService` – Ban/delete operations and audit logging
- Simulate and assert expected outcomes and exception flows (`ResourceNotFoundException`, etc.).

#### Controllers:
- Use **MockMvc** to simulate HTTP requests to REST controllers:
  - Validate status codes (`200 OK`, `404 Not Found`, `400 Bad Request`, etc.)
  - Check input validation (e.g. required fields, invalid types)
  - Ensure proper payloads (JSON structure, missing attributes)

#### Exception Handler:
- Verify responses from `GlobalExceptionHandler`:
  - Return correct HTTP codes and error messages for:
    - Resource not found
    - Invalid input
    - Unexpected server errors

---

### 🔗 Integration Tests

- Use **@SpringBootTest + @AutoConfigureMockMvc** to wire up the entire Spring context.
- Set up **H2 in-memory DB** for isolated database interactions.
- Test end-to-end flow:
  - Create users, threads, and posts
  - Send private messages over REST/WebSocket
  - Fetch notification feeds
  - Test moderation actions (ban/delete)
- Simulate multiple user interactions and verify persistence and real-time communication.

---

### 🚀 Performance Testing (Optional)

- Measure response times using **Apache JMeter** or **Postman Collection Runner**.
- Simulate high-traffic scenarios with concurrent user/message requests.
- Track latency and DB query reduction by caching popular threads/posts (if added).
- Evaluate WebSocket messaging throughput.

---


## 📘 Plan

I will implement a **complete online forum system** using **Spring Boot**, adhering to clean architecture principles and modular MVCS (Model-View-Controller-Service) design. The platform will support core community features including **discussion threads**, **user profile management**, **moderation tools**, **private messaging**, and **notifications**.

I will create core modules such as:
- `ThreadService`, `PostService`, `UserService` – to handle CRUD logic for content and users.
- `ModerationService` – to support thread/post deletion and user bans with audit logging.
- `MessagingService` – for real-time WebSocket-based private chat between users.
- `NotificationService` – to deliver alerts (e.g. new messages, replies) to users.

The application will expose REST APIs for interacting with all modules and a WebSocket endpoint (`/chat`) for messaging. I will use **Thymeleaf** to build a lightweight UI for features like chat, notifications, and profiles.

I will enforce **input validation** using `@Valid` and DTOs where applicable. A centralized `GlobalExceptionHandler` will handle all exceptions, returning meaningful messages and status codes. Logging will be implemented using `Slf4j`, with care taken to avoid logging sensitive user data.

Security will be enforced via **Spring Security**, supporting **JWT-based login/registration**, **role-based access control**, and **CORS configuration**. Admin-only routes (e.g. banning, ticket assignment) will be protected.

I will write **unit tests** using **JUnit 5 + Mockito** for all services and repositories. **Integration tests** will be developed using **Spring Boot Test + MockMvc**, simulating complete flows including user interactions, post/thread creation, moderation, and messaging.

The codebase will follow clean coding standards and all classes/methods will be documented for easy maintainability and scalability.


---

## 📁 Folder Structure

```plaintext
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- supportservice
|   |               |-- TuringLLMTuningSystem.java
|   |               |-- config
|   |               |   |-- SecurityConfig.java
|   |               |   |-- WebSocketConfig.java
|   |               |   `-- WebSocketJwtInterceptor.java
|   |               |-- controller
|   |               |   |-- AuthController.java
|   |               |   |-- ChatController.java
|   |               |   |-- ChatRestController.java
|   |               |   `-- TicketController.java
|   |               |-- dto
|   |               |   |-- ChatMessage.java
|   |               |   |-- LoginRequest.java
|   |               |   |-- RegisterRequest.java
|   |               |   |-- TicketRequest.java
|   |               |   |-- TicketResponse.java
|   |               |   |-- UserDto.java
|   |               |   `-- UserResponseDTO.java
|   |               |-- enums
|   |               |   |-- Priority.java
|   |               |   |-- Role.java
|   |               |   `-- TicketStatus.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   |-- ResourceNotFoundException.java
|   |               |   `-- TicketNotFoundException.java
|   |               |-- filter
|   |               |   `-- JwtAuthenticationFilter.java
|   |               |-- model
|   |               |   |-- Attachment.java
|   |               |   |-- MessageEntity.java
|   |               |   |-- Ticket.java
|   |               |   |-- User.java
|   |               |   `-- UserRole.java
|   |               |-- repository
|   |               |   |-- MessageRepository.java
|   |               |   |-- TicketRepository.java
|   |               |   `-- UserRepository.java
|   |               |-- service
|   |               |   |-- ChatService.java
|   |               |   |-- CustomUserDetailsService.java
|   |               |   |-- EmailService.java
|   |               |   |-- JwtService.java
|   |               |   |-- TicketReminderScheduler.java
|   |               |   |-- TicketService.java
|   |               |   `-- UserService.java
|   |               `-- utils
|   |                   |-- FileStorageUtil.java
|   |                   `-- JwtUtil.java
|   `-- resources
|       |-- application-mysql.properties
|       |-- application.properties
|       `-- templates
|           `-- index.html
`-- test
    `-- java
        `-- com
            `-- example
                `-- supportservice
                    |-- TuringLLMTuningSystem.java
                    |-- config
                    |   |-- SecurityConfigTest.java
                    |   |-- WebSocketConfigTest.java
                    |   `-- WebSocketJwtInterceptorTest.java
                    |-- controller
                    |   |-- AuthControllerIntegrationTest.java
                    |   |-- ChatControllerMockTest.java
                    |   |-- ChatRestControllerTest.java
                    |   |-- TicketControllerIntegrationTest.java
                    |   `-- WebSocketIntegrationTest.java
                    |-- dto
                    |   |-- ChatMessageTest.java
                    |   |-- LoginRequestTest.java
                    |   |-- RegisterRequestTest.java
                    |   |-- TicketRequestTest.java
                    |   |-- TicketResponseTest.java
                    |   |-- UserDtoTest.java
                    |   `-- UserResponseDTOTest.java
                    |-- exception
                    |   `-- GlobalExceptionHandlerTest.java
                    |-- model
                    |   |-- AttachmentTest.java
                    |   `-- UserRoleTest.java
                    |-- service
                    |   |-- ChatServiceTest.java
                    |   |-- CustomUserDetailsServiceTest.java
                    |   |-- JwtServiceTest.java
                    |   `-- TicketReminderScheduler.java
                    `-- utils
                        `-- JwtUtilTest.java


```

---

## 🌐 1. `TuringOnlineForumSystem` — Application Entry Point

📁 **File**: `src/main/java/com/example/turingOnlineForumSystem/TuringOnlineForumSystem.java`

```java
package com.example.turingOnlineForumSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 🚀 Main entry point for the Turing Online Forum System application.
 *
 * This class is responsible for bootstrapping and launching the Spring Boot-based
 * forum application. It sets up the application context, starts the embedded server,
 * and initializes all components including controllers, services, repositories, and configuration classes.
 *
 * 🧩 Features Initialized:
 * - REST API support for managing threads, posts, users, messaging, moderation, and notifications.
 * - WebSocket support for real-time private messaging.
 * - Thymeleaf support for dynamic server-rendered HTML views (chat, profile, notifications).
 * - Spring Security for JWT-based authentication and role-based authorization.
 * - JPA with MySQL integration for data persistence.
 *
 * 🏷️ Annotations Used:
 * - {@code @SpringBootApplication}: Combines {@code @Configuration}, {@code @EnableAutoConfiguration},
 *   and {@code @ComponentScan} into a single annotation. It marks this class as the base configuration class
 *   and triggers component scanning for the base package and subpackages.
 */
@SpringBootApplication
public class TuringOnlineForumSystem {

    /**
     * Main method to launch the Spring Boot application.
     *
     * @param args Command-line arguments passed at startup.
     */
    public static void main(String[] args) {
        SpringApplication.run(TuringOnlineForumSystem.class, args);
    }
}

```


## 🌐 2. `SecurityConfig` 

📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/config/SecurityConfig.java`

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
 * 🔐 SecurityConfig
 *
 * This configuration class sets up HTTP security, CORS, and user authentication
 * for the Turing Online Forum System.
 *
 * 📌 Annotations Used:
 * - @Configuration: Marks this class as a Spring configuration.
 * - @EnableWebSecurity: Enables Spring Security support.
 *
 * 🧩 Features Configured:
 * - Allows all WebSocket and REST endpoints.
 * - Disables CSRF for simplicity (suitable for stateless APIs and WebSockets).
 * - Configures CORS to allow cross-origin requests (especially for local development).
 * - Defines an in-memory user with no password encoding for testing.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 🔐 Defines the security filter chain:
     * - Enables CORS.
     * - Disables CSRF.
     * - Permits access to all endpoints including WebSocket and REST.
     * - Enables HTTP basic authentication for quick testing.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(
                    "/**",
                    "/chat/**",
                    "/ws/**",
                    "/topic/**",
                    "/app/**",
                    "/api/**",
                    "/webjars/**",
                    "/js/**", "/css/**", "/images/**"
                ).permitAll()
                .anyRequest().permitAll()
            )
            .httpBasic(); // for basic testing

        return http.build();
    }

    /**
     * 🌍 Configures global CORS policy:
     * - Allows frontend domains (e.g., localhost:3000).
     * - Supports all HTTP methods and headers.
     * - Allows credentials and caches preflight results for 1 hour.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 👤 In-memory user authentication setup:
     * - Adds a default user with username: `user` and password: `password`
     * - Role: `USER`
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                               .password("{noop}password") // {noop} disables password encoding
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }
}
```
## 🌐 3. `WebSocketConfig` 

📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/config/WebSocketConfig.java`

```java
package com.example.turingOnlineForumSystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 📡 WebSocketConfig
 *
 * This configuration class enables and sets up the WebSocket messaging capabilities
 * using STOMP protocol for the Turing Online Forum System.
 *
 * 📌 Annotations Used:
 * - @Configuration: Indicates this is a Spring configuration class.
 * - @EnableWebSocketMessageBroker: Enables support for WebSocket message handling, backed by a message broker.
 *
 * 🧩 Features Configured:
 * - Defines a STOMP endpoint at `/chat` for client connections.
 * - Enables a simple in-memory message broker to handle messaging between clients.
 * - Prefixes destinations with `/app` for messages sent from client to server.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 🔌 Registers the STOMP WebSocket endpoint:
     * - Path: `/chat`
     * - Allows connections from all origins (for development ease).
     * - Enables SockJS fallback for browsers that don't support WebSocket natively.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * 🧭 Configures the message broker:
     * - Enables a simple in-memory broker with destination prefix `/topic`
     *   (used for broadcasting messages to subscribed clients).
     * - Sets the application destination prefix to `/app`, which is used for messages
     *   sent from client to server-side @MessageMapping handlers.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // broker for subscribers
        registry.setApplicationDestinationPrefixes("/app"); // for message sending
    }
}

```

## 💬 **3. ChatViewController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/ChatViewController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 💬 ChatViewController
 *
 * This controller is responsible for rendering the WebSocket-based chat UI
 * using Thymeleaf templating for the Turing Online Forum System.
 *
 * 📌 Annotations Used:
 * - @Controller: Indicates that this is a Spring MVC controller that returns view templates.
 *
 * 🧩 Features Configured:
 * - Renders the chat interface located at `templates/chat.html`.
 * - Accepts a `userId` as a query parameter and injects it into the view model.
 */
@Controller
public class ChatViewController {

    /**
     * 🌐 GET `/chat`
     *
     * Endpoint to render the chat UI page using Thymeleaf.
     *
     * @param userId The ID of the current user (passed as a query parameter).
     * @param model  Spring's `Model` object to pass attributes to the view.
     * @return The name of the Thymeleaf template to be rendered: `chat.html`.
     *
     * 🧠 Usage:
     * Visiting `/chat?userId=123` will render `chat.html` with `userId` available in the frontend.
     */
    @GetMapping("/chat")
    public String chatPage(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);  // Inject user ID into the frontend
        return "chat";  // Loads templates/chat.html
    }
}
```

## 📧 **4. EmailController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/EmailController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.EmailRequest;
import com.example.turingOnlineForumSystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 📧 EmailController
 *
 * This REST controller handles email-related operations for the
 * Turing Online Forum System, specifically sending emails via a POST request.
 *
 * 📌 Annotations Used:
 * - @RestController: Indicates that this class handles REST requests and returns JSON/XML responses.
 * - @RequestMapping("/api/email"): Base URL path for all email-related endpoints.
 *
 * 🧩 Features Configured:
 * - Sends email using a service layer.
 * - Accepts email data in JSON format.
 * - Returns HTTP response indicating success.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    /**
     * 📬 EmailService instance to delegate email sending logic.
     */
    @Autowired
    private EmailService emailService;

    /**
     * 📤 POST `/api/email/send`
     *
     * Endpoint to send an email using the provided request body.
     *
     * @param emailRequest A JSON payload containing recipient, subject, and body.
     * @return HTTP 200 OK response with success message if email is sent.
     *
     * 🧠 Example Request:
     * ```json
     * {
     *   "to": "user@example.com",
     *   "subject": "Welcome!",
     *   "body": "Thank you for joining the forum."
     * }
     * ```
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
        return ResponseEntity.ok("Email sent successfully!");
    }
}
```



## 💬 **5. MessagingController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/MessagingController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.dto.ChatMessageDTO;
import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.service.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 💬 MessagingController
 *
 * This controller handles both WebSocket messaging and RESTful endpoints
 * for chat history retrieval and chat UI rendering in the Turing Online Forum System.
 *
 * 📌 Annotations Used:
 * - @RestController: Exposes REST endpoints that return JSON responses.
 * - @RequestMapping("/api/messages"): Base URL for all message-related endpoints.
 * - @Slf4j: Lombok annotation for logging.
 * - @RequiredArgsConstructor: Lombok annotation for constructor injection of final fields.
 *
 * 🧩 Features Configured:
 * - Handles real-time message sending via WebSocket.
 * - Fetches chat history between two users.
 * - Renders a Thymeleaf-based chat page.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessagingController {

    private final MessagingService messagingService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 📡 WebSocket: `/app/chat.send`
     *
     * Handles incoming WebSocket messages from clients.
     * Converts and forwards the message to a specific `/topic` destination for the receiver.
     *
     * @param chatMessage The message DTO containing sender, receiver, and content.
     *
     * 🧠 Example Flow:
     * - Client sends to: `/app/chat.send`
     * - Server relays to: `/topic/messages/{receiverId}`
     */
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDTO chatMessage) {
        log.info("WebSocket: Received message {}", chatMessage.getContent());

        Message message = messagingService.sendMessage(chatMessage);

        simpMessagingTemplate.convertAndSend("/topic/messages/" + chatMessage.getReceiverId(), message);
    }

    /**
     * 📜 GET `/api/messages/history`
     *
     * REST API to retrieve chat message history between two users.
     *
     * @param senderId   The ID of the sender.
     * @param receiverId The ID of the receiver.
     * @return List of chat messages exchanged between sender and receiver.
     */
    @GetMapping("/history")
    public List<Message> getHistory(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return messagingService.getChatHistory(senderId, receiverId);
    }

    /**
     * 🖥️ GET `/api/messages/chat`
     *
     * Renders the chat UI page using Thymeleaf.
     *
     * @param userId The ID of the current user (from query param).
     * @param model  Spring's `Model` to pass attributes to the view.
     * @return The `chat.html` template from the `templates/` directory.
     */
    @GetMapping("/chat")
    public String chatPage(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);  // Inject user ID into the frontend
        return "chat";  // Loads templates/chat.html
    }
}
```

## 👥 **6. FollowController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/FollowController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 👥 FollowController
 *
 * REST controller responsible for managing "follow" relationships between users
 * in the Turing Online Forum System.
 *
 * 📌 Annotations Used:
 * - @RestController: Marks this class as a REST controller for API responses.
 * - @RequestMapping("/api/follow"): Sets the base URL for all follow-related endpoints.
 * - @RequiredArgsConstructor: Lombok annotation to generate constructor for `final` fields.
 * - @Slf4j: Enables logging via `log` object.
 *
 * 🧩 Features Configured:
 * - Allows users to follow other users.
 * - Retrieves a list of users being followed by a specific user.
 */
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;

    /**
     * ➕ POST `/api/follow`
     *
     * Endpoint to follow another user.
     *
     * @param followerId  The ID of the user who is following.
     * @param followingId The ID of the user being followed.
     * @return A success message as a string response.
     *
     * 🧠 Usage:
     * POST request to `/api/follow?followerId=1&followingId=2` establishes a follow relationship.
     */
    @PostMapping
    public ResponseEntity<String> followUser(@RequestParam Long followerId, @RequestParam Long followingId) {
        log.info("User {} is attempting to follow User {}", followerId, followingId);
        followService.followUser(followerId, followingId);
        return ResponseEntity.ok("Followed successfully");
    }

    /**
     * 📄 GET `/api/follow/{userId}/following`
     *
     * Retrieves a list of users that the specified user is following.
     *
     * @param userId The ID of the user whose "following" list is to be fetched.
     * @return A list of `User` objects the user is following.
     *
     * 🧠 Usage:
     * GET request to `/api/follow/5/following` returns the list of users followed by user 5.
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
}
```

## 👥 **7. FollowController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/FollowController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 👥 FollowController
 *
 * REST controller responsible for managing "follow" relationships between users
 * in the Turing Online Forum System.
 *
 * 📌 Annotations Used:
 * - @RestController: Marks this class as a REST controller for API responses.
 * - @RequestMapping("/api/follow"): Sets the base URL for all follow-related endpoints.
 * - @RequiredArgsConstructor: Lombok annotation to generate constructor for `final` fields.
 * - @Slf4j: Enables logging via `log` object.
 *
 * 🧩 Features Configured:
 * - Allows users to follow other users.
 * - Retrieves a list of users being followed by a specific user.
 */
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;

    /**
     * ➕ POST `/api/follow`
     *
     * Endpoint to follow another user.
     *
     * @param followerId  The ID of the user who is following.
     * @param followingId The ID of the user being followed.
     * @return A success message as a string response.
     *
     * 🧠 Usage:
     * POST request to `/api/follow?followerId=1&followingId=2` establishes a follow relationship.
     */
    @PostMapping
    public ResponseEntity<String> followUser(@RequestParam Long followerId, @RequestParam Long followingId) {
        log.info("User {} is attempting to follow User {}", followerId, followingId);
        followService.followUser(followerId, followingId);
        return ResponseEntity.ok("Followed successfully");
    }

    /**
     * 📄 GET `/api/follow/{userId}/following`
     *
     * Retrieves a list of users that the specified user is following.
     *
     * @param userId The ID of the user whose "following" list is to be fetched.
     * @return A list of `User` objects the user is following.
     *
     * 🧠 Usage:
     * GET request to `/api/follow/5/following` returns the list of users followed by user 5.
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
}
```



## 🛡️ **8. ModerationController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/ModerationController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.dto.ModerationDTO;
import com.example.turingOnlineForumSystem.service.ModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 🛡️ ModerationController
 *
 * This controller provides REST endpoints for forum moderation actions such as
 * deleting threads or posts, banning users, and viewing moderation history.
 * It allows moderators to enforce rules and maintain community standards.
 *
 * 📌 Annotations Used:
 * - @RestController: Indicates this is a REST controller that returns JSON responses.
 * - @RequestMapping("/api/moderation"): Base path for all moderation-related endpoints.
 * - @RequiredArgsConstructor: Lombok annotation for auto-injecting final fields.
 * - @Slf4j: Enables logging using `log` object.
 *
 * 🧩 Features Configured:
 * - Delete threads and posts by moderators.
 * - Ban users for violating forum rules.
 * - Retrieve moderation action history per user.
 */
@RestController
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
@Slf4j
public class ModerationController {

    private final ModerationService moderationService;

    /**
     * 🧹 DELETE `/api/moderation/thread/{threadId}`
     *
     * Deletes a forum thread as a moderation action.
     *
     * @param threadId     The ID of the thread to be deleted.
     * @param moderatorId  The ID of the moderator performing the action.
     * @param reason       The reason for deleting the thread.
     */
    @DeleteMapping("/thread/{threadId}")
    public void deleteThread(@PathVariable Long threadId,
                             @RequestParam Long moderatorId,
                             @RequestParam String reason) {
        moderationService.deleteThread(threadId, moderatorId, reason);
    }

    /**
     * 🧼 DELETE `/api/moderation/post/{postId}`
     *
     * Deletes a specific post by a moderator.
     *
     * @param postId       The ID of the post to be deleted.
     * @param moderatorId  The ID of the moderator performing the action.
     * @param reason       The reason for deleting the post.
     */
    @DeleteMapping("/post/{postId}")
    public void deletePost(@PathVariable Long postId,
                           @RequestParam Long moderatorId,
                           @RequestParam String reason) {
        moderationService.deletePost(postId, moderatorId, reason);
    }

    /**
     * 🚫 POST `/api/moderation/ban-user/{userId}`
     *
     * Bans a user from posting in the forum.
     *
     * @param userId The ID of the user to be banned.
     * @param reason The reason for banning the user.
     */
    @PostMapping("/ban-user/{userId}")
    public void banUser(@PathVariable Long userId,
                        @RequestParam String reason) {
        moderationService.banUser(userId, reason);
    }

    /**
     * 📜 GET `/api/moderation/history/{userId}`
     *
     * Retrieves a list of all moderation actions performed on a given user.
     *
     * @param userId The ID of the user whose moderation history is requested.
     * @return A list of `ModerationDTO` entries representing each moderation action.
     */
    @GetMapping("/history/{userId}")
    public List<ModerationDTO> getModerationHistory(@PathVariable Long userId) {
        return moderationService.getModerationHistory(userId);
    }
}
```


## 📝 **9. PostController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/PostController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.dto.PostDto;
import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 📝 PostController
 *
 * REST controller for managing forum posts within a thread in the Turing Online Forum System.
 *
 * 📌 Annotations Used:
 * - @RestController: Indicates that this class handles RESTful API endpoints.
 * - @RequestMapping("/api/posts"): Base path for all post-related endpoints.
 * - @RequiredArgsConstructor: Lombok annotation to generate constructor for `final` dependencies.
 * - @Slf4j: Enables logging for monitoring and debugging.
 *
 * 🧩 Features Configured:
 * - Create a new post in a discussion thread.
 * - Fetch all posts for a specific thread.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    /**
     * ➕ POST `/api/posts/thread/{threadId}`
     *
     * Creates a new post in the specified thread.
     *
     * @param threadId ID of the thread in which the post is to be added.
     * @param post     The `Post` object containing post content and metadata.
     * @return The created `Post` object.
     *
     * 🧠 Usage:
     * POST request to `/api/posts/thread/3` with JSON body creates a new post in thread ID 3.
     */
    @PostMapping("/thread/{threadId}")
    public Post createPost(@PathVariable Long threadId, @RequestBody Post post) {
        log.info("Creating a new post in thread {}", threadId);
        return postService.createPost(post, threadId);
    }

    /**
     * 📄 GET `/api/posts/thread/{threadId}`
     *
     * Retrieves all posts for a specific thread.
     *
     * @param threadId ID of the thread whose posts are to be fetched.
     * @return A list of `PostDto` objects representing posts in the thread.
     *
     * 🧠 Usage:
     * GET `/api/posts/thread/3` returns all posts under thread ID 3.
     */
    @GetMapping("/thread/{threadId}")
    public List<PostDto> getPostsByThread(@PathVariable Long threadId) {
        log.info("Fetching posts for thread {}", threadId);
        return postService.getPostsByThread(threadId);
    }
}
```

## 🔔 **10. NotificationController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/NotificationController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 🔔 NotificationController
 *
 * REST controller that manages user notifications within the Turing Online Forum System.
 * Handles fetching, updating, and deleting notifications.
 *
 * 📌 Annotations Used:
 * - @RestController: Defines this class as a RESTful web controller.
 * - @RequestMapping("/api/notifications"): Maps requests under `/api/notifications`.
 * - @RequiredArgsConstructor: Lombok annotation to inject dependencies via constructor.
 * - @Slf4j: Enables SLF4J-based logging for traceability.
 *
 * 🧩 Features Configured:
 * - Fetch notifications for a given user.
 * - Mark a specific notification as read.
 * - Delete a notification by ID.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 📄 GET `/api/notifications/{userId}`
     *
     * Fetches all notifications for a specific user.
     *
     * @param userId ID of the user whose notifications are to be retrieved.
     * @return List of `Notification` objects.
     *
     * 🧠 Usage:
     * GET `/api/notifications/7` will return all notifications for user with ID 7.
     */
    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        log.info("Fetching notifications for user {}", userId);
        return notificationService.getNotificationsForUser(userId);
    }

    /**
     * ✅ PUT `/api/notifications/read/{notificationId}`
     *
     * Marks a specific notification as read.
     *
     * @param notificationId ID of the notification to be marked as read.
     *
     * 🧠 Usage:
     * PUT `/api/notifications/read/10` will update notification ID 10 to read status.
     */
    @PutMapping("/read/{notificationId}")
    public void markNotificationAsRead(@PathVariable Long notificationId) {
        log.info("Marking notification {} as read", notificationId);
        notificationService.markAsRead(notificationId);
    }

    /**
     * ❌ DELETE `/api/notifications/{notificationId}`
     *
     * Deletes a specific notification.
     *
     * @param notificationId ID of the notification to be deleted.
     *
     * 🧠 Usage:
     * DELETE `/api/notifications/15` will delete notification ID 15.
     */
    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        log.info("Deleting notification {}", notificationId);
        notificationService.deleteNotification(notificationId);
    }
}
```


## ⚙️ Features



Sure! Here's the full list of features for your **Spring Boot Online Forum System** in the requested format:

---

## ⚙️ Features

- **REST APIs** for user management, threads, posts, moderation, messaging, notifications, search, follow, and email
- **JWT Authentication** for secure login, registration, and role-based access
- **Docker** compose support
- **Real-time messaging** using **WebSocket (SockJS + STOMP)**
- **Thymeleaf-based chat UI** for private messaging between users
- **User moderation tools** – delete posts/threads, ban users with audit logs
- **Notification system** for new messages, replies, and admin actions
- **Search functionality** for users and threads (title/content)
- **User following system** to build a social connection
- **Email sending support** via configured SMTP server
- **DTO-based request/response models** for clean API contracts
- **Global exception handling** with custom messages and error logging
- **Role-based authorization** using `@PreAuthorize` and security filters
- **Threaded discussion support** with comments (posts) under each thread
- **Input validation** using `@Valid` and manual checks
- **Modular layered architecture (Controller, Service, Repository)**
- **Logging** throughout controllers and services using `@Slf4j`
- **Custom exception classes** like `ResourceNotFoundException`
- **Unit and Integration Tests** with MockMvc, Mockito, and JUnit
- **Dynamic WebSocket routing** for user-specific subscriptions
- **H2/Embedded DB** for testing and in-memory persistence
- **CORS support** and security configuration via `SecurityConfig`
- **Builder patterns and Lombok** for boilerplate reduction
- **Extensive logging**
- **Unit + Integration testing with 90%+ coverage**

---

## 🔨 How to Run

### 1. Clone and Navigate

```bash
git clone <your-repo-url>
cd online-forum-system
```

### 3. Configure `docker-compose.yaml`

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

### 4. Build and Run the App

```bash
./docker-compose up
./mvn clean install
./mvn spring-boot:run
```

---

## 🚀 API Endpoints

Here’s the list of **API endpoints** for your **Spring Boot Online Forum System** in the format you requested:


### 🔐 Authentication

| Method | Endpoint                 | Description                           |
|--------|--------------------------|---------------------------------------|
| POST   | `/api/auth/register`     | Register a new user                   |
| POST   | `/api/auth/login`        | Authenticate user and return JWT      |
| GET    | `/api/auth/profile`      | Get profile of logged-in user         |
| PUT    | `/api/auth/profile`      | Update profile of logged-in user      |

---

### 👤 Users

| Method | Endpoint           | Description                     |
|--------|--------------------|---------------------------------|
| GET    | `/api/users`       | Get all users                   |
| GET    | `/api/users/{id}`  | Get user by ID                  |
| POST   | `/api/users`       | Create a new user               |
| PUT    | `/api/users/{id}`  | Update user info                |

---

### 💬 Threads

| Method | Endpoint               | Description                         |
|--------|------------------------|-------------------------------------|
| GET    | `/api/threads`         | Get all threads                     |
| GET    | `/api/threads/{id}`    | Get a thread by ID                  |
| POST   | `/api/threads`         | Create a new thread                 |
| PUT    | `/api/threads/{id}`    | Update an existing thread           |
| DELETE | `/api/threads/{id}`    | Delete a thread                     |

---

### 🧵 Posts

| Method | Endpoint                         | Description                         |
|--------|----------------------------------|-------------------------------------|
| GET    | `/api/posts/thread/{threadId}`   | Get posts under a thread            |
| POST   | `/api/posts/thread/{threadId}`   | Create post under a thread          |

---

### 🔧 Moderation

| Method | Endpoint                          | Description                          |
|--------|-----------------------------------|--------------------------------------|
| DELETE | `/api/moderation/thread/{id}`     | Delete a thread (moderator only)     |
| DELETE | `/api/moderation/post/{id}`       | Delete a post (moderator only)       |
| POST   | `/api/moderation/ban-user/{id}`   | Ban a user from posting              |
| GET    | `/api/moderation/history/{id}`    | Get moderation history of a user     |

---

### 📥 Messaging

| Method | Endpoint                        | Description                              |
|--------|----------------------------------|------------------------------------------|
| GET    | `/api/messages/history`         | Get chat history between two users       |
| GET    | `/api/messages/chat`            | Render chat UI (Thymeleaf-based)         |
| WS     | `/chat`                         | WebSocket endpoint (SockJS)              |
| SEND   | `/app/chat.send`                | Send message via WebSocket               |
| SUB    | `/topic/messages/{userId}`      | Subscribe to receive messages (STOMP)    |

---

### 🔔 Notifications

| Method | Endpoint                              | Description                             |
|--------|---------------------------------------|-----------------------------------------|
| GET    | `/api/notifications/{userId}`         | Get notifications for a user            |
| PUT    | `/api/notifications/read/{id}`        | Mark notification as read               |
| DELETE | `/api/notifications/{id}`             | Delete a notification                   |

---

### 👥 Follow System

| Method | Endpoint                         | Description                          |
|--------|----------------------------------|--------------------------------------|
| POST   | `/api/follow`                    | Follow another user                  |
| GET    | `/api/follow/{id}/following`     | Get users followed by the user       |

---

### 🔎 Search

| Method | Endpoint                   | Description                          |
|--------|----------------------------|--------------------------------------|
| GET    | `/api/search/users`        | Search users by username             |
| GET    | `/api/search/threads`      | Search threads by title/content      |

---

### 📧 Email

| Method | Endpoint           | Description                |
|--------|--------------------|----------------------------|
| POST   | `/api/email/send`  | Send email to user         |

---


### Example Request


---

## 🧪  `curl` Commands for above API

> Replace `localhost:8080` with your running host.

### 🔹 1.Create a User
```bash
curl --location 'http://localhost:8080/api/users' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=65F781753C8BF95C3E32E9ED40FC1BB9' \
--data-raw '{
  "username": "alice",
  "password": "secret",
  "email": "alice@example.com",
  "role": "USER"
}'
```

### 🔹 1.1.View User Profile
```bash
curl --location 'http://localhost:8080/api/users/1' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 1.2.Update User Profile
```bash
curl --location --request PUT 'http://localhost:8080/api/users/1' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF' \
--data-raw '{
    "username": "newUsername",
    "email": "newemail@example.com"
}'
```

### 🔹 2. Create a Thread (Requires user ID — e.g. user with id: 1)
```bash
curl --location 'http://localhost:8080/api/threads' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF' \
--data '{
    "title": "Spring Boot Tips",
    "content": "Let'\''s discuss Spring Boot best practices.",
    "user": {
        "id": 1
    }
}'
```

### 🔹 3. Create a Post for that Thread (Assuming thread with id: 1 and user with id: 1
```bash
curl --location 'http://localhost:8080/api/posts/thread/1' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF' \
--data-raw '{
    "content": "I love using @Slf4j in services!",
    "user": {
        "id": 1
    }
}'
```

### 🔹 4. Get Posts of a Thread 
```bash
curl --location 'http://localhost:8080/api/posts/thread/1' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 5.Ban User bash not working
```bash
curl --location --request POST 'http://localhost:8080/api/moderation/ban-user/1?reason=Spamming' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 6.Delete Thread
```bash
curl --location --request DELETE 'http://localhost:8080/api/moderation/thread/1?moderatorId=99&reason=Duplicate%20Topic' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 6.View moderation history
```bash
curl --location 'http://localhost:8080/api/moderation/history/1' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 7.Get Message History
```bash
curl --location 'http://localhost:8080/api/messages/history?senderId=1&receiverId=2' \
--header 'Accept: application/json' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 8.Send Message via WebSocket (STOMP)
```bash
curl --location 'http://localhost:8080/api/messages/history?senderId=1&receiverId=2' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 9.Following Other Users
```bash
curl --location --request POST 'http://localhost:8080/api/follow?followerId=1&followingId=2' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 10.Get Following List of a User
```bash
curl --location 'http://localhost:8080/api/follow/1/following' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 11.Get Notifications for a User
```bash
curl --location 'http://localhost:8080/api/follow/1/following' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```
### 🔹 12. Search Users by Keyword 
```bash
curl --location 'http://localhost:8080/api/search/users?q=al' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```
### 🔹 13.Search Threads by Title or Content
```bash
curl --location 'http://localhost:8080/api/search/threads?q=message' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

---

## ✅ Unit & Integration Testing

Run tests:
```bash
./gradlew test
```

- Tested components:
    - Services (CRUD logic)
    - Controller (REST APIs + validations)
    - Exception handling
    - Utility methods
    - Rate limiting

---

## 📈 Performance Considerations

| Operation          | Time Complexity |
|-------------------|-----------------|
| Get / Set          | `O(1)`          |
| Invalidate (key)   | `O(1)`          |
| Clear all          | `O(n)`          |

Redis is inherently fast and supports thousands of operations per second.

---

## 📊 Code Coverage Reports

- ✅ 1st Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1IzGuO5mnlXnDNSw1JV6TfV1CYvv7ZyRb/view?usp=drive_link)
- ✅ 2nd Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1P4AxTTKwBcM12m9zRKK8MuYqmG7JZ8gy/view?usp=drive_link)

---

## 📦 Download Code

[👉 Click to Download the Project Source Code](https://drive.google.com/file/d/14AN3SJFyM61_6aBwRYmmNC4iQsjdeUC_/view?usp=drive_link)

---

## 🧠 Conclusion

This Spring Boot caching service provides:
- High-performance data retrieval
- Low database dependency
- Scalable modular design
- Reliable Redis integration
- Production-grade error handling & observability

> A great foundational microservice for caching in distributed applications.

---
```

Let me know if you'd like this saved as a downloadable `.md` file or want a version tailored for GitHub README style.
