package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.constant.GuestStatus;
import com.slinkdigital.wedding.domain.Guest;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>{
    @Query(value = "select * from Guest g where wedding_id = ?1 or name like %?2% or email like %?3%", nativeQuery = true)
    Page<Guest> findByWeddingAndNameContainsOrEmailContains(Wedding wedding,String name,String email, Pageable pageable);
    
    Page<Guest> findByWedding(Wedding wedding, Pageable pageable);
    
    Optional<Guest> findByIdAndWedding(Long id, Wedding wedding);
    
    List<Guest> findByGuestStatus(GuestStatus guestStatus);
}
