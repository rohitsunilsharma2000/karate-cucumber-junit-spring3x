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
