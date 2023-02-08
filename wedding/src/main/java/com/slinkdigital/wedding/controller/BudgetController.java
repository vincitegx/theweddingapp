package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.BudgetDto;
import com.slinkdigital.wedding.service.BudgetService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<ApiResponse> getBudgetForWedding(@PathVariable(value = "weddingId") Long id) {
        List<BudgetDto> budget = budgetService.getWeddingBudgets(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("budget", budget))
                        .message("Budget Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("budgets")
    public ResponseEntity<ApiResponse> createBudget(@Valid @RequestBody BudgetDto budgetDto) {
        BudgetDto budget = budgetService.createBudget(budgetDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("budget", budget))
                        .message("Budget Created Successfully")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("budgets")
    public ResponseEntity<ApiResponse> editBudget(@Valid @RequestBody BudgetDto budgetDto) {
        BudgetDto budget = budgetService.editBudget(budgetDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("budget", budget))
                        .message("Budget Updated Successfully")
                        .status(OK)
                        .build()
        );
    }

    @DeleteMapping("budgets/{id}")
    public ResponseEntity<ApiResponse> deleteBudget(@PathVariable Long id) {
        List<BudgetDto> budgets = budgetService.deleteBudget(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("budgets", budgets))
                        .message("Budget Deleted Successfully")
                        .status(OK)
                        .build()
        );
    }
}
