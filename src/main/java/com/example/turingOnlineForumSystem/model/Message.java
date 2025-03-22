package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    // Getters and setters
}
