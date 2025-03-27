package com.example.userqueryhub.repository;

import com.example.userqueryhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface extends Spring Data JPA's {@link JpaRepository} to provide CRUD operations and query methods
 * for the User entity without requiring explicit implementation. It serves as the data access layer in the UserPurge application.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>CRUD Operations:</strong> Inherits standard create, read, update, and delete methods from {@code JpaRepository}.</li>
 *   <li><strong>Query Derivation:</strong> Supports query method derivation, allowing custom finder methods to be defined by following Spring Data naming conventions.</li>
 *   <li><strong>Integration with Spring Data JPA:</strong> Seamlessly integrates with Spring Boot to automatically implement repository logic at runtime.</li>
 * </ul>
 *
 * <p>
 * This repository is used by the service layer to interact with the database, ensuring that user data is persisted and retrieved efficiently.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
