package com.slinkdigital.vendor.repository;

import com.slinkdigital.vendor.domain.VendorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface VendorCategoryRepository extends JpaRepository<VendorCategory, Long>{
}
