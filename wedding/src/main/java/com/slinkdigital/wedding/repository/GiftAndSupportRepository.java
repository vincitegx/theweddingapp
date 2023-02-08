package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.GiftAndSupport;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface GiftAndSupportRepository extends JpaRepository<GiftAndSupport, Long>{
    List<GiftAndSupport> findByWedding(Wedding weddingId);
}
