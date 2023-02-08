package com.slinkdigital.wedding.service.impl;

import com.slinkdigital.wedding.domain.Budget;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.BudgetDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.BudgetMapper;
import com.slinkdigital.wedding.repository.BudgetRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import com.slinkdigital.wedding.service.BudgetService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final WeddingRepository weddingRepository;
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final HttpServletRequest request;

    @Override
    public List<BudgetDto> getWeddingBudgets(Long weddingId) {
        try {
            Wedding wedding = weddingRepository.findById(weddingId).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                List<Budget> budgetList = budgetRepository.findByWedding(wedding);
                List<BudgetDto> budget = new ArrayList<>();
                budgetList.forEach(b -> {
                    budget.add(budgetMapper.mapBudgetToDto(b));
                });
                return budget;
            }
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public BudgetDto createBudget(BudgetDto budgetDto) {
        try {
            Wedding wedding = weddingRepository.findById(budgetDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
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

    @Override
    public BudgetDto editBudget(BudgetDto budgetDto) {
        try {
            Wedding wedding = weddingRepository.findById(budgetDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
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

    @Override
    public List<BudgetDto> deleteBudget(Long id) {
        try {
            Budget budget = budgetRepository.findById(id).orElseThrow(() -> new WeddingException("No budget associated to this id"));
            Wedding wedding = weddingRepository.findById(budget.getWedding().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
            Long loggedInUser = getLoggedInUserId();
            if (loggedInUser == null || (!loggedInUser.equals(wedding.getAuthorId()) && !loggedInUser.equals(wedding.getSpouseId()))) {
                throw new WeddingException("Cannot Identify The User, Therefore operation cannot be performed");
            } else if (!wedding.isPublished()) {
                throw new WeddingException("You have to publish this wedding first");
            } else {
                budgetRepository.delete(budget);
                List<Budget> budgetList = budgetRepository.findByWedding(wedding);
                List<BudgetDto> bg = new ArrayList<>();
                budgetList.forEach(b -> {
                    bg.add(budgetMapper.mapBudgetToDto(b));
                });
                return bg;
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
