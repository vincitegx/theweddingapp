package com.slinkdigital.vendor.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slinkdigital.vendor.constant.VendorPlan;
import com.slinkdigital.vendor.constant.VerificationStatus;
import com.slinkdigital.vendor.domain.Vendor;
import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.dto.VendorDto;
import com.slinkdigital.vendor.exception.VendorException;
import com.slinkdigital.vendor.mapper.VendorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class VendorMapperImpl implements VendorMapper{
//    @Override
//    public Vendor mapDtoToVendor(VendorDto vendorDto) {
//        return Vendor.builder()
//                .companyAddress(vendorOwnerDto.getCompanyAddress())
//                .companyDescription(vendorOwnerDto.getCompanyDescription())
//                .companyEmail(vendorOwnerDto.getCompanyEmail())
//                .companyImageUrl(vendorOwnerDto.getCompanyImageUrl())
//                .companyName(vendorOwnerDto.getCompanyName())
//                .companyPhoneNo(vendorOwnerDto.getCompanyPhoneNo())
//                .corporateAccountBank(vendorOwnerDto.getCorporateAccountBank())
//                .corporateAccountName(vendorOwnerDto.getCorporateAccountName())
//                .corporateAccountNo(vendorOwnerDto.getCorporateAccountNo())
//                .dateOfCommencement(vendorOwnerDto.getDateOfCommencement())
//                .nameOfAccountSignatory(vendorOwnerDto.getNameOfAccountSignatory())
//                .verificationStatus(vendorOwnerDto.getVerificationStatus())
//                .regCertificateUrl(vendorOwnerDto.getRegCertificateUrl())
//                .regNo(vendorOwnerDto.getRegNo())
//                .userId(vendorDto.getUserId())
//                .createdAt(vendorDto.getCreatedAt())
//                .build();
//    }

//    @Override
//    public VendorOwnerDto mapVendorOwnerToDto(VendorOwner vendorOwner) {
//        return VendorOwnerDto.builder()
//                .companyAddress(vendorOwner.getCompanyAddress())
//                .companyDescription(vendorOwner.getCompanyDescription())
//                .companyEmail(vendorOwner.getCompanyEmail())
//                .companyImageUrl(vendorOwner.getCompanyImageUrl())
//                .companyName(vendorOwner.getCompanyName())
//                .companyPhoneNo(vendorOwner.getCompanyPhoneNo())
//                .corporateAccountBank(vendorOwner.getCorporateAccountBank())
//                .corporateAccountName(vendorOwner.getCorporateAccountName())
//                .corporateAccountNo(vendorOwner.getCorporateAccountNo())
//                .dateOfCommencement(vendorOwner.getDateOfCommencement())
//                .nameOfAccountSignatory(vendorOwner.getNameOfAccountSignatory())
//                .verificationStatus(vendorOwner.getVerificationStatus())
//                .regCertificateUrl(vendorOwner.getRegCertificateUrl())
//                .regNo(vendorOwner.getRegNo())
//                .userId(vendorOwner.getUserId())
//                .createdAt(vendorOwner.getCreatedAt())
//                .build();
//    }

    @Override
    public PersonalVendorRegistrationRequest getPersonalVendorRegistrationRequestJson(String request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(request, PersonalVendorRegistrationRequest.class);
        } catch (IOException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public Vendor mapRegistrationRequestToVendorDto(PersonalVendorRegistrationRequest request) {
        return Vendor.builder()
                .category(request.getCategory())
                .name(request.getName())
                .description(request.getDescription())
                .vendorPlan(VendorPlan.PERSONAL)
                .verificationStatus(VerificationStatus.PENDING)
                .userId(request.getUserId())
                .createdAt(LocalDateTime.now())
                .build();
    }
    @Override
    public Vendor mapDtoToVendor(VendorDto vendorDto) {
        return Vendor.builder()
                .category(vendorDto.getCategory())
                .coverImageUrl(vendorDto.getCoverImageUrl())
                .createdAt(vendorDto.getCreatedAt())
                .description(vendorDto.getDescription())
                .id(vendorDto.getId())
                .userId(vendorDto.getUserId())
                .name(vendorDto.getName())
                .rating(vendorDto.getRating())
                .verificationStatus(vendorDto.getVerificationStatus())
                .category(vendorDto.getCategory())
                .build();
    }

    @Override
    public VendorDto mapVendorToDto(Vendor vendor) {
        return VendorDto.builder()
                .category(vendor.getCategory())
                .coverImageUrl(vendor.getCoverImageUrl())
                .createdAt(vendor.getCreatedAt())
                .description(vendor.getDescription())
                .id(vendor.getId())
                .userId(vendor.getUserId())
                .name(vendor.getName())
                .rating(vendor.getRating())
                .verificationStatus(vendor.getVerificationStatus())
                .build();
    }
}
