package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moderation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Reference to the user who was moderated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Hibernate-specific
    private User user;

    // 💡 Allow nullable & delete-safe relationship to Threads
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "thread_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL) // ← key fix
    private Threads thread;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
