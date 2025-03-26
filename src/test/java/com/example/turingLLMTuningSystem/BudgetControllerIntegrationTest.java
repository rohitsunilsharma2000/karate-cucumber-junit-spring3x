package com.example.turingLLMTuningSystem;


import com.example.turingLLMTuningSystem.controller.BudgetController;
import com.example.turingLLMTuningSystem.model.Budget;
import com.example.turingLLMTuningSystem.model.User;
import com.example.turingLLMTuningSystem.service.BudgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for BudgetController.
 * Verifies functionality for budget creation and retrieval by user.
 */
@WebMvcTest(BudgetController.class)
class BudgetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetService budgetService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests the creation of a new budget.
     */
    @Test
    void testCreateBudget() throws Exception {
        User user = new User();
        user.setId(1L);

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setCategory("Entertainment");
        budget.setAmount(300.0);
        budget.setUser(user);

        Mockito.when(budgetService.createBudget(any(Budget.class))).thenReturn(budget);

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budget)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.category").value("Entertainment"))
                .andExpect(jsonPath("$.amount").value(300.0));
    }

    /**
     * Tests retrieving budgets associated with a specific user ID.
     */
    @Test
    void testGetBudgetsByUserId() throws Exception {
        User user = new User();
        user.setId(1L);

        Budget budget1 = new Budget();
        budget1.setId(1L);
        budget1.setCategory("Groceries");
        budget1.setAmount(200.0);
        budget1.setUser(user);

        Budget budget2 = new Budget();
        budget2.setId(2L);
        budget2.setCategory("Bills");
        budget2.setAmount(150.0);
        budget2.setUser(user);

        List<Budget> budgets = List.of(budget1, budget2);

        Mockito.when(budgetService.getBudgetsByUser(1L)).thenReturn(budgets);

        mockMvc.perform(get("/api/budgets/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].category").value("Groceries"))
                .andExpect(jsonPath("$[1].category").value("Bills"));
    }
}
