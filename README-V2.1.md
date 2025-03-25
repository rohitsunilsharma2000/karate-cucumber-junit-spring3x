# 6984:   Create a Spring Boot application that manages video hosting, transcoding, streaming endpoints, and user access control.

---



## 📌 Use Case
**This video hosting system is designed for content creators to upload and manage multimedia content while enabling seamless transcoding and streaming for end users.
It supports asynchronous processing, secure user management, and real-time video playback.
The system is ideal for community-driven platforms, educational portals, or entertainment services that require efficient video handling and delivery.**

## 📌 **Prompt Title**  
Spring Boot Video Hosting System with Transcoding, Streaming, and User Management

## 📋 **High-Level Description**  
Develop a robust video hosting application using Spring Boot that supports:
- User registration, authentication, and profile management
- Video upload with asynchronous transcoding via FFmpeg and comprehensive error handling
- Direct streaming of video content from a designated directory
- Detailed logging and exception management to ensure reliable operations
- Seamless integration for community-driven platforms, educational portals, or media sharing services

---

## 🧱 Functions / Classes to Be Created by LLM

1. **UserController.java**  
   *Purpose*: Handle user creation, retrieval, and profile management.
  - Method: **createUser(User)** – Creates a new user account (registration).
  - Method: **getUserByUsername(String)** – Retrieves user details by username.

2. **VideoController.java**  
   *Purpose*: Manage video upload, retrieval, and initiate transcoding processes.
  - Method: **getAllVideos()** – Returns a list of all uploaded videos.
  - Method: **uploadVideo(Video)** – Uploads a new video with an initial status ("PENDING").
  - Method: **transcodeVideo(Long, String)** – Triggers asynchronous transcoding for a video by ID and target format.

3. **StreamingController.java**  
   *Purpose*: Serve video content directly from a designated directory for streaming.
  - Method: **streamVideo(String filename)** – Streams a video file based on its filename with proper HTTP headers.

4. **TranscodingService.java**  
   *Purpose*: Handle asynchronous video transcoding using FFmpeg.
  - Method: **transcodeVideoAsync(Video, String)** – Asynchronously transcodes a video to the specified format, updating its status.

5. **UserService.java**  
   *Purpose*: Implement business logic for user management.
  - Method: **save(User)** – Saves a new user to the database.
  - Method: **findByUsername(String)** – Retrieves a user by username.

6. **VideoService.java**  
   *Purpose*: Implement business logic for video management.
  - Method: **getAllVideos()** – Returns a list of all videos.
  - Method: **saveVideo(Video)** – Saves a video to the database.
  - Method: **getVideoById(Long)** – Retrieves a video by its unique identifier.

7. **GlobalExceptionHandler.java**  
   *Purpose*: Provide centralized exception handling for the application.
  - Method: **handleHttpClientErrorException(HttpClientErrorException, WebRequest)** – Handles 4xx HTTP errors.
  - Method: **handleHttpServerErrorException(HttpServerErrorException, WebRequest)** – Handles 5xx HTTP errors.
  - Method: **handleAccessDeniedException(Exception, WebRequest)** – Handles security-related access denials.
  - Method: **handleResourceNotFoundException(ResourceNotFoundException, WebRequest)** – Handles scenarios when a resource is not found.
  - Method: **buildErrorResponse(String, HttpStatus, WebRequest)** – Helper method to construct a standardized error response.

8. **ResourceNotFoundException.java**  
   *Purpose*: Define a custom exception for scenarios when a resource is not found in the system.
  - Constructor: **ResourceNotFoundException(String message)** – Constructs the exception with a detailed error message.

---


📦 **Dependencies to Use for Above Application**

- **spring-boot-starter-web** – For building RESTful APIs and web applications.
- **spring-boot-starter-security** – For user authentication and authorization.
- **spring-boot-starter-validation** – For request validation using JSR 380 (Jakarta Bean Validation).
- **spring-boot-starter-data-jpa** – For database access using JPA.
- **mysql-connector-j** – MySQL JDBC driver for runtime database connectivity.
- **lombok** – For reducing boilerplate code (provided scope).
- **spring-boot-devtools** – For enhanced development experience with live reload and debugging tools.
- **jakarta.validation-api** – For Jakarta Bean Validation support.


---


## ✅ Testing the Whole Function

🧪 **Unit Tests**

- **Services:**
  - Mock database repositories (e.g., UserRepository, VideoRepository) using Mockito.
  - Test service methods in:
    - **UserService** – Verify CRUD operations and any business validations.
    - **VideoService** – Test video upload, retrieval, and fetching by ID.
    - **TranscodingService** – Simulate FFmpeg command execution, validate status updates ("PENDING", "TRANSCODING", "READY"/"FAILED"), and assert exception flows.
- **Controllers:**
  - Use MockMvc to simulate HTTP requests to REST controllers:
    - **UserController** – Validate status codes for registration and user retrieval.
    - **VideoController** – Test endpoints for video upload and asynchronous transcoding, ensuring proper JSON responses and status codes.
    - **StreamingController** – Simulate file streaming requests, verifying correct HTTP headers and media types.
- **Exception Handler:**
  - Test responses from GlobalExceptionHandler:
    - Ensure correct HTTP codes and error messages for ResourceNotFoundException, access issues, and general errors.

🔗 **Integration Tests**

- Use @SpringBootTest and @AutoConfigureMockMvc to wire up the entire Spring context.
- Set up an H2 in-memory database for isolated database interactions.
- Test end-to-end flows:
  - Create users and verify persistence.
  - Upload videos, trigger transcoding, and check status transitions.
  - Simulate video streaming requests to confirm file delivery.
  - Verify that error scenarios (e.g., video not found) return appropriate error responses.
- Test asynchronous processing by ensuring that transcoding operations complete and update the database as expected.

🚀 **Performance Testing (Optional)**

- Measure response times using tools like Apache JMeter or Postman Collection Runner.
- Simulate high-traffic scenarios with concurrent video uploads, transcoding requests, and streaming operations.
- Track latency, resource usage, and the impact of asynchronous processing.
- Evaluate system performance under load, including the throughput of real-time video streaming.
---
### 📘 **Plan for Video Hosting Application**

I will implement a complete video hosting system using Spring Boot, following clean architecture principles and a modular MVCS (Model-View-Controller-Service) design. The platform will support core functionalities including user management, video uploads, asynchronous transcoding, and direct video streaming.

I will create core modules such as:

- **UserService** and **UserController** – to handle user registration, authentication, and profile management.
- **VideoService** and **VideoController** – to manage video uploads, retrieval, and asynchronous transcoding initiation.
- **TranscodingService** – to perform asynchronous transcoding of videos using FFmpeg, updating video statuses based on processing outcomes.
- **StreamingController** – to serve video files directly from a designated directory, enabling real-time video playback.
- **GlobalExceptionHandler** – to centrally manage exceptions across the application, ensuring meaningful error responses.

The application will expose REST APIs for all operations and use asynchronous processing (via @Async) for transcoding tasks, ensuring responsive endpoints even during long-running processes. I will enforce input validation using Spring’s validation framework and document DTOs where applicable. Logging will be implemented with Slf4j, taking care not to log sensitive data.

Security will be enforced via Spring Security with HTTP Basic authentication for initial development, and CORS will be configured to support frontend integrations. The codebase will follow clean coding standards with comprehensive inline documentation for easy maintainability and scalability.

I will write unit tests using JUnit 5 and Mockito for all services and repositories, and integration tests using @SpringBootTest + @AutoConfigureMockMvc to simulate end-to-end flows, including user registration, video upload, transcoding, and streaming. Optional performance testing will be considered using tools like Apache JMeter to ensure the system handles high load and concurrent requests efficiently.

## Unit tests *


### 🧪 **1. ChatViewControllerTest**: `src/test/java/com/example/turingOnlineForumSystem/controller/ChatViewControllerTest.java`

```java
```

---


### Iteration number 2(for better coverage)


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


🔹 **1. Create a User**
```bash
curl --location --request POST 'http://localhost:8080/api/users/register' \
--header 'Content-Type: application/json' \
--data-raw '{
  "username": "alice",
  "password": "secret",
  "role": "USER"
}'
```

🔹 **2. Upload a Video**
```bash
curl --location --request POST 'http://localhost:8080/api/videos' \
--header 'Content-Type: application/json' \
--data-raw '{
  "title": "Sample Video",
  "description": "A sample video upload.",
  "filePath": "/path/to/sample.mp4",
  "status": "PENDING"
}'
```

🔹 **3. Trigger Video Transcoding**
```bash
curl --location --request POST 'http://localhost:8080/api/videos/transcode/1?format=mp4'
```

🔹 **4. Get All Videos**
```bash
curl --location --request GET 'http://localhost:8080/api/videos'
```

🔹 **5. Stream a Video**  
Replace `sample.mp4` with the actual file name stored in your video directory.
```bash
curl --location --request GET 'http://localhost:8080/api/stream/sample.mp4' --output streamed_sample.mp4
```

---



###  Time and Space Complexity Analysis

---
Below is a high-level analysis of time and space complexity for the key API endpoints in the application:

---

**User API Endpoints (UserController)**

- **Create/Register User:**
  - **Time Complexity:** O(1)  
    The operation involves creating a new user record and saving it to the database (assuming average-case constant time for the database insert).
  - **Space Complexity:** O(1)  
    Only the new user record is stored temporarily during the operation.

---

**Video API Endpoints (VideoController)**

- **Get All Videos:**
  - **Time Complexity:** O(n)  
    Retrieving all videos requires scanning the database table, where _n_ is the number of video records.
  - **Space Complexity:** O(n)  
    The response contains all video records, so memory usage scales with _n_.

- **Upload Video:**
  - **Time Complexity:** O(1)  
    Saving a new video record generally executes in constant time with respect to the number of videos (assuming the database insert is efficient).
  - **Space Complexity:** O(1)  
    Only one video record is processed and stored.

- **Trigger Video Transcoding (Asynchronous):**
  - **Time Complexity:** O(1) for initiating the asynchronous process.  
    The immediate API call only updates the status and triggers a background job; the actual transcoding process depends on the file size and FFmpeg processing time, which is external to the API call.
  - **Space Complexity:** O(1)  
    The API only handles status updates; any intermediate memory usage during transcoding is managed by the FFmpeg process and is independent of the API.

---

**Streaming API Endpoints (StreamingController)**

- **Stream Video:**
  - **Time Complexity:** O(fileSize) if the entire file is read into memory; however, in practical streaming implementations the file is typically streamed in chunks, reducing the effective per-request overhead.
  - **Space Complexity:** O(1) to O(chunkSize) per request  
    With chunked streaming, only a small portion (or a fixed chunk) of the video is kept in memory at any time rather than loading the full file.

---



---

## 📊 Code Coverage Reports

- ✅ 1st Iteration: [Coverage Screenshot](https://drive.google.com/file/d/13MWnsCL0vlw0rJE0hk-3QoZIbeBy_f3c/view?usp=drive_link)
- ✅ 2nd Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1OAdCQviFS3kqaEiYiVLRZz7ssyCpW3hO/view?usp=drive_link)

---

## 📦 Download Code

[👉 Click to Download the Project Source Code](https://drive.google.com/file/d/18x1FymwcoO10PyiZMMKr_-PO1GdYMXbC/view?usp=drive_link)

---

🧠 **Conclusion**

The Video Hosting Application provides a robust, scalable solution for managing video content, featuring secure user management, seamless video uploads, and efficient transcoding and streaming capabilities. With a modular MVCS architecture and asynchronous processing for resource-intensive tasks, the system ensures high responsiveness and maintainability. Comprehensive error handling and logging further enhance reliability, making the application well-suited for community-driven platforms, educational portals, or media sharing services.

