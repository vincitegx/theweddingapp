package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Message;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
    
    List<Message> findByWedding(Wedding wedding);
    
}
