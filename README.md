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
###   `UserService`
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
 * Service class for managing user-related operations.
 * <p>
 * Provides business logic for:
 * - Retrieving a user by ID
 * - Updating user profiles
 * - Saving new users
 * - Fetching all users
 * </p>
 * 
 * Interacts with the {@link UserRepository} for database operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepo;

    /**
     * Retrieve a user by their ID or throw an exception if not found.
     *
     * @param id The ID of the user to retrieve.
     * @return The {@link User} object.
     * @throws ResourceNotFoundException if the user does not exist.
     */
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    /**
     * Update an existing user's profile.
     *
     * @param id          The ID of the user to update.
     * @param updatedUser The updated user details.
     * @return The updated {@link User} object after saving to the database.
     */
    public User updateUserProfile(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        return userRepo.save(existing);
    }

    /**
     * Find a user by their ID.
     *
     * @param id The ID of the user.
     * @return An {@link Optional} containing the user if found.
     */
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    /**
     * Retrieve all users from the database.
     *
     * @return A list of all {@link User} objects.
     */
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * Save a new or existing user to the database.
     *
     * @param user The {@link User} object to save.
     * @return The saved {@link User} object with any generated fields populated.
     */
    public User save(User user) {
        return userRepo.save(user);
    }
}

```
###   `UserRepository`
```java
package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on {@link User} entities.
 * <p>
 * Extends {@link JpaRepository} to provide built-in support for:
 * - Saving, deleting, and finding users
 * - Pagination and sorting
 * </p>
 * 
 * Also includes custom query methods for searching users by username.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find all users whose username contains the specified keyword, ignoring case.
     *
     * @param keyword The keyword to search for within usernames.
     * @return A list of users with usernames that contain the keyword (case-insensitive).
     */
    List<User> findByUsernameContainingIgnoreCase(String keyword);
}

```
###   `User`
```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity representing a user in the Turing Online Forum System.
 * 
 * <p>This class is mapped to the "user" table in the database and includes
 * attributes related to authentication, authorization, and profile management.</p>
 */
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    /**
     * Unique identifier for the user (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The hashed password of the user.
     */
    private String password;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The role assigned to the user (e.g., ADMIN, USER, MODERATOR).
     */
    private String role;

    /**
     * The date and time when the user account was created.
     */
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Indicates whether the user is banned.
     */
    @Column(name = "is_banned")
    private Boolean banned = false;
}

```
###   `ThreadController`
```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.service.ThreadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing discussion threads in the Turing Online Forum System.
 *
 * <p>Exposes endpoints to create, retrieve, update, and delete threads.</p>
 *
 * <p>Base URL: <code>/api/threads</code></p>
 */
@RestController
@RequestMapping("/api/threads")
@RequiredArgsConstructor
@Slf4j
public class ThreadController {

    private final ThreadService threadsService;

    /**
     * Create a new discussion thread.
     *
     * @param threads The {@link Threads} object to create.
     * @return The created {@link Threads} object.
     */
    @PostMapping
    public Threads createThreads(@RequestBody Threads threads) {
        log.debug("Request to create Threads: {}", threads.getTitle());
        return threadsService.createThread(threads);
    }

    /**
     * Get a thread by its ID.
     *
     * @param id The ID of the thread.
     * @return The {@link Threads} object with the specified ID.
     */
    @GetMapping("/{id}")
    public Threads getThreads(@PathVariable Long id) {
        return threadsService.getThread(id);
    }

    /**
     * Retrieve all threads.
     *
     * @return A list of all {@link Threads} objects.
     */
    @GetMapping
    public List<Threads> getAllThreadss() {
        return threadsService.getAllThreads();
    }

    /**
     * Update an existing thread.
     *
     * @param id      The ID of the thread to update.
     * @param threads The updated {@link Threads} object.
     * @return The updated {@link Threads} object.
     */
    @PutMapping("/{id}")
    public Threads updateThreads(@PathVariable Long id, @RequestBody Threads threads) {
        return threadsService.updateThread(id, threads);
    }

    /**
     * Delete a thread by its ID.
     *
     * @param id The ID of the thread to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteThreads(@PathVariable Long id) {
        threadsService.deleteThread(id);
    }
}

```
###   `ThreadService`
```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing discussion threads in the Turing Online Forum System.
 * 
 * <p>This class handles the core business logic for creating, updating, retrieving,
 * and deleting threads. It interacts with the {@link ThreadRepository} for database operations.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ThreadService {

    private final ThreadRepository threadRepository;

    /**
     * Create a new thread and set its creation and update timestamps.
     *
     * @param thread The {@link Threads} object to create.
     * @return The saved {@link Threads} object with generated ID.
     */
    public Threads createThread(Threads thread) {
        thread.setCreatedAt(LocalDateTime.now());
        thread.setUpdatedAt(LocalDateTime.now());
        Threads saved = threadRepository.save(thread);
        log.info("Created thread with ID {}", saved.getId());
        return saved;
    }

    /**
     * Update an existing thread with new content and title.
     *
     * @param id            The ID of the thread to update.
     * @param updatedThread The thread data with updated fields.
     * @return The updated {@link Threads} object.
     * @throws ResourceNotFoundException if the thread with the specified ID is not found.
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
     * Delete a thread by its ID.
     *
     * @param id The ID of the thread to delete.
     * @throws ResourceNotFoundException if the thread with the specified ID is not found.
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
     * Retrieve a single thread by its ID.
     *
     * @param id The ID of the thread.
     * @return The {@link Threads} object.
     * @throws ResourceNotFoundException if the thread is not found.
     */
    public Threads getThread(Long id) {
        return threadRepository.findById(id).orElseThrow(() -> {
            log.error("Thread with ID {} not found", id);
            return new ResourceNotFoundException("Thread not found");
        });
    }

    /**
     * Retrieve all threads from the database.
     *
     * @return A list of all {@link Threads} objects.
     */
    public List<Threads> getAllThreads() {
        log.info("Fetching all threads");
        return threadRepository.findAll();
    }
}


```
###   `ThreadRepository`
```java
package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Threads;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Threads} entities.
 * <p>
 * Provides built-in CRUD operations and custom query methods to
 * interact with the discussion threads stored in the database.
 * </p>
 */
public interface ThreadRepository extends JpaRepository<Threads, Long> {

    /**
     * Find threads where the title or content contains the given keyword (case-insensitive).
     *
     * @param title   The keyword to search in the title.
     * @param content The keyword to search in the content.
     * @return A list of {@link Threads} that match the search criteria.
     */
    List<Threads> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}

```
###   `Threads`
```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a discussion thread in the Turing Online Forum System.
 * <p>
 * Each thread is associated with a {@link User} and contains multiple {@link Post} entries.
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "thread") // Ensure this matches your actual DB table name
public class Threads {

    /**
     * Unique identifier for the thread.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the thread.
     */
    private String title;

    /**
     * Content/body of the thread.
     */
    private String content;

    /**
     * Timestamp indicating when the thread was created.
     */
    private LocalDateTime createdAt = LocalDateTime

```
###   `Post`
```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity representing a post or reply in a discussion thread.
 * <p>
 * Each post is linked to a {@link Threads} object and is authored by a {@link User}.
 * </p>
 */
@Getter
@Setter
@Entity
public class Post {

    /**
     * Unique identifier for the post.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Content of the post.
     */
    private String content;

    /**
     * Timestamp indicating when the post was created.
     */
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * The thread to which this post belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private Threads thread;

    /**
     * The user who created this post.
     */
    @ManyToOne
    private User user;
}

```
###   `PostController`
```java
package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.dto.PostDto;
import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping("/thread/{threadId}")
    public Post createPost(@PathVariable Long threadId, @RequestBody Post post) {
        return postService.createPost(post, threadId);
    }

    @GetMapping("/thread/{threadId}")
    public List<PostDto> getPostsByThread(@PathVariable Long threadId) {
        return postService.getPostsByThread(threadId);
    }
}

```
###   `PostService`
```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.dto.PostDto;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.PostRepository;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling business logic related to posts.
 * 
 * <p>Includes operations such as creating posts, retrieving posts by thread,
 * and deleting all posts under a thread.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ThreadRepository threadRepository;
    private final UserRepository userRepository;

    /**
     * Create a new post under a given thread.
     *
     * @param post      The post to create.
     * @param threadId  The ID of the thread the post belongs to.
     * @return The saved {@link Post} object.
     * @throws ResourceNotFoundException if the thread or user is not found.
     */
    public Post createPost(Post post, Long threadId) {
        Threads thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Thread not found with ID: " + threadId));

        User user = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + post.getUser().getId()));

        Post newPost = new Post();
        newPost.setContent(post.getContent());
        newPost.setUser(user);
        newPost.setThread(thread);
        newPost.setCreatedAt(LocalDateTime.now());

        log.info("Saving post: content={}, userId={}, threadId={}",
                newPost.getContent(),
                newPost.getUser().getId(),
                newPost.getThread().getId());

        return postRepository.save(newPost);
    }

    /**
     * Get all posts associated with a thread.
     *
     * @param threadId The ID of the thread.
     * @return A list of {@link PostDto} representing the posts.
     */
    public List<PostDto> getPostsByThread(Long threadId) {
        log.info("Fetching posts for thread ID {}", threadId);

        return postRepository.findByThreadId(threadId).stream()
                .map(post -> PostDto.builder()
                        .id(post.getId())
                        .content(post.getContent())
                        .userId(post.getUser().getId())
                        .threadId(post.getThread().getId())
                        .createdAt(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Delete all posts under a specific thread.
     *
     * @param threadId The ID of the thread.
     */
    public void deletePostsByThread(Long threadId) {
        List<Post> posts = postRepository.findByThreadId(threadId);
        postRepository.deleteAll(posts);
        log.info("Deleted {} posts under thread ID {}", posts.size(), threadId);
    }
}

```
###   `PostRepository`
```java
package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Post} entity.
 * <p>Provides CRUD operations and custom query methods for working with posts.</p>
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find all posts that belong to the specified thread.
     *
     * @param threadId The ID of the thread.
     * @return A list of {@link Post} objects associated with the thread.
     */
    List<Post> findByThreadId(Long threadId);
}

```

###   `ModerationController`
```java
package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.dto.ModerationDTO;
import com.example.turingOnlineForumSystem.service.ModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for moderation actions in the Turing Online Forum System.
 * <p>
 * Provides administrative endpoints for moderators to manage threads, posts,
 * and user behavior.
 * </p>
 *
 * <p>Base URL: <code>/api/moderation</code></p>
 */
@RestController
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
@Slf4j
public class ModerationController {

    private final ModerationService moderationService;

    /**
     * Delete a thread as a moderator.
     *
     * @param threadId     The ID of the thread to delete.
     * @param moderatorId  The ID of the moderator performing the action.
     * @param reason       The reason for deletion.
     */
    @DeleteMapping("/thread/{threadId}")
    public void deleteThread(@PathVariable Long threadId,
                             @RequestParam Long moderatorId,
                             @RequestParam String reason) {
        moderationService.deleteThread(threadId, moderatorId, reason);
    }

    /**
     * Delete a post as a moderator.
     *
     * @param postId       The ID of the post to delete.
     * @param moderatorId  The ID of the moderator performing the action.
     * @param reason       The reason for deletion.
     */
    @DeleteMapping("/post/{postId}")
    public void deletePost(@PathVariable Long postId,
                           @RequestParam Long moderatorId,
                           @RequestParam String reason) {
        moderationService.deletePost(postId, moderatorId, reason);
    }

    /**
     * Ban a user from posting.
     *
     * @param userId The ID of the user to ban.
     * @param reason The reason for banning the user.
     */
    @PostMapping("/ban-user/{userId}")
    public void banUser(@PathVariable Long userId,
                        @RequestParam String reason) {
        moderationService.banUser(userId, reason);
    }

    /**
     * Get moderation history for a specific user.
     *
     * @param userId The ID of the user whose moderation history is requested.
     * @return A list of {@link ModerationDTO} representing moderation actions taken on the user.
     */
    @GetMapping("/history/{userId}")
    public List<ModerationDTO> getModerationHistory(@PathVariable Long userId) {
        return moderationService.getModerationHistory(userId);
    }
}

```
###   `ModerationService`
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
 * Service class for handling moderation actions in the forum system.
 *
 * <p>This includes deleting threads/posts, banning users, and logging all moderation activities.</p>
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
     * Delete a thread by its ID and log the moderation action.
     *
     * @param threadId     The ID of the thread to delete.
     * @param moderatorId  The moderator performing the action.
     * @param reason       The reason for deletion.
     */
    public void deleteThread(Long threadId, Long moderatorId, String reason) {
        Threads threadRef = threadsRepository.getReferenceById(threadId);
        User threadOwner = userRepository.getReferenceById(threadRef.getUser().getId());

        Moderation moderation = Moderation.builder()
                .action("DELETE_THREAD")
                .reason(reason)
                .user(threadOwner)
                .thread(threadRef)
                .createdAt(LocalDateTime.now())
                .build();

        moderationRepository.saveAndFlush(moderation);

        List<Post> posts = postRepository.findByThreadId(threadId);
        postRepository.deleteAll(posts);
        threadsRepository.deleteById(threadId);

        log.info("Deleted thread ID {} by moderator {}", threadId, moderatorId);
    }

    /**
     * Delete a post by its ID and log the moderation action.
     *
     * @param postId       The ID of the post to delete.
     * @param moderatorId  The moderator performing the action.
     * @param reason       The reason for deletion.
     */
    public void deletePost(Long postId, Long moderatorId, String reason) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

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
     * Ban a user from posting and log the action.
     *
     * @param userId The ID of the user to ban.
     * @param reason The reason for the ban.
     */
    public void banUser(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
     * Get moderation history for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of moderation actions taken against the user.
     */
    public List<ModerationDTO> getModerationHistory(Long userId

```
###   `ModerationRepository`
```java
package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Moderation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Moderation} entity.
 *
 * <p>Provides database access methods for retrieving moderation history records.</p>
 */
public interface ModerationRepository extends JpaRepository<Moderation, Long> {

    /**
     * Find all moderation records for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of moderation actions associated with the user.
     */
    List<Moderation> findByUserId(Long userId);
}

```
###   `Moderation`
```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * Entity representing a moderation action (e.g., delete, ban) performed on a user/thread/post.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moderation {

    /**
     * Unique identifier for the moderation record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Type of action (e.g., DELETE_THREAD, DELETE_POST, BAN_USER).
     */
    private String action;

    /**
     * Reason provided for the action.
     */
    private String reason;

    /**
     * Timestamp when the action was performed.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The user who was moderated.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The thread associated with the action, if applicable.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "thread_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Threads thread;

    /**
     * Automatically set createdAt before persisting if not set.
     */
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

```
###   `MessagingController`
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
 * Controller for handling messaging functionality in the Turing Online Forum System.
 *
 * <p>This controller includes:
 * - WebSocket message handling for real-time chat
 * - REST endpoint for chat history
 * - A Thymeleaf view renderer for the chat UI
 * </p>
 *
 * <p>Base URL: <code>/api/messages</code></p>
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessagingController {

    private final MessagingService messagingService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * WebSocket endpoint to handle incoming chat messages.
     * 
     * <p>Messages sent to <code>/app/chat.send</code> will be received here.
     * The message is persisted and then broadcast to the receiver's topic.</p>
     *
     * @param chatMessage DTO containing sender ID, receiver ID, and message content.
     */
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDTO chatMessage) {
        log.info("WebSocket: Received message {}", chatMessage.getContent());

        Message message = messagingService.sendMessage(chatMessage);

        simpMessagingTemplate.convertAndSend("/topic/messages/" + chatMessage.getReceiverId(), message);
    }

    /**
     * REST API to retrieve the message history between two users.
     *
     * @param senderId   ID of the sender.
     * @param receiverId ID of the receiver.
     * @return A list of {@link Message} objects exchanged between the two users.
     */
    @GetMapping("/history")
    public List<Message> getHistory(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return messagingService.getChatHistory(senderId, receiverId);
    }

    /**
     * Renders the chat user interface (UI) using Thymeleaf.
     *
     * @param userId The ID of the current logged-in user.
     * @param model  Spring's model object used to pass attributes to the view.
     * @return The name of the Thymeleaf template ("chat") to render.
     */
    @GetMapping("/chat")
    public String chatPage(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);  // Inject user ID into the frontend
        return "chat";  // Loads templates/chat.html
    }
}

```
###   `MessagingService`
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
 * Service class responsible for handling messaging logic.
 * <p>
 * This includes sending messages, persisting them, fetching chat history, 
 * and sending real-time notifications to users.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    /**
     * Sends a message from one user to another.
     *
     * <p>This method will:
     * <ul>
     *   <li>Validate sender and receiver IDs</li>
     *   <li>Persist the message in the database</li>
     *   <li>Send a real-time notification to the receiver</li>
     * </ul>
     * </p>
     *
     * @param dto The {@link ChatMessageDTO} containing message content and user IDs.
     * @return The persisted {@link Message} entity.
     * @throws ResourceNotFoundException if either the sender or receiver does not exist.
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

        // 🔔 Create notification
        notificationService.sendNotification(receiver, "📩 New message from " + sender.getUsername());

        return saved;
    }

    /**
     * Retrieves the complete chat history between two users.
     * 
     * <p>Note: Only messages sent from user1 → user2 are currently returned. 
     * To support bidirectional history, consider modifying the repository method.</p>
     *
     * @param user1Id The ID of the first user.
     * @param user2Id The ID of the second user.
     * @return A list of {@link Message} objects exchanged between the users.
     * @throws ResourceNotFoundException if either user is not found.
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
###   `MessageRepository`
```java
package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Message} entities.
 * <p>
 * Provides methods to persist and retrieve chat messages between users.
 * </p>
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Find messages sent from one specific user to another.
     *
     * @param sender   The user who sent the messages.
     * @param receiver The user who received the messages.
     * @return A list of {@link Message} objects from sender to receiver.
     */
    List<Message> findBySenderAndReceiver(User sender, User receiver);

}

```
###   `Message`
```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a private message exchanged between two users in the forum system.
 * <p>
 * Each message contains content, timestamp, and references to both the sender and receiver.
 * </p>
 */
@Getter
@Setter
@Entity
@NoArgsConstructor   // Required by JPA
@AllArgsConstructor
@Builder
public class Message {

    /**
     * Unique identifier for the message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The textual content of the message.
     */
    private String content;

    /**
     * Timestamp when the message was created.
     * Automatically set before persistence via {@link #prePersist()}.
     */
    private LocalDateTime timestamp;

    /**
     * The user who sent the message.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    /**
     * The user who received the message.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    /**
     * Lifecycle hook to automatically set the timestamp before the entity is persisted.
     */
    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}

```
###   `FollowController`
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
 * REST controller for managing follow relationships between users.
 *
 * <p>Exposes endpoints to follow another user and retrieve the list of users being followed.</p>
 *
 * <p>Base URL: <code>/api/follow</code></p>
 */
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;

    /**
     * Follow another user.
     *
     * @param followerId  The ID of the user who wants to follow someone.
     * @param followingId The ID of the user to be followed.
     * @return A success message indicating that the follow action was completed.
     */
    @PostMapping
    public ResponseEntity<String> followUser(@RequestParam Long followerId, @RequestParam Long followingId) {
        log.info("User {} is attempting to follow User {}", followerId, followingId);
        followService.followUser(followerId, followingId);
        return ResponseEntity.ok("Followed successfully");
    }

    /**
     * Get a list of users that the given user is following.
     *
     * @param userId The ID of the user whose following list is to be retrieved.
     * @return A list of {@link User} entities that the user is following.
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
}

```
###   `FollowService`
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
 * Service class that handles the logic for following users in the system.
 *
 * <p>Includes functionality to:
 * - Follow another user
 * - Retrieve the list of users a given user is following
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepo;
    private final UserRepository userRepo;

    /**
     * Follows a user by creating a follow relationship between two users.
     *
     * @param followerId  The ID of the user who is following.
     * @param followingId The ID of the user to be followed.
     * @throws java.util.NoSuchElementException if either user is not found.
     */
    public void followUser(Long followerId, Long followingId) {
        User follower = userRepo.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found with ID: " + followerId));
        User following = userRepo.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Following not found with ID: " + followingId));

        Follow follow = new Follow(null, follower, following);
        followRepo.save(follow);
        log.info("User {} followed user {}", followerId, followingId);
    }

    /**
     * Retrieves the list of users that a given user is following.
     *
     * @param userId The ID of the user.
     * @return A list of {@link User} entities the user is following.
     */
    public List<User> getFollowing(Long userId) {
        List<User> followingList = followRepo.findByFollowerId(userId).stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        log.info("User {} is following {} users", userId, followingList.size());
        return followingList;
    }
}

```
###   `FollowRepository`
```java
package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link Follow} relationships between users.
 *
 * <p>Provides methods to query the follow table for operations such as retrieving
 * all users followed by a specific user.</p>
 */
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * Finds all follow relationships where the given user is the follower.
     *
     * @param followerId The ID of the follower user.
     * @return A list of {@link Follow} entities where the user is following others.
     */
    List<Follow> findByFollowerId(Long followerId);
}

```
###   `Follow`
```java
package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a "follow" relationship between two users.
 *
 * <p>This table models the connection where one user follows another. 
 * Each record links a follower to the user they are following.</p>
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    /**
     * Primary key for the follow relationship.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who is following another user.
     */
    @ManyToOne
    private User follower;

    /**
     * The user who is being followed.
     */
    @ManyToOne
    private User following;
}

```
###   `ChatMessageDTO`
```java
package com.example.turingOnlineForumSystem.dto;


import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long senderId;
    private Long receiverId;
    private String content;
}

```
###   `ModerationDTO`
```java
package com.example.turingOnlineForumSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModerationDTO {
    private Long id;
    private String action;
    private String reason;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private Long threadId;
}

```
###   `PostDto`
```java
package com.example.turingOnlineForumSystem.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String content;
    private Long userId;
    private Long threadId;
    private LocalDateTime createdAt;
}

```

###   `GlobalExceptionHandler`
```java
package com.example.turingOnlineForumSystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Global exception handler for the Turing Online Forum System.
 *
 * <p>This class handles exceptions thrown across all REST controllers
 * and returns consistent error responses to the client.</p>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} and returns a 404 response.
     *
     * @param ex the thrown exception
     * @return ResponseEntity with status 404 and the exception message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Handles uncaught exceptions and returns a generic 500 error response.
     *
     * @param ex the thrown exception
     * @return ResponseEntity with status 500 and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("Unhandled error: ", ex);
        return ResponseEntity.status(500).body("Internal server error");
    }
}

```
###   `ResourceNotFoundException`
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
###   `TuringOnlineForumSystem`
```java
package com.example.turingOnlineForumSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TuringOnlineForumSystem {

	public static void main ( String[] args ) {
		SpringApplication.run(TuringOnlineForumSystem.class , args);
	}
}
```
###   `PostRepository`
#### path : /src/main/resources/templates/chat.html
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Private Messaging</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.0/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        #chatBox, #notificationsList {
            border: 1px solid #ccc;
            height: 250px;
            overflow-y: auto;
            padding: 10px;
            margin-top: 10px;
            background-color: #f9f9f9;
        }

        input, button {
            margin: 5px;
            padding: 5px;
        }

        .message {
            margin: 4px 0;
        }
    </style>
</head>
<body>

<h2>Private Messaging</h2>

<!-- Message Form -->
<div>
    <label for="receiverId">Receiver ID:</label>
    <input id="receiverId" placeholder="Enter Receiver ID" required type="text"/>

    <input id="messageInput" placeholder="Type your message..." required type="text"/>
    <button onclick="sendMessage()">Send</button>
</div>

<!-- Chat Messages -->
<div>
    <h4>Messages</h4>
    <div id="chatBox"></div>
</div>

<!-- Notifications -->
<div>
    <h4>🔔 Notifications</h4>
    <ul id="notificationsList"></ul>
</div>

<script type="text/javascript">
    let stompClient = null;

    // ✅ Dynamic userId from Thymeleaf
    const userId = [[${userId}]];

    function connectWebSocket() {
        const socket = new SockJS('/chat');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            // ✅ Subscribe to messages
            stompClient.subscribe('/topic/messages/' + userId, (msgOutput) => {
                const msg = JSON.parse(msgOutput.body);
                showMessage(msg.sender.username, msg.content);
            });
        });
    }

    function sendMessage() {
        const receiverId = document.getElementById("receiverId").value.trim();
        const content = document.getElementById("messageInput").value.trim();

        if (!receiverId || !content) {
            alert("Receiver ID and message are required!");
            return;
        }

        stompClient.send("/app/chat.send", {}, JSON.stringify({
            senderId: userId,
            receiverId: receiverId,
            content: content
        }));

        showMessage("You", content);
        document.getElementById("messageInput").value = '';
    }

    function showMessage(sender, content) {
        const chatBox = document.getElementById("chatBox");
        const msgElem = document.createElement("p");
        msgElem.classList.add("message");
        msgElem.innerHTML = `<strong>${sender}:</strong> ${content}`;
        chatBox.appendChild(msgElem);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    function loadChatHistory(receiverId) {
        fetch(`/api/messages/history?senderId=${userId}&receiverId=${receiverId}`)
            .then(response => response.json())
            .then(messages => {
                const chatBox = document.getElementById("chatBox");
                chatBox.innerHTML = '';
                messages.forEach(msg => {
                    showMessage(msg.sender.username, msg.content);
                });
            });
    }

    function loadNotifications() {
        fetch(`/api/notifications/${userId}`)
            .then(response => response.json())
            .then(notifications => {
                const list = document.getElementById("notificationsList");
                list.innerHTML = '';
                notifications.forEach(n => {
                    const li = document.createElement("li");
                    li.textContent = `[${new Date(n.timestamp).toLocaleTimeString()}] ${n.message}`;
                    list.appendChild(li);
                });
            });
    }

    // Load chat when receiver is entered
    document.getElementById("receiverId").addEventListener("blur", function () {
        const receiverId = this.value.trim();
        if (receiverId) {
            loadChatHistory(receiverId);
        }
    });

    connectWebSocket();
    loadNotifications();
    setInterval(loadNotifications, 15000); // refresh notifications every 15s
</script>

</body>
</html>

```

###   `ModerationControllerIntegrationTest`
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
        moderationRepository.deleteAll(); // ✅ No more NPE
        messageRepository.deleteAll(); // <-- Add this line
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

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/moderation/post/" + post.getId())
                        .param("moderatorId", moderator.getId().toString())
                        .param("reason", "Violation of rules"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteThread() throws Exception {
        mockMvc.perform(delete("/api/moderation/thread/" + thread.getId())
                        .param("moderatorId", moderator.getId().toString())
                        .param("reason", "Duplicate content"))
                .andExpect(status().isOk());
    }

    @Test
    void testBanUser() throws Exception {
        mockMvc.perform(post("/api/moderation/ban-user/" + regularUser.getId())
                        .param("reason", "Spamming"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetModerationHistory() throws Exception {
        mockMvc.perform(get("/api/moderation/history/" + regularUser.getId()))
                .andExpect(status().isOk());
    }
}



```
###   `NotificationControllerIntegrationTest`
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

    @Test
    public void testGetUserNotifications() throws Exception {
        mockMvc.perform(get("/api/notifications/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test Notification"));
    }

    @Test
    public void testMarkNotificationAsRead() throws Exception {
        Notification notification = notificationRepository.findByRecipientId(user.getId()).get(0);
        mockMvc.perform(put("/api/notifications/read/" + notification.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNotification() throws Exception {
        Notification notification = notificationRepository.findByRecipientId(user.getId()).get(0);
        mockMvc.perform(delete("/api/notifications/" + notification.getId()))
                .andExpect(status().isOk());
    }
}
```
###   `PostControllerIntegrationTest`
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
###   `ThreadControllerIntegrationTest`
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
//@ActiveProfiles("test") // Optional: for test-specific configs
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
###   `UserControllerIntegrationTest`
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

    /**
     * Test user creation.
     */
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

    /**
     * Test fetching all users.
     */
    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test getting a user by ID.
     */
    @Test
    void testGetUserById() throws Exception {
        // First create a user
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

        // Then fetch the user by ID
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"));
    }

    /**
     * Test updating user profile.
     */
    @Test
    void testUpdateUser() throws Exception {
        // Create user first
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

        // Update request
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
###   `FollowServiceTest`
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

    @Test
    void testFollowUser_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepo.findById(2L)).thenReturn(Optional.of(following));

        followService.followUser(1L, 2L);

        verify(followRepo, times(1)).save(any(Follow.class));
    }

    @Test
    void testGetFollowing_ReturnsListOfUsers() {
        Follow follow = new Follow(1L, follower, following);
        when(followRepo.findByFollowerId(1L)).thenReturn(List.of(follow));

        List<User> result = followService.getFollowing(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testFollowUser_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> followService.followUser(1L, 2L));
    }
}

```
###   `MessagingServiceTest`
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

    @Test
    void testSendMessage_SenderNotFound() {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(99L);
        dto.setReceiverId(2L);
        dto.setContent("Hi");

        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messagingService.sendMessage(dto));
    }

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

    @Test
    void testGetChatHistory_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messagingService.getChatHistory(1L, 2L));
    }
}


```
###   `ModerationServiceTest`
```java
package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.dto.ModerationDTO;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.*;
import com.example.turingOnlineForumSystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModerationServiceTest {

    @Mock private ModerationRepository moderationRepository;
    @Mock private ThreadRepository threadRepository;
    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private ModerationService moderationService;

    private User user;
    private Threads thread;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).username("Alice").build();
        thread = Threads.builder().id(10L).title("Sample Thread").user(user).build();
        post = Post.builder().id(100L).content("Sample post").thread(thread).user(user).build();
    }

    @Test
    void testDeleteThread() {
        when(threadRepository.getReferenceById(10L)).thenReturn(thread);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(postRepository.findByThreadId(10L)).thenReturn(List.of(post));

        moderationService.deleteThread(10L, 99L, "Inappropriate content");

        verify(moderationRepository).save(any(Moderation.class));
        verify(postRepository).deleteAll(List.of(post));
        verify(threadRepository).deleteById(10L);
    }

    @Test
    void testDeletePost_Success() {
        when(postRepository.findById(100L)).thenReturn(Optional.of(post));

        moderationService.deletePost(100L, 88L, "Spam");

        verify(postRepository).deleteById(100L);
        verify(moderationRepository).save(any(Moderation.class));
    }

    @Test
    void testDeletePost_NotFound() {
        when(postRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                moderationService.deletePost(100L, 88L, "Spam"));
    }

    @Test
    void testBanUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        moderationService.banUser(1L, "Toxic behavior");

        assertTrue(user.getBanned());
        verify(userRepository).save(user);
        verify(moderationRepository).save(any(Moderation.class));
    }

    @Test
    void testBanUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                moderationService.banUser(1L, "Toxic behavior"));
    }

    @Test
    void testGetModerationHistory() {
        Moderation moderation = Moderation.builder()
                                          .id(1L)
                                          .action("DELETE_POST")
                                          .reason("Bad content")
                                          .createdAt(LocalDateTime.now())
                                          .user(user)
                                          .thread(thread)
                                          .build();

        when(moderationRepository.findByUserId(1L)).thenReturn(List.of(moderation));

        List<ModerationDTO> result = moderationService.getModerationHistory(1L);

        assertEquals(1, result.size());
        assertEquals("DELETE_POST", result.get(0).getAction());
        assertEquals(user.getId(), result.get(0).getUserId());
    }
}

```
###   `NotificationServiceTest`
```java
package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.NotificationRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock private NotificationRepository notificationRepo;
    @Mock private NotificationRepository notificationRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private NotificationService notificationService;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).username("testUser").build();
        notification = Notification.builder()
                                   .id(10L)
                                   .recipient(user)
                                   .message("Test Message")
                                   .isRead(false)
                                   .build();
    }

//    @Test
    void sendNotification_ByUserId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        notificationService.sendNotification(1L, "Hello!");

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void sendNotification_ByUserId_ThrowsIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                notificationService.sendNotification(1L, "Hello"));
    }

    //@Test
    void sendNotification_ByUserObject_Success() {
        notificationService.sendNotification(user, "Welcome");
        verify(notificationRepo).save(any(Notification.class));
    }

   // @Test
    void markAsRead_Success() {
        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(10L);

        assertTrue(notification.getIsRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void markAsRead_ThrowsIfNotFound() {
        when(notificationRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                notificationService.markAsRead(10L));
    }

    //@Test
    void deleteNotification_Success() {
        when(notificationRepository.existsById(10L)).thenReturn(true);

        notificationService.deleteNotification(10L);

        verify(notificationRepository).deleteById(10L);
    }

    @Test
    void deleteNotification_NotFound() {
        when(notificationRepository.existsById(10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                notificationService.deleteNotification(10L));
    }

    //@Test
    void getNotificationsForUser() {
        when(notificationRepository.findByRecipientId(1L)).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getNotificationsForUser(1L);

        assertEquals(1, result.size());
        assertEquals("Test Message", result.get(0).getMessage());
    }
}

```
###   `PostServiceTest`
```java
package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.dto.PostDto;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.PostRepository;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private ThreadRepository threadRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private PostService postService;

    private Threads thread;
    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).username("user1").build();
        thread = Threads.builder().id(1L).title("Thread Title").build();
        post = Post.builder().id(100L).content("Sample post").user(user).thread(thread).createdAt(LocalDateTime.now()).build();
    }

    @Test
    void testCreatePost_Success() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post inputPost = new Post();
        inputPost.setContent("Sample post");
        inputPost.setUser(user);

        Post saved = postService.createPost(inputPost, 1L);

        assertNotNull(saved);
        assertEquals("Sample post", saved.getContent());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testCreatePost_ThreadNotFound() {
        when(threadRepository.findById(99L)).thenReturn(Optional.empty());

        Post inputPost = new Post();
        inputPost.setUser(user);
        inputPost.setContent("X");

        assertThrows(ResourceNotFoundException.class,
                     () -> postService.createPost(inputPost, 99L));
    }

    @Test
    void testCreatePost_UserNotFound() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Post inputPost = new Post();
        inputPost.setContent("Hello");
        inputPost.setUser(User.builder().id(99L).build());

        assertThrows(ResourceNotFoundException.class,
                     () -> postService.createPost(inputPost, 1L));
    }

    @Test
    void testGetPostsByThread() {
        when(postRepository.findByThreadId(1L)).thenReturn(List.of(post));

        List<PostDto> dtos = postService.getPostsByThread(1L);

        assertEquals(1, dtos.size());
        assertEquals(post.getContent(), dtos.get(0).getContent());
    }

    @Test
    void testDeletePostsByThread() {
        when(postRepository.findByThreadId(1L)).thenReturn(List.of(post));

        postService.deletePostsByThread(1L);

        verify(postRepository).deleteAll(List.of(post));
    }
}

```
###   `ThreadServiceTest`
```java
package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ThreadServiceTest {

    @Mock
    private ThreadRepository threadRepository;

    @InjectMocks
    private ThreadService threadService;

    private Threads thread;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        thread = Threads.builder()
                        .id(1L)
                        .title("Sample Thread")
                        .content("Sample Content")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
    }

    @Test
    void testCreateThread_Success() {
        when(threadRepository.save(any(Threads.class))).thenReturn(thread);

        Threads result = threadService.createThread(new Threads());

        assertNotNull(result);
        assertEquals("Sample Thread", result.getTitle());
        verify(threadRepository).save(any(Threads.class));
    }

    @Test
    void testUpdateThread_Success() {
        Threads updated = Threads.builder()
                                 .title("Updated Title")
                                 .content("Updated Content")
                                 .build();

        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(threadRepository.save(any(Threads.class))).thenReturn(thread);

        Threads result = threadService.updateThread(1L, updated);

        assertNotNull(result);
        assertEquals(thread.getId(), result.getId());
        verify(threadRepository).save(any(Threads.class));
    }

    @Test
    void testUpdateThread_NotFound() {
        when(threadRepository.findById(99L)).thenReturn(Optional.empty());

        Threads updated = Threads.builder().title("T").content("C").build();

        assertThrows(ResourceNotFoundException.class, () -> {
            threadService.updateThread(99L, updated);
        });
    }

    @Test
    void testDeleteThread_Success() {
        when(threadRepository.existsById(1L)).thenReturn(true);
        doNothing().when(threadRepository).deleteById(1L);

        threadService.deleteThread(1L);

        verify(threadRepository).deleteById(1L);
    }

    @Test
    void testDeleteThread_NotFound() {
        when(threadRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            threadService.deleteThread(99L);
        });
    }

    @Test
    void testGetThread_Success() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));

        Threads result = threadService.getThread(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetThread_NotFound() {
        when(threadRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            threadService.getThread(999L);
        });
    }

    @Test
    void testGetAllThreads() {
        when(threadRepository.findAll()).thenReturn(List.of(thread));

        List<Threads> result = threadService.getAllThreads();

        assertEquals(1, result.size());
        verify(threadRepository).findAll();
    }
}

```
###   `UserServiceTest`
```java
package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()
                       .id(1L)
                       .username("testuser")
                       .email("test@example.com")
                       .build();
    }

    @Test
    void testGetUserById_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepo).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void testUpdateUserProfile_Success() {
        User updatedUser = User.builder().username("updated").email("updated@example.com").build();
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUserProfile(1L, updatedUser);

        assertNotNull(result);
        assertEquals("updated", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void testFindById() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testFindAll() {
        when(userRepo.findAll()).thenReturn(Arrays.asList(mockUser));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void testSaveUser() {
        when(userRepo.save(mockUser)).thenReturn(mockUser);

        User result = userService.save(mockUser);

        assertEquals("testuser", result.getUsername());
        verify(userRepo).save(mockUser);
    }
}

```
###   `PostServiceTest`
```java

```

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

Here's a detailed **Time and Space Complexity Analysis** tailored for your **Spring Boot Online Forum System** with discussion threads, messaging, moderation, notifications, and user interactions:

---

## ✅ 5. Time and Space Complexity Analysis

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

## 📌 Conclusion

The **Spring Boot Online Forum System** demonstrates efficient **constant to linear time complexity** across all core operations. The system:

- Handles user interaction at scale with optimized DB queries.
- Uses **lazy loading** and **pagination** where applicable to prevent memory bloat.
- Guarantees **data consistency** and **low latency** through minimal joins and eager moderation logging.
- Provides real-time communication and notification delivery with negligible overhead via **WebSocket** and **event-driven triggers**.

With good database indexing, caching (optional Redis), and async logging or messaging, this architecture ensures **scalability and performance** for high-traffic production environments.

---



