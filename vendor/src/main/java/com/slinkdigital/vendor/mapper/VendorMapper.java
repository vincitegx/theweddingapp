package com.slinkdigital.vendor.mapper;

import com.slinkdigital.vendor.domain.Vendor;
import com.slinkdigital.vendor.dto.VendorDto;

/**
 *
 * @author TEGA
 */
public interface VendorMapper {
    
    Vendor mapDtoToVendor(VendorDto vendorDto);
    
    VendorDto mapVendorToDto(Vendor vendor);
    
}
