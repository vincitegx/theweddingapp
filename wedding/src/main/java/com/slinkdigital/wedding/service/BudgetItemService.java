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
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;
    private final HttpServletRequest request;
    private final BudgetItemMapper budgetItemMapper;
    private final WeddingRepository weddingRepository;
    private final BudgetRepository budgetRepository;
    private final WishlistService wishlistService;
    private static final String WEDDING_NOT_PUBLISHED = "You have to publish this wedding first";
    private static final String AUTHORIZATION_ERROR = "Cannot Identify The User, Therefore operation cannot be performed";
    private static final String WEDDING_NOT_FOUND = "No Such Wedding";

    public Page<BudgetItemDto> getBudgetItems(Long budgetId, String search, Pageable pageable) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow(() -> new WeddingException("No Wedding Associated To This Id"));
        Wedding wedding = weddingRepository.findById(budget.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException(WEDDING_NOT_PUBLISHED);
        } else {
            Page<BudgetItem> budgetItems = budgetItemRepository.findByBudgetAndItemContains(budget, search, pageable);
            List<BudgetItem> budgetItemList = budgetItems.toList();
            Page<BudgetItemDto> budgetItemDto = new PageImpl(budgetItemList);
            budgetItems.forEach(budgetItem -> budgetItemDto.and(budgetItemMapper.mapBudgetItemToDto(budgetItem)));
            return budgetItemDto;
        }
    }

    public BudgetItemDto addBudgetItem(BudgetItemDto budgetItemDto) {
        Wedding wedding = weddingRepository.findById(budgetItemDto.getBudget().getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException(WEDDING_NOT_PUBLISHED);
        } else {
            BudgetItem budgetItem = budgetItemMapper.mapDtoToBudgetItem(budgetItemDto);
            budgetItem = budgetItemRepository.saveAndFlush(budgetItem);
            return budgetItemMapper.mapBudgetItemToDto(budgetItem);
        }
    }

    public BudgetItemDto editBudgetItem(BudgetItemDto budgetItemDto) {
        BudgetItem budgetItem = budgetItemRepository.findById(budgetItemDto.getId()).orElseThrow(() -> new WeddingException("No Such Item Matches Id"));
        Wedding wedding = weddingRepository.findById(budgetItem.getBudget().getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException(WEDDING_NOT_PUBLISHED);
        } else {
            budgetItem.setAmount(budgetItemDto.getAmount());
            budgetItem.setItem(budgetItemDto.getItem());
            budgetItem.setItemLink(budgetItemDto.getItemLink());
            budgetItem.setQuantity(budgetItemDto.getQuantity());
            budgetItem.setItemStatus(budgetItemDto.getItemStatus());
            budgetItem = budgetItemRepository.saveAndFlush(budgetItem);
            return budgetItemMapper.mapBudgetItemToDto(budgetItem);
        }
    }

    public void deleteBudgetItem(Long id) {
        BudgetItem budgetItem = budgetItemRepository.findById(id).orElseThrow(() -> new WeddingException("No Item Associated To This Id"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(budgetItem.getBudget().getWedding().getAuthorId()) && !loggedInUser.equals(budgetItem.getBudget().getWedding().getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (!budgetItem.getBudget().getWedding().getIsPublished()) {
            throw new WeddingException(WEDDING_NOT_PUBLISHED);
        } else {
            budgetItemRepository.delete(budgetItem);
        }
    }

    public WishlistDto addToWishlist(Long budgetItemId) {
        BudgetItem budgetItem = budgetItemRepository.findById(budgetItemId).orElseThrow(() -> new WeddingException("No Item Associated To This Id"));
        Wedding wedding = weddingRepository.findById(budgetItem.getBudget().getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException(AUTHORIZATION_ERROR);
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException(WEDDING_NOT_PUBLISHED);
        } else {
            return wishlistService.addWishlist(budgetItemMapper.mapBudgetToWishlist(budgetItem));
        }
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }
}
