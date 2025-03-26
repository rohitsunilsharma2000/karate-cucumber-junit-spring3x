package com.example.turingLLMTuningSystem;


import com.example.turingLLMTuningSystem.model.Budget;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Budget entity persistence.
 * Validates correct persistence and retrieval from the database.
 */
@DataJpaTest
class BudgetIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    /**
     * Tests persisting and retrieving a Budget entity.
     */
    @Test
    @Transactional
    void testPersistAndRetrieveBudget() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("user@test.com");
        entityManager.persist(user);

        Budget budget = new Budget();
        budget.setCategory("Travel");
        budget.setAmount(500.0);
        budget.setUser(user);

        entityManager.persist(budget);
        entityManager.flush();
        entityManager.clear();

        Budget foundBudget = entityManager.find(Budget.class, budget.getId());

        assertThat(foundBudget).isNotNull();
        assertThat(foundBudget.getCategory()).isEqualTo("Travel");
        assertThat(foundBudget.getAmount()).isEqualTo(500.0);
        assertThat(foundBudget.getUser().getUsername()).isEqualTo("testUser");
    }
}

