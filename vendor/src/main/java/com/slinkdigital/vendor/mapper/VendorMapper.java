package com.slinkdigital.vendor.mapper;

import com.slinkdigital.vendor.domain.Vendor;
import com.slinkdigital.vendor.domain.VendorOwner;
import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.dto.VendorDto;
import com.slinkdigital.vendor.dto.VendorOwnerDto;

/**
 *
 * @author TEGA
 */
public interface VendorMapper {
    
    Vendor mapDtoToVendor(VendorDto vendorDto);
    
    VendorDto mapVendorToDto(Vendor vendor);

    Vendor mapRegistrationRequestToVendorDto(PersonalVendorRegistrationRequest request);

    PersonalVendorRegistrationRequest getPersonalVendorRegistrationRequestJson(String request);
}
