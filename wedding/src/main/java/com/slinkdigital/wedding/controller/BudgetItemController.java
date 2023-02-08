package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.BudgetItemDto;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.service.BudgetItemService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class BudgetItemController {
    
    private final BudgetItemService budgetItemService;
    
    @GetMapping("budgets/{budgetId}/items")
    public ResponseEntity<ApiResponse> getBudgetItems(@PathVariable(value = "budgetId") Long budgetId,
            @RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<BudgetItemDto> budgets = budgetItemService.getBudgetItems(budgetId,
                search.orElse(""),
                PageRequest.of(page.orElse(0), length.orElse(8), direction, sortBy.orElse("id")));
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("budgets", budgets))
                        .message("List Of Budgets Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping("budgets/items")
    public ResponseEntity<ApiResponse> addBudgetItem(@Valid @RequestBody BudgetItemDto budgetItemDto){
        BudgetItemDto budget = budgetItemService.addBudgetItem(budgetItemDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("budget", budget))
                        .message("Budget Created Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping("budgets/items/{budgetItemId}")
    public ResponseEntity<ApiResponse> addBudgetItemToWishlist(@PathVariable(value = "budgetItemId") Long id){
        WishlistDto wishlist = budgetItemService.addToWishlist(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wishlist", wishlist))
                        .message("Wishlist Created Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @PutMapping("budgets/items")
    public ResponseEntity<ApiResponse> editBudgetItem(@Valid @RequestBody BudgetItemDto budgetItemDto){
        BudgetItemDto budget = budgetItemService.editBudgetItem(budgetItemDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("budget", budget))
                        .message("Budget Updated Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("budgets/items/{id}")
    public ResponseEntity<ApiResponse> deleteBudgetItem(@PathVariable Long id){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<BudgetItemDto> budgets = budgetItemService.deleteBudgetItem(id,
                PageRequest.of(0, 8, direction, "id"));
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
