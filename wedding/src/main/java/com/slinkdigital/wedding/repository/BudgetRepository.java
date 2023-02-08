package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Budget;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>{
    
    List<Budget> findByWedding(Wedding wedding);
    
}
