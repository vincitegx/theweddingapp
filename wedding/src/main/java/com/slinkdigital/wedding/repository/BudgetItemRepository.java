package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Budget;
import com.slinkdigital.wedding.domain.BudgetItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long>{

    public Page<BudgetItem> findByBudgetAndItemContains(Budget budget, String search, Pageable pageable);
    
}
