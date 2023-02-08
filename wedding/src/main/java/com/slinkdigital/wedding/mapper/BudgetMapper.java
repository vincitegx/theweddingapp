package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Budget;
import com.slinkdigital.wedding.dto.BudgetDto;

/**
 *
 * @author TEGA
 */
public interface BudgetMapper {
    
    Budget mapDtoToBudget(BudgetDto budgetDto);
    
    BudgetDto mapBudgetToDto(Budget budget);
}
