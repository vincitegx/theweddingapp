package com.slinkdigital.vendor.mapper.impl;

import com.slinkdigital.vendor.domain.Vendor;
import com.slinkdigital.vendor.dto.VendorDto;
import com.slinkdigital.vendor.mapper.VendorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class VendorMapperImpl implements VendorMapper{

    @Override
    public Vendor mapDtoToVendor(VendorDto vendorDto) {
        return Vendor.builder()
                .category(vendorDto.getCategory())
                .coverImageUrl(vendorDto.getCoverImageUrl())
                .createdAt(vendorDto.getCreatedAt())
                .description(vendorDto.getDescription())
                .gallery(vendorDto.getGallery())
                .id(vendorDto.getId())
                .userId(vendorDto.getUserId())
                .name(vendorDto.getName())
                .price(vendorDto.getPrice())
                .rating(vendorDto.getRating())
                .verificationStatus(vendorDto.getVerificationStatus())
                .category(vendorDto.getCategory())
                .vendorOwner(vendorDto.getVendorOwner())
                .build();
    }

    @Override
    public VendorDto mapVendorToDto(Vendor vendor) {
        return VendorDto.builder()
                .category(vendor.getCategory())
                .coverImageUrl(vendor.getCoverImageUrl())
                .createdAt(vendor.getCreatedAt())
                .description(vendor.getDescription())
                .gallery(vendor.getGallery())
                .id(vendor.getId())
                .userId(vendor.getUserId())
                .name(vendor.getName())
                .price(vendor.getPrice())
                .rating(vendor.getRating())
                .verificationStatus(vendor.getVerificationStatus())
                .category(vendor.getCategory())
                .vendorOwner(vendor.getVendorOwner())
                .build();
    }
    
    
    
}
