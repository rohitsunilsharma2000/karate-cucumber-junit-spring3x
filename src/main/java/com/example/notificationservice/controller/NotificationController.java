package com.example.notificationservice.controller;

import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for sending notifications.
 *
 * <p><strong>Overview:</strong></p>
 * This controller provides an endpoint for sending notifications. It supports multiple notification types
 * (EMAIL, SMS, and PUSH) by delegating the processing to the {@link NotificationService}. Input parameters are validated
 * to ensure that they are complete and adhere to expected formats.
 *
 * <p><strong>References:</strong></p>
 * <ul>
 *   <li>{@link NotificationService} handles the business logic for sending notifications.</li>
 *   <li>{@link NotificationType} defines the supported types of notifications.</li>
 * </ul>
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *   <li>Enables clients to send a notification by providing the notification type, recipient identifier, and subject/message content.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> The notification is sent successfully and a corresponding success HTTP status is returned.</li>
 *   <li><strong>Fail:</strong> If any input validation fails or processing errors occur, appropriate exceptions are thrown
 *       and handled by the global exception mechanism.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@RestController
@RequestMapping("/notifications")
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
     * This endpoint allows clients to send a notification by specifying:
     * <ul>
     *   <li><strong>type</strong> - The type of notification to be sent (EMAIL, SMS, or PUSH).</li>
     *   <li><strong>recipient</strong> - The identifier of the recipient. For example, an email address for EMAIL notifications,
     *       a phone number for SMS, or a device token for PUSH notifications.</li>
     *   <li><strong>subjectOrMessage</strong> - The subject or message content of the notification.</li>
     * </ul>
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>type:</strong> ({@link NotificationType}) The notification type. Acceptable values: EMAIL, SMS, PUSH.</li>
     *   <li><strong>recipient:</strong> ({@link String}) The recipient's identifier, which must be non-null and non-empty.</li>
     *   <li><strong>subjectOrMessage:</strong> ({@link String}) The subject or message content, which must be non-null and non-empty.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *   <li>If the <code>recipient</code> or <code>subjectOrMessage</code> parameters are null or empty, input validation
     *       in the service layer will throw an appropriate exception.</li>
     *   <li>If the specified notification type is unsupported, the service layer will raise an error.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> The notification is processed successfully and the endpoint returns an HTTP 200 (OK) status.</li>
     *   <li><strong>Fail:</strong> In case of input validation failure or processing errors, exceptions are thrown and handled
     *       by the global exception handlers, resulting in appropriate error responses (e.g., HTTP 400 or HTTP 500).</li>
     * </ul>
     *
     * @param type             the notification type (EMAIL, SMS, PUSH).
     * @param recipient        the recipient's identifier.
     * @param subjectOrMessage the subject or message content.
     * @return a {@link ResponseEntity} indicating the result of the notification send operation.
     */
    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestParam NotificationType type,
                                                 @RequestParam String recipient,
                                                 @RequestParam String subjectOrMessage) {
        notificationService.sendNotification(type, recipient, subjectOrMessage);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
