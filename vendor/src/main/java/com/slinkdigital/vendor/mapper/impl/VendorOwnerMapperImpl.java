package com.slinkdigital.vendor.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slinkdigital.vendor.domain.VendorOwner;
import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.dto.VendorOwnerDto;
import com.slinkdigital.vendor.exception.VendorException;
import com.slinkdigital.vendor.mapper.VendorOwnerMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class VendorOwnerMapperImpl implements VendorOwnerMapper{

    @Override
    public VendorOwner mapDtoToVendorOwner(VendorOwnerDto vendorOwnerDto) {
        return VendorOwner.builder()
                .companyAddress(vendorOwnerDto.getCompanyAddress())
                .companyDescription(vendorOwnerDto.getCompanyDescription())
                .companyEmail(vendorOwnerDto.getCompanyEmail())
                .companyImageUrl(vendorOwnerDto.getCompanyImageUrl())
                .companyName(vendorOwnerDto.getCompanyName())
                .companyPhoneNo(vendorOwnerDto.getCompanyPhoneNo())
                .corporateAccountBank(vendorOwnerDto.getCorporateAccountBank())
                .corporateAccountName(vendorOwnerDto.getCorporateAccountName())
                .corporateAccountNo(vendorOwnerDto.getCorporateAccountNo())
                .dateOfCommencement(vendorOwnerDto.getDateOfCommencement())
                .nameOfAccountSignatory(vendorOwnerDto.getNameOfAccountSignatory())
                .verificationStatus(vendorOwnerDto.getVerificationStatus())
                .regCertificateUrl(vendorOwnerDto.getRegCertificateUrl())
                .regNo(vendorOwnerDto.getRegNo())
                .userId(vendorOwnerDto.getUserId())
                .createdAt(vendorOwnerDto.getCreatedAt())
                .build();
    }

    @Override
    public VendorOwnerDto mapVendorOwnerToDto(VendorOwner vendorOwner) {
        return VendorOwnerDto.builder()
                .companyAddress(vendorOwner.getCompanyAddress())
                .companyDescription(vendorOwner.getCompanyDescription())
                .companyEmail(vendorOwner.getCompanyEmail())
                .companyImageUrl(vendorOwner.getCompanyImageUrl())
                .companyName(vendorOwner.getCompanyName())
                .companyPhoneNo(vendorOwner.getCompanyPhoneNo())
                .corporateAccountBank(vendorOwner.getCorporateAccountBank())
                .corporateAccountName(vendorOwner.getCorporateAccountName())
                .corporateAccountNo(vendorOwner.getCorporateAccountNo())
                .dateOfCommencement(vendorOwner.getDateOfCommencement())
                .nameOfAccountSignatory(vendorOwner.getNameOfAccountSignatory())
                .verificationStatus(vendorOwner.getVerificationStatus())
                .regCertificateUrl(vendorOwner.getRegCertificateUrl())
                .regNo(vendorOwner.getRegNo())
                .userId(vendorOwner.getUserId())
                .createdAt(vendorOwner.getCreatedAt())
                .build();
    }

    @Override
    public PersonalVendorRegistrationRequest getPersonalVendorRegistrationRequestJson(String request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PersonalVendorRegistrationRequest dto = objectMapper.readValue(request, PersonalVendorRegistrationRequest.class);
            return dto;
        } catch (IOException ex) {
            throw new VendorException(ex.getMessage());
        }        
    }

    @Override
    public VendorOwnerDto mapRegistrationRequestToVendorOwnerDto(PersonalVendorRegistrationRequest request) {
        return VendorOwnerDto.builder()
                .companyAddress(request.getAddress())
                .companyName(request.getFullName())
                .vendorOwnerIdentity(request.getVendorOwnerIdentity())
                .build();
    }
    
}
