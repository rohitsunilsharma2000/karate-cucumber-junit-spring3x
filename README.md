7088 : Write a Spring Boot @Service class named NotificationService that integrates multiple notification channels (email, SMS, push).
---
---

## 📌 Use Case

**This Spring Boot microservice integrates multiple notification channels (email, SMS, push) into a unified service. It enables the application to send notifications via different channels seamlessly, ensuring that users receive timely updates regardless of the channel chosen.**

---

## 📌 Prompt Title

**Spring Boot Notification Service for Multi-Channel Communication**

---
**Title:** Spring Boot Social Media Platform — Integrated Notification Service

**High-Level Description:**  
You need to build a Spring Boot @Service class named `NotificationService` that consolidates multiple notification channels—email, SMS, and push notifications—into a single service. This service will validate input parameters, log operations at multiple levels, and handle errors gracefully using custom exceptions. It should delegate the actual sending of notifications to dedicated services (e.g., `EmailService`, `SmsService`, and `PushNotificationService`) based on the notification type provided.

**Key Features:**

1. **Class Definition & Annotations:**
  - Create a new Spring Boot @Service class named `NotificationService`.
  - Use constructor injection to inject the dependencies (`EmailService`, `SmsService`, and `PushNotificationService`).

2. **Integration with Multiple Notification Channels:**
  - Integrate with dedicated channel services for email, SMS, and push notifications.
  - Each channel service should be invoked based on the notification type.

3. **Input Validation:**
  - Validate that all required input parameters (e.g., recipient identifier, message/subject content, and notification type) are non-null and non-empty.
  - If any input is invalid, throw a custom exception (e.g., `NotificationException`).

4. **Logging:**
  - Implement logging at various levels (INFO, DEBUG, ERROR) to trace the operation flow and assist in troubleshooting.
  - Log key events such as input validation success, initiation of a notification dispatch, and any errors encountered.

5. **Error Handling:**
  - Use custom exceptions to capture and rethrow errors from underlying services.
  - Wrap unexpected exceptions in a `NotificationException` to provide clear feedback on failure.

6. **Dispatch Logic:**
  - Implement a method (e.g., `sendNotification`) that takes parameters including the notification type (EMAIL, SMS, PUSH), recipient, and message/subject.
  - Use a control flow mechanism (such as a switch-case) to route the request to the appropriate channel service.

7. **Testability:**
  - Design the class to be easily unit-testable by allowing mocking of the individual channel services.
  - Ensure thorough documentation of each method's purpose, parameters, and error conditions.

**Dependency Requirements:**

- **Spring Boot Starter:** For core application support.
- **Spring Boot Starter Logging (SLF4J):** For logging capabilities.
- **Spring Boot Starter Test (JUnit 5, Mockito):** For unit and integration testing.
- **Additional dependencies** (if needed): Custom exception classes such as `NotificationException`.

**Goal:**  
Develop a fully functional, well-documented, and testable `NotificationService` class that efficiently integrates and dispatches notifications across email, SMS, and push channels in a Spring Boot application. This service will serve as the backbone for real-time user engagement in a social media platform designed for creative professionals.

---

# **Complete Project Code**
**Project Structure:** A logical structure (typical Maven layout)
```plaintext
notification-service\src
src
|-- main
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           `-- notificationservice
|   |               |-- NotificationApplication.java
|   |               |-- config
|   |               |   `-- SecurityConfig.java
|   |               |-- controller
|   |               |   `-- NotificationController.java
|   |               |-- enums
|   |               |   `-- NotificationType.java
|   |               |-- exception
|   |               |   |-- EmailNotificationException.java
|   |               |   |-- GlobalExceptionHandler.java
|   |               |   |-- NotificationException.java
|   |               |   |-- PushNotificationException.java
|   |               |   `-- SmsNotificationException.java
|   |               `-- service
|   |                   |-- EmailService.java
|   |                   |-- NotificationService.java
|   |                   |-- PushNotificationService.java
|   |                   `-- SmsService.java
|   `-- resources
`-- test
    `-- java
        `-- com
            `-- example
                `-- notificationservice
                    |-- config
                    |   `-- SecurityConfigTest.java
                    |-- controller
                    |   `-- NotificationControllerIntegrationTest.java
                    |-- exception
                    |   |-- EmailNotificationExceptionTest.java
                    |   |-- GlobalExceptionHandlerTest.java
                    |   |-- NotificationExceptionTest.java
                    |   |-- PushNotificationExceptionTest.java
                    |   `-- SmsNotificationExceptionTest.java
                    `-- service
                        |-- EmailServiceTest.java
                        |-- NotificationServiceTest.java
                        |-- PushNotificationServiceTest.java
                        `-- SmsServiceTest.java
                            

```

---
## Code

## 🔐 **1. SecurityConfig** : `src/main/java/com/example/notificationservice/config/SecurityConfig.java`
```java

```

## 🔐 **2. NotificationController** : `src/main/java/com/example/notificationservice/controller/NotificationController.java`
```java

```

## 🔐 **3. NotificationType** : `src/main/java/com/example/notificationservice/enums/NotificationType.java`
```java

```

## 🔐 **4. GlobalExceptionHandler** : `src/main/java/com/example/notificationservice/exception/GlobalExceptionHandler.java`
```java

```

## 🔐 **5. SecurityConfig** : `src/main/java/com/example/notificationservice/exception/NotificationException.java`
```java

```

## 🔐 **6. EmailService** : `src/main/java/com/example/notificationservice/service/EmailService.java`
```java

```

## 🔐 **7. NotificationService** : `src/main/java/com/example/notificationservice/service/NotificationService.java`
```java

```

## 🔐 **8. PushNotificationService** : `src/main/java/com/example/notificationservice/service/PushNotificationService.java`
```java

```

## 🔐 **9. SmsService** : `src/main/java/com/example/notificationservice/service/SmsService.java`
```java

```



## Unit tests
## 🔐 **1. *SecurityConfigTest* : `src/test/java/com/example/notificationservice/config/SecurityConfigTest.java`
```java

```

## 🔐 **2. *NotificationControllerIntegrationTest* : `src/test/java/com/example/notificationservice/controller/NotificationControllerIntegrationTest.java`
```java

```

## 🔐 **3. *GlobalExceptionHandlerTest* : `src/test/java/com/example/notificationservice/exception/GlobalExceptionHandlerTest.java`
```java

```

**Result:** Total  coverage is 85%

:**Plan:** The goal is to achieve >90% total code coverage . To achieve this goal, will be writing tests for all models, dto, service, exception, event and constroller packages.
## Iteration number 2(for better coverage)

## 🔐 **4. *NotificationExceptionTest* : `src/test/java/com/example/notificationservice/exception/NotificationExceptionTest.java`
```java

```

## 🔐 **5. *NotificationServiceTest* : `src/test/java/com/example/notificationservice/service/NotificationServiceTest.java`
```java

```

## 🔐 **6. *TuringLLMTuningSystemTest* : `src/test/java/com/example/notificationservice/TuringLLMTuningSystemTest.java`
```java

```

**Result:** Total  coverage is 97%

## 🔨 How to Run

### 1. Clone and Navigate

```bash
git clone <your-repo-url>
cd project-name
```


### 4.  `pom.yml`

```xml
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


---

### 1. Send Email Notification

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -d "type=EMAIL" \
  -d "recipient=user@example.com" \
  -d "subjectOrMessage=Test Email Subject"
```

*This command dispatches an email notification. The endpoint delegates to the EmailService to send an email with a preset body.*

---

### 2. Send SMS Notification

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -d "type=SMS" \
  -d "recipient=1234567890" \
  -d "subjectOrMessage=Test SMS Message"
```

*This command dispatches an SMS notification. The service calls the SmsService to deliver the message to the provided phone number.*

---

### 3. Send Push Notification

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -d "type=PUSH" \
  -d "recipient=deviceToken" \
  -d "subjectOrMessage=Test Push Notification"
```

*This command dispatches a push notification. The service calls the PushNotificationService to send the notification to the specified device token.*

---

### 4. Test Invalid Notification Type

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -d "type=INVALID" \
  -d "recipient=recipient@example.com" \
  -d "subjectOrMessage=Test Invalid Type"
```

*This command attempts to send a notification using an invalid type. The service is expected to throw a NotificationException and return an error response.*



## 📊 Code Coverage Reports

- ✅ 1st
  Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1_dPiAFw3xR36ncLjPMIA2xM-tdNOQb1A/view?usp=drive_link)
- ✅ 2nd
  Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1ZxgGlsLh6BjJNupYWoHtIPrVRVSBD9ia/view?usp=drive_link)

---

## 📦 Download Code

[👉 Click to Download the Project Source Code](https://drive.google.com/file/d/1hAXj6I8pf99t4cs-rFRvrRcTcm35bFKA/view?usp=drive_link)

---

## 🧠 Conclusion

The **Spring Boot Notification Service** efficiently integrates multiple notification channels (email, SMS, push) with constant time complexity across its endpoints. The system:

✅ **Supports scalable notifications** by providing a unified API that delegates to dedicated services for each notification type, ensuring that operations remain fast and consistent.  
✅ **Ensures low latency and minimal overhead**, with each endpoint executing in constant time regardless of the input size.  
✅ **Delivers robust error handling and logging**, utilizing custom exceptions and centralized logging to simplify troubleshooting and maintain high code quality.  
✅ **Facilitates future extensibility** through its modular design, allowing for the seamless addition of new notification channels as needed.  
✅ **Provides maintainability and testability** with comprehensive unit and integration tests that guarantee high coverage and reliability.

With enhancements such as asynchronous processing and potential integration with message queuing systems, this architecture is well-positioned to support high performance and scalability in production-ready, enterprise-level applications. 🚀

---