package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.GuestSetting;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface GuestSettingRepository extends JpaRepository<GuestSetting, Long>{
    
    Optional<GuestSetting> findByWedding(Wedding wedding);
}
