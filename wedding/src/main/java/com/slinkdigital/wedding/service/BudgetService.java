package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Budget;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.BudgetDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.BudgetMapper;
import com.slinkdigital.wedding.repository.BudgetRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BudgetService {

    private final WeddingRepository weddingRepository;
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final HttpServletRequest request;

    public List<BudgetDto> getWeddingBudgets(Long weddingId) {

        Wedding wedding = weddingRepository.findById(weddingId).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
            throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
        } else if (!wedding.getIsPublished()) {
            throw new WeddingException("You have to publish this wedding first");
        } else {
            List<Budget> budgetList = budgetRepository.findByWedding(wedding);
            List<BudgetDto> budget = new ArrayList<>();
            budgetList.forEach(b -> {
                budget.add(budgetMapper.mapBudgetToDto(b));
            });
            return budget;
        }
    }

    public BudgetDto createBudget(BudgetDto budgetDto) {
        try {
            Wedding wedding = weddingRepository.findById(budgetDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.getIsPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                budgetDto.setCreatedAt(LocalDateTime.now());
                Budget budget = budgetMapper.mapDtoToBudget(budgetDto);
                budget = budgetRepository.saveAndFlush(budget);
                return budgetMapper.mapBudgetToDto(budget);
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public BudgetDto editBudget(BudgetDto budgetDto) {
        try {
            Wedding wedding = weddingRepository.findById(budgetDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.getIsPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                Budget budget = budgetRepository.findById(budgetDto.getId()).orElseThrow(() -> new WeddingException("No budget associated to this id"));
                budget.setDescription(budgetDto.getDescription());
                budget.setTitle(budgetDto.getTitle());
                budget = budgetRepository.saveAndFlush(budget);
                return budgetMapper.mapBudgetToDto(budget);
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public void deleteBudget(Long id) {
        try {
            Budget budget = budgetRepository.findById(id).orElseThrow(() -> new WeddingException("No budget associated to this id"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(budget.getWedding().getAuthorId()) && !loggedInUser.equals(budget.getWedding().getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!budget.getWedding().getIsPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                budgetRepository.delete(budget);
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
