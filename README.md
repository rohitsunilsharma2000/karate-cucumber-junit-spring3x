# 1111 : Build a Spring Boot microservice Template
---

## 📌 Use Case

**This Spring Boot microservice handles support tickets, live chat, and email follow-ups. It provides an efficient solution for managing
customer support requests, enabling real-time communication between users and support agents, while automating follow-up processes to ensure
timely responses.**

## 📌 Prompt Title

**Spring Boot Microservice for Support Tickets, Live Chat, and Email Follow-Ups**

## 📋 High-Level Description

Develop a Spring Boot microservice that manages support tickets, live chat, and automated email follow-ups. The service will handle:

- User and agent authentication with role-based access (Admin, Agent, Customer)
- Ticket creation, status updates, and attachment management
- Live chat functionality with real-time messaging via WebSocket
- Automated email notifications for ticket updates, new assignments, and reminders
- Integration of role-based permissions for agents and admins to manage tickets
- Real-time notifications for users and agents about ticket updates or new messages
- Efficient handling of ticket priority and escalation
- Comprehensive exception handling and logging for auditing purposes
- Scalability to handle increasing numbers of support tickets and user interactions



---

## 🧱 Functions / Classes to Be Created by LLM

---

### 1. `TicketController.java`

**Purpose**: Manage support ticket creation, retrieval, updates, and ticket assignment.

| Method                          | Description                                       |
|---------------------------------|---------------------------------------------------|
| `createTicket(TicketRequest)`   | Creates a new support ticket                     |
| `getTicketById(Long id)`        | Fetches a ticket's details by its ID             |
| `updateTicket(Long id, TicketRequest)` | Updates an existing support ticket            |
| `assignTicketToAgent(Long ticketId, Long agentId)` | Assigns a ticket to an agent               |
| `autoAssignTicket(Long ticketId)` | Auto-assigns a ticket to an available agent     |
| `listAllTickets()`              | Returns a list of all tickets                    |

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

- Mock database repositories (e.g., `TicketRepository`, `MessageRepository`, etc.) using **Mockito** to isolate the service layer.
- Test service methods in:
  - `TicketService` – Test CRUD operations for tickets, auto-assign functionality, and validation for updates and assignments.
  - `ChatService` – Test saving chat messages and retrieving chat history.
  - `EmailService` – Test email notifications for ticket updates, assignments, and creation.
  - `JwtService` – Validate JWT token generation, validation, and username extraction.
  - `UserService` – Test user creation, updating, and profile retrieval.
  - `ModerationService` – Test moderation logic like banning a user or deleting posts.

- Simulate and assert expected outcomes and exception flows (e.g., `TicketNotFoundException`, `ResourceNotFoundException`, etc.).

#### Controllers:

- Use **MockMvc** to simulate HTTP requests to REST controllers:
  - Validate status codes (`200 OK`, `404 Not Found`, `400 Bad Request`, etc.).
  - Check input validation (e.g., required fields, invalid types).
  - Ensure proper payloads (e.g., JSON structure, missing attributes).
  - Test methods in:
    - `TicketController` – Ensure tickets are created, updated, and assigned properly.
    - `ChatController` – Test message sending and retrieval of chat history.
    - `UserController` – Test user profile creation and updates.
    - `ModerationController` – Ensure post deletion and user bans are handled correctly.

#### Exception Handler:

- Verify responses from `GlobalExceptionHandler`:
  - Ensure correct HTTP codes and error messages for:
    - `ResourceNotFoundException` – Return HTTP `404 Not Found` and appropriate error messages.
    - `AccessDeniedException` – Return HTTP `403 Forbidden` and appropriate error message.
    - `TicketNotFoundException` – Return `404 Not Found` with the error message indicating no ticket found.
    - Unexpected errors – Return HTTP `500 Internal Server Error` for unhandled exceptions.

---

### 🔗 Integration Tests

- Use **@SpringBootTest + @AutoConfigureMockMvc** to wire up the entire Spring context for integration testing.
- Set up **H2 in-memory DB** for isolated and repeatable tests.
- Test end-to-end flows:
  - Create users, tickets, and messages using the REST API.
  - Test ticket CRUD operations, including status changes and assigning tickets to agents.
  - Test private messaging functionality with WebSocket, ensuring that messages are sent and received in real-time.
  - Fetch and validate notifications on ticket updates or new messages.
  - Test user profile creation, editing, and fetching.
  - Simulate ticket assignment to different agents and validate email notifications sent.

---

### 🚀 Performance Testing (Optional)

- Measure response times using **Apache JMeter** or **Postman Collection Runner**.
- Simulate high-traffic scenarios with concurrent users performing actions such as:
  - Creating tickets and messages.
  - Fetching ticket details and chat history.
  - Sending private messages through WebSocket.
  - Handling multiple user interactions simultaneously.
- Track system performance under load:
  - Evaluate response times for ticket creation, message sending, and ticket updates.
  - Analyze WebSocket message throughput and latency.
  - Test for database query optimization using caching mechanisms for popular tickets and messages.

---

## 📘 Plan

I will implement a **complete support service platform** using **Spring Boot** with core features including **ticket management**, **real-time chat**, **email follow-ups**, and **moderation tools**. This microservice will help organizations handle customer support tickets, communicate with users via live chat, and send email notifications for follow-ups.

I will create core modules such as:

- `TicketService`, `ChatService`, `UserService` – to handle CRUD operations for tickets, users, and chat messages.
- `ModerationService` – to support ticket closures, deletion, and user bans with audit logging.
- `EmailService` – for sending email notifications related to ticket updates, assignments, and reminders.
- `NotificationService` – to alert users about ticket changes or new messages.

The application will expose REST APIs for interacting with all modules (e.g., ticket creation, chat management) and a WebSocket endpoint (`/chat`) for real-time private messaging.

I will use **Thymeleaf** to build a lightweight UI for features like live chat, notifications, and user profile management. The UI will also support dynamic real-time updates using WebSocket.

I will enforce **input validation** using `@Valid` and DTOs where applicable, ensuring that only valid data is processed. A centralized `GlobalExceptionHandler` will handle all exceptions, providing meaningful messages and appropriate HTTP status codes for various error scenarios. Logging will be implemented using **Slf4j**, ensuring that sensitive user data is not logged.

Security will be enforced via **Spring Security**, supporting **JWT-based login/registration**, **role-based access control**, and **CORS configuration**. Sensitive endpoints like ticket assignment, ticket closure, and user moderation will be restricted to authorized roles (Admin, Agent).

I will write **unit tests** using **JUnit 5 + Mockito** for all services and repositories to ensure business logic correctness. **Integration tests** will be developed using **Spring Boot Test + MockMvc**, simulating end-to-end flows like ticket creation, messaging, email notifications, and moderation actions.

The codebase will follow clean architecture principles, maintaining a modular structure and ensuring the application is easily maintainable and scalable. All methods and classes will be documented for future readability and extension.

---

## Folder Structure

```plaintext
support-ticket-livechat-email\src
|-- main
|   |-- java
                            

```

---


## 🔐 **1. SecurityConfig** : `src\main\java\com\example\supportservice\config\SecurityConfig.java`

```java
```

## Unit tests



**Result In Iteration number 1** :-

| Coverage Type | Percentage | Details  |
|---------------|------------|----------|
| Class         | 100%       | 11/11    |
| Method        | 85%        | 28/28    |
| Line          | 93%        | 93/95    |

## Iteration number 2(for better coverage)

**Result In Iteration number 2** :-

| Coverage Type | Percentage | Details  |
|---------------|------------|----------|
| Class         | 100%       | 11/11    |
| Method        | 85%        | 28/28    |
| Line          | 93%        | 93/95    |




**In Iteration number 2( Result:** Total line coverage is 91%
## ⚙️ Features

- **REST APIs** for ticket management, user management, chat, notifications, and email follow-ups
- **JWT Authentication** for secure login, registration, and role-based access control (Admin, Agent, Customer)
- **Docker Compose** support for easy deployment with a MySQL database
- **Real-time messaging** using **WebSocket (SockJS + STOMP)** for private chat between users
- **Thymeleaf-based UI** for chat, notifications, and user profiles
- **Ticket CRUD operations** – create, view, update, and delete support tickets
- **Email notifications** for ticket updates, new messages, ticket assignment, and reminders
- **Ticket assignment logic** – manual and auto-assignment of tickets to agents
- **Global exception handling** with meaningful error messages and HTTP status codes
- **Role-based authorization** using `@PreAuthorize` for controlling access to endpoints based on user roles
- **Real-time ticket and message status updates** for better user engagement
- **Input validation** using `@Valid` and manual validation checks for clean data processing
- **Modular layered architecture** with Controller, Service, and Repository layers for clean code separation
- **Logging** throughout controllers and services using `@Slf4j`
- **Custom exception classes** such as `TicketNotFoundException`, `ResourceNotFoundException` for specific error handling
- **Unit and Integration Tests** with MockMvc, Mockito, and JUnit for testing various functionalities and ensuring code quality
- **Dynamic WebSocket routing** for user-specific subscriptions in the chat system
- **H2/Embedded DB** for testing and in-memory persistence during development and CI/CD processes
- **CORS support** and security configuration via `SecurityConfig` to allow cross-origin requests
- **Builder patterns and Lombok** for reducing boilerplate code in entities and DTOs
- **Comprehensive email sending support** via configured SMTP servers like Gmail, SendGrid, or Mailgun
- **Threaded conversation support** for ticket discussions and real-time chat messages
- **High-level modularity** with clean separation of concerns (Services, Controllers, Repositories)
- **Extensive logging** for debugging and tracking issues with detailed logs for operations
- **Unit and Integration tests** with over 90% test coverage for service and controller layers to ensure robustness


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
    <name>Support tickets, live chat, and email follow-ups</name>
    <description>
      Build a Spring Boot microservice that handles support tickets, live chat, and email follow-ups.
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


