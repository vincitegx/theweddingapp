package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.BudgetDto;
import com.slinkdigital.wedding.service.BudgetService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("{weddingId}/budgets")
    @ResponseStatus(HttpStatus.OK)
    public List<BudgetDto> getWeddingBudgets(@PathVariable(value = "weddingId") Long id) {
        return budgetService.getWeddingBudgets(id);
    }

    @PostMapping("budgets")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDto createBudget(@Valid @RequestBody BudgetDto budgetDto) {
        return budgetService.createBudget(budgetDto);
    }

    @PutMapping("budgets")
    @ResponseStatus(HttpStatus.OK)
    public BudgetDto editBudget(@Valid @RequestBody BudgetDto budgetDto) {
        return budgetService.editBudget(budgetDto);
    }

    @DeleteMapping("budgets/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }
}
