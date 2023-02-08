package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.constant.WeddingType;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.Date;
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
public interface WeddingRepository extends JpaRepository<Wedding, Long>{
//    Optional<List<Wedding>> findByCouple(Couple couple); 
    List<Wedding> findByAuthorIdOrSpouseId(Long authorId, Long spouseId);
    List<Wedding> findByIsPublishedAndAuthorIdOrSpouseId(Boolean isPublished, Long authorId, Long spouseId);
    Optional<Wedding> findByCode(String code);
    
    Page<Wedding> findByTitleContains(String title, Pageable page);
    
//    @Query(value="SELECT w.* FROM wedding w ORDER BY w.dateOfWedding DESC", nativeQuery = true)	
    @Query(value ="select w from wedding w where is_published like %?1% and wedding_type like %?2% and title like %?3%", nativeQuery = true)
    Page<Wedding> findByIsPublishedAndWeddingTypeAndTitleContains(Boolean isPublished, WeddingType weddingType, String title, Pageable pageable);
    

    @Query(value ="select w from wedding w where is_published like %?1% and wedding_type like %?2% and title like %?3% and date_of_wedding like %?4%", nativeQuery = true)
    Page<Wedding> findByIsPublishedAndWeddingTypeAndTitleContainsAndDateOfWedding(Boolean isPublished, WeddingType weddingType, String title,Date date, Pageable pageable);
}
