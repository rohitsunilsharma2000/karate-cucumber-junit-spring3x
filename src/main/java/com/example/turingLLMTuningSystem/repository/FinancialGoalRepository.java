package com.example.turingLLMTuningSystem.repository;

import com.example.turingLLMTuningSystem.model.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
}
