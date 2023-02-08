package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.domain.Wishlist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{
    List<Wishlist> findByWedding(Wedding wedding);
}
