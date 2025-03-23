package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "thread")  // <-- match the table name EXACTLY as in your DB schema
@Builder
@AllArgsConstructor // <-- Ensures the full constructor is public
@NoArgsConstructor
public class Threads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @ManyToOne
    private User user;

    // Getters and setters

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

}
