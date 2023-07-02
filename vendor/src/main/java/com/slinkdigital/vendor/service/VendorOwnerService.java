package com.slinkdigital.vendor.service;

import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.dto.VendorOwnerDto;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
public interface VendorOwnerService {
    
    VendorOwnerDto getVendorOwnerById(Long id);
    
    VendorOwnerDto updateVerificationStatusForUserAsVendor(VendorOwnerDto vendorDetailsDto);
}
