package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.GuestMessage;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface MessageRepository extends JpaRepository<GuestMessage, Long>{
    
    List<GuestMessage> findByWedding(Wedding wedding);
    
}
