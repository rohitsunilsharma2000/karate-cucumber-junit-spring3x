package com.example.multichannelnotifier.dto;


import com.example.multichannelnotifier.enums.NotificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object for sending notification requests.
 *
 * <p><strong>Overview:</strong> Encapsulates the parameters needed to send a notification.
 * This DTO includes input validation annotations to ensure that required fields are provided.</p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@Getter
@Setter
@ToString
public class NotificationRequestDTO {

    /**
     * The notification type. Allowed values are EMAIL, SMS, or PUSH.
     */
    @NotNull(message = "Notification type cannot be null")
    private NotificationType type;

    /**
     * The recipient's identifier, such as an email address, phone number, or device token.
     */
    @NotBlank(message = "Recipient cannot be blank")
    private String recipient;

    /**
     * The subject or message content for the notification.
     */
    @NotBlank(message = "Subject or message cannot be blank")
    private String subjectOrMessage;
}
