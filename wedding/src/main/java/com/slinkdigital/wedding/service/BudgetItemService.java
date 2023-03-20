package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Budget;
import com.slinkdigital.wedding.domain.BudgetItem;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.BudgetItemDto;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.BudgetItemMapper;
import com.slinkdigital.wedding.repository.BudgetItemRepository;
import com.slinkdigital.wedding.repository.BudgetRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import com.slinkdigital.wedding.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;
    private final HttpServletRequest request;
    private final BudgetItemMapper budgetItemMapper;
    private final WeddingRepository weddingRepository;
    private final BudgetRepository budgetRepository;
    private final WishlistService wishlistService;

    public Page<BudgetItemDto> getBudgetItems(Long budgetId, String search, PageRequest pageRequest) {
        try {
            Budget budget = budgetRepository.findById(budgetId).orElseThrow(() -> new WeddingException("No Wedding Associated To This Id"));
            Wedding wedding = weddingRepository.findById(budget.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                Page<BudgetItem> budgetItems = budgetItemRepository.findByBudgetAndItemContains(budget, search, pageRequest);
                List<BudgetItem> budgetItemList = budgetItems.toList();
                Page<BudgetItemDto> budgetItemDto = new PageImpl(budgetItemList);
                budgetItems.forEach(budgetItem -> {
                    budgetItemDto.and(budgetItemMapper.mapBudgetItemToDto(budgetItem));
                });
                return budgetItemDto;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public BudgetItemDto addBudgetItem(BudgetItemDto budgetItemDto) {
        try {
            Wedding wedding = weddingRepository.findById(budgetItemDto.getBudget().getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                BudgetItem budgetItem = budgetItemMapper.mapDtoToBudgetItem(budgetItemDto);
                budgetItem = budgetItemRepository.saveAndFlush(budgetItem);
                return budgetItemMapper.mapBudgetItemToDto(budgetItem);
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public BudgetItemDto editBudgetItem(BudgetItemDto budgetItemDto) {
        try {
            BudgetItem budgetItem = budgetItemRepository.findById(budgetItemDto.getId()).orElseThrow(() -> new WeddingException("No Such Item Matches Id"));
            Wedding wedding = weddingRepository.findById(budgetItem.getBudget().getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                budgetItem.setAmount(budgetItemDto.getAmount());
                budgetItem.setItem(budgetItemDto.getItem());
                budgetItem.setItemLink(budgetItemDto.getItemLink());
                budgetItem.setQuantity(budgetItemDto.getQuantity());
                budgetItem.setItemStatus(budgetItemDto.getItemStatus());
                budgetItem = budgetItemRepository.saveAndFlush(budgetItem);
                return budgetItemMapper.mapBudgetItemToDto(budgetItem);
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public Page<BudgetItemDto> deleteBudgetItem(Long id, PageRequest of) {
        try {
            BudgetItem budgetItem = budgetItemRepository.findById(id).orElseThrow(() -> new WeddingException("No Item Associated To This Id"));
            Wedding wedding = weddingRepository.findById(budgetItem.getBudget().getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                budgetItemRepository.delete(budgetItem);
                Page<BudgetItem> budgetItems = budgetItemRepository.findByBudgetAndItemContains(budgetItem.getBudget(), "", of);
                List<BudgetItem> budgetItemList = budgetItems.toList();
                Page<BudgetItemDto> budgetItemDto = new PageImpl(budgetItemList);
                budgetItems.forEach(b -> {
                    budgetItemDto.and(budgetItemMapper.mapBudgetItemToDto(b));
                });
                return budgetItemDto;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WishlistDto addToWishlist(Long budgetItemId) {
        try {
            BudgetItem budgetItem = budgetItemRepository.findById(budgetItemId).orElseThrow(() -> new WeddingException("No Item Associated To This Id"));
            Wedding wedding = weddingRepository.findById(budgetItem.getBudget().getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                WishlistDto wishlist = wishlistService.addWishlist(budgetItemMapper.mapBudgetToWishlist(budgetItem));
                return wishlist;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    private Long getLoggedInUserId() {
        try {
            return Long.parseLong(request.getHeader("x-id"));
        } catch (WeddingException ex) {
            throw new WeddingException("You Need To Be Logged In !!!");
        }
    }
}
