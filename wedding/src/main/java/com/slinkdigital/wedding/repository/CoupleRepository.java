package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Couple;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author TEGA
 */
public interface CoupleRepository extends JpaRepository<Couple, Long>{        
    
    Optional<Couple> findByUserId(Long userId);
    List<Couple> findByWedding(Wedding wedding);
    Optional<Couple> findByUserIdAndWedding(Long userId, Wedding wedding);
}
