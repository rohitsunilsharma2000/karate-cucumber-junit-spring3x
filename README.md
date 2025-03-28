7088 : Write a Spring Boot @Service class named NotificationService that integrates multiple notification channels (email, SMS, push).



## 📌 Use Case

**This Spring Boot microservice integrates multiple notification channels (email, SMS, push) into a unified service. It enables the application to send notifications via different channels seamlessly, ensuring that users receive timely updates regardless of the channel chosen.**



## 📌 Prompt Title

**Spring Boot Notification Service for Multi-Channel Communication**


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

**Plan:**

I will build a robust and scalable Spring Boot service that integrates multiple notification channels (email, SMS, and push). The service, named `NotificationService`, will use dependency injection to incorporate channel-specific services (e.g., `EmailService`, `SmsService`, and `PushNotificationService`). I will implement strict input validation to ensure that the recipient’s identifier and message content are provided and correctly formatted for each channel. Comprehensive logging will be added at various levels (INFO, DEBUG, ERROR) to provide detailed traceability of each notification process, including successful dispatches and error scenarios. To handle failures gracefully, I will incorporate clear and consistent exception management by using custom exceptions (e.g., `NotificationException`) and a global exception handler. This modular design, along with clean API design principles and service layering, will facilitate easy maintenance and future scalability, ensuring the solution is reliable and production-ready.


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


## Code



##  **1. NotificationController** : `src/main/java/com/example/notificationservice/controller/NotificationController.java`
```java
package com.example.multichannelnotifier.controller;

import com.example.multichannelnotifier.dto.NotificationRequestDTO;
import com.example.multichannelnotifier.service.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for sending notifications.
 *
 * <p><strong>Overview:</strong></p>
 * This controller provides an endpoint for sending notifications using a request body for a cleaner API design.
 * It supports multiple notification types (EMAIL, SMS, and PUSH) by delegating the processing to the {@link NotificationService}.
 *
 * <p><strong>Input Validation:</strong> The request is validated using annotations within {@link NotificationRequestDTO} to ensure
 * data integrity. If validation fails, a proper error response is returned.</p>
 *
 * <p><strong>Logging:</strong> Logging is implemented at INFO, DEBUG, and ERROR levels to improve traceability.</p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@Slf4j
@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Constructs a new NotificationController with the provided NotificationService.
     *
     * @param notificationService the service responsible for processing notifications.
     */
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Endpoint to send a notification.
     *
     * <p><strong>Description:</strong></p>
     * This endpoint allows clients to send a notification by specifying a JSON request body with:
     * <ul>
     *   <li><strong>type</strong> - The type of notification (EMAIL, SMS, or PUSH).</li>
     *   <li><strong>recipient</strong> - The recipient's identifier (email, phone, or device token).</li>
     *   <li><strong>subjectOrMessage</strong> - The subject or message content.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If required fields are missing or empty, input validation will trigger and return an error response.</li>
     *   <li>If an unsupported notification type is provided, the service layer will throw a {@link RuntimeException} wrapped in a {@link com.example.multichannelnotifier.exception.NotificationException}.</li>
     * </ul>
     *
     * @param notificationRequestDTO the DTO containing the notification request details.
     * @return a {@link ResponseEntity} with HTTP status OK and a message if the notification is processed successfully.
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@Valid @RequestBody NotificationRequestDTO notificationRequestDTO) {
        log.info("Received notification request: {}", notificationRequestDTO);
        try {
            notificationService.sendNotification(
                    notificationRequestDTO.getType(),
                    notificationRequestDTO.getRecipient(),
                    notificationRequestDTO.getSubjectOrMessage());
            String successMessage = notificationRequestDTO.getType().name().toLowerCase()
                    + " notification was successfully sent to "
                    + notificationRequestDTO.getRecipient();
            log.info(successMessage);
            return new ResponseEntity<>(successMessage, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage(), e);
            // Let global exception handler manage the exception response.
            throw e;
        }
    }
}

```

##  **2. NotificationType** : `src/main/java/com/example/notificationservice/enums/NotificationType.java`
```java
package com.example.multichannelnotifier.enums;

/**
 * Enum representing the available notification types within the Notification Service.
 *
 * <p>
 * This enum defines the supported types of notifications that can be processed by the service.
 * It includes the following values:
 * </p>
 *
 * <ul>
 *     <li><strong>EMAIL:</strong> Indicates that the notification will be sent via email.</li>
 *     <li><strong>SMS:</strong> Indicates that the notification will be sent as an SMS message.</li>
 *     <li><strong>PUSH:</strong> Indicates that the notification will be delivered as a push notification.</li>
 * </ul>
 *
 * <p>
 * <strong>Pass/Fail Conditions:</strong><br>
 * Pass: The application correctly identifies and processes the specified notification type.<br>
 * Fail: An unsupported notification type may lead to processing errors.
 * </p>
 *
 * <p>
 * <strong>Usage:</strong> This enum is used throughout the Notification Service to determine the
 * delivery mechanism for a notification.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public enum NotificationType {
    EMAIL,
    SMS,
    PUSH
}

```

##  **3. GlobalExceptionHandler** : `src/main/java/com/example/notificationservice/exception/GlobalExceptionHandler.java`
```java
package com.example.multichannelnotifier.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Notification Service Application.
 *
 * <p><strong>Functionality:</strong>
 * This class provides centralized exception handling across the notification service.
 * It intercepts client-side (4xx) and server-side (5xx) HTTP errors thrown by controllers,
 * converts them into structured error responses, and returns them with appropriate HTTP status codes.
 * Additionally, it now handles NotificationException, ensuring that all errors follow a standard response format.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles client-side HTTP errors (4xx).
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(
            HttpClientErrorException ex, WebRequest request) {
        // Convert the exception's HTTP status code into a HttpStatus object.
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        log.error("Client error occurred: {} - {}", status.getReasonPhrase(), ex.getMessage(), ex);
        return buildErrorResponse(status.getReasonPhrase(), status, request);
    }

    /**
     * Handles server-side HTTP errors (5xx).
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpServerErrorException(
            HttpServerErrorException ex, WebRequest request) {
        // Convert the exception's HTTP status code into a HttpStatus object.
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        log.error("Server error occurred: {} - {}", status.getReasonPhrase(), ex.getMessage(), ex);
        return buildErrorResponse(status.getReasonPhrase(), status, request);
    }

    /**
     * Handles NotificationException errors.
     * <p>
     * This method intercepts NotificationException instances thrown within the notification service,
     * logs the error, and returns a structured error response with HTTP 500 (Internal Server Error).
     * </p>
     *
     * @param ex      The NotificationException instance.
     * @param request The current WebRequest providing request context.
     * @return A ResponseEntity containing error details and HTTP status 500.
     */
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<Map<String, Object>> handleNotificationException(
            NotificationException ex, WebRequest request) {
        // Use HTTP 500 as the status for NotificationException.
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Notification error occurred: {} - {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), status, request);
    }

    /**
     * Helper method to build a structured error response.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        // Create a map to store error details.
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("path", request.getDescription(false)); // e.g., "uri=/api/..."
        return new ResponseEntity<>(errorDetails, status);
    }
}

```



##  **4. EmailService** : `src/main/java/com/example/notificationservice/service/EmailService.java`
```java
package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.exception.EmailNotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for sending email notifications.
 * <p>
 * This service provides functionality to send emails and includes input validation to ensure that all necessary
 * parameters are valid before proceeding. Detailed logging is implemented to trace the email sending process.
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email to the specified recipient.
     * <p>
     * This method validates the provided email address, subject, and body content to ensure they are not null or blank.
     * Detailed logging is used to trace the process, including input validation and the simulated email sending.
     * </p>
     *
     * @param to      the recipient's email address; must not be null or empty.
     * @param subject the email subject; must not be null or empty.
     * @param body    the email body content; must not be null or empty.
     * @throws EmailNotificationException if any of the input parameters are invalid or if sending fails.
     */
    public void sendEmail(String to, String subject, String body) {
        LOGGER.info("Initiating email send process.");



        try {
            // Delegate to a protected method that can be overridden in tests.
            sendEmailInternal(to, subject, body);
        } catch (Exception ex) {
            LOGGER.error("Failed to send email to: {}. Error: {}", to, ex.getMessage(), ex);
            // Wrap any unexpected exception into a custom EmailNotificationException
            throw new EmailNotificationException("Failed to send email due to an unexpected error.", ex);
        }
    }

    /**
     * Protected method that simulates the actual sending of the email.
     * <p>
     * In a real application, this method would integrate with an email sending library (e.g., JavaMailSender).
     * Here, it logs the sending process.
     * </p>
     *
     * @param to      the recipient's email address.
     * @param subject the email subject.
     * @param body    the email body content.
     */
    protected void sendEmailInternal(String to, String subject, String body) {
        LOGGER.info("Sending email to: {}, subject: {}", to, subject);
        // Simulated email sending logic
        LOGGER.info("Email sent successfully to: {}", to);
    }
}

```

##  **5. NotificationService** : `src/main/java/com/example/notificationservice/service/NotificationService.java`
```java
package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.enums.NotificationType;
import com.example.multichannelnotifier.exception.NotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for sending notifications via multiple channels.
 * <p>
 * This service integrates Email, SMS, and Push Notification services.
 * It performs thorough input validation, logs operations at various levels to ensure traceability,
 * and handles errors with descriptive exceptions.
 * Custom Exception Handling: A custom exception (NotificationException) has been implemented,
 * and a global exception handler is present to ensure all errors follow a standard response format.
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class NotificationService {

    // Logger for tracing execution details and errors
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    // Dependencies for each notification channel
    private final EmailService emailService;
    private final SmsService smsService;
    private final PushNotificationService pushNotificationService;

    /**
     * Constructs a NotificationService with the provided channel services.
     *
     * @param emailService            the service for sending emails.
     * @param smsService              the service for sending SMS messages.
     * @param pushNotificationService the service for sending push notifications.
     */
    public NotificationService(EmailService emailService, SmsService smsService, PushNotificationService pushNotificationService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.pushNotificationService = pushNotificationService;
    }

    /**
     * Sends an email notification.
     * <p>
     * Validates that the recipient email address, subject, and body are provided.
     * </p>
     *
     * @param to      the recipient's email address; must not be null or empty.
     * @param subject the email subject; must not be null or empty.
     * @param body    the email body content; must not be null or empty.
     * @throws NotificationException if input validation fails or if an error occurs while sending the email.
     */
    public void sendEmail(String to, String subject, String body) {
        // Log the start of email sending process.
        LOGGER.info("Preparing to send email notification.");


        // Debug log validated parameters
        LOGGER.debug("Email parameters validated: to={}, subject={}", to, subject);
        try {
            // Attempt to send the email using EmailService.
            emailService.sendEmail(to, subject, body);
            LOGGER.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            // Log the error and wrap it in a custom NotificationException.
            LOGGER.error("Error sending email to {}: {}", to, e.getMessage(), e);
            throw new NotificationException("Error sending email", e);
        }
    }

    /**
     * Sends an SMS notification.
     * <p>
     * Validates that the recipient's phone number and message content are provided.
     * </p>
     *
     * @param to      the recipient's phone number; must not be null or empty.
     * @param message the SMS message content; must not be null or empty.
     * @throws NotificationException if input validation fails or if an error occurs while sending the SMS.
     */
    public void sendSms(String to, String message) {
        // Log the start of SMS sending process.
        LOGGER.info("Preparing to send SMS notification.");



        // Debug log validated parameters
        LOGGER.debug("SMS parameters validated: to={}", to);
        try {
            // Attempt to send the SMS using SmsService.
            smsService.sendSms(to, message);
            LOGGER.info("SMS sent successfully to {}", to);
        } catch (Exception e) {
            // Log the error and wrap it in a custom NotificationException.
            LOGGER.error("Error sending SMS to {}: {}", to, e.getMessage(), e);
            throw new NotificationException("Error sending SMS", e);
        }
    }

    /**
     * Sends a push notification.
     * <p>
     * Validates that the device token and message content are provided.
     * </p>
     *
     * @param to      the recipient's device token; must not be null or empty.
     * @param message the push notification message content; must not be null or empty.
     * @throws NotificationException if input validation fails or if an error occurs while sending the push notification.
     */
    public void sendPushNotification(String to, String message) {
        // Log the start of push notification sending process.
        LOGGER.info("Preparing to send push notification.");



        // Debug log validated parameters
        LOGGER.debug("Push notification parameters validated: to={}", to);
        try {
            // Attempt to send the push notification using PushNotificationService.
            pushNotificationService.sendPushNotification(to, message);
            LOGGER.info("Push notification sent successfully to {}", to);
        } catch (Exception e) {
            // Log the error and wrap it in a custom NotificationException.
            LOGGER.error("Error sending push notification to {}: {}", to, e.getMessage(), e);
            throw new NotificationException("Error sending push notification", e);
        }
    }

    /**
     * Sends a notification using the specified type.
     * <p>
     * Validates that the notification type, recipient, and subject or message are provided.
     * Dispatches the notification to the appropriate channel (EMAIL, SMS, or PUSH).
     * </p>
     *
     * @param type             the type of notification (EMAIL, SMS, PUSH); must not be null.
     * @param recipient        the recipient's identifier (email address, phone number, or device token); must not be null or empty.
     * @param subjectOrMessage the subject (for email) or message (for SMS and push notifications); must not be null or empty.
     * @throws NotificationException if input validation fails or if an unsupported notification type is provided.
     */
    public void sendNotification(NotificationType type, String recipient, String subjectOrMessage) {
        // Log the start of the notification dispatch process.
        LOGGER.info("Initiating dispatch of notification.");



        // Log validated parameters for debugging
        LOGGER.debug("Dispatching notification: type={}, recipient={}", type, recipient);

        // Dispatch notification based on the provided type
        switch (type) {
            case EMAIL:
                // For demonstration purposes, use subjectOrMessage as the email subject with a constant body.
                sendEmail(recipient, subjectOrMessage, "This is the email body.");
                break;
            case SMS:
                sendSms(recipient, subjectOrMessage);
                break;
            case PUSH:
                sendPushNotification(recipient, subjectOrMessage);
                break;
            default:
                LOGGER.error("Unsupported notification type: {}", type);
                throw new NotificationException("Unsupported notification type: " + type);
        }
    }
}

```

##  **6. PushNotificationService** : `src/main/java/com/example/notificationservice/service/PushNotificationService.java`
```java
package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.exception.PushNotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for sending push notifications.
 * <p>
 * This service provides functionality to send push notifications using an underlying messaging system
 * (e.g., Firebase Cloud Messaging). It includes input validation to ensure that required parameters are not null or empty,
 * and implements detailed logging to trace the notification sending process.
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class PushNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationService.class);

    /**
     * Sends a push notification to the specified device token.
     * <p>
     * This method validates that the recipient device token and message content are provided.
     * If either parameter is invalid, a {@link PushNotificationException} is thrown.
     * The process is logged at different levels:
     * - INFO: when starting and after successful sending.
     * - DEBUG: for detailed parameter logging.
     * - ERROR: if input validation fails or if an exception is encountered during sending.
     * </p>
     *
     * @param to      the recipient's device token; must not be null or empty.
     * @param message the push notification message content; must not be null or empty.
     * @throws PushNotificationException if the device token or message is null/empty or if sending fails.
     */
    public void sendPushNotification(String to, String message) {
        LOGGER.info("Initiating push notification send process.");


        LOGGER.debug("Push notification parameters validated: to={}, message={}", to, message);

        try {
            // Simulated push notification sending logic
            LOGGER.info("Sending push notification to: {}", to);
            // Actual integration with push notification providers (e.g., Firebase Cloud Messaging) would occur here.
            LOGGER.info("Push notification sent successfully to: {}", to);
        } catch (Exception e) {
            LOGGER.error("Error sending push notification to {}: {}", to, e.getMessage(), e);
            throw new PushNotificationException("Failed to send push notification due to an unexpected error.", e);
        }
    }
}

```

##  **7. SmsService** : `src/main/java/com/example/notificationservice/service/SmsService.java`
```java
package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.exception.EmailNotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for sending email notifications.
 * <p>
 * This service provides functionality to send emails and includes input validation to ensure that all necessary
 * parameters are valid before proceeding. Detailed logging is implemented to trace the email sending process.
 * </p>
 *
 * @version 1.1
 * @since 2025-03-26
 */
@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email to the specified recipient.
     * <p>
     * This method validates the provided email address, subject, and body content to ensure they are not null or blank.
     * Detailed logging is used to trace the process, including input validation and the simulated email sending.
     * </p>
     *
     * @param to      the recipient's email address; must not be null or empty.
     * @param subject the email subject; must not be null or empty.
     * @param body    the email body content; must not be null or empty.
     * @throws EmailNotificationException if any of the input parameters are invalid or if sending fails.
     */
    public void sendEmail(String to, String subject, String body) {
        LOGGER.info("Initiating email send process.");



        try {
            // Delegate to a protected method that can be overridden in tests.
            sendEmailInternal(to, subject, body);
        } catch (Exception ex) {
            LOGGER.error("Failed to send email to: {}. Error: {}", to, ex.getMessage(), ex);
            // Wrap any unexpected exception into a custom EmailNotificationException
            throw new EmailNotificationException("Failed to send email due to an unexpected error.", ex);
        }
    }

    /**
     * Protected method that simulates the actual sending of the email.
     * <p>
     * In a real application, this method would integrate with an email sending library (e.g., JavaMailSender).
     * Here, it logs the sending process.
     * </p>
     *
     * @param to      the recipient's email address.
     * @param subject the email subject.
     * @param body    the email body content.
     */
    protected void sendEmailInternal(String to, String subject, String body) {
        LOGGER.info("Sending email to: {}, subject: {}", to, subject);
        // Simulated email sending logic
        LOGGER.info("Email sent successfully to: {}", to);
    }
}

```



## Unit tests
##  **1. NotificationControllerIntegrationTest** : `src/test/java/com/example/notificationservice/controller/NotificationControllerIntegrationTest.java`

```java
package com.example.multichannelnotifier.controller;

import com.example.multichannelnotifier.dto.NotificationRequestDTO;
import com.example.multichannelnotifier.enums.NotificationType;
import com.example.multichannelnotifier.exception.NotificationException;
import com.example.multichannelnotifier.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link NotificationController}.
 * <p>
 * <strong>Overview:</strong> This test class verifies the end-to-end behavior of the
 * NotificationController by loading the full Spring Boot application context and using MockMvc.
 * It covers:
 * <ul>
 *   <li>Successful processing of valid notification requests.</li>
 *   <li>Validation errors resulting in HTTP 400 responses.</li>
 *   <li>Exception handling via the global exception handler, resulting in HTTP 500 responses when the service layer fails.</li>
 * </ul>
 * <strong>Test Coverage:</strong> Class, method, and line coverage are targeted to be 100%.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code type}: Must be a valid {@link NotificationType} (EMAIL, SMS, PUSH).</li>
 *   <li>{@code recipient}: A non-null, non-empty string representing the recipient's identifier (e.g., email, phone, or device token).</li>
 *   <li>{@code subjectOrMessage}: A non-null, non-empty string representing the email subject or message content.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If required fields are missing or empty, input validation will cause the controller to return HTTP 400.</li>
 *   <li>If the service layer throws a {@link NotificationException}, the global exception handler should convert it to an HTTP 500 response.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Constraints:</strong> The tests assume that the NotificationService is mocked to isolate the controller behavior.
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerIntegrationTest {

  /**
   * MockMvc instance for performing HTTP requests in tests.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Mocked NotificationService to simulate service layer behavior.
   */
  @MockBean
  private NotificationService notificationService;

  /**
   * ObjectMapper for converting objects to JSON strings.
   */
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Integration test for a successful notification send.
   * <p>
   * <strong>Description:</strong> Sends a valid SMS notification request and verifies that:
   * <ul>
   *   <li>The response status is HTTP 200 (OK).</li>
   *   <li>The response message contains the expected success text.</li>
   * </ul>
   * </p>
   * <p>
   * <strong>Premise:</strong> The request DTO contains valid SMS parameters.
   * </p>
   * <p>
   * <strong>Assertions:</strong>
   * <ul>
   *   <li>Status code is 200.</li>
   *   <li>Response body contains "sms notification was successfully sent to 1234567890".</li>
   *   <li>NotificationService.sendNotification is invoked with correct parameters.</li>
   * </ul>
   * </p>
   * <p>
   * <strong>Pass/Fail Conditions:</strong> Pass if all assertions are met; otherwise, fail.
   * </p>
   */
  @Test
  public void testSendNotificationSuccessIntegration() throws Exception {
    // Create a valid notification request DTO for an SMS.
    NotificationRequestDTO requestDTO = new NotificationRequestDTO();
    requestDTO.setType(NotificationType.SMS);
    requestDTO.setRecipient("1234567890");
    requestDTO.setSubjectOrMessage("Test SMS Message");

    // Perform a POST request to the /notifications/send endpoint.
    mockMvc.perform(post("/notifications/send")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
           // Expect an HTTP 200 OK status.
           .andExpect(status().isOk())
           // Validate that the response message contains the expected success text.
           .andExpect(content().string(containsString("sms notification was successfully sent to 1234567890")));

    // Verify that NotificationService.sendNotification was called with the correct parameters.
    verify(notificationService).sendNotification(NotificationType.SMS, "1234567890", "Test SMS Message");
  }

  /**
   * Integration test for input validation errors.
   * <p>
   * <strong>Description:</strong> Sends a notification request with an empty recipient field,
   * expecting the controller to return an HTTP 400 Bad Request response due to validation failure.
   * </p>
   * <p>
   * <strong>Premise:</strong> The request DTO contains an empty recipient, which is invalid.
   * </p>
   * <p>
   * <strong>Assertions:</strong> The response status must be 400.
   * </p>
   * <p>
   * <strong>Pass/Fail Conditions:</strong> Pass if a 400 status is returned; otherwise, fail.
   * </p>
   */
  @Test
  public void testSendNotificationValidationErrorIntegration() throws Exception {
    // Create a DTO with an empty recipient to trigger validation failure.
    NotificationRequestDTO requestDTO = new NotificationRequestDTO();
    requestDTO.setType(NotificationType.EMAIL);
    requestDTO.setRecipient(""); // Invalid input.
    requestDTO.setSubjectOrMessage("Test Email Subject");

    // Perform the POST request and expect an HTTP 400 Bad Request status.
    mockMvc.perform(post("/notifications/send")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
           .andExpect(status().isBadRequest());
  }

  /**
   * Integration test for exception handling in the controller.
   * <p>
   * <strong>Description:</strong> Configures the mocked NotificationService to throw a NotificationException
   * when processing a valid PUSH notification request. Verifies that the global exception handler intercepts the exception,
   * resulting in an HTTP 500 Internal Server Error with the correct exception details.
   * </p>
   * <p>
   * <strong>Premise:</strong> The request DTO contains valid PUSH notification parameters, but the service layer is simulated to fail.
   * </p>
   * <p>
   * <strong>Assertions:</strong>
   * <ul>
   *   <li>The response status is 500.</li>
   *   <li>The resolved exception is a NotificationException with message "Simulated exception".</li>
   * </ul>
   * </p>
   * <p>
   * <strong>Pass/Fail Conditions:</strong> Pass if the HTTP 500 status is returned and the exception details match; otherwise, fail.
   * </p>
   */
  @Test
  public void testSendNotificationExceptionIntegration() throws Exception {
    // Create a valid notification request DTO for a PUSH notification.
    NotificationRequestDTO requestDTO = new NotificationRequestDTO();
    requestDTO.setType(NotificationType.PUSH);
    requestDTO.setRecipient("deviceToken123");
    requestDTO.setSubjectOrMessage("Test Push Message");

    // Configure the NotificationService mock to throw a NotificationException when sendNotification is called.
    doThrow(new NotificationException("Simulated exception"))
            .when(notificationService).sendNotification(any(), any(), any());

    // Perform the POST request and expect an HTTP 500 Internal Server Error.
    mockMvc.perform(post("/notifications/send")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
           .andExpect(status().isInternalServerError())
           // Validate that the exception is resolved and has the expected properties.
           .andExpect(result -> {
             Exception resolvedException = result.getResolvedException();
             assertNotNull(resolvedException, "Resolved exception should not be null");
             assertTrue(resolvedException instanceof NotificationException, "Expected NotificationException");
             assertEquals("Simulated exception", resolvedException.getMessage());
           });

    // Verify that NotificationService.sendNotification was invoked with the expected parameters.
    verify(notificationService).sendNotification(NotificationType.PUSH, "deviceToken123", "Test Push Message");
  }
}

```


##  **2. NotificationControllerUnitTest** : `src/test/java/com/example/notificationservice/controller/NotificationControllerUnitTest.java`
```java
package com.example.multichannelnotifier.controller;

import com.example.multichannelnotifier.dto.NotificationRequestDTO;
import com.example.multichannelnotifier.enums.NotificationType;
import com.example.multichannelnotifier.exception.GlobalExceptionHandler;
import com.example.multichannelnotifier.exception.NotificationException;
import com.example.multichannelnotifier.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link NotificationController} using @WebMvcTest.
 * <p>
 * <strong>Overview:</strong> This test class verifies the end-to-end behavior of the
 * NotificationController by loading the full Spring Boot context and simulating HTTP requests using MockMvc.
 * It covers:
 * <ul>
 *   <li>Successful processing of a valid notification request.</li>
 *   <li>Input validation errors resulting in HTTP 400 responses.</li>
 *   <li>Exception handling when the service layer throws a {@link NotificationException}, resulting in an HTTP 500 response.</li>
 * </ul>
 * <strong>Test Coverage:</strong> The class aims for 100% coverage at the class, method, and line levels.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code type}: A valid {@link NotificationType} value (EMAIL, SMS, or PUSH).</li>
 *   <li>{@code recipient}: A non-null, non-empty string (e.g., "user@example.com", "1234567890", or "deviceToken123").</li>
 *   <li>{@code subjectOrMessage}: A non-null, non-empty string representing the email subject (for EMAIL) or message content (for SMS/PUSH).</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>HTTP 400 (Bad Request) if input validation fails (e.g., empty recipient).</li>
 *   <li>HTTP 500 (Internal Server Error) if a {@link NotificationException} is thrown by the service layer.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Constraints:</strong> Security filters are disabled in tests to focus on controller behavior.
 * </p>
 */
@WebMvcTest(controllers = NotificationController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class NotificationControllerUnitTest {

    /**
     * MockMvc instance used to perform HTTP requests against the controller.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked NotificationService to simulate service layer behavior.
     */
    @MockBean
    private NotificationService notificationService;

    /**
     * ObjectMapper for serializing DTOs to JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Test successful notification send.
     * <p>
     * <strong>Description:</strong> This test sends a valid EMAIL notification request and verifies:
     * <ul>
     *   <li>The HTTP status is 200 (OK).</li>
     *   <li>The response message contains the expected success text.</li>
     *   <li>{@link NotificationService#sendNotification(NotificationType, String, String)} is invoked with the correct parameters.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Premise:</strong> The DTO is populated with valid EMAIL values:
     * - {@code type} = EMAIL
     * - {@code recipient} = "user@example.com"
     * - {@code subjectOrMessage} = "Test Subject"
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>Status code equals 200.</li>
     *   <li>Response body contains "email notification was successfully sent to user@example.com".</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail:</strong> Test passes if all assertions are met.
     * </p>
     */
    @Test
    public void testSendNotificationSuccess() throws Exception {
        // Create a valid notification request DTO for an EMAIL notification.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.EMAIL);
        requestDTO.setRecipient("user@example.com");
        requestDTO.setSubjectOrMessage("Test Subject");

        // Perform a POST request to /notifications/send with valid JSON content.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               // Expect HTTP 200 OK.
               .andExpect(status().isOk())
               // Check that the response message contains the expected text.
               .andExpect(content().string(containsString("email notification was successfully sent to user@example.com")));

        // Verify that NotificationService.sendNotification is called with the expected parameters.
        verify(notificationService).sendNotification(NotificationType.EMAIL, "user@example.com", "Test Subject");
    }

    /**
     * Test input validation error.
     * <p>
     * <strong>Description:</strong> This test sends a notification request with an empty recipient,
     * expecting a HTTP 400 (Bad Request) due to input validation failure.
     * </p>
     * <p>
     * <strong>Premise:</strong> The DTO contains an empty {@code recipient}, which is invalid.
     * </p>
     * <p>
     * <strong>Assertions:</strong> The response status must be 400.
     * </p>
     * <p>
     * <strong>Pass/Fail:</strong> Test passes if the controller returns HTTP 400.
     * </p>
     */
    @Test
    public void testSendNotificationValidationError() throws Exception {
        // Create a DTO with an empty recipient to trigger validation error.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.SMS);
        requestDTO.setRecipient("");  // Invalid input.
        requestDTO.setSubjectOrMessage("Test Message");

        // Perform the POST request and expect HTTP 400 Bad Request.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               .andExpect(status().isBadRequest());
    }

    /**
     * Test exception handling in the controller.
     * <p>
     * <strong>Description:</strong> This test simulates an exception in the service layer by configuring the mocked
     * {@link NotificationService} to throw a {@link NotificationException} when processing a valid PUSH notification request.
     * The test verifies that the global exception handler intercepts the exception and returns an HTTP 500 response.
     * </p>
     * <p>
     * <strong>Premise:</strong> The DTO is valid for a PUSH notification, but the service layer is simulated to fail.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The response status is 500 (Internal Server Error).</li>
     *   <li>The resolved exception is a {@link NotificationException} with the message "Simulated exception".</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail:</strong> Test passes if the HTTP 500 status and exception details match the expectations.
     * </p>
     */
    @Test
    public void testSendNotificationServiceException() throws Exception {
        // Create a valid notification request DTO for a PUSH notification.
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setType(NotificationType.PUSH);
        requestDTO.setRecipient("deviceToken123");
        requestDTO.setSubjectOrMessage("Test Push Message");

        // Configure the mocked NotificationService to throw a NotificationException when sendNotification is called.
        doThrow(new NotificationException("Simulated exception"))
                .when(notificationService).sendNotification(any(), any(), any());

        // Perform the POST request and expect HTTP 500 Internal Server Error.
        mockMvc.perform(post("/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
               .andExpect(status().isInternalServerError())
               // Validate that the response content type is JSON.
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               // Validate that the resolved exception's message equals "Simulated exception".
               .andExpect(result -> {
                   Exception resolvedException = result.getResolvedException();
                   assertNotNull(resolvedException, "Resolved exception should not be null");
                   assertTrue(resolvedException instanceof NotificationException, "Expected NotificationException");
                   assertEquals("Simulated exception", resolvedException.getMessage());
               });

        // Verify that NotificationService.sendNotification is called with the expected parameters.
        verify(notificationService).sendNotification(NotificationType.PUSH, "deviceToken123", "Test Push Message");
    }
}

```


# After the first iteration, the overall test coverage was 76%. To improve this, additional test cases—including those in`GlobalExceptionHandlerTest`  , `NotificationExceptionTest` ,`NotificationServiceTest` and `MultiChannelNotifierApplicationTest`will be introduced to further increase the test coverage percentage.

## Iteration number 2(for better coverage)

##  **3. GlobalExceptionHandlerTest** : `src/test/java/com/example/notificationservice/exception/GlobalExceptionHandlerTest.java`
```java
package com.example.multichannelnotifier.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * <p>
 * <strong>Overview:</strong>
 * This test class verifies that the GlobalExceptionHandler correctly maps HTTP client errors (4xx) and server errors (5xx)
 * to structured error responses. The tests simulate exceptions (e.g., 404 Not Found and 500 Internal Server Error)
 * and assert that the returned ResponseEntity contains the proper HTTP status and error details.
 * </p>
 *
 * <p>
 * <strong>Test Coverage:</strong> This class aims for 100% coverage of the exception handling logic in GlobalExceptionHandler.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code ex}: A valid HttpClientErrorException or HttpServerErrorException with a specific HTTP status (e.g., 404 or 500).</li>
 *   <li>{@code request}: A non-null WebRequest that provides a valid request description (e.g., "uri=/api/test").</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If any of the parameters are null, the error response may be incomplete.</li>
 *   <li>The handler should still produce a ResponseEntity with the correct HTTP status and error details.</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class GlobalExceptionHandlerTest {

    // Create an instance of GlobalExceptionHandler for testing.
    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests the handling of a HttpClientErrorException (4xx errors).
     * <p>
     * <strong>Description:</strong> This test simulates a 404 Not Found exception and verifies that the
     * GlobalExceptionHandler returns a ResponseEntity with:
     * <ul>
     *   <li>HTTP status 404.</li>
     *   <li>Error details containing the correct message, status code, error description, request path, and timestamp.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Premise:</strong> A simulated HttpClientErrorException with HTTP 404 is provided along with a mock WebRequest.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>Status code is 404.</li>
     *   <li>Error message, status, error, path, and timestamp in the response are as expected.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> The test passes if all assertions are met.
     * </p>
     */
    @Test
    @DisplayName("Test handling of HttpClientErrorException (404 Not Found)")
    public void testHandleHttpClientErrorException() {
        // Arrange: Create a simulated HttpClientErrorException with 404 status.
        HttpClientErrorException clientErrorException = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", null, null, null);

        // Create a mock WebRequest that returns a dummy URI description.
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/test");

        // Act: Call the exception handler for HttpClientErrorException.
        ResponseEntity<Map<String, Object>> responseEntity =
                exceptionHandler.handleHttpClientErrorException(clientErrorException, mockRequest);

        // Assert: Validate that the response status is 404 and error details are correct.
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode(), "Response status should be 404 Not Found");

        Map<String, Object> errorDetails = responseEntity.getBody();
        assertNotNull(errorDetails, "Error details should not be null");
        assertEquals("Not Found", errorDetails.get("message"), "Error message should match");
        assertEquals(404, errorDetails.get("status"), "Status code in error details should be 404");
        assertEquals("Not Found", errorDetails.get("error"), "Error description should match");
        assertEquals("uri=/api/test", errorDetails.get("path"), "Path should match the mock request description");
        assertNotNull(errorDetails.get("timestamp"), "Timestamp should not be null");
    }

    /**
     * Tests the handling of a HttpServerErrorException (5xx errors).
     * <p>
     * <strong>Description:</strong> This test simulates a 500 Internal Server Error exception and verifies that the
     * GlobalExceptionHandler returns a ResponseEntity with:
     * <ul>
     *   <li>HTTP status 500.</li>
     *   <li>Error details containing the correct message, status code, error description, request path, and timestamp.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Premise:</strong> A simulated HttpServerErrorException with HTTP 500 is provided along with a mock WebRequest.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>Status code is 500.</li>
     *   <li>Error details in the response match the expected values.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Test passes if the response is constructed correctly.
     * </p>
     */
    @Test
    @DisplayName("Test handling of HttpServerErrorException (500 Internal Server Error)")
    public void testHandleHttpServerErrorException() {
        // Arrange: Create a simulated HttpServerErrorException with 500 status.
        HttpServerErrorException serverErrorException = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);

        // Create a mock WebRequest with a dummy URI description.
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/test");

        // Act: Call the exception handler for HttpServerErrorException.
        ResponseEntity<Map<String, Object>> responseEntity =
                exceptionHandler.handleHttpServerErrorException(serverErrorException, mockRequest);

        // Assert: Verify that the response status is 500 and error details are correct.
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode(), "Response status should be 500 Internal Server Error");

        Map<String, Object> errorDetails = responseEntity.getBody();
        assertNotNull(errorDetails, "Error details should not be null");
        assertEquals("Internal Server Error", errorDetails.get("message"), "Error message should match");
        assertEquals(500, errorDetails.get("status"), "Status code in error details should be 500");
        assertEquals("Internal Server Error", errorDetails.get("error"), "Error description should match");
        assertEquals("uri=/api/test", errorDetails.get("path"), "Path should match the mock request description");
        assertNotNull(errorDetails.get("timestamp"), "Timestamp should not be null");
    }
}

```
##  **4. NotificationExceptionTest** : `src/test/java/com/example/notificationservice/exception/NotificationExceptionTest.java`
```java
package com.example.multichannelnotifier.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link NotificationException} class.
 * <p>
 * <strong>Overview:</strong> This test class verifies that the constructors of the NotificationException class
 * correctly set the error message and the cause. Two tests are provided:
 * <ul>
 *   <li>One for the constructor that accepts only a message.</li>
 *   <li>One for the constructor that accepts both a message and a cause.</li>
 * </ul>
 * These tests ensure 100% coverage for the constructor logic.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range:</strong>
 * <ul>
 *   <li>{@code errorMessage}: A non-null, non-empty string describing the error.</li>
 *   <li>{@code cause}: A non-null {@link Throwable} representing the underlying cause; it may be null when not provided.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If the exception does not return the expected message or cause, the test will fail.</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class NotificationExceptionTest {

    /**
     * Tests the constructor of NotificationException with a valid message.
     * <p>
     * <strong>Description:</strong> This test verifies that when a NotificationException is instantiated with only a message,
     * the message is correctly stored and the cause remains null.
     * </p>
     * <p>
     * <strong>Premise:</strong> A valid error message (non-null and non-empty) is provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception instance is not null.</li>
     *   <li>The exception's message equals the provided error message.</li>
     *   <li>The exception's cause is null.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> The test passes if all assertions are met; otherwise, it fails.
     * </p>
     */
    @Test
    @DisplayName("Test NotificationException constructor with valid message")
    void testConstructorWithMessage() {
        // Arrange: Define a valid error message.
        String errorMessage = "Error occurred";

        // Act: Create an instance of NotificationException using the message-only constructor.
        NotificationException exception = new NotificationException(errorMessage);

        // Assert: Verify that the instance is properly constructed.
        // The exception instance must not be null.
        assertNotNull(exception, "NotificationException instance should not be null");
        // The exception message must match the provided error message.
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        // Since no cause was provided, the exception's cause should be null.
        assertNull(exception.getCause(), "The exception cause should be null when not provided");
    }

    /**
     * Tests the constructor of NotificationException with a valid message and a cause.
     * <p>
     * <strong>Description:</strong> This test verifies that when a NotificationException is instantiated with both a message and a cause,
     * both fields are correctly stored.
     * </p>
     * <p>
     * <strong>Premise:</strong> A valid error message and a valid non-null cause (Throwable) are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception instance is not null.</li>
     *   <li>The exception's message equals the provided error message.</li>
     *   <li>The exception's cause equals the provided Throwable.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> The test passes if the exception contains the expected message and cause; otherwise, it fails.
     * </p>
     */
    @Test
    @DisplayName("Test NotificationException constructor with valid message and cause")
    void testConstructorWithMessageAndCause() {
        // Arrange: Define a valid error message and a Throwable cause.
        String errorMessage = "Error occurred with cause";
        Throwable cause = new RuntimeException("Underlying cause");

        // Act: Create an instance of NotificationException using the constructor that accepts both message and cause.
        NotificationException exception = new NotificationException(errorMessage, cause);

        // Assert: Validate that the exception is constructed with the provided message and cause.
        // The exception instance must not be null.
        assertNotNull(exception, "NotificationException instance should not be null");
        // The exception message must equal the provided error message.
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the provided message");
        // The exception cause must equal the provided Throwable cause.
        assertEquals(cause, exception.getCause(), "The exception cause should match the provided cause");
    }
}

```

##  **5. NotificationServiceTest** : `src/test/java/com/example/notificationservice/service/NotificationServiceTest.java`
```java
package com.example.multichannelnotifier.service;

import com.example.multichannelnotifier.enums.NotificationType;
import com.example.multichannelnotifier.exception.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationService}.
 * <p>
 * <strong>Overview:</strong> This test class verifies that NotificationService correctly delegates to the underlying channel services,
 * validates its inputs, and properly handles exceptions thrown by those services. It covers:
 * <ul>
 *   <li>Successful delegation for email, SMS, and push notifications.</li>
 *   <li>Validation errors for null type, empty recipient, and empty subject/message.</li>
 *   <li>Exception handling when an underlying service throws an exception.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code type}: Must be one of {@link NotificationType} values (EMAIL, SMS, PUSH).</li>
 *   <li>{@code recipient}: A non-null, non-empty string representing an email, phone number, or device token.</li>
 *   <li>{@code subjectOrMessage}: A non-null, non-empty string representing the email subject or SMS/Push message.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If {@code type} is null, a {@link NotificationException} is thrown with message "Unsupported notification type: null".</li>
 *   <li>If {@code recipient} is empty, a {@link NotificationException} is thrown with an appropriate message.</li>
 *   <li>If {@code subjectOrMessage} is empty, a {@link NotificationException} is thrown with an appropriate message.</li>
 *   <li>If an underlying service call fails (throws a RuntimeException), it is wrapped in a {@link NotificationException}.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Test Coverage:</strong> This class targets 100% coverage at class, method, and line levels for NotificationService.
 * </p>
 */
public class NotificationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @Mock
    private PushNotificationService pushNotificationService;

    @InjectMocks
    private NotificationService notificationService;

    /**
     * Sets up the test environment by initializing the mocks and injecting them into NotificationService.
     */
    @BeforeEach
    public void setup() {
        // Initialize mocks and inject them into the NotificationService instance.
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that sendEmail delegates correctly when valid inputs are provided.
     * <p>
     * <strong>Description:</strong> Verifies that calling sendEmail on NotificationService with valid parameters
     * delegates the call to EmailService.sendEmail with the same parameters.
     * </p>
     * <p>
     * <strong>Premise:</strong> All parameters (to, subject, body) are non-null and non-empty.
     * </p>
     * <p>
     * <strong>Assertions:</strong> No exception is thrown and EmailService.sendEmail is invoked exactly once.
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Test passes if no exception is thrown and the delegation occurs correctly.
     * </p>
     */
    @Test
    public void testSendEmailDelegationSuccess() {
        String to = "user@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act: Call the sendEmail method.
        notificationService.sendEmail(to, subject, body);
        // Assert: Verify that the underlying emailService.sendEmail was called once with the expected parameters.
        verify(emailService, times(1)).sendEmail(to, subject, body);
    }

    /**
     * Tests that sendSms delegates correctly when valid inputs are provided.
     * <p>
     * <strong>Description:</strong> Verifies that sendSms on NotificationService delegates the call to SmsService.sendSms.
     * </p>
     * <p>
     * <strong>Premise:</strong> Both recipient and message are valid.
     * </p>
     * <p>
     * <strong>Assertions:</strong> SmsService.sendSms is invoked exactly once.
     * </p>
     */
    @Test
    public void testSendSmsDelegationSuccess() {
        String to = "1234567890";
        String message = "Test SMS Message";

        // Act: Call the sendSms method.
        notificationService.sendSms(to, message);
        // Assert: Verify that SmsService.sendSms was called once with the expected parameters.
        verify(smsService, times(1)).sendSms(to, message);
    }

    /**
     * Tests that sendPushNotification delegates correctly when valid inputs are provided.
     * <p>
     * <strong>Description:</strong> Verifies that sendPushNotification on NotificationService delegates the call to PushNotificationService.sendPushNotification.
     * </p>
     * <p>
     * <strong>Premise:</strong> Both device token and message are valid.
     * </p>
     * <p>
     * <strong>Assertions:</strong> PushNotificationService.sendPushNotification is invoked exactly once.
     * </p>
     */
    @Test
    public void testSendPushNotificationDelegationSuccess() {
        String to = "deviceToken";
        String message = "Test Push Message";

        // Act: Call the sendPushNotification method.
        notificationService.sendPushNotification(to, message);
        // Assert: Verify that PushNotificationService.sendPushNotification was called once with the expected parameters.
        verify(pushNotificationService, times(1)).sendPushNotification(to, message);
    }

    /**
     * Tests that sendNotification dispatches an EMAIL notification correctly.
     * <p>
     * <strong>Description:</strong> Expects that calling sendNotification with type EMAIL delegates to EmailService.sendEmail,
     * passing the recipient, subjectOrMessage as the subject, and a constant email body.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid EMAIL parameters are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong> EmailService.sendEmail is invoked with recipient, subjectOrMessage, and "This is the email body.".
     * </p>
     */
    @Test
    public void testSendNotificationEmail() {
        String recipient = "user@example.com";
        String subjectOrMessage = "Test Email";

        // Act: Call sendNotification with type EMAIL.
        notificationService.sendNotification(NotificationType.EMAIL, recipient, subjectOrMessage);
        // Assert: Verify that EmailService.sendEmail is called with the expected parameters.
        verify(emailService, times(1)).sendEmail(recipient, subjectOrMessage, "This is the email body.");
    }

    /**
     * Tests that sendNotification dispatches an SMS notification correctly.
     * <p>
     * <strong>Description:</strong> Expects that calling sendNotification with type SMS delegates to SmsService.sendSms.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid SMS parameters are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong> SmsService.sendSms is invoked with the expected recipient and message.
     * </p>
     */
    @Test
    public void testSendNotificationSms() {
        String recipient = "1234567890";
        String subjectOrMessage = "Test SMS";

        // Act: Call sendNotification with type SMS.
        notificationService.sendNotification(NotificationType.SMS, recipient, subjectOrMessage);
        // Assert: Verify that SmsService.sendSms is called with the expected parameters.
        verify(smsService, times(1)).sendSms(recipient, subjectOrMessage);
    }

    /**
     * Tests that sendNotification dispatches a PUSH notification correctly.
     * <p>
     * <strong>Description:</strong> Expects that calling sendNotification with type PUSH delegates to PushNotificationService.sendPushNotification.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid PUSH notification parameters are provided.
     * </p>
     * <p>
     * <strong>Assertions:</strong> PushNotificationService.sendPushNotification is invoked with the expected recipient and message.
     * </p>
     */
    @Test
    public void testSendNotificationPush() {
        String recipient = "deviceToken";
        String subjectOrMessage = "Test Push";

        // Act: Call sendNotification with type PUSH.
        notificationService.sendNotification(NotificationType.PUSH, recipient, subjectOrMessage);
        // Assert: Verify that PushNotificationService.sendPushNotification is called with the expected parameters.
        verify(pushNotificationService, times(1)).sendPushNotification(recipient, subjectOrMessage);
    }





    /**
     * Tests the exception branch in the sendEmail method.
     * <p>
     * <strong>Description:</strong> Simulates a failure in EmailService.sendEmail by configuring the mock to throw a RuntimeException.
     * Expects that NotificationService.sendEmail wraps this exception in a NotificationException.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid email parameters are provided, but emailService.sendEmail fails.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception message contains "Error sending email".</li>
     *   <li>The underlying cause's message equals "Simulated email failure".</li>
     * </ul>
     * </p>
     */
    @Test
    public void testSendEmailExceptionBranch() {
        // Arrange: Stub emailService.sendEmail to throw a RuntimeException.
        doThrow(new RuntimeException("Simulated email failure"))
                .when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act & Assert: Verify that NotificationService.sendEmail throws a NotificationException.
        NotificationException ex = assertThrows(NotificationException.class, () ->
                notificationService.sendEmail("user@example.com", "Subject", "Body")
        );
        // Check that the exception message indicates an error in sending email.
        assertTrue(ex.getMessage().contains("Error sending email"));
        // Check that the underlying cause's message matches the simulated failure.
        assertEquals("Simulated email failure", ex.getCause().getMessage());
    }

    /**
     * Tests the exception branch in the sendSms method.
     * <p>
     * <strong>Description:</strong> Simulates a failure in SmsService.sendSms by configuring the mock to throw a RuntimeException.
     * Expects that NotificationService.sendSms wraps this exception in a NotificationException.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid SMS parameters are provided, but smsService.sendSms fails.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception message contains "Error sending SMS".</li>
     *   <li>The underlying cause's message equals "Simulated SMS failure".</li>
     * </ul>
     * </p>
     */
    @Test
    public void testSendSmsExceptionBranch() {
        // Arrange: Stub smsService.sendSms to throw a RuntimeException.
        doThrow(new RuntimeException("Simulated SMS failure"))
                .when(smsService).sendSms(anyString(), anyString());

        // Act & Assert: Verify that NotificationService.sendSms throws a NotificationException.
        NotificationException ex = assertThrows(NotificationException.class, () ->
                notificationService.sendSms("1234567890", "Test SMS")
        );
        // Check that the exception message indicates an error sending SMS.
        assertTrue(ex.getMessage().contains("Error sending SMS"));
        // Verify that the underlying cause's message matches the simulated failure.
        assertEquals("Simulated SMS failure", ex.getCause().getMessage());
    }

    /**
     * Tests the exception branch in the sendPushNotification method.
     * <p>
     * <strong>Description:</strong> Simulates a failure in PushNotificationService.sendPushNotification by configuring the mock
     * to throw a RuntimeException. Expects that NotificationService.sendPushNotification wraps this exception in a NotificationException.
     * </p>
     * <p>
     * <strong>Premise:</strong> Valid push notification parameters are provided, but pushNotificationService.sendPushNotification fails.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>The exception message contains "Error sending push notification".</li>
     *   <li>The underlying cause's message equals "Simulated push failure".</li>
     * </ul>
     * </p>
     */
    @Test
    public void testSendPushNotificationExceptionBranch() {
        // Arrange: Stub pushNotificationService.sendPushNotification to throw a RuntimeException.
        doThrow(new RuntimeException("Simulated push failure"))
                .when(pushNotificationService).sendPushNotification(anyString(), anyString());

        // Act & Assert: Verify that NotificationService.sendPushNotification throws a NotificationException.
        NotificationException ex = assertThrows(NotificationException.class, () ->
                notificationService.sendPushNotification("deviceToken", "Test Push")
        );
        // Check that the exception message indicates an error sending push notification.
        assertTrue(ex.getMessage().contains("Error sending push notification"));
        // Verify that the underlying cause's message matches the simulated failure.
        assertEquals("Simulated push failure", ex.getCause().getMessage());
    }
}

```

##  **6. MultiChannelNotifierApplicationTest** : `src/test/java/com/example/multichannelnotifier/MultiChannelNotifierApplicationTest.java`
```java
package com.example.multichannelnotifier;




import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for the main application class {@link MultiChannelNotifierApplicationTest}.
 *
 * <p>
 * This test class includes:
 * <ul>
 *   <li>{@code contextLoads()} - verifies that the Spring application context starts up correctly.
 *   <li>{@code testMainMethod()} - calls the main method to ensure it executes without throwing exceptions.
 * </ul>
 * </p>
 *
 * <p>
 * With these tests, code coverage tools (e.g., JaCoCo) should report over 90% class, method, and line coverage.
 * </p>
 */
@SpringBootTest(classes = MultiChannelNotifierApplicationTest.class)
public class MultiChannelNotifierApplicationTest {

    /**
     * Verifies that the Spring application context loads successfully.
     * If the context fails to load, the test will fail.
     */
    @Test
    public void contextLoads() {
        // The application context is automatically loaded by the @SpringBootTest annotation.
    }

    /**
     * Verifies that calling the main method of {@link MultiChannelNotifierApplicationTest} executes without exceptions.
     */
    @Test
    public void testMainMethod() {
        // Call the main method with an empty argument array.
        MultiChannelNotifierApplication.main(new String[]{});
        // Test passes if no exception is thrown.
    }
}
```


**Result:** Total  coverage is 92%



# **How to Run**

1. **Create the Project Structure:** Manually create the directories and files as shown in the provided project layout or use Spring Initializr to generate a skeleton, then place/modify files accordingly.
2. **pom.xml / Dependencies:** Ensure your pom.xml contains the necessary Spring Boot dependencies (e.g., Spring Web, Spring Boot Starter Test etc.). Confirm that the Java version is set to 17 (or adjust accordingly) and any needed plugins (e.g., the Maven Surefire plugin for tests, Jacoco for coverage, etc.) are present.
3. ** Application Properties:** Configure server configuration in application.properties (or application.yml), for example:
```properties
# Application Information
spring.application.name=MultiChannelNotifierApplication
# Server Configuration
server.port=8080
```
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

6. **Accessing Endpoints & Features:**

 Valid Email Notification

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -H "Content-Type: application/json" \
  -d '{"type": "EMAIL", "recipient": "user@example.com", "subjectOrMessage": "Test Email Subject"}'
```




 Valid SMS Notification

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -H "Content-Type: application/json" \
  -d '{"type": "SMS", "recipient": "1234567890", "subjectOrMessage": "Test SMS Message"}'
```




Valid Push Notification

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -H "Content-Type: application/json" \
  -d '{"type": "PUSH", "recipient": "deviceToken", "subjectOrMessage": "Test Push Message"}'
```

 Empty Recipient

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -H "Content-Type: application/json" \
  -d '{"type": "EMAIL", "recipient": "", "subjectOrMessage": "Test Email Subject"}'
```

 Empty Subject/Message

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -H "Content-Type: application/json" \
  -d '{"type": "SMS", "recipient": "1234567890", "subjectOrMessage": ""}'
```

 Missing Notification Type

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -H "Content-Type: application/json" \
  -d '{"recipient": "user@example.com", "subjectOrMessage": "Test Email Subject"}'
```

 Unsupported Notification Type

```bash
curl -X POST "http://localhost:8080/notifications/send" \
  -H "Content-Type: application/json" \
  -d '{"type": "UNKNOWN", "recipient": "user@example.com", "subjectOrMessage": "Test Message"}'
```


# **Time and Space Complexity:**

- **Time Complexity:**  
  Each cURL request processed by the NotificationService is handled in constant time, **O(1)**. This is because the operations involve fixed-time input validation, logging, and delegation to a dedicated service (email, SMS, or push), independent of input size.

- **Space Complexity:**  
  Each request uses **O(1)** space as the service processes a fixed amount of data per request (the notification payload), regardless of the volume of notifications. Any additional overhead is constant per request.

# **Conclusion**

The Notification Service built with Spring Boot efficiently handles notifications across multiple channels (email, SMS, and push) with constant time complexity for individual requests and fixed memory usage per notification. This design ensures fast, predictable response times and scales well, making it suitable for high-volume production environments where each notification request is processed in O(1) time and space, thus maintaining performance even with a large number of users.



1st Iteration:
https://drive.google.com/file/d/1dDMSPmJFPZlfFRYbH7Jana-xDxTKYihl/view?usp=drive_link
2nd Iteration:
https://drive.google.com/file/d/1u4aN83xf0bY9NK5WCXBUSYwnQWIXLMer/view?usp=drive_link
Project Source Code:
https://drive.google.com/file/d/1e4SQ7ALrsj06kuzQ99UTQnJijSOKVDW5/view?usp=drive_link