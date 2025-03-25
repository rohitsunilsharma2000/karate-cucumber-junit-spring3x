# 5910 : Build a Spring Boot microservice that handles support tickets, live chat, and email follow-ups
---
## 📌 Use Case

This Spring Boot application manages video hosting with an emphasis on efficient video processing. It supports user registration and access control, video uploads, asynchronous transcoding using FFmpeg, and real-time streaming of video content. The system ensures secure, scalable, and robust management of multimedia assets.

---

## 📌 Prompt Title

**Spring Boot Video Hosting System with Transcoding, Streaming, and User Access Control**

---

## 📋 High-Level Description

Develop a Spring Boot application that manages video hosting with the following features:

- **User Management:**  
  User registration, authentication, and role-based access control ensure that only authorized users can upload and manage videos.

- **Video Upload & Management:**  
  Allow users to upload video files which are then managed by the system with statuses such as "PENDING", "TRANSCODING", and "READY".

- **Asynchronous Transcoding:**  
  Utilize FFmpeg to transcode videos to various formats asynchronously, ensuring that resource-intensive processing does not block user interactions.

- **Real-Time Streaming:**  
  Provide endpoints for streaming video content directly from a designated directory with proper HTTP headers for inline playback.

- **Robust Security & Exception Handling:**  
  Implement comprehensive logging, exception handling via a centralized GlobalExceptionHandler, and secure endpoints with Spring Security.

- **RESTful APIs:**  
  Expose REST APIs to allow integration with other systems and front-end clients, ensuring modularity and scalability.

---

## 🧱 Functions / Classes to Be Created by LLM

1. **UserController.java**  
   *Purpose*: Handle user registration, retrieval, and profile management.
  - **createUser(User)** – Registers a new user.
  - **getUserByUsername(String)** – Retrieves user details based on username.

2. **VideoController.java**  
   *Purpose*: Manage video uploads, retrievals, and initiate transcoding processes.
  - **getAllVideos()** – Returns a list of all uploaded videos.
  - **uploadVideo(Video)** – Uploads a new video, setting an initial status ("PENDING").
  - **transcodeVideo(Long id, String format)** – Triggers asynchronous transcoding for a specific video by its ID and target format.

3. **StreamingController.java**  
   *Purpose*: Serve video content directly from a designated directory for streaming.
  - **streamVideo(String filename)** – Streams a video file based on its filename with proper HTTP headers (e.g., Content-Disposition, Content-Type).

4. **TranscodingService.java**  
   *Purpose*: Handle asynchronous video transcoding using FFmpeg.
  - **transcodeVideoAsync(Video, String)** – Asynchronously transcodes a video to the specified format and updates its status accordingly.

5. **UserService.java**  
   *Purpose*: Implement business logic for user management.
  - **save(User)** – Saves a new user to the database.
  - **findByUsername(String)** – Retrieves a user by username.

6. **VideoService.java**  
   *Purpose*: Implement business logic for video management.
  - **getAllVideos()** – Retrieves a list of all video records.
  - **saveVideo(Video)** – Saves a video record to the database.
  - **getVideoById(Long)** – Retrieves a video by its unique identifier.

7. **GlobalExceptionHandler.java**  
   *Purpose*: Provide centralized exception handling for the application.
  - **handleHttpClientErrorException(HttpClientErrorException, WebRequest)** – Handles 4xx HTTP errors.
  - **handleHttpServerErrorException(HttpServerErrorException, WebRequest)** – Handles 5xx HTTP errors.
  - **handleAccessDeniedException(Exception, WebRequest)** – Handles security-related access denials (HTTP 403).
  - **handleResourceNotFoundException(ResourceNotFoundException, WebRequest)** – Handles scenarios when a resource is not found (HTTP 404).
  - **buildErrorResponse(String, HttpStatus, WebRequest)** – Helper method to construct a standardized error response.

8. **ResourceNotFoundException.java**  
   *Purpose*: Define a custom exception for scenarios when a requested resource is not found in the system.
  - **ResourceNotFoundException(String message)** – Constructs the exception with a detailed error message.

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
Yes, you'll need to install FFmpeg on your Mac since it isn't included by default. A common way to install it is using Homebrew. You can install Homebrew if you haven't already, then run:

```bash
brew install ffmpeg
```

After installation, verify it’s available by running:

```bash
ffmpeg -version
```
## ✅ Testing the Whole Function

### 🧪 Unit Tests

#### Services:
- **Mock database repositories** (e.g., `UserRepository`, `VideoRepository`) using **Mockito** to isolate the service layer.
- Test service methods in:
  - **UserService** – Verify CRUD operations for users (e.g., user registration, retrieval by username).
  - **VideoService** – Test video upload, retrieval of all videos, and fetching video details by ID.
  - **TranscodingService** – Validate asynchronous transcoding logic, ensuring that video statuses change from "PENDING" to "TRANSCODING" and finally to either "READY" or "FAILED" based on the FFmpeg process outcome.
- Simulate and assert expected outcomes and exception flows (e.g., `ResourceNotFoundException` when a video is not found).

#### Controllers:
- Use **MockMvc** to simulate HTTP requests to REST controllers.
- Validate HTTP status codes (`200 OK`, `202 Accepted`, `404 Not Found`, etc.).
- Check input validation (e.g., required fields, invalid types).
- Ensure proper JSON payloads (e.g., auto-generated IDs, status values).
- Test methods in:
  - **UserController** – Ensure that user registration endpoints work correctly.
  - **VideoController** – Test endpoints for uploading videos and triggering asynchronous transcoding.
  - **StreamingController** – Validate that streaming endpoints return proper HTTP headers and media types for video playback.

#### Exception Handler:
- Verify responses from **GlobalExceptionHandler**:
  - Ensure that `ResourceNotFoundException` returns HTTP `404 Not Found` with an appropriate error message.
  - Ensure that security-related exceptions (e.g., `AccessDeniedException`) return HTTP `403 Forbidden`.
  - Ensure that unexpected errors (e.g., `HttpServerErrorException`) return HTTP `500 Internal Server Error`.

---

### 🔗 Integration Tests

- Use **@SpringBootTest + @AutoConfigureMockMvc** to wire up the entire Spring context.
- Set up an **H2 in-memory DB** for isolated, repeatable tests.
- Test end-to-end flows:
  - **User Registration**: Create users via the REST API and verify that they are persisted.
  - **Video Upload and Management**: Upload videos, check that their initial status is "PENDING", and then trigger transcoding.
  - **Video Transcoding**: Simulate the asynchronous transcoding process and verify status updates.
  - **Video Streaming**: Request video streaming endpoints and assert that the correct media is delivered with proper headers.
  - **Error Handling**: Simulate error scenarios (e.g., missing video file) and verify that the GlobalExceptionHandler returns appropriate error messages and status codes.

---

### 🚀 Performance Testing (Optional)

- **Response Time Measurement**: Use **Apache JMeter** or **Postman Collection Runner** to measure the response times of the video upload, transcoding initiation, and streaming endpoints.
- **Concurrent Load Simulation**: Simulate high-traffic scenarios where multiple users upload videos, trigger transcoding, and stream content concurrently.
- **System Resource Monitoring**: Evaluate CPU, memory, and network usage during high load to ensure the system scales efficiently.
- **WebSocket Throughput** (if applicable): Test real-time communication if integrated with WebSocket for user notifications.

---

## 📘 Plan

I will implement a complete video hosting system using Spring Boot, focusing on efficient multimedia management. The core features include:

- **User Management**:  
  Enable secure user registration, authentication, and role-based access control to ensure that only authorized users can manage and view videos.

- **Video Upload & Management**:  
  Allow users to upload video files which are stored with an initial "PENDING" status. A REST API will facilitate video management operations such as viewing all videos and retrieving individual video details.

- **Asynchronous Transcoding**:  
  Utilize FFmpeg to transcode videos to various formats asynchronously. The transcoding service will update the video status to "TRANSCODING" during processing and finally mark the video as "READY" or "FAILED" based on the outcome.

- **Real-Time Streaming**:  
  Provide an endpoint to stream video content directly from a designated directory. The streaming controller will serve video files with appropriate HTTP headers, enabling smooth playback in browsers.

- **Security and Exception Handling**:  
  Implement Spring Security to secure all endpoints, ensuring that sensitive operations are accessible only to authenticated users with appropriate roles. A centralized GlobalExceptionHandler will handle exceptions and provide meaningful error responses.

- **Testing and Quality Assurance**:  
  Write extensive unit tests using JUnit 5 and Mockito for services, controllers, and exception handlers to ensure business logic correctness. Develop integration tests using Spring Boot Test and MockMvc to simulate end-to-end user interactions. Optionally, conduct performance tests to validate system scalability under load.

By following this plan and maintaining a modular architecture, the application will be robust, scalable, and maintainable, capable of handling high volumes of video content and user interactions efficiently.

## Folder Structure

```plaintext
video-hosting\src
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- videohosting
|   |               |-- VideoHostingApplication.java
|   |               |-- config
|   |               |   `-- SecurityConfig.java
|   |               |-- controller
|   |               |   |-- StreamingController.java
|   |               |   |-- UserController.java
|   |               |   `-- VideoController.java
|   |               |-- exception
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   `-- ResourceNotFoundException.java
|   |               |-- model
|   |               |   |-- User.java
|   |               |   `-- Video.java
|   |               |-- repository
|   |               |   |-- UserRepository.java
|   |               |   `-- VideoRepository.java
|   |               `-- service
|   |                   |-- TranscodingService.java
|   |                   |-- UserService.java
|   |                   `-- VideoService.java
|   `-- resources
`-- test
    `-- java
        `-- com
            `-- example
                `-- videohosting
                    |-- VideoHostingApplicationTest.java
                    |-- config
                    |   `-- SecurityConfigTest.java
                    |-- controller
                    |   |-- StreamingControllerMockTest.java
                    |   |-- UserControllerTest.java
                    |   `-- VideoControllerTest.java
                    |-- exception
                    |   `-- GlobalExceptionHandlerDirectTest.java
                    |-- model
                    |   |-- UserEntityTest.java
                    |   `-- VideoEntityTest.java
                    `-- service
                        |-- TranscodingServiceTest.java
                        |-- UserServiceTest.java
                        `-- VideoServiceTest.java


                            

```

---


## 🔐 **1. SecurityConfig** : `src/main/java/com/example/videohosting/config/SecurityConfig.java`

```java
package com.example.videohosting.config;

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
 * SecurityConfig class sets up the security configuration for the Video Hosting Application.
 *
 * <p>
 * This configuration class performs the following tasks:
 * <ul>
 *   <li>Enables web security using {@code @EnableWebSecurity}.</li>
 *   <li>Configures CORS (Cross-Origin Resource Sharing) to allow requests from specific origins.</li>
 *   <li>Disables CSRF protection for simplicity during development (note: consider enabling CSRF in production environments).</li>
 *   <li>Configures URL patterns that are permitted to all users (e.g., static resources, REST APIs, and WebSocket endpoints).</li>
 *   <li>Sets up HTTP Basic authentication for testing purposes.</li>
 *   <li>Provides a custom {@link UserDetailsService} that uses an in-memory user store with a default user.</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class ensures that the application has a secure foundation while allowing flexibility during development.
 * </p>
 *
 * File: src/main/java/com/example/videohosting/config/SecurityConfig.java
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     *
     * <p>
     * This method defines the following security configuration:
     * <ul>
     *   <li>Enables CORS and disables CSRF protection.</li>
     *   <li>Allows all requests to a set of predefined URL patterns including:
     *       <ul>
     *         <li>All paths ("/**")</li>
     *         <li>WebSocket endpoints ("/chat/**", "/ws/**", "/topic/**", "/app/**")</li>
     *         <li>REST APIs ("/api/**")</li>
     *         <li>Static resources ("/webjars/**", "/js/**", "/css/**", "/images/**")</li>
     *       </ul>
     *   </li>
     *   <li>Any other requests are permitted as a catch-all.</li>
     *   <li>Enables HTTP Basic authentication, which is useful for testing and debugging.</li>
     * </ul>
     * </p>
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors()  // Enable CORS
            .and()
            .csrf().disable()  // Disable CSRF protection
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(
                    "/**",                  // allow all paths (WebSocket + REST)
                    "/chat/**",             // STOMP WebSocket endpoint
                    "/ws/**",               // optional WS prefix if used
                    "/topic/**",            // message broker destinations
                    "/app/**",              // application prefix for sending messages
                    "/api/**",              // REST APIs
                    "/webjars/**",          // static resources (e.g. SockJS client)
                    "/js/**", "/css/**", "/images/**"  // static assets if using Thymeleaf
                ).permitAll()
                .anyRequest().permitAll() // catch-all: permit all other requests
            )
            .httpBasic();  // Use HTTP Basic authentication for testing

        return http.build();
    }

    /**
     * Configures the CORS settings for the application.
     *
     * <p>
     * This method sets up a {@link CorsConfigurationSource} that defines:
     * <ul>
     *   <li>Allowed origins: "http://localhost:3000" and "http://127.0.0.1:3000" (suitable for local development)</li>
     *   <li>Allowed HTTP methods: GET, POST, PUT, DELETE, and OPTIONS</li>
     *   <li>Allowed headers: all headers are allowed ("*")</li>
     *   <li>Allows credentials (cookies, authorization headers, etc.)</li>
     *   <li>Preflight cache duration set to 3600 seconds (1 hour)</li>
     * </ul>
     * </p>
     *
     * @return the CorsConfigurationSource instance with the specified settings
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allowed origins (adjust for production as needed)
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        // Allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allowed headers
        configuration.setAllowedHeaders(List.of("*"));
        // Allow credentials (cookies, authorization headers, etc.)
        configuration.setAllowCredentials(true);
        // Preflight cache duration (in seconds)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply the CORS configuration to all endpoints
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Provides a custom UserDetailsService bean.
     *
     * <p>
     * This method creates an in-memory user store with a single default user:
     * <ul>
     *   <li>Username: "user"</li>
     *   <li>Password: "password" (using the {noop} prefix, which means no password encoding is applied)</li>
     *   <li>Role: "USER"</li>
     * </ul>
     * This is intended for development and testing purposes. In production, a more robust user management system should be used.
     * </p>
     *
     * @return an instance of InMemoryUserDetailsManager containing the default user
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}password") // {noop} indicates that no encoding is applied to the password
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

```
## 🔐 **2. StreamingController** : `src/main/java/com/example/videohosting/controller/StreamingController.java`

```java

```
## 🔐 **3. SecurityConfig** : `src/main/java/com/example/videohosting/controller/StreamingController.java`

```java
```
## 🔐 **4. SecurityConfig** : `src/main/java/com/example/videohosting/controller/StreamingController.java`

```java
```
## 🔐 **5. SecurityConfig** : ``

```java
```
## 🔐 **6. SecurityConfig** : ``

```java
```
## 🔐 **7. SecurityConfig** : ``

```java
```
## 🔐 **8. SecurityConfig** : ``

```java
```
## 🔐 **9. SecurityConfig** : ``

```java
```
## 🔐 **10. SecurityConfig** : ``

```java
```
## 🔐 **11. SecurityConfig** : ``

```java
```
## 🔐 **12. SecurityConfig** : ``

```java
```
## 🔐 **13. SecurityConfig** : ``

```java
```
## 🔐 **14. SecurityConfig** : ``

```java
```

## Unit tests

## 🔐 **1. SecurityConfig** : `src/test/java/com/example/videohosting/config/SecurityConfig.java`

```java
```
## 🔐 **2. SecurityConfig** : ``

```java
```
## 🔐 **3. SecurityConfig** : ``

```java
```
## 🔐 **4. SecurityConfig** : ``

```java
```
## 🔐 **5. SecurityConfig** : ``

```java
```
## 🔐 **6. SecurityConfig** : ``

```java
```
## 🔐 **7. SecurityConfig** : ``

```java
```
## 🔐 **8. SecurityConfig** : ``

```java
```

## Iteration number 2(for better coverage)


## 🔐 **9. SecurityConfig** : ``

```java
```
## 🔐 **10. SecurityConfig** : ``

```java
```
## 🔐 **11. SecurityConfig** : ``

```java
```
## 🔐 **12. SecurityConfig** : ``

```java
```
## 🔐 **13. SecurityConfig** : ``

```java
```
## 🔐 **14. SecurityConfig** : ``

```java
```

## ⚙️ Features

- **REST APIs** for user management, video upload, transcoding, and streaming endpoints
- **JWT Authentication** for secure login, registration, and role-based access control
- **Docker Compose** support for containerized deployment along with a MySQL database
- **Asynchronous video transcoding** using FFmpeg to process video uploads efficiently
- **Real-time video streaming** with proper HTTP headers for inline playback
- **Video CRUD operations** – upload, view, update, and delete video records
- **Global exception handling** with meaningful error messages and HTTP status codes
- **Role-based authorization** using Spring Security annotations to secure sensitive endpoints
- **Input validation** using `@Valid` and custom DTOs for robust data processing
- **Modular layered architecture** with Controller, Service, and Repository layers ensuring clean separation of concerns
- **Extensive logging** throughout the application using Slf4j for effective debugging and monitoring
- **Custom exception classes** such as `ResourceNotFoundException` for specific error scenarios
- **Unit and Integration tests** with MockMvc, Mockito, and JUnit to ensure over 90% test coverage
- **High-level modularity and scalability** designed to accommodate increasing video content and user interactions
- **Comprehensive documentation** and inline comments for ease of maintenance and future enhancements

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
    <name>video hosting, transcoding, streaming app</name>
    <description>
       Spring Boot application that manages video hosting, transcoding, streaming endpoints, and user access control.
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

### 🔹 1. Register a User

```bash
curl --location 'http://localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
  "username": "admin_user",
  "email": "admin@example.com",
  "password": "admin123",
  "role": "ADMIN"
}'
```

---

---

###  ⏱ Time and Space Complexity Analysis 🧠


---


### 🔹 1. Register User

- **Time Complexity:** `O(1)` – inserts a user into the DB.
- **Space Complexity:** `O(1)` – uses constant memory.

---

## 📊 Code Coverage Reports

- ✅ 1st
  Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1l95Eoz2u6XCmRoQtE0nMBid1GAndU6T8/view?usp=drive_link)
- ✅ 2nd
  Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1swsVQzhJXqczKt8ljiBPF5z5LcL_AV-v/view?usp=drive_link)

---

## 📦 Download Code

[👉 Click to Download the Project Source Code](https://drive.google.com/file/d/1V_ggn0gIMbjGr2tudKLUfNrC2GjLvnFi/view?usp=drive_link)


---

## 🧠 Conclusion

The **Spring Boot Support Ticket System** efficiently manages user registration, ticketing, real-time messaging, and email notifications with **constant to linear time complexity** across most endpoints. The system:

✅ **Supports scalable user interaction** with optimized indexed database queries and role-based access control.  
✅ **Ensures data integrity and low latency**, leveraging lazy loading and transactional consistency across ticket and chat workflows.  
✅ **Delivers real-time communication and notifications** with minimal overhead using **WebSocket + STOMP**, and asynchronous event-driven logic for responsiveness.  
✅ **Handles file uploads and large datasets** efficiently through streaming and controlled attachment handling.  
✅ **Provides maintainability and testability**, with modularized services, proper logging, exception handling, and JWT-based security.

With proper **database indexing**, optional **caching (e.g., Redis)**, **async email/event processing**, and a robust **microservice-friendly design**, the application architecture ensures **high performance and scalability** for production-ready, enterprise-level use cases. 🚀

---


