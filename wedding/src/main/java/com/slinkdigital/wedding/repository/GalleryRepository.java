package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Gallery;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author TEGA
 */
public interface GalleryRepository extends JpaRepository<Gallery, Long>{
    Optional<Gallery> findByWedding(Wedding wedding);
}
