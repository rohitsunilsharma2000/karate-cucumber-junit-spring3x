package com.example.userqueryhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for transferring user data between layers.
 *
 * <p>
 * This DTO is used to encapsulate user details in the UserPurge system. It includes fields for the username,
 * email, and password, along with validation constraints to ensure data integrity and consistency.
 * </p>
 *
 * <h3>Validation Constraints:</h3>
 * <ul>
 *   <li><strong>username:</strong> Must not be blank.</li>
 *   <li><strong>email:</strong> Must be a valid email format and not blank.</li>
 *   <li><strong>password:</strong> Must not be blank; additional security constraints can be applied as needed.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-27
 */
@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
