package com.slinkdigital.vendor.service;

import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.dto.VendorCategoryDto;
import com.slinkdigital.vendor.dto.VendorOwnerDto;
import com.slinkdigital.vendor.dto.VendorDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
public interface VendorService {

    Page<VendorDto> getAllVendorsForUser(Long userId, String search, PageRequest pageRequest);

    Page<VendorDto> getAllApprovedVendors(String search, PageRequest pageRequest);

    VendorCategoryDto addVendorCatergory(VendorCategoryDto categoryDto);

    Boolean hasRoleVendor(Long userId);

    Map<String, String> addVendor(VendorDto vendorDto, MultipartFile coverImage, List<MultipartFile> gallery);

    VendorDto updateVerificationStatusForVendor(VendorDto vendorDto);
    
    VendorDto getVendorById(Long id);

    List<VendorCategoryDto> getVendorCategories();

    Map<String, String> registerAsPersonalVendor(PersonalVendorRegistrationRequest request, MultipartFile identityImage);
    
}
