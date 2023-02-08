package com.slinkdigital.vendor.mapper;

import com.slinkdigital.vendor.domain.VendorOwner;
import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.dto.VendorOwnerDto;

/**
 *
 * @author TEGA
 */
public interface VendorOwnerMapper {
    
    VendorOwner mapDtoToVendorOwner(VendorOwnerDto vendorOwnerDto);
    
    VendorOwnerDto mapVendorOwnerToDto(VendorOwner vendorOwner);
    
    VendorOwnerDto mapRegistrationRequestToVendorOwnerDto(PersonalVendorRegistrationRequest request);

    PersonalVendorRegistrationRequest getPersonalVendorRegistrationRequestJson(String request);
}
