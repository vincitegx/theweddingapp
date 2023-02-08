package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.CoupleRequest;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author TEGA
 */
public interface CoupleRequestRepository extends JpaRepository<CoupleRequest, Long>{
    Optional<CoupleRequest> findByRequestToken(String requestToken);
    Optional<CoupleRequest> findByEmailAndWedding(String email, Wedding wedding);
    List<CoupleRequest> findByEmail(String email);
    Optional<List<CoupleRequest>> findByWedding(Wedding wedding);
}
