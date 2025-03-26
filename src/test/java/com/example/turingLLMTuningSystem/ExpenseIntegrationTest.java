package com.example.turingLLMTuningSystem;

import com.example.turingLLMTuningSystem.repository.ExpenseRepository;
import com.example.turingLLMTuningSystem.repository.UserRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Expense entity persistence and retrieval.
 */
@DataJpaTest
class ExpenseIntegrationTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Tests saving and retrieving an Expense entity.
     */
    @Test
    void testCreateAndRetrieveExpense() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        User savedUser = userRepository.save(user);

        Expense expense = new Expense();
        expense.setDescription("Lunch");
        expense.setAmount(25.50);
        expense.setDate(LocalDate.of(2025, 3, 25));
        expense.setUser(savedUser);

        // Act
        Expense savedExpense = expenseRepository.save(expense);
        Optional<Expense> retrievedExpense = expenseRepository.findById(savedExpense.getId());

        // Assert
        assertThat(retrievedExpense).isPresent();
        assertThat(retrievedExpense.get().getDescription()).isEqualTo("Lunch");
        assertThat(retrievedExpense.get().getAmount()).isEqualTo(25.50);
        assertThat(retrievedExpense.get().getDate()).isEqualTo(LocalDate.of(2025, 3, 25));
        assertThat(retrievedExpense.get().getUser().getUsername()).isEqualTo("testuser");
    }
}

