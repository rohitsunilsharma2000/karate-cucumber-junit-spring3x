package com.example.turingLLMTuningSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private Double amount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // Getters and Setters
}