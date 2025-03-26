package com.example.turingLLMTuningSystem.controller;

import com.example.turingLLMTuningSystem.model.Budget;
import com.example.turingLLMTuningSystem.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        Budget createdBudget = budgetService.createBudget(budget);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Budget>> getBudgets(@PathVariable Long userId) {
        List<Budget> budgets = budgetService.getBudgetsByUser(userId);
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }
}
