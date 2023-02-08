package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.BudgetItemDto;
import com.slinkdigital.wedding.dto.WishlistDto;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author TEGA
 */
public interface BudgetItemService {
    
    Page<BudgetItemDto> getBudgetItems(Long budgetId, String search, PageRequest pageRequest);
    
    BudgetItemDto addBudgetItem(BudgetItemDto budgetItemDto);

    BudgetItemDto editBudgetItem(BudgetItemDto budgetItemDto);

    Page<BudgetItemDto> deleteBudgetItem(Long budgetItemId, PageRequest of);
    
    WishlistDto addToWishlist(Long budgetItemId);
}
