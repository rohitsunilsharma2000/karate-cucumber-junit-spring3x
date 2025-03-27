package com.example.userqueryhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a user entity in the UserPurge system.
 * <p>
 * This class models the user details for the UserPurge project, which is focused on efficiently removing user entities from the database.
 * It is mapped to the "user" table using JPA annotations and serves as a fundamental component for performing CRUD operations.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Entity Mapping:</strong> Annotated with {@code @Entity} and {@code @Table} to map the class to the database table "user".</li>
 *   <li><strong>Lombok Integration:</strong> Uses Lombok's {@code @Getter} and {@code @Setter} to automatically generate getter and setter methods, reducing boilerplate code.</li>
 *   <li><strong>Primary Key Generation:</strong> The {@code id} field is marked with {@code @Id} and {@code @GeneratedValue} using the {@code GenerationType.IDENTITY} strategy to automatically generate unique identifiers.</li>
 *   <li><strong>Essential Attributes:</strong> Contains core attributes such as {@code username} and {@code email} to represent user information.</li>
 * </ul>
 *
 * <p>
 * This entity is used by the service layer to manage user data and is critical for the functionality of operations such as user deletion,
 * ensuring that the data integrity and business logic of the system are maintained.
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;


}
