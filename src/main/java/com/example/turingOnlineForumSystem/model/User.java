package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_banned")
    private Boolean banned = false;  // Use Boolean instead of boolean


    // Getters and setters
}