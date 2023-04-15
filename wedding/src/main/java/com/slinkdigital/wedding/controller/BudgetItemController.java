package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.BudgetItemDto;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.service.BudgetItemService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings/budgets")
@RequiredArgsConstructor
public class BudgetItemController {

    private final BudgetItemService budgetItemService;

    @GetMapping("{budgetId}/items")
    @ResponseStatus(HttpStatus.OK)
    public Page<BudgetItemDto> getBudgetItems(@PathVariable(value = "budgetId") Long budgetId, Pageable pageable, 
            @RequestParam Optional<String> search) {
        return budgetItemService.getBudgetItems(budgetId, search.orElse(""), pageable);
    }

    @PostMapping("items")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetItemDto addBudgetItem(@Valid @RequestBody BudgetItemDto budgetItemDto) {
        return budgetItemService.addBudgetItem(budgetItemDto);
    }

    @PostMapping("items/{budgetItemId}/wishlist")
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistDto addBudgetItemToWishlist(@PathVariable(value = "budgetItemId") Long id) {
        return budgetItemService.addToWishlist(id);
    }

    @PutMapping("items")
    @ResponseStatus(HttpStatus.OK)
    public BudgetItemDto editBudgetItem(@Valid @RequestBody BudgetItemDto budgetItemDto) {
        return budgetItemService.editBudgetItem(budgetItemDto);
    }

    @DeleteMapping("items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudgetItem(@PathVariable Long id) {
        budgetItemService.deleteBudgetItem(id);
    }
}
