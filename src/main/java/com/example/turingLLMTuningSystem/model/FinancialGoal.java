package com.example.turingLLMTuningSystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "financial_goals")
public class FinancialGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double targetAmount;
    private LocalDate targetDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // Getters and Setters
}