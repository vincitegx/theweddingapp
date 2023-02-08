package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.BudgetDto;
import java.util.List;
/**
 *
 * @author TEGA
 */
public interface BudgetService {

    List<BudgetDto> getWeddingBudgets(Long weddingId);

    BudgetDto createBudget(BudgetDto budgetDto);

    BudgetDto editBudget(BudgetDto budgetDto);

    List<BudgetDto> deleteBudget(Long id);
    
}
