package com.slinkdigital.wedding.mapper.impl;

import com.slinkdigital.wedding.domain.BudgetItem;
import com.slinkdigital.wedding.dto.BudgetItemDto;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.mapper.BudgetItemMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class BudgetItemMapperImpl implements BudgetItemMapper{

    private final WeddingMapper weddingMapper;
    
    @Override
    public BudgetItem mapDtoToBudgetItem(BudgetItemDto budgetItemDto) {
        return BudgetItem.builder()
                .id(budgetItemDto.getId())
                .amount(budgetItemDto.getAmount())
                .item(budgetItemDto.getItem())
                .itemLink(budgetItemDto.getItemLink())
                .quantity(budgetItemDto.getQuantity())
                .itemStatus(budgetItemDto.getItemStatus())
                .build();
    }

    @Override
    public BudgetItemDto mapBudgetItemToDto(BudgetItem budgetItem) {
        return BudgetItemDto.builder()
                .id(budgetItem.getId())
                .amount(budgetItem.getAmount())
                .item(budgetItem.getItem())
                .itemLink(budgetItem.getItemLink())
                .quantity(budgetItem.getQuantity())
                .itemStatus(budgetItem.getItemStatus())
                .build();
    }

    @Override
    public WishlistDto mapBudgetToWishlist(BudgetItem budgetItem) {
        return WishlistDto.builder()
                .amount(budgetItem.getAmount())
                .name(budgetItem.getItem())
                .itemLink(budgetItem.getItemLink())
                .quantity(budgetItem.getQuantity())
                .wedding(weddingMapper.mapWeddingToDto(budgetItem.getBudget().getWedding()))
                .build();
    }
    
}
