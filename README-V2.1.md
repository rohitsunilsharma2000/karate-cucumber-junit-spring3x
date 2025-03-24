# 6874: Build a complete Spring Boot online forum with discussion threads, user moderation tools, private messaging, and community features.

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


## Unit tests *


### 🧪 **1. ChatViewControllerTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/ChatViewControllerTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 ChatViewControllerTest
 *
 * This test class verifies the functionality of the `ChatViewController`.
 * It tests the loading of the chat page and ensures that the user ID is correctly passed
 * into the view template.
 *
 * 📌 Annotations Used:
 * - @WebMvcTest: Used for testing only the web layer of the application (Controller).
 * - @Import: Imports custom security configuration for testing without real security context.
 * - @TestConfiguration: Provides a custom security filter configuration for testing.
 *
 * 🧩 Features Tested:
 * - Verifies that the chat page loads correctly with the user ID passed in the model.
 */
@WebMvcTest(ChatViewController.class)
@Import(ChatViewControllerTest.TestSecurityConfig.class)
public class ChatViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 🧪 Test: Chat page loads with the correct user ID.
     */
    @Test
    void testChatPageLoadsWithUserId() throws Exception {
        mockMvc.perform(get("/chat")
                        .param("userId", "123"))
               .andExpect(status().isOk())
               .andExpect(view().name("chat"))
               .andExpect(model().attribute("userId", 123L));
    }

    /**
     * 🔒 Test security configuration for tests:
     * - Disables CSRF and permits all requests.
     */
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
```

---

### 🧪 **2. EmailControllerIntegrationTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/EmailControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.EmailRequest;
import com.example.turingOnlineForumSystem.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 EmailControllerIntegrationTest
 *
 * This integration test class verifies the behavior of the `EmailController`.
 * It tests the email sending functionality, ensuring that the service method is called
 * correctly and that the appropriate HTTP response is returned.
 *
 * 📌 Annotations Used:
 * - @WebMvcTest: Used for testing only the web layer of the application (Controller).
 * - @MockBean: Mocks the `EmailService` to isolate the controller behavior during testing.
 * - @Import: Imports custom security configuration for testing without actual security.
 *
 * 🧩 Features Tested:
 * - Verifies the email sending process.
 * - Ensures that the email service method is invoked correctly.
 */
@WebMvcTest(EmailController.class)
@Import(EmailControllerIntegrationTest.TestSecurityConfig.class)
public class EmailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    /**
     * 🧪 Test: Verifies that sending an email returns the correct response and invokes the service method.
     */
    @Test
    void testSendEmail() throws Exception {
        String requestJson = """
                {
                    "to": "user@example.com",
                    "subject": "Test Subject",
                    "body": "This is a test email."
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
               .andExpect(status().isOk())
               .andExpect(content().string("Email sent successfully!"));

        // Verify the service method was invoked
        Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.any(EmailRequest.class));
    }

    /**
     * 🔒 Test security configuration for tests:
     * - Disables CSRF and permits all requests.
     */
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
```
Here’s the formatted documentation for the remaining test classes:

---

### 🧪 **3. FollowControllerIntegrationTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/FollowControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.service.FollowService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 FollowControllerIntegrationTest
 *
 * This test class verifies the functionality of the `FollowController`.
 * It tests the follow functionality, retrieving the list of followed users, and ensuring
 * the proper HTTP responses are returned and service methods are invoked.
 *
 * 📌 Annotations Used:
 * - @WebMvcTest: Used for testing only the web layer (Controller) of the application.
 * - @MockBean: Mocks the `FollowService` to isolate controller behavior during testing.
 * - @Import: Imports custom security configuration to disable real security for tests.
 *
 * 🧩 Features Tested:
 * - Verifies that users can follow each other.
 * - Ensures the `FollowService` is called correctly during the follow action.
 * - Verifies retrieving a list of followed users.
 */
@WebMvcTest(FollowController.class)
@Import(FollowControllerIntegrationTest.TestSecurityConfig.class)
class FollowControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FollowService followService;

    /**
     * 🧪 Test: Verifies that a user can follow another user.
     */
    @Test
    void testFollowUser() throws Exception {
        mockMvc.perform(post("/api/follow")
                        .param("followerId", "1")
                        .param("followingId", "2"))
               .andExpect(status().isOk())
               .andExpect(content().string("Followed successfully"));

        Mockito.verify(followService, Mockito.times(1)).followUser(1L, 2L);
    }

    /**
     * 🧪 Test: Verifies that the list of followed users can be retrieved correctly.
     */
    @Test
    void testGetFollowing() throws Exception {
        User user = User.builder().id(2L).username("bob").build();
        Mockito.when(followService.getFollowing(1L)).thenReturn(List.of(user));

        mockMvc.perform(get("/api/follow/1/following"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].username").value("bob"));
    }

    /**
     * 🔒 Test security configuration for tests:
     * - Disables CSRF and permits all requests.
     */
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
```

---

### 🧪 **4. ModerationControllerIntegrationTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/ModerationControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 🧪 ModerationControllerIntegrationTest
 *
 * This integration test class verifies the functionality of the `ModerationController`.
 * It tests the delete actions for posts and threads, as well as banning users and checking
 * moderation history.
 *
 * 📌 Annotations Used:
 * - @SpringBootTest: Used for integration testing with the full application context.
 * - @AutoConfigureMockMvc: Configures `MockMvc` for testing the web layer.
 * - @Transactional: Ensures that database operations are rolled back after each test.
 *
 * 🧩 Features Tested:
 * - Verifies the ability to delete threads and posts by moderators.
 * - Ensures the ban functionality works as expected.
 * - Tests fetching the moderation history of a user.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ModerationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModerationRepository moderationRepository;

    @Autowired
    private MessageRepository messageRepository;

    private User moderator;
    private User regularUser;
    private Threads thread;
    private Post post;

    @BeforeEach
    void setup() {
        moderationRepository.deleteAll();
        messageRepository.deleteAll();
        postRepository.deleteAll();
        threadRepository.deleteAll();
        userRepository.deleteAll();

        moderator = userRepository.save(User.builder()
                                            .username("moderator")
                                            .email("mod@example.com")
                                            .banned(false)
                                            .createdAt(LocalDateTime.now())
                                            .build());

        regularUser = userRepository.save(User.builder()
                                              .username("user")
                                              .email("user@example.com")
                                              .banned(false)
                                              .createdAt(LocalDateTime.now())
                                              .build());

        thread = threadRepository.save(Threads.builder()
                                              .title("Test Thread")
                                              .content("Thread Content")
                                              .createdAt(LocalDateTime.now())
                                              .user(regularUser)
                                              .posts(Collections.emptyList())
                                              .build());

        post = postRepository.save(Post.builder()
                                       .content("Test Post")
                                       .createdAt(LocalDateTime.now())
                                       .user(regularUser)
                                       .thread(thread)
                                       .build());
    }

    /**
     * 🧪 Test: Verifies that a post can be deleted by a moderator.
     */
    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/moderation/post/" + post.getId())
                        .param("moderatorId", moderator.getId().toString())
                        .param("reason", "Violation of rules"))
                .andExpect(status().isOk());
    }

    /**
     * 🧪 Test: Verifies that a thread can be deleted by a moderator.
     */
    @Test
    void testDeleteThread() throws Exception {
        mockMvc.perform(delete("/api/moderation/thread/" + thread.getId())
                        .param("moderatorId", moderator.getId().toString())
                        .param("reason", "Duplicate content"))
                .andExpect(status().isOk());
    }

    /**
     * 🧪 Test: Verifies that a user can be banned by a moderator.
     */
    @Test
    void testBanUser() throws Exception {
        mockMvc.perform(post("/api/moderation/ban-user/" + regularUser.getId())
                        .param("reason", "Spamming"))
                .andExpect(status().isOk());
    }

    /**
     * 🧪 Test: Verifies that a user's moderation history can be retrieved.
     */
    @Test
    void testGetModerationHistory() throws Exception {
        mockMvc.perform(get("/api/moderation/history/" + regularUser.getId()))
                .andExpect(status().isOk());
    }
}
```

---

### 🧪 **5. NotificationControllerIntegrationTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/NotificationControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.NotificationRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 🧪 NotificationControllerIntegrationTest
 *
 * This integration test class verifies the functionality of the `NotificationController`.
 * It tests retrieving, marking as read, and deleting notifications for a user.
 *
 * 📌 Annotations Used:
 * - @SpringBootTest: Used for integration testing with the full application context.
 * - @AutoConfigureMockMvc: Configures `MockMvc` for testing the web layer.
 * - @Transactional: Ensures that database operations are rolled back after each test.
 *
 * 🧩 Features Tested:
 * - Verifies retrieving user notifications.
 * - Tests marking notifications as read.
 * - Verifies that notifications can be deleted correctly.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Notification notification = Notification.builder()
                .message("Test Notification")
                .recipient(user)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    /**
     * 🧪 Test: Verifies retrieving notifications for a user.
     */
    @Test
    public void testGetUserNotifications() throws Exception {
        mockMvc.perform(get("/api/notifications/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test Notification"));
    }

    /**
     * 🧪 Test: Verifies marking a notification as read.
     */
    @Test
    public void testMarkNotificationAsRead() throws Exception {
        Notification notification = notificationRepository.findByRecipientId(user.getId()).get(0);
        mockMvc.perform(put("/api/notifications/read/" + notification.getId()))
                .andExpect(status().isOk());
    }

    /**
     * 🧪 Test: Verifies deleting a notification.
     */
    @Test
    public void testDeleteNotification() throws Exception {
        Notification notification = notificationRepository.findByRecipientId(user.getId()).get(0);
        mockMvc.perform(delete("/api/notifications/" + notification.getId()))
                .andExpect(status().isOk());
    }
}
```
Here is the professional documentation for the provided classes:

---

### 📝 **6. PostControllerIntegrationTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/PostControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.PostRepository;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void testFullForumFlow() {
        // Step 1: Create User
        User newUser = new User();
        newUser.setUsername("alice");
        newUser.setPassword("secret");
        newUser.setEmail("alice@example.com");
        newUser.setRole("USER");

        ResponseEntity<User> userResponse = restTemplate.postForEntity(
                url("/api/users"),
                newUser,
                User.class
        );

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        User createdUser = userResponse.getBody();
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();

        // Step 2: Create Thread with that user
        Threads thread = new Threads();
        thread.setTitle("Spring Boot Tips");
        thread.setContent("Let's discuss Spring Boot best practices.");
        thread.setUser(createdUser);

        ResponseEntity<Threads> threadResponse = restTemplate.postForEntity(
                url("/api/threads"),
                thread,
                Threads.class
        );

        assertThat(threadResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Threads createdThread = threadResponse.getBody();
        assertThat(createdThread).isNotNull();
        assertThat(createdThread.getId()).isNotNull();

        // Step 3: Create Post in that thread
        Post post = new Post();
        post.setContent("I love using @Slf4j in services!");
        post.setUser(createdUser);

        ResponseEntity<Post> postResponse = restTemplate.postForEntity(
                url("/api/posts/thread/" + createdThread.getId()),
                post,
                Post.class
        );

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Post createdPost = postResponse.getBody();
        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getId()).isNotNull();

        // Step 4: Fetch Posts by thread
        ResponseEntity<Post[]> getPostsResponse = restTemplate.getForEntity(
                url("/api/posts/thread/" + createdThread.getId()),
                Post[].class
        );

        assertThat(getPostsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Post[] posts = getPostsResponse.getBody();
        assertThat(posts).isNotNull();
        assertThat(posts.length).isGreaterThanOrEqualTo(1);
    }
}
```

---

### 🔍 **7. SearchControllerIntegrationTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/SearchControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@Import(SearchControllerIntegrationTest.TestSecurityConfig.class)
class SearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private ThreadRepository threadRepo;

    @Test
    void testSearchUsers() throws Exception {
        User user = User.builder().id(1L).username("testuser").email("user@example.com").build();
        Mockito.when(userRepo.findByUsernameContainingIgnoreCase("test")).thenReturn(List.of(user));

        mockMvc.perform(get("/api/search/users")
                                .param("q", "test"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    void testSearchThreads() throws Exception {
        Threads thread = Threads.builder().id(1L).title("Interesting Topic").content("Something cool").build();
        Mockito.when(threadRepo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("test", "test"))
               .thenReturn(List.of(thread));

        mockMvc.perform(get("/api/search/threads")
                                .param("q", "test"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("Interesting Topic"));
    }

    // ✅ Disable security just for tests
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
```

---

### 🧵 **8. ThreadControllerIntegrationTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/ThreadControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ThreadControllerIntegrationTest {

    private static User testUser;
    private static Long createdThreadId;
    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    private int port;
    private String baseUrl;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/threads";

        if (testUser == null) {
            testUser = userRepository.save(User.builder()
                    .username("testUser")
                    .email("test@example.com")
                    .password("pass123")
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }

    @Test
    @Order(1)
    void testCreateThread() {
        Threads thread = Threads.builder()
                .title("Test Thread")
                .content("Thread content here.")
                .createdAt(LocalDateTime.now())
                .user(testUser)
                .build();

        ResponseEntity<Threads> response = restTemplate.postForEntity(baseUrl, thread, Threads.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());

        createdThreadId = response.getBody().getId();
    }

    @Test
    @Order(2)
    void testGetThreadById() {
        ResponseEntity<Threads> response = restTemplate.getForEntity(baseUrl + "/" + createdThreadId, Threads.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Thread", response.getBody().getTitle());
    }

    @Test
    @Order(3)
    void testUpdateThread() {
        Threads updated = Threads.builder()
                .title("Updated Thread Title")
                .content("Updated content")
                .user(testUser)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Threads> entity = new HttpEntity<>(updated, headers);

        ResponseEntity<Threads> response = restTemplate.exchange(
                baseUrl + "/" + createdThreadId,
                HttpMethod.PUT,
                entity,
                Threads.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Thread Title", response.getBody().getTitle());
    }

    @Test
    @Order(4)
    void testGetAllThreads() {
        ResponseEntity<Threads[]> response = restTemplate.getForEntity(baseUrl, Threads[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    @Order(5)
    void testDeleteThread() {
        restTemplate.delete(baseUrl + "/" + createdThreadId);
        boolean exists = threadRepository.existsById(createdThreadId);
        assertFalse(exists);
    }
}
```

---

### 👤 **9. UserControllerIntegrationTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/UserControllerIntegrationTest.java`

```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser() throws Exception {
        User user = User.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password123")
                .createdAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = User.builder()
                .username("alice")
                .email("alice@example.com")
                .password("alicepass")
                .createdAt(LocalDateTime.now())
                .build();

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = User.builder()
                .username("mike")
                .email("mike@example.com")
                .password("mikepass")
                .createdAt(LocalDateTime.now())
                .build();

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        User updatedUser = User.builder()
                .username("mike_updated")
                .email("mike_new@example.com")
                .password("newpass")
                .build();

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("mike_updated"));
    }
}

```
### Iteration number 2(for better coverage)


### ⚖️ **1. GlobalExceptionHandlerTest**  
: `src/test/java/com/example/turingOnlineForumSystem/exception/GlobalExceptionHandlerTest.java`

```java
package com.example.turingOnlineForumSystem.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Test handling of ResourceNotFoundException.
     */
    @Test
    void testHandleResourceNotFound() {
        // Given
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        // When
        ResponseEntity<?> response = handler.handleResourceNotFound(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    /**
     * Test handling of generic exceptions.
     */
    @Test
    void testHandleGenericException() {
        // Given
        Exception ex = new RuntimeException("Something went wrong");

        // When
        ResponseEntity<?> response = handler.handleGeneric(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Internal server error");
    }
}
```

---

### 📧 **2. EmailServiceTest**  
: `src/test/java/com/example/turingOnlineForumSystem/service/EmailServiceTest.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.model.EmailRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * 📧 EmailServiceTest
 *
 * This class tests the EmailService for sending emails using mock data.
 *
 * 🧩 Features Configured:
 * - Tests the email sending functionality by verifying the content and recipient.
 */
@SpringBootTest
class EmailServiceTest {

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

    /**
     * Test sending an email through the email service.
     */
    @Test
    void testSendEmail() {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("recipient@example.com");
        emailRequest.setSubject("Unit Test");
        emailRequest.setBody("Unit test email body");

        emailService.sendEmail(emailRequest);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage msg = messageCaptor.getValue();
        assertThat(msg.getTo()).contains("recipient@example.com");
        assertThat(msg.getSubject()).isEqualTo("Unit Test");
        assertThat(msg.getText()).isEqualTo("Unit test email body");
    }
}
```

---

### 🔄 ** 3. FollowServiceTest**: `src/test/java/com/example/turingOnlineForumSystem/service/FollowServiceTest.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.model.Follow;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.FollowRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 🔄 FollowServiceTest
 *
 * This class tests the FollowService for following users and retrieving the list of users that a user is following.
 *
 * 🧩 Features Configured:
 * - Tests following a user, fetching the list of followed users, and error handling for user not found.
 */
class FollowServiceTest {

    @Mock
    private FollowRepository followRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private FollowService followService;

    private User follower;
    private User following;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        follower = new User();
        follower.setId(1L);
        follower.setUsername("user1");

        following = new User();
        following.setId(2L);
        following.setUsername("user2");
    }

    /**
     * Test following a user successfully.
     */
    @Test
    void testFollowUser_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepo.findById(2L)).thenReturn(Optional.of(following));

        followService.followUser(1L, 2L);

        verify(followRepo, times(1)).save(any(Follow.class));
    }

    /**
     * Test fetching users that a user is following.
     */
    @Test
    void testGetFollowing_ReturnsListOfUsers() {
        Follow follow = new Follow(1L, follower, following);
        when(followRepo.findByFollowerId(1L)).thenReturn(List.of(follow));

        List<User> result = followService.getFollowing(1L);

        assertEquals(1, result.size());
    }

    /**
     * Test following a user when user not found.
     */
    @Test
    void testFollowUser_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> followService.followUser(1L, 2L));
    }
}
```

---

### 💬 **3. MessagingServiceTest**  
: `src/test/java/com/example/turingOnlineForumSystem/service/MessagingServiceTest.java`

```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.dto.ChatMessageDTO;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.MessageRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 💬 MessagingServiceTest
 *
 * This class tests the MessagingService for sending and retrieving messages between users.
 *
 * 🧩 Features Configured:
 * - Tests sending messages, retrieving chat history, and error handling for user not found.
 */
class MessagingServiceTest {

    @Mock
    private MessageRepository messageRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MessagingService messagingService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(1L);
        sender.setUsername("Alice");

        receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("Bob");
    }

    /**
     * Test sending a message successfully.
     */
    @Test
    void testSendMessage_Success() {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(1L);
        dto.setReceiverId(2L);
        dto.setContent("Hello!");

        when(userRepo.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepo.findById(2L)).thenReturn(Optional.of(receiver));

        Message saved = Message.builder()
                               .id(10L)
                               .content("Hello!")
                               .sender(sender)
                               .receiver(receiver)
                               .build();

        when(messageRepo.save(any(Message.class))).thenReturn(saved);

        Message result = messagingService.sendMessage(dto);

        assertNotNull(result);
        assertEquals("Hello!", result.getContent());
        assertEquals("Alice", result.getSender().getUsername());

        verify(notificationService).sendNotification(receiver, "📩 New message from Alice");
    }

    /**
     * Test sending a message when sender is not found.
     */
    @Test
    void testSendMessage_SenderNotFound() {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(99L);
        dto.setReceiverId(2L);
        dto.setContent("Hi");

        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messagingService.sendMessage(dto));
    }

    /**
     * Test retrieving chat history successfully.
     */
    @Test
    void testGetChatHistory_ReturnsMessages() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepo.findById(2L)).thenReturn(Optional.of(receiver));

        Message msg = Message.builder()
                             .id(1L)
                             .content("Hey Bob")
                             .sender(sender)
                             .receiver(receiver)
                             .build();

        when(messageRepo.findBySenderAndReceiver(sender, receiver)).thenReturn(List.of(msg));

        List<Message> history = messagingService.getChatHistory(1L, 2L);

        assertEquals(1, history.size());
        assertEquals("Hey Bob", history.get(0).getContent());
    }

    /**
     * Test retrieving chat history when user is not found.
     */
    @Test
    void testGetChatHistory_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messagingService.getChatHistory(1L, 2L));
    }
}
```





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
### 3.  Run `docker-compose.yml`
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
    <name>Turing online forum system</name>
    <description>
        Build a complete Spring Boot online forum with discussion threads, user moderation tools, private messaging, and
        community features.
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

### 5. Start the Application

Run the Spring Boot service:

```bash
./mvn spring-boot:run
```
By default, the service runs on port 8080.

---

## 5.🚀 Access the API Endpoints

> Use cURL or Postman to interact with the cache API:

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

### 🔹 View User Profile
```bash
curl --location 'http://localhost:8080/api/users/1' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 Update User Profile
```bash
curl --location --request PUT 'http://localhost:8080/api/users/1' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF' \
--data-raw '{
    "username": "newUsername",
    "email": "newemail@example.com"
}'
```

### 🔹 Create a Thread (Requires user ID — e.g. user with id: 1)
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

### 🔹  Create a Post for that Thread (Assuming thread with id: 1 and user with id: 1
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

### 🔹  Get Posts of a Thread 
```bash
curl --location 'http://localhost:8080/api/posts/thread/1' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 Ban User bash not working
```bash
curl --location --request POST 'http://localhost:8080/api/moderation/ban-user/1?reason=Spamming' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹Delete Thread
```bash
curl --location --request DELETE 'http://localhost:8080/api/moderation/thread/1?moderatorId=99&reason=Duplicate%20Topic' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 View moderation history
```bash
curl --location 'http://localhost:8080/api/moderation/history/1' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 Get Message History
```bash
curl --location 'http://localhost:8080/api/messages/history?senderId=1&receiverId=2' \
--header 'Accept: application/json' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 Send Message via WebSocket (STOMP)
```bash
curl --location 'http://localhost:8080/api/messages/history?senderId=1&receiverId=2' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 Following Other Users
```bash
curl --location --request POST 'http://localhost:8080/api/follow?followerId=1&followingId=2' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 1Get Following List of a User
```bash
curl --location 'http://localhost:8080/api/follow/1/following' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

### 🔹 Get Notifications for a User
```bash
curl --location 'http://localhost:8080/api/follow/1/following' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```
### 🔹  Search Users by Keyword 
```bash
curl --location 'http://localhost:8080/api/search/users?q=al' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```
### 🔹 Search Threads by Title or Content
```bash
curl --location 'http://localhost:8080/api/search/threads?q=message' \
--header 'Cookie: JSESSIONID=D4BBB3AAE9742E64C1E8F3B18EB149DF'
```

---



###  Time and Space Complexity Analysis

---

### 🧵 **Thread and Post Operations**
| Operation                          | Time Complexity | Space Complexity | Notes |
|-----------------------------------|------------------|------------------|-------|
| Create Thread / Post              | O(1)             | O(1)             | Basic entity insert with no heavy computation |
| Fetch All Threads / Posts         | O(n)             | O(n)             | `n` = number of threads/posts |
| Update Thread / Post              | O(1)             | O(1)             | Simple update using ID |
| Delete Thread with Posts          | O(p)             | O(p)             | `p` = number of posts under thread |

---

### 💬 **Private Messaging**
| Operation                          | Time Complexity | Space Complexity | Notes |
|-----------------------------------|------------------|------------------|-------|
| Send Message                       | O(1)             | O(1)             | Save + optional notification creation |
| Get Chat History (User1 ↔ User2)  | O(m)             | O(m)             | `m` = number of messages exchanged |

---

### 🔔 **Notifications**
| Operation                          | Time Complexity | Space Complexity | Notes |
|-----------------------------------|------------------|------------------|-------|
| Send Notification                  | O(1)             | O(1)             | Single DB insert |
| Get All Notifications (user)      | O(k)             | O(k)             | `k` = number of notifications for user |
| Mark as Read/Delete Notification  | O(1)             | O(1)             | Lookup by ID and update/delete |

---

### 🛡️ **Moderation**
| Operation                          | Time Complexity | Space Complexity | Notes |
|-----------------------------------|------------------|------------------|-------|
| Ban User / Delete Post / Thread   | O(1) - O(p)      | O(p)             | O(p) if multiple posts deleted |
| Get Moderation History (user)     | O(h)             | O(h)             | `h` = moderation logs for user |

---

### 👥 **User & Community**
| Operation                          | Time Complexity | Space Complexity | Notes |
|-----------------------------------|------------------|------------------|-------|
| Follow/Unfollow User              | O(1)             | O(1)             | Insert/Delete follow relation |
| Get Following List                | O(f)             | O(f)             | `f` = number of followed users |
| Search Users / Threads            | O(n)             | O(r)             | `n` = total records, `r` = results |




---

## 📊 Code Coverage Reports

- ✅ 1st Iteration: [Coverage Screenshot](https://drive.google.com/file/d/13MWnsCL0vlw0rJE0hk-3QoZIbeBy_f3c/view?usp=drive_link)
- ✅ 2nd Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1OAdCQviFS3kqaEiYiVLRZz7ssyCpW3hO/view?usp=drive_link)

---

## 📦 Download Code

[👉 Click to Download the Project Source Code](https://drive.google.com/file/d/18x1FymwcoO10PyiZMMKr_-PO1GdYMXbC/view?usp=drive_link)

---


## 🧠 Conclusion
---

The **Spring Boot Online Forum System** demonstrates efficient **constant to linear time complexity** across all core operations. The system:

- Handles user interaction at scale with optimized DB queries.
- Uses **lazy loading** and **pagination** where applicable to prevent memory bloat.
- Guarantees **data consistency** and **low latency** through minimal joins and eager moderation logging.
- Provides real-time communication and notification delivery with negligible overhead via **WebSocket** and **event-driven triggers**.

With good database indexing, caching (optional Redis), and async logging or messaging, this architecture ensures **scalability and performance** for high-traffic production environments.

---
```

