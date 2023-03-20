package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Budget;
import com.slinkdigital.wedding.dto.BudgetDto;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class BudgetMapper {

    private final WeddingMapper weddingMapper;

    public Budget mapDtoToBudget(BudgetDto budgetDto) {
        return Budget.builder()
                .id(budgetDto.getId())
                .title(budgetDto.getTitle())
                .description(budgetDto.getDescription())
                .createdAt(budgetDto.getCreatedAt())
                .wedding(weddingMapper.mapWeddingDtoToWedding(budgetDto.getWedding()))
                .build();
    }

    public BudgetDto mapBudgetToDto(Budget budget) {
        return BudgetDto.builder()
                .id(budget.getId())
                .title(budget.getTitle())
                .description(budget.getDescription())
                .createdAt(budget.getCreatedAt())
                .wedding(weddingMapper.mapWeddingToDto(budget.getWedding()))
                .build();
    }

}
