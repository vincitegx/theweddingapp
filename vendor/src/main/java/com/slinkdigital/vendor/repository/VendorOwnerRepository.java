package com.slinkdigital.vendor.repository;

import com.slinkdigital.vendor.domain.VendorOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface VendorOwnerRepository extends JpaRepository<VendorOwner, Long>{
    
    VendorOwner findByVerificationStatusAndId(String verificationStatus, String id);
    
}
