package com.slinkdigital.vendor.repository;

import com.slinkdigital.vendor.constant.VerificationStatus;
import com.slinkdigital.vendor.domain.Vendor;
import com.slinkdigital.vendor.domain.VendorOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long>{
    VendorOwner findByVerificationStatusAndId(String verificationStatus, String id);
    Page<Vendor> findByVerificationStatusAndNameContains(VerificationStatus verificationStatus,String name, Pageable pageable);
    
    Page<Vendor> findByUserIdAndNameContains(Long userId,String name, Pageable pageable);
    
    Vendor findByVerificationStatusAndId(String verificationStatus, Long id);
}
