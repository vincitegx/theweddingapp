package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.BudgetItem;
import com.slinkdigital.wedding.dto.BudgetItemDto;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class BudgetItemMapper {

    private final WeddingMapper weddingMapper;

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
