package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Reaction;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long>{
    List<Reaction> findByWedding(Wedding wedding);
}
