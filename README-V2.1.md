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


## 🔍 **11. SearchController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/SearchController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 🔍 SearchController
 *
 * This REST controller handles search functionality for users and threads in the
 * Turing Online Forum System. It provides endpoints to perform case-insensitive
 * keyword-based searches.
 *
 * 📌 Annotations Used:
 * - @RestController: Declares this as a RESTful controller.
 * - @RequestMapping("/api/search"): Base path for all search-related endpoints.
 * - @Slf4j: Lombok annotation to enable logging with `log`.
 * - @RequiredArgsConstructor: Lombok annotation to generate constructor for final fields.
 *
 * 🧩 Features Configured:
 * - Search users by username.
 * - Search threads by title or content.
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final UserRepository userRepo;
    private final ThreadRepository threadRepo;

    /**
     * 🔎 GET `/api/search/users`
     *
     * Searches users by a keyword in their username (case-insensitive).
     *
     * @param q The search query string.
     * @return A list of users whose usernames contain the given keyword.
     *
     * 🧠 Example: `/api/search/users?q=alice`
     */
    @GetMapping("/users")
    public List<User> searchUsers(@RequestParam String q) {
        log.info("Searching users with keyword: {}", q);
        return userRepo.findByUsernameContainingIgnoreCase(q);
    }

    /**
     * 🧵 GET `/api/search/threads`
     *
     * Searches threads by keyword in either the title or content (case-insensitive).
     *
     * @param q The search query string.
     * @return A list of threads where title or content matches the keyword.
     *
     * 🧠 Example: `/api/search/threads?q=springboot`
     */
    @GetMapping("/threads")
    public List<Threads> searchThreads(@RequestParam String q) {
        log.info("Searching threads with keyword: {}", q);
        return threadRepo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(q, q);
    }
}
```

## 🧵 **12. ThreadController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/ThreadController.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.service.ThreadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 🧵 ThreadController
 *
 * This REST controller manages CRUD operations for forum threads
 * in the Turing Online Forum System.
 *
 * 📌 Annotations Used:
 * - @RestController: Indicates this is a REST controller returning JSON.
 * - @RequestMapping("/api/threads"): Sets the base URI path for thread endpoints.
 * - @Slf4j: Enables logging via Lombok.
 * - @RequiredArgsConstructor: Auto-generates constructor for final fields.
 *
 * 🧩 Features Configured:
 * - Create, retrieve, update, and delete threads.
 * - List all threads in the system.
 */
@RestController
@RequestMapping("/api/threads")
@RequiredArgsConstructor
@Slf4j
public class ThreadController {

    private final ThreadService threadsService;

    /**
     * ➕ POST `/api/threads`
     *
     * Creates a new thread with provided details.
     *
     * @param Threads The thread object to create.
     * @return The created thread with generated ID.
     */
    @PostMapping
    public Threads createThreads(@RequestBody Threads Threads) {
        log.debug("Request to create Threads: {}", Threads.getTitle());
        return threadsService.createThread(Threads);
    }

    /**
     * 📄 GET `/api/threads/{id}`
     *
     * Retrieves a thread by its ID.
     *
     * @param id The ID of the thread to retrieve.
     * @return The thread object if found.
     */
    @GetMapping("/{id}")
    public Threads getThreads(@PathVariable Long id) {
        return threadsService.getThread(id);
    }

    /**
     * 📚 GET `/api/threads`
     *
     * Retrieves all threads.
     *
     * @return A list of all thread objects.
     */
    @GetMapping
    public List<Threads> getAllThreadss() {
        return threadsService.getAllThreads();
    }

    /**
     * ✏️ PUT `/api/threads/{id}`
     *
     * Updates an existing thread by ID.
     *
     * @param id      The ID of the thread to update.
     * @param Threads The updated thread data.
     * @return The updated thread object.
     */
    @PutMapping("/{id}")
    public Threads updateThreads(@PathVariable Long id, @RequestBody Threads Threads) {
        return threadsService.updateThread(id, Threads);
    }

    /**
     * ❌ DELETE `/api/threads/{id}`
     *
     * Deletes a thread by ID.
     *
     * @param id The ID of the thread to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteThreads(@PathVariable Long id) {
        threadsService.deleteThread(id);
    }
}
```


## 👤 **13. UserController**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/controller/UserController.java`

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
 * 👤 UserController
 *
 * This REST controller provides endpoints for user management in the
 * Turing Online Forum System. It supports user creation, retrieval, and profile updates.
 *
 * 📌 Annotations Used:
 * - @RestController: Declares this class as a RESTful web controller.
 * - @RequestMapping("/api/users"): Base path for all user-related endpoints.
 * - @Slf4j: Lombok annotation to enable logging.
 * - @RequiredArgsConstructor: Generates constructor for final fields.
 *
 * 🧩 Features Configured:
 * - Create a new user with timestamp.
 * - Fetch all users or a specific user by ID.
 * - Update user profile information.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * ➕ POST `/api/users`
     *
     * Creates a new user in the system.
     *
     * @param user The user object to be created.
     * @return The saved user object with ID and timestamp.
     *
     * 🧠 Automatically sets the `createdAt` timestamp at creation.
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        User saved = userService.save(user);
        log.info("Created user with ID {}", saved.getId());
        return saved;
    }

    /**
     * 📄 GET `/api/users`
     *
     * Fetches a list of all users in the system.
     *
     * @return A list of user objects.
     */
    @GetMapping
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userService.findAll();
    }

    /**
     * 🔍 GET `/api/users/{id}`
     *
     * Retrieves a single user by their unique ID.
     *
     * @param id The ID of the user to fetch.
     * @return The user object, if found.
     *
     * @throws RuntimeException if the user is not found.
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
     * ✏️ PUT `/api/users/{id}`
     *
     * Updates an existing user's profile.
     *
     * @param id           The ID of the user to update.
     * @param updatedUser  The new user data.
     * @return The updated user wrapped in a ResponseEntity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("Updating profile for user ID {}", id);
        return ResponseEntity.ok(userService.updateUserProfile(id, updatedUser));
    }
}
```

## ❗ **14. GlobalExceptionHandler**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/exception/GlobalExceptionHandler.java`

```java
package com.example.turingOnlineForumSystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ❗ GlobalExceptionHandler
 *
 * A centralized exception handler for the Turing Online Forum System. It captures
 * application-wide exceptions and ensures meaningful and consistent responses are returned
 * to the client.
 *
 * 📌 Annotations Used:
 * - @RestControllerAdvice: A combination of @ControllerAdvice and @ResponseBody. Handles
 *   exceptions across the whole application and automatically serializes responses.
 * - @Slf4j: Lombok annotation for logging exceptions.
 *
 * 🧩 Features Configured:
 * - Handles resource-not-found scenarios with `ResourceNotFoundException`.
 * - Handles all other uncaught exceptions generically with logging.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 🛑 Handles `ResourceNotFoundException`
     *
     * Logs and returns a 404 response when a specific resource (like User, Thread, etc.)
     * is not found.
     *
     * @param ex The thrown ResourceNotFoundException.
     * @return HTTP 404 with error message as the response body.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * 💥 Handles all other unhandled exceptions
     *
     * Logs unexpected exceptions and returns a generic 500 Internal Server Error response.
     *
     * @param ex The thrown Exception.
     * @return HTTP 500 with a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("Unhandled error: ", ex);
        return ResponseEntity.status(500).body("Internal server error");
    }
}
```

## ❗ **14. ResourceNotFoundException**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/exception/ResourceNotFoundException.java`

```java
package com.example.turingOnlineForumSystem.exception;

/**
 * ❗ ResourceNotFoundException
 *
 * This is a custom runtime exception used throughout the Turing Online Forum System
 * to indicate that a requested resource (e.g., User, Thread, etc.) could not be found.
 *
 * ⚙️ Extends:
 * - RuntimeException: Makes this an unchecked exception, which can be thrown without requiring explicit try-catch blocks.
 *
 * 🔧 Usage:
 * - Commonly thrown in service or controller layers when a database query returns empty
 *   for a requested resource by ID, username, or other identifiers.
 *
 * 📌 Example:
 * ```java
 * userRepository.findById(id)
 *     .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
 * ```
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
Here’s the well-structured and professionally formatted documentation for your `EmailService` class, consistent with the style of the `SearchController` documentation:

---

## 📧 **15. EmailService**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/service/EmailService.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.model.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 📧 EmailService
 *
 * This service handles sending plain text emails using the Spring Boot
 * `JavaMailSender`. It encapsulates the logic required to send simple
 * email messages such as notifications, alerts, or general correspondence.
 *
 * 📌 Annotations Used:
 * - @Service: Indicates this class is a service component in the Spring context.
 * - @Autowired: Injects the configured JavaMailSender bean.
 *
 * 🧩 Features:
 * - Send plain text emails using recipient address, subject, and body.
 *
 * 📨 Dependencies:
 * - `JavaMailSender`: Spring’s interface for sending emails.
 * - `EmailRequest`: A custom model encapsulating email parameters (to, subject, body).
 */
@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    /**
     * ✉️ sendEmail()
     *
     * Sends an email using the provided `EmailRequest` data.
     *
     * @param emailRequest An object containing `to`, `subject`, and `body` fields.
     *
     * 🧠 Example:
     * ```json
     * {
     *   "to": "user@example.com",
     *   "subject": "Welcome to Turing Forum",
     *   "body": "Thanks for joining our platform!"
     * }
     * ```
     */
    public void sendEmail(EmailRequest emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getTo());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());
        mailSender.send(message);
    }
}
```


## 🤝 **16. FollowService**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/service/FollowService.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.model.Follow;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.FollowRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 🤝 FollowService
 *
 * This service class provides functionality related to the "follow" feature
 * within the Turing Online Forum System. It allows users to follow other users
 * and retrieve the list of users they are following.
 *
 * 📌 Annotations Used:
 * - @Service: Indicates that this class is a service component in the Spring context.
 * - @RequiredArgsConstructor: Lombok annotation to auto-generate constructor for final fields.
 * - @Slf4j: Lombok annotation to enable logging using the `log` object.
 *
 * 🧩 Core Responsibilities:
 * - Follow a user by storing a `Follow` relation.
 * - Fetch the list of users a given user is following.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepo;
    private final UserRepository userRepo;

    /**
     * ➕ followUser
     *
     * Creates a follow relationship between the follower and the following user.
     *
     * @param followerId  The ID of the user who wants to follow.
     * @param followingId The ID of the user to be followed.
     * @throws NoSuchElementException if either user does not exist.
     *
     * 🧠 Example Use: Called when a user clicks "Follow" on another user's profile.
     */
    public void followUser(Long followerId, Long followingId) {
        User follower = userRepo.findById(followerId).orElseThrow();
        User following = userRepo.findById(followingId).orElseThrow();

        Follow follow = new Follow(null, follower, following);
        followRepo.save(follow);

        log.info("User {} followed user {}", followerId, followingId);
    }

    /**
     * 📜 getFollowing
     *
     * Retrieves a list of users that the given user is following.
     *
     * @param userId The ID of the user whose following list is to be fetched.
     * @return A list of `User` entities that the user is following.
     *
     * 🧠 Example Use: Displayed on the "Following" tab of a user's profile.
     */
    public List<User> getFollowing(Long userId) {
        List<User> followingList = followRepo.findByFollowerId(userId).stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        log.info("Retrieved following list for user {}: {} users", userId, followingList.size());
        return followingList;
    }
}
```


## 💬 **17. MessagingService**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/service/MessagingService.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.dto.ChatMessageDTO;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.MessageRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 💬 MessagingService
 *
 * This service handles the core messaging logic, including sending messages,
 * fetching chat history, and notifying users when they receive new messages.
 *
 * 📌 Annotations Used:
 * - @Service: Marks this class as a Spring service bean to handle business logic.
 * - @Slf4j: Enables logging for the class using Lombok's logging utility.
 * - @RequiredArgsConstructor: Generates constructor-based dependency injection for final fields.
 *
 * 🧩 Features Configured:
 * - Sending and saving messages between users.
 * - Retrieving chat history between two users.
 * - Creating notifications for users when they receive new messages.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    /**
     * 📩 sendMessage
     *
     * This method persists a new message and sends a notification to the receiver.
     *
     * @param dto The data transfer object containing the message content and recipient information.
     * @return The saved message object.
     * 
     * 🧠 Steps:
     * - Fetches sender and receiver from the database using their IDs.
     * - Saves the message to the database.
     * - Sends a notification to the receiver about the new message.
     */
    public Message sendMessage(ChatMessageDTO dto) {
        log.info("Sending message from {} to {}", dto.getSenderId(), dto.getReceiverId());

        User sender = userRepo.findById(dto.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepo.findById(dto.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        Message message = Message.builder()
                .content(dto.getContent())
                .sender(sender)
                .receiver(receiver)
                .build();

        Message saved = messageRepo.save(message);
        log.info("Message saved: ID {}", saved.getId());

        // 🔔 Create Notification
        notificationService.sendNotification(receiver, "📩 New message from " + sender.getUsername());

        return saved;
    }

    /**
     * 📜 getChatHistory
     *
     * This method retrieves the chat history between two users.
     *
     * @param user1Id The ID of the first user.
     * @param user2Id The ID of the second user.
     * @return A list of message objects exchanged between the two users.
     *
     * 🧠 Steps:
     * - Fetches both users from the database by their IDs.
     * - Retrieves all messages sent between the two users.
     */
    public List<Message> getChatHistory(Long user1Id, Long user2Id) {
        log.info("Fetching chat history between {} and {}", user1Id, user2Id);
        User u1 = userRepo.findById(user1Id)
                .orElseThrow(() -> new ResourceNotFoundException("User 1 not found"));
        User u2 = userRepo.findById(user2Id)
                .orElseThrow(() -> new ResourceNotFoundException("User 2 not found"));
        return messageRepo.findBySenderAndReceiver(u1, u2);
    }
}
```

## ⚖️ **18. ModerationService**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/service/ModerationService.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.dto.ModerationDTO;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Moderation;
import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.ModerationRepository;
import com.example.turingOnlineForumSystem.repository.PostRepository;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ⚖️ ModerationService
 *
 * This service handles moderation operations, including deleting threads or posts,
 * banning users, and logging the moderation actions in the system.
 *
 * 📌 Annotations Used:
 * - @Service: Declares this class as a Spring service.
 * - @RequiredArgsConstructor: Lombok annotation to generate constructor for final fields.
 * - @Slf4j: Lombok annotation to enable logging with `log`.
 * - @Transactional: Ensures that database operations within this service are atomic.
 *
 * 🧩 Features Configured:
 * - Delete threads and posts with moderation logging.
 * - Ban users from posting and log the actions.
 * - Retrieve moderation history for users.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ModerationService {

    private final ModerationRepository moderationRepository;
    private final ThreadRepository threadsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 🧵 Delete a thread and log moderation action.
     *
     * Deletes a thread and all associated posts, while logging the moderation action.
     *
     * @param threadId The ID of the thread to be deleted.
     * @param moderatorId The ID of the moderator performing the action.
     * @param reason The reason for deleting the thread.
     */
    @Transactional
    public void deleteThread(Long threadId, Long moderatorId, String reason) {
        // Use proxy reference to avoid TransientObjectException
        Threads threadRef = threadsRepository.getReferenceById(threadId);

        // Fetch thread owner safely
        User threadOwner = userRepository.getReferenceById(threadRef.getUser().getId());

        // Save moderation log BEFORE deleting thread
        Moderation moderation = Moderation.builder()
                .action("DELETE_THREAD")
                .reason(reason)
                .user(threadOwner)
                .thread(threadRef)
                .createdAt(LocalDateTime.now())
                .build();

        moderationRepository.save(moderation);
        moderationRepository.flush(); // ensure it persists before delete

        // Delete posts
        List<Post> posts = postRepository.findByThreadId(threadId);
        postRepository.deleteAll(posts);

        // Delete the thread
        threadsRepository.deleteById(threadId);

        log.info("Deleted thread ID {} by moderator {}", threadId, moderatorId);
    }

    /**
     * 📝 Delete a post and log moderation action.
     *
     * Deletes a post and logs the action taken by the moderator.
     *
     * @param postId The ID of the post to be deleted.
     * @param moderatorId The ID of the moderator performing the action.
     * @param reason The reason for deleting the post.
     */
    public void deletePost(Long postId, Long moderatorId, String reason) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("Post with ID {} not found for moderation", postId);
            return new ResourceNotFoundException("Post not found");
        });

        postRepository.deleteById(postId);

        Moderation moderation = Moderation.builder()
                .action("DELETE_POST")
                .reason(reason)
                .user(post.getUser())
                .thread(post.getThread())
                .createdAt(LocalDateTime.now())
                .build();

        moderationRepository.save(moderation);
        log.info("Moderator {} deleted post ID {} with reason: {}", moderatorId, postId, reason);
    }

    /**
     * 🚫 Ban a user from posting and log moderation action.
     *
     * Bans a user from posting and logs the moderation action.
     *
     * @param userId The ID of the user to be banned.
     * @param reason The reason for banning the user.
     */
    public void banUser(Long userId, String reason) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID {} not found for banning", userId);
            return new ResourceNotFoundException("User not found");
        });

        user.setBanned(true);
        userRepository.save(user);

        Moderation moderation = Moderation.builder()
                .action("BAN_USER")
                .reason(reason)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        moderationRepository.save(moderation);
        log.info("User ID {} has been banned for reason: {}", userId, reason);
    }

    /**
     * 🏛️ Get moderation history for a user.
     *
     * Retrieves and returns the moderation history for a specific user.
     *
     * @param userId The ID of the user whose moderation history is to be fetched.
     * @return A list of moderation actions performed on the user.
     */
    public List<ModerationDTO> getModerationHistory(Long userId) {
        log.info("Fetching moderation history for user ID {}", userId);

        List<Moderation> moderationList = moderationRepository.findByUserId(userId);

        return moderationList.stream().map(m -> new ModerationDTO(
                m.getId(),
                m.getAction(),
                m.getReason(),
                m.getCreatedAt(),
                m.getUser().getId(),
                m.getUser().getUsername(),
                m.getThread() != null ? m.getThread().getId() : null
        )).collect(Collectors.toList());
    }

}
```



## 📣 **19. NotificationService**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/service/NotificationService.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.NotificationRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 📣 NotificationService
 *
 * This service handles operations related to notifications for users in the
 * Turing Online Forum System. It includes sending, marking as read, deleting, and fetching notifications.
 *
 * 📌 Annotations Used:
 * - @Service: Marks this class as a service bean for business logic handling.
 * - @Slf4j: Lombok annotation for logging within the class.
 * - @RequiredArgsConstructor: Lombok annotation for generating a constructor to inject final fields.
 *
 * 🧩 Features Configured:
 * - Send notifications to users.
 * - Mark notifications as read.
 * - Delete notifications.
 * - Fetch all notifications for a specific user.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * 📩 sendNotification
     *
     * Sends a notification to a user with a given message.
     *
     * @param recipientId The ID of the user receiving the notification.
     * @param message     The content of the notification message.
     *
     * 🧠 Creates a new `Notification` entity, saves it to the database, and logs the action.
     */
    public void sendNotification(Long recipientId, String message) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
        log.info("Notification sent to user {}: {}", recipientId, message);

        notificationRepository.save(notification);
        log.info("Notification sent to user {}: {}", recipientId, message);
    }

    /**
     * 📝 markAsRead
     *
     * Marks a notification as read by its ID.
     *
     * @param notificationId The ID of the notification to mark as read.
     *
     * 🧠 Updates the `isRead` flag of the notification to `true` and saves the change.
     */
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        notification.setIsRead(true);
        notificationRepository.save(notification);
        log.info("Notification ID {} marked as read", notificationId);
    }

    /**
     * ❌ deleteNotification
     *
     * Deletes a notification by its ID.
     *
     * @param notificationId The ID of the notification to delete.
     *
     * 🧠 Verifies existence of the notification, then deletes it from the database.
     */
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification not found");
        }
        notificationRepository.deleteById(notificationId);
        log.info("Notification ID {} deleted", notificationId);
    }

    /**
     * 📜 getNotificationsForUser
     *
     * Retrieves all notifications for a given user.
     *
     * @param userId The ID of the user whose notifications are to be fetched.
     * @return A list of notifications for the user.
     */
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByRecipientId(userId);
    }

    /**
     * 📩 sendNotification (Overloaded)
     *
     * Sends a notification to a user directly via a `User` object.
     *
     * @param recipient The user receiving the notification.
     * @param message   The content of the notification.
     *
     * 🧠 Saves the notification to the database and logs the action.
     */
    public void sendNotification(User recipient, String message) {
        Notification notification = Notification.builder()
                .message(message)
                .recipient(recipient)
                .isRead(false)
                .build();

        notificationRepo.save(notification);
        log.info("Sent notification to user {}: {}", recipient.getId(), message);
    }
}
```


## 👤 **19. UserService**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/service/UserService.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 👤 UserService
 *
 * This service class handles the core business logic for managing users,
 * including retrieving, updating, and saving user data.
 *
 * 📌 Annotations Used:
 * - @Service: Marks this class as a service bean for business logic.
 * - @Slf4j: Enables logging within the class using Lombok.
 * - @RequiredArgsConstructor: Automatically generates a constructor for final fields.
 *
 * 🧩 Features Configured:
 * - Fetching a user by ID.
 * - Updating user profiles.
 * - Saving and retrieving users from the repository.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepo;

    /**
     * 📄 getUserById
     *
     * Retrieves a user by their unique ID.
     *
     * @param id The ID of the user to fetch.
     * @return The user object if found.
     *
     * 🧠 Throws a `ResourceNotFoundException` if no user is found with the given ID.
     */
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    /**
     * ✏️ updateUserProfile
     *
     * Updates the profile details of an existing user.
     *
     * @param id           The ID of the user to update.
     * @param updatedUser  The user object containing updated profile details.
     * @return The updated user object after saving it to the repository.
     *
     * 🧠 Fetches the user by ID, updates their `username` and `email`, and saves the updated user.
     */
    public User updateUserProfile(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        return userRepo.save(existing);
    }

    /**
     * 🔍 findById
     *
     * Finds a user by their unique ID.
     *
     * @param id The ID of the user to find.
     * @return An `Optional<User>` containing the user if found, or empty if not.
     */
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    /**
     * 📚 findAll
     *
     * Retrieves all users in the system.
     *
     * @return A list of all users.
     */
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * ➕ save
     *
     * Saves a new user or updates an existing one.
     *
     * @param user The user object to save.
     * @return The saved user object.
     */
    public User save(User user) {
        return userRepo.save(user);
    }
}
```


## 🧵 **20. ThreadService**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/service/ThreadService.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.model.Threads;

/**
 * 🧵 ThreadService
 *
 * This service handles operations related to threads in the Turing Online Forum System.
 * It provides methods to create, update, delete, and retrieve threads.
 *
 * 📌 Annotations Used:
 * - @Service: Marks this class as a Spring service, indicating it contains business logic.
 * - @RequiredArgsConstructor: Lombok annotation that generates a constructor for required final fields.
 * - @Slf4j: Lombok annotation for logging support with `log`.
 *
 * 🧩 Features Configured:
 * - Create a new thread.
 * - Update an existing thread.
 * - Delete a thread.
 * - Retrieve a single thread by ID.
 * - Retrieve all threads.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ThreadService {

    private final ThreadRepository threadRepository;

    /**
     * ✨ Create a new thread.
     *
     * Creates and saves a new thread in the system with the current timestamp for creation and update.
     *
     * @param thread The thread object to be created.
     * @return The saved thread object with its ID and timestamps.
     */
    public Threads createThread(Threads thread) {
        thread.setCreatedAt(LocalDateTime.now());
        thread.setUpdatedAt(LocalDateTime.now());
        Threads saved = threadRepository.save(thread);
        log.info("Created thread with ID {}", saved.getId());
        return saved;
    }

    /**
     * ✏️ Update an existing thread.
     *
     * Updates the title and content of an existing thread and sets a new updated timestamp.
     *
     * @param id The ID of the thread to be updated.
     * @param updatedThread The updated thread object.
     * @return The updated thread object.
     * @throws ResourceNotFoundException If the thread with the specified ID is not found.
     */
    public Threads updateThread(Long id, Threads updatedThread) {
        return threadRepository.findById(id).map(thread -> {
            thread.setTitle(updatedThread.getTitle());
            thread.setContent(updatedThread.getContent());
            thread.setUpdatedAt(LocalDateTime.now());
            Threads saved = threadRepository.save(thread);
            log.info("Updated thread with ID {}", saved.getId());
            return saved;
        }).orElseThrow(() -> {
            log.error("Thread with ID {} not found", id);
            return new ResourceNotFoundException("Thread not found");
        });
    }

    /**
     * 🗑️ Delete a thread by its ID.
     *
     * Deletes a thread from the system. If the thread doesn't exist, it throws a `ResourceNotFoundException`.
     *
     * @param id The ID of the thread to be deleted.
     * @throws ResourceNotFoundException If the thread with the specified ID is not found.
     */
    public void deleteThread(Long id) {
        if (!threadRepository.existsById(id)) {
            log.error("Thread with ID {} not found for deletion", id);
            throw new ResourceNotFoundException("Thread not found");
        }
        threadRepository.deleteById(id);
        log.info("Deleted thread with ID {}", id);
    }

    /**
     * 🔍 Get a thread by its ID.
     *
     * Retrieves a thread by its ID. If the thread doesn't exist, it throws a `ResourceNotFoundException`.
     *
     * @param id The ID of the thread to be retrieved.
     * @return The thread object.
     * @throws ResourceNotFoundException If the thread with the specified ID is not found.
     */
    public Threads getThread(Long id) {
        return threadRepository.findById(id).orElseThrow(() -> {
            log.error("Thread with ID {} not found", id);
            return new ResourceNotFoundException("Thread not found");
        });
    }

    /**
     * 🔄 Get all threads in the system.
     *
     * Fetches and returns all threads stored in the system.
     *
     * @return A list of all threads.
     */
    public List<Threads> getAllThreads() {
        log.info("Fetching all threads");
        return threadRepository.findAll();
    }
}
```
Here's the structured documentation for your `EmailRequest` model, following the same professional format as the previous ones:

---

## 📧 **21. EmailRequest**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/model/EmailRequest.java`

```java
package com.example.turingOnlineForumSystem.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 📧 EmailRequest
 *
 * This class serves as a data transfer object (DTO) to represent an email
 * request, containing the recipient's email, subject, and body content.
 *
 * 📌 Annotations Used:
 * - @Getter: Lombok annotation to generate getter methods for all fields.
 * - @Setter: Lombok annotation to generate setter methods for all fields.
 *
 * 🧩 Features Configured:
 * - Stores email details: recipient (`to`), subject, and body.
 */
@Getter
@Setter
public class EmailRequest {
    private String to;       // The recipient's email address
    private String subject;  // The subject of the email
    private String body;     // The body/content of the email
}
```


## 🔄 **22. Follow**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/model/Follow.java`

```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * 🔄 Follow
 *
 * This entity represents a "follow" relationship between two users in the system:
 * one user following another.
 *
 * 📌 Annotations Used:
 * - @Entity: Marks this class as a JPA entity to be persisted in the database.
 * - @Table(name = "follow"): Specifies the name of the table in the database.
 * - @Data: Lombok annotation that generates getters, setters, toString, equals, and hashCode methods.
 * - @NoArgsConstructor: Lombok annotation to generate a no-argument constructor.
 * - @AllArgsConstructor: Lombok annotation to generate a constructor with all fields.
 * - @Builder: Lombok annotation to implement the builder pattern for object creation.
 *
 * 🧩 Features Configured:
 * - Represents the "follower" and "following" relationship between users.
 * - Cascades delete action to the follower entity when a follow relationship is deleted.
 */
@Entity
@Table(name = "follow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the follow relationship

    // @ManyToOne: Maps to the "following" user entity.
    @ManyToOne
    private User following;  // The user who is being followed

    // @ManyToOne and @JoinColumn: Maps to the "follower" user entity.
    @ManyToOne
    @JoinColumn(name = "follower_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // Hibernate-specific annotation to cascade delete
    private User follower;  // The user who is following
}
```



## 📣 **23. Notification**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/model/Notification.java`

```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 📣 Notification
 *
 * This entity represents a notification sent to a user. It stores the message content, 
 * the recipient user, the read status, and the timestamp when the notification was created.
 *
 * 📌 Annotations Used:
 * - @Entity: Marks this class as a JPA entity to be persisted in the database.
 * - @Data: Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods.
 * - @NoArgsConstructor: Lombok annotation to generate a no-argument constructor.
 * - @AllArgsConstructor: Lombok annotation to generate a constructor with all fields.
 * - @Builder: Lombok annotation to implement the builder pattern for object creation.
 * - @PrePersist: Specifies a method to run before persisting the entity, setting the timestamp.
 *
 * 🧩 Features Configured:
 * - Stores the notification's message content.
 * - Links each notification to a user (recipient).
 * - Tracks if the notification has been read.
 * - Automatically sets the timestamp before saving to the database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the notification

    private String message;  // The content/message of the notification

    // @ManyToOne: Maps to the recipient user who will receive the notification
    @ManyToOne
    private User recipient;  // The user who will receive the notification

    private Boolean isRead;  // The read status of the notification (true or false)

    private LocalDateTime timestamp;  // Timestamp when the notification was created

    /**
     * @PrePersist: Sets the timestamp field to the current time before persisting the entity.
     * Ensures the notification has a timestamp when created.
     */
    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();  // Automatically sets the timestamp to the current time
        }
    }
}
```


## 👤 **24. User**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/model/User.java`

```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 👤 User
 *
 * This entity represents a user in the Turing Online Forum System.
 * It stores essential user details like username, password, email, role, account creation time, 
 * and banned status.
 *
 * 📌 Annotations Used:
 * - @Entity: Marks this class as a JPA entity to be persisted in the database.
 * - @Table(name = "user"): Specifies the name of the table in the database.
 * - @Getter: Lombok annotation to automatically generate getter methods for all fields.
 * - @Setter: Lombok annotation to automatically generate setter methods for all fields.
 * - @Builder: Lombok annotation to implement the builder pattern for object creation.
 * - @AllArgsConstructor: Lombok annotation to generate a constructor with all fields.
 * - @NoArgsConstructor: Lombok annotation to generate a no-argument constructor.
 *
 * 🧩 Features Configured:
 * - Stores user information: `username`, `password`, `email`, `role`, `createdAt`, and `is_banned`.
 * - Automatically sets the `createdAt` field to the current time when the user is created.
 */
@Getter
@Setter
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the user

    private String username;  // The username of the user

    private String password;  // The user's password (likely hashed in a real system)

    private String email;  // The user's email address

    private String role;  // The role of the user (e.g., "USER", "ADMIN")

    private LocalDateTime createdAt = LocalDateTime.now();  // Timestamp for when the user account was created

    @Column(name = "is_banned")
    private Boolean banned = false;  // Whether the user is banned or not (default is false)

    // Getters and setters are generated by Lombok
}
```



## 📩 **25. Message**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/model/Message.java`

```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 📩 Message
 *
 * This entity represents a message in the Turing Online Forum System. It is used to store
 * the content of the message, the sender and receiver, and the timestamp when the message
 * was sent.
 *
 * 📌 Annotations Used:
 * - @Entity: Marks this class as a JPA entity that will be mapped to a table in the database.
 * - @Getter, @Setter: Lombok annotations that automatically generate getter and setter methods.
 * - @NoArgsConstructor, @AllArgsConstructor: Lombok annotations to generate constructors.
 * - @Builder: Lombok annotation to provide a builder pattern for constructing `Message` objects.
 * - @PrePersist: JPA annotation that allows the setting of values before persisting the entity.
 *
 * 🧩 Features Configured:
 * - Represents a message between two users with a sender and receiver.
 * - Stores the message content and the timestamp of when the message was created.
 * - Supports lazy fetching for sender and receiver relationships.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor   // ✅ Required for JPA
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    /**
     * 🕒 PrePersist method to automatically set the timestamp before persisting.
     *
     * This method is triggered before the entity is persisted to the database.
     * It sets the timestamp of the message to the current time.
     */
    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}
```


## ⚖️ **25. Moderation**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/model/Moderation.java`

```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * ⚖️ Moderation
 *
 * This entity represents a moderation action taken on a user or thread in the Turing Online Forum System.
 * It logs actions such as deleting a post, banning a user, or deleting a thread, along with the reason
 * and timestamp of when the action occurred.
 *
 * 📌 Annotations Used:
 * - @Entity: Marks this class as a JPA entity that will be mapped to a table in the database.
 * - @Getter, @Setter: Lombok annotations that automatically generate getter and setter methods.
 * - @NoArgsConstructor, @AllArgsConstructor: Lombok annotations for generating constructors.
 * - @Builder: Lombok annotation to provide a builder pattern for constructing `Moderation` objects.
 * - @ManyToOne: Indicates that the `Moderation` entity has many-to-one relationships with `User` and `Thread`.
 * - @OnDelete: Specifies how to handle deletions for related entities in Hibernate.
 * - @PrePersist: JPA annotation to allow automatic population of the `createdAt` field before persistence.
 *
 * 🧩 Features Configured:
 * - Logs actions related to users (e.g., banning or moderating posts).
 * - Allows nullable and delete-safe relationships with the `Threads` entity.
 * - Automatically sets the timestamp for the moderation action before persisting the entity.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moderation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Reference to the user who was moderated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Hibernate-specific
    private User user;

    // 💡 Allow nullable & delete-safe relationship to Threads
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "thread_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL) // ← key fix
    private Threads thread;

    /**
     * 🕒 PrePersist method to automatically set the createdAt field before persisting.
     *
     * This method ensures that the `createdAt` field is always populated with the current timestamp
     * when a new `Moderation` entity is created.
     */
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
```


## 📝 **26. Post**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/model/Post.java`

```java
package com.example.turingOnlineForumSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 📝 Post
 *
 * This entity represents a post in the Turing Online Forum System. A post is created by a user,
 * and it is associated with a specific thread. It contains the post's content, creation timestamp,
 * and references to the thread and the user who created the post.
 *
 * 📌 Annotations Used:
 * - @Entity: Marks this class as a JPA entity to be mapped to a table in the database.
 * - @Getter, @Setter: Lombok annotations to automatically generate getter and setter methods.
 * - @Builder: Lombok annotation to provide a builder pattern for constructing `Post` objects.
 * - @NoArgsConstructor, @AllArgsConstructor: Lombok annotations for generating constructors.
 * - @ManyToOne: Specifies that `Post` has a many-to-one relationship with both `Threads` and `User` entities.
 * - @JsonBackReference: Prevents infinite recursion in the JSON serialization process for bidirectional relationships.
 *
 * 🧩 Features Configured:
 * - Represents a post under a specific thread.
 * - Associates the post with a user who created it.
 * - Contains a `createdAt` timestamp that is set automatically when the post is created.
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    @JsonBackReference
    private Threads thread;

    @ManyToOne
    private User user;

    // Getters and setters generated by Lombok
}
```



## 🧵 **27. Threads**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/model/Threads.java`

```java
package com.example.turingOnlineForumSystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🧵 Threads
 *
 * This entity represents a thread in the Turing Online Forum System. A thread contains a title,
 * content, and is associated with a user who created it. It also has a list of posts that belong to
 * that thread. The thread is stored with timestamps for when it was created and last updated.
 *
 * 📌 Annotations Used:
 * - @Entity: Marks this class as a JPA entity to be mapped to a table in the database.
 * - @Table: Specifies the exact table name in the database schema.
 * - @Getter, @Setter: Lombok annotations to automatically generate getter and setter methods.
 * - @Builder: Lombok annotation for the builder pattern to easily create `Thread` objects.
 * - @NoArgsConstructor, @AllArgsConstructor: Lombok annotations to generate constructors.
 * - @ManyToOne: Specifies that a thread is related to one user (creator of the thread).
 * - @OneToMany: Indicates that a thread has many posts, and these posts are automatically deleted if the thread is deleted.
 * - @JsonManagedReference: Used for bidirectional relationships to avoid infinite recursion during JSON serialization.
 *
 * 🧩 Features Configured:
 * - Represents a thread with a title and content.
 * - Associates the thread with the user who created it.
 * - Contains timestamps for creation and updates.
 * - Contains a list of posts associated with the thread, with automatic cascading on delete.
 */
@Getter
@Setter
@Entity
@Table(name = "thread")  // <-- match the table name EXACTLY as in your DB schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Threads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @ManyToOne
    private User user;

    /**
     * 💬 One-to-many relationship with `Post` entity.
     *
     * Represents a list of posts that belong to this thread. The cascade option ensures that posts
     * are deleted when the thread is deleted, and orphanRemoval ensures that posts are removed from
     * the database when they are disassociated from the thread.
     */
    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Post> posts;
}
```


## 🔄 **28. FollowRepository**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/repository/FollowRepository.java`

```java
package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 🔄 FollowRepository
 *
 * This interface defines methods for interacting with the "follow" relationship data in the database.
 * It extends `JpaRepository` to provide CRUD operations and custom queries for managing follow relationships.
 *
 * 📌 Annotations Used:
 * - @Repository: Marks this interface as a Spring Data repository for DAO operations.
 * - Extends `JpaRepository<Follow, Long>`: Provides built-in methods for CRUD operations on the `Follow` entity.
 *
 * 🧩 Features Configured:
 * - Retrieves a list of `Follow` records by the follower's ID.
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 🔍 findByFollowerId
     *
     * Finds all follow relationships where the given user is the follower.
     *
     * @param followerId The ID of the follower.
     * @return A list of `Follow` entities where the given user is the follower.
     */
    List<Follow> findByFollowerId(Long followerId);
}
```

---

## 💬 **29. MessageRepository**  
📁 **Path:** `src/main/java/com/example/turingOnlineForumSystem/repository/MessageRepository.java`

```java
package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 💬 MessageRepository
 *
 * This interface provides methods for interacting with the `Message` entity in the database.
 * It extends `JpaRepository` to handle CRUD operations and custom queries for message data.
 *
 * 📌 Annotations Used:
 * - @Repository: Marks this interface as a Spring Data repository for DAO operations.
 * - Extends `JpaRepository<Message, Long>`: Provides built-in methods for CRUD operations on the `Message` entity.
 *
 * 🧩 Features Configured:
 * - Retrieves a list of messages sent from one user to another.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * 🔍 findBySenderAndReceiver
     *
     * Retrieves all messages exchanged between two users.
     *
     * @param sender The sender user.
     * @param receiver The receiver user.
     * @return A list of `Message` entities exchanged between the sender and receiver.
     */
    List<Message> findBySenderAndReceiver(User sender, User receiver);
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
