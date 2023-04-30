package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.GuestMessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface GuestMessageTemplateRepository extends JpaRepository<GuestMessageTemplate, Long>{
    
}
