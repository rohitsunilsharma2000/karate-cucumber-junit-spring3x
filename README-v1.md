
**Use Case:**  A new social media platform targeting professionals in creative industries. The platform needs to provide real-time engagement features to foster meaningful interactions between users while maintaining high performance and scalability and these requirements:



# **Prompt**

**Title:** Spring Boot Social Media Platform — Real-Time Engagement & High Scalability

**High-Level Description:** You need to build a Spring Boot application that serves as a new social media platform geared toward professionals in creative industries. The platform allows users to create and share posts (including text and media), interact through likes, comments, and shares, and receive real-time notifications of new interactions. The solution must integrate database persistence (MySQL), real-time messaging (WebSockets), caching (Redis), comprehensive REST endpoints, security configuration, and thorough documentation (Swagger). It should also implement strong access control and handle large-scale usage (100K+ daily active users).

**Key Features:**

1. **Project Structure & Setup**

- Create a new Spring Boot project (e.g., via Spring Initializr).
- Follow the provided directory layout.
2. **Database Configuration**

- Use MySQL for persistent storage.
- Set up application.properties (or application.yml) with the necessary DB connection details.
3. **Entity Models & JPA**

- Implement User.java, Post.java, Comment.java, Like.java, Notification.java, and Media.java entities.
- Annotate with JPA/Hibernate relationships (@OneToMany, @ManyToOne, etc.).
- Create DTO classes for each entity and define mapping logic.
4. **Repository Layer**

- Define interfaces for each entity (UserRepository, PostRepository, etc.).
- Add custom query methods if needed.
5. **Service Layer**

- Implement service classes (UserService, PostService, etc.) with business logic.
- Interact with the repository layer for data access.
6. **Controller Layer**

- Create REST controllers for all entities (UserController, PostController, etc.).
- Provide CRUD endpoints and additional features (liking, commenting, sharing).
- Invoke service methods to handle logic.
7. **Security Configuration**

- Implement SecurityConfig.java for authentication and authorization.
- Set up user roles/permissions and protect endpoints accordingly.
8. **WebSocket Configuration**

- Implement WebSocketConfig.java for real-time notifications.
- Send notifications to users when posts are liked/commented/shared.
9. **Redis Configuration**

- Configure caching with Redis in RedisConfig.java for performance and scalability.
10. **Swagger Configuration**

- Implement SwaggerConfig.java for API documentation.
- Ensure that all endpoints are documented and accessible via Swagger UI.
11. **Exception Handling**
- Create custom exception classes (e.g., ResourceNotFoundException).
- Implement a GlobalExceptionHandler.java to centralize error responses.
12. **Event Handling**
- Implement NotificationEvent.java and an EventPublisher.java (or use Spring’s ApplicationEventPublisher) for asynchronous operations and user engagement events.

13. **Expected Behavior**
- Return:
    - `200 OK` for valid inputs.
    - `400 Bad Request` for:
        - `vertices <= 0`
        - `edges == null || edges.isEmpty()`
        - `null` edge nodes
        - malformed JSON
        - out-of-bound vertex indices
    - `500 Internal Server Error` for unexpected logic errors.

14. **Edge Case Handling**
- `vertices = 0` → return 400.
- `edges = null or []` → return 400.
- Self-loops (e.g., `[0, 0]`) → valid, must not crash.
- Duplicate edges → should be handled gracefully.
- Out-of-bound vertex indices → validation error.
- Isolated nodes → no bridges or articulation points.
- Maintain uniform error format with:
    - `timestamp`
    - `status`
    - `error`
    - `message`

**Dependency Requirements:**

- **JUnit 5:** For unit testing.
- **Maven:** For dependency management and build automation
- **Spring Web:** For REST endpoints and WebSocket configuration.
- **Spring Data JPA:** For database interactions with MySQL.
- **Spring Boot Starter Security:** For authentication/authorization.
- **MySQL Driver:** For connecting to MySQL DB.
- **Lombok (optional):** For reducing boilerplate in entity classes.
- **Spring Boot Starter Test:** For unit and integration testing (JUnit 5, Mockito).
- **Spring Boot Starter Websocket:** For real-time event streaming.
- **Spring Boot Starter Data Redis:** For caching.
- **Springfox/Swagger or Springdoc:** For API documentation.

**Goal:** A fully functional social media system capable of handling high user engagement with real-time interactive features, strong security, and robust performance at scale.

**Plan:**  
I will set up the Spring Boot project with packages for controller, service, DTOs, config, and exceptions, using Java 17 and required dependencies. I will implement a REST controller to accept task scheduling requests and return slot assignments based on a greedy graph coloring algorithm. The service layer will include detailed logging and edge case handling for invalid edges, self-loops, and disconnected graphs. I will add a global exception handler and write unit and integration tests to validate core logic and edge cases.

# **Complete Project Code**

**1) Project Structure:** A logical structure (typical Maven layout)
```properties
social-media-app/
├─ pom.xml

```



**7) Main Application:** `src/main/java/com/socialmedia/SocialMediaApp.java`
```java
package com.socialmedia.controller;

import com.socialmedia.dto.CommentDTO;
import com.socialmedia.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing comments in the Social Media Application.
 *
 * <p><strong>Overview:</strong></p>
 * This controller provides CRUD (Create, Read, Update, Delete) operations for comments associated with posts
 * within the Social Media application. It interacts with the {@link CommentService} to perform business logic
 * and ensures that all comment-related operations adhere to the application's validation and authorization rules.
 *
 * <p><strong>References:</strong></p>
 * <ul>
 *   <li>{@link CommentService} handles the business logic and interacts with repositories to manage comments.</li>
 *   <li>{@link CommentDTO} serves as the Data Transfer Object for comment data between client and server.</li>
 * </ul>
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *   <li>Creates new comments associated with specific posts and users.</li>
 *   <li>Retrieves comments for a given post.</li>
 *   <li>Updates existing comments with new content.</li>
 *   <li>Deletes comments based on their unique identifiers.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> Successfully performs the requested CRUD operation and returns the appropriate HTTP status.</li>
 *   <li><strong>Fail:</strong> Returns error responses (e.g., HTTP 400, 404) when input constraints are violated or resources are not found.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * Creates a new comment.
     *
     * <p><strong>Description:</strong></p>
     * This endpoint allows authenticated users to create a new comment associated with a specific post.
     * It validates the input data and ensures that the referenced post and user exist in the database.
     *
     * <p><strong>Acceptable Values / Constraints:</strong></p>
     * <ul>
     *   <li><strong>CommentDTO.content:</strong> Must be non-null, non-empty, and adhere to length constraints (e.g., maximum 500 characters).</li>
     *   <li><strong>postId:</strong> Must reference an existing post in the database.</li>
     *   <li><strong>userId:</strong> Must reference an existing user in the database.</li>
     * </ul>
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>commentDTO</strong> (<em>{@link CommentDTO}</em>): The data transfer object containing details of the comment to be created.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If {@code commentDTO.content} is null or empty, an {@link InvalidInputException} is thrown.</li>
     *   <li>If the provided {@code postId} or {@code userId} does not exist in the database, a {@link ResourceNotFoundException} is thrown.</li>
     * </ul>
     *
     * <p><strong>Premise and Assertions:</strong></p>
     * <ul>
     *   <li>The user creating the comment must be authenticated and authorized to comment on the specified post.</li>
     *   <li>The {@link CommentService} must correctly handle the creation logic and interact with the repository layer.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Successfully creates a comment and returns the created {@link CommentDTO} with HTTP 201 (Created) status.</li>
     *   <li><strong>Fail:</strong> Returns appropriate error responses if input validation fails or referenced resources are not found.</li>
     * </ul>
     *
     * @param commentDTO The data transfer object containing comment details.
     * @return The created {@link CommentDTO} with HTTP 201 (Created) status.
     */
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    /**
     * Retrieves all comments associated with a specific post.
     *
     * <p><strong>Description:</strong></p>
     * This endpoint fetches all comments linked to the provided {@code postId}. It returns a list of {@link CommentDTO}
     * objects, which represent the comments made on the specified post. If the post has no comments, an empty list is returned.
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>postId</strong> (<em>Long</em>): The unique identifier of the post for which comments are to be retrieved.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Successfully retrieves and returns a list of comments associated with the specified post, even if the list is empty.</li>
     *   <li><strong>Fail:</strong> If the {@code postId} does not correspond to an existing post, a {@link ResourceNotFoundException} might be thrown depending on service implementation.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If the specified {@code postId} does not exist, the service may handle it gracefully by returning an empty list or throwing a {@link ResourceNotFoundException}.</li>
     * </ul>
     *
     * <p><strong>Premise and Assertions:</strong></p>
     * <ul>
     *   <li>The specified {@code postId} should reference an existing post within the database.</li>
     *   <li>The {@link CommentService} must correctly retrieve comments linked to the given post.</li>
     * </ul>
     *
     * @param postId The ID of the post for which comments are to be retrieved.
     * @return A list of {@link CommentDTO} with HTTP 200 (OK) status.
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    /**
     * Updates an existing comment.
     *
     * <p><strong>Description:</strong></p>
     * This endpoint allows authenticated users to update the content of an existing comment identified by its {@code id}.
     * It validates the input data and ensures that the comment exists before performing the update.
     *
     * <p><strong>Acceptable Values / Constraints:</strong></p>
     * <ul>
     *   <li><strong>CommentDTO.content:</strong> Must be non-null, non-empty, and adhere to length constraints (e.g., maximum 500 characters).</li>
     * </ul>
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>id</strong> (<em>Long</em>): The unique identifier of the comment to be updated.</li>
     *   <li><strong>commentDTO</strong> (<em>{@link CommentDTO}</em>): The data transfer object containing updated comment details.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If the comment with the given {@code id} does not exist, a {@link ResourceNotFoundException} is thrown.</li>
     *   <li>If the new content in {@code commentDTO} is invalid (e.g., null, empty, or exceeds length constraints), an {@link InvalidInputException} might be thrown.</li>
     * </ul>
     *
     * <p><strong>Premise and Assertions:</strong></p>
     * <ul>
     *   <li>The comment to be updated must exist in the database.</li>
     *   <li>The user performing the update must have the necessary permissions to modify the comment.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Successfully updates the comment and returns the updated {@link CommentDTO} with HTTP 200 (OK) status.</li>
     *   <li><strong>Fail:</strong> Returns appropriate error responses if the comment does not exist or if input validation fails.</li>
     * </ul>
     *
     * @param id         The ID of the comment to be updated.
     * @param commentDTO The data transfer object containing updated comment details.
     * @return The updated {@link CommentDTO} with HTTP 200 (OK) status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    /**
     * Deletes a comment.
     *
     * <p><strong>Description:</strong></p>
     * This endpoint allows authenticated users to delete a comment identified by its {@code id}. It ensures that the comment exists
     * before performing the deletion.
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>id</strong> (<em>Long</em>): The unique identifier of the comment to be deleted.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> If the comment exists and is successfully deleted, returns HTTP 204 (No Content) status.</li>
     *   <li><strong>Fail:</strong> If the comment does not exist, a {@link ResourceNotFoundException} is thrown.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If the comment with the given {@code id} does not exist, a {@link ResourceNotFoundException} is thrown.</li>
     *   <li>If the user does not have permission to delete the comment, an {@link AccessDeniedException} might be thrown.</li>
     * </ul>
     *
     * <p><strong>Premise and Assertions:</strong></p>
     * <ul>
     *   <li>The comment to be deleted must exist in the database.</li>
     *   <li>The user performing the deletion must have the necessary permissions to delete the comment.</li>
     * </ul>
     *
     * @param id The ID of the comment to be deleted.
     * @return HTTP 204 (No Content) status on successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
```

**2) Main Application:** `src/main/java/com/example/colorsched/GraphSlotterSystem.java`
```java

```
**3) TaskSchedulerServiceImpl:** `src/main/java/com/example/colorsched/service/TaskSchedulerServiceImpl.java`
```java

```
**4) GlobalExceptionHandler:** `src/main/java/com/example/colorsched/exception/GlobalExceptionHandler.java`
```java

```
**5) ScheduleResponse:** `src/main/java/com/example/colorsched/dto/ScheduleResponse.java`
```java

```
**6) TaskRequest:** `src/main/java/com/example/colorsched/dto/TaskRequest.java`
```java

```
**7) TaskController:** `src/main/java/com/example/colorsched/controller/TaskController.java`
```java

```
**8) Main Application:** `src/main/java/com/example/colorsched/GraphSlotterSystem.java`
```java

```

**9) pom.xml:** `pom.xml`
```xml

```

**10) application.properties:** `src/main/resources/application.properties`
```properties

```

# **Unit Tests (JUnit 5 + Mockito)**
```java
package com.socialmedia.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WebSocketConfig}.
 * <p>
 * This class contains unit tests to verify the configuration of the WebSocket endpoints
 * and message broker settings.
 * </p>
 *
 * @version 1.0
 * @since 2025-01-28
 */
@ExtendWith(MockitoExtension.class)
class WebSocketConfigTest {

    /**
     * Mocked instance of {@link MessageBrokerRegistry} for testing message broker configurations.
     */
    @Mock
    private MessageBrokerRegistry messageBrokerRegistry;

    /**
     * Mocked instance of {@link StompEndpointRegistry} for testing STOMP endpoint registrations.
     */
    @Mock
    private StompEndpointRegistry stompEndpointRegistry;

    /**
     * Mocked instance of {@link StompWebSocketEndpointRegistration} for chaining endpoint configurations.
     */
    @Mock
    private StompWebSocketEndpointRegistration endpointRegistration;

    /**
     * Injected instance of {@link WebSocketConfig} to be tested.
     */
    @InjectMocks
    private WebSocketConfig webSocketConfig;

    /**
     * Tests the configuration of the message broker.
     * <p>
     * Verifies that the message broker is enabled with the correct destination prefixes.
     * </p>
     */
    @Test
    void testConfigureMessageBroker() {
        // Invoke the method under test
        webSocketConfig.configureMessageBroker(messageBrokerRegistry);

        // Verify that enableSimpleBroker is called with the correct arguments
        verify(messageBrokerRegistry, times(1)).enableSimpleBroker("/topic", "/queue");

        // Verify that setApplicationDestinationPrefixes is called with the correct argument
        verify(messageBrokerRegistry, times(1)).setApplicationDestinationPrefixes("/app");
    }

    /**
     * Tests the registration of STOMP endpoints.
     * <p>
     * Verifies that the STOMP endpoint is registered with SockJS fallback and allowed origins.
     * </p>
     */
    @Test
    void testRegisterStompEndpoints() {
        // Define the behavior of mocked methods
        when(stompEndpointRegistry.addEndpoint("/ws")).thenReturn(endpointRegistration);
        when(endpointRegistration.setAllowedOrigins("*")).thenReturn(endpointRegistration);
        when(endpointRegistration.withSockJS()).thenReturn(endpointRegistration);

        // Invoke the method under test
        webSocketConfig.registerStompEndpoints(stompEndpointRegistry);

        // Verify that addEndpoint is called with the correct endpoint
        verify(stompEndpointRegistry, times(1)).addEndpoint("/ws");

        // Verify that setAllowedOrigins is called with the correct argument
        verify(endpointRegistration, times(1)).setAllowedOrigins("*");

        // Verify that withSockJS is called to enable SockJS fallback
        verify(endpointRegistration, times(1)).withSockJS();
    }

    /**
     * Tests that the {@link WebSocketConfig} bean is loaded successfully.
     * <p>
     * Ensures that the {@link WebSocketConfig} instance is not null after configuration.
     * </p>
     */
    @Test
    void testWebSocketConfigurationLoaded() {
        // Assert that the webSocketConfig instance is not null
        assertNotNull(webSocketConfig, "WebSocketConfig should be loaded");
    }
}

```

**2) Main Application:** `src/main/java/com/example/colorsched/GraphSlotterSystem.java`
```java

```
**3) TaskSchedulerServiceImpl:** `src/main/java/com/example/colorsched/service/TaskSchedulerServiceImpl.java`
```java

```
**4) GlobalExceptionHandler:** `src/main/java/com/example/colorsched/exception/GlobalExceptionHandler.java`
```java

```
**5) ScheduleResponse:** `src/main/java/com/example/colorsched/dto/ScheduleResponse.java`
```java

```
# After the first iteration, the overall test coverage was 88%. To improve this, additional test cases—including those in `TaskControllerIntegrationTest`  , `TaskSchedulerServiceImplTest` and `TaskControllerTest` will be introduced to further increase the test coverage percentage.
**6) TaskControllerIntegrationTest:** `src/main/java/com/example/colorsched/dto/TaskRequest.java`
```java

```
**7) TaskSchedulerServiceImplTest:** `src/main/java/com/example/colorsched/controller/TaskController.java`
```java

```
**8) TaskControllerTest:** `src/main/java/com/example/colorsched/GraphSlotterSystem.java`
```java

```
# After the second iteration, test coverage increased to 99%.
**Result:** Total line coverage is 99%

# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml contains the necessary Spring Boot dependencies (e.g., Spring Web, Spring Data JPA, Spring Security, Spring WebSocket, Spring Data Redis, Spring Boot Starter Test, MySQL Driver, Swagger libs, etc.). Confirm that the Java version is set to 17 (or adjust accordingly) and any needed plugins (e.g., the Maven Surefire plugin for tests, Jacoco for coverage, etc.) are present.
3. **Database & Application Properties:** Configure MySQL credentials in application.properties (or application.yml), for example:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/social_db
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASS
spring.jpa.hibernate.ddl-auto=update
```
- If using Redis, include the Redis connection settings as well.
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
- By default, it starts on port 8080 (unless configured otherwise).



### 6. **Accessing Endpoints & Features:**

Below are runnable cURL commands along with sample success and error responses for the **Graph Bridges and Articulation Points Detection** endpoints.




Detect Bridges – Valid Request
```bash
curl -X POST http://localhost:8080/graph/bridges \
  -H "Content-Type: application/json" \
  -d '{"vertices":5,"edges":[[0,1],[1,2],[2,0],[1,3],[3,4]]}'

```



Missing Vertices
```bash
curl -X POST http://localhost:8080/graph/bridges \
  -H "Content-Type: application/json" \
  -d '{"edges":[[0,1],[1,2]]}'
```



Malformed JSON
```bash
curl -X POST http://localhost:8080/graph/bridges \
  -H "Content-Type: application/json" \
  -d '{"vertices":4,"edges":[[0,1],[1,2]'
```



Detect Articulation Points – Valid Request
```bash
curl -X POST http://localhost:8080/graph/articulation \
  -H "Content-Type: application/json" \
  -d '{"vertices":5,"edges":[[0,1],[1,2],[2,0],[1,3],[3,4]]}'
```



Empty Edges
```bash
curl -X POST http://localhost:8080/graph/articulation \
  -H "Content-Type: application/json" \
  -d '{"vertices":4,"edges":[]}'
```




# **Time and Space Complexity:**

- **Time Complexity:**  
  The detection of both bridges and articulation points in an undirected graph is performed using **Depth-First Search (DFS)**, resulting in:
    - **O(V + E)** per DFS traversal,  
      where **V** is the number of vertices and **E** is the number of edges.
    - This complexity is optimal for sparse and dense graphs alike, as it guarantees linear time processing relative to input size.

- **Space Complexity:**
    - **O(V + E)** for storing the adjacency list.
    - **O(V)** for auxiliary arrays like `disc[]`, `low[]`, `visited[]`, `ap[]`, `parent[]`, etc.
    - Therefore, total space complexity is **O(V + E)**.



# **Conclusion**

The **Graph Integrity App**, built with **Spring Boot**, delivers efficient and scalable detection of graph **bridges** and **articulation points** through a REST API interface. Leveraging the optimal **DFS-based Tarjan's algorithm**, the system achieves linear-time performance **(O(V + E))**, which is ideal for real-time graph integrity validation in network topologies, dependency chains, or infrastructure graphs.

The solution is further strengthened with:
- **Robust input validation** using Jakarta Bean Validation,
- **Comprehensive logging** and error handling,
- **Unit and integration testing** with JUnit and MockMvc.

This makes the application suitable for academic, enterprise, and analytical use cases that demand both **correctness** and **performance** in graph structural analysis.



# ** Iteration**


First Iteration: https://drive.google.com/file/d/1k5aWK_9uDu8GKvd5dCHPl24LrUv3JI5i/view?usp=drive_link
First Iteration: https://drive.google.com/file/d/1FoUCCNkvL8b-V59H3SjpqOGzpAY5UeZj/view?usp=drive_link
Code Download:https://drive.google.com/file/d/13r91CiKdWESVm379HkGiZJ9XDSP7Wdbe/view?usp=drive_link
