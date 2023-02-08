package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.BudgetItem;
import com.slinkdigital.wedding.dto.BudgetItemDto;
import com.slinkdigital.wedding.dto.WishlistDto;

/**
 *
 * @author TEGA
 */
public interface BudgetItemMapper {
    
    BudgetItem mapDtoToBudgetItem(BudgetItemDto budgetItemDto);
    
    BudgetItemDto mapBudgetItemToDto(BudgetItem budgetItem);
    
    WishlistDto mapBudgetToWishlist(BudgetItem budgetItem);
}
