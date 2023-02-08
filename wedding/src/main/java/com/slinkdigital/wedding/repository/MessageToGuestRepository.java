package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.MessageToGuest;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface MessageToGuestRepository extends JpaRepository<MessageToGuest, Long>{
    
    List<MessageToGuest> findByWedding(Wedding wedding);
    
}
