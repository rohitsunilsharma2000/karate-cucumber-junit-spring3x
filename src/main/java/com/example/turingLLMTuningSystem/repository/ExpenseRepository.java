package com.example.turingLLMTuningSystem.repository;

import com.example.turingLLMTuningSystem.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
