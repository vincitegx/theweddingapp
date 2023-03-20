package com.slinkdigital.vendor.service.impl;

import com.slinkdigital.vendor.constant.VerificationStatus;
import com.slinkdigital.vendor.domain.VendorOwner;
import com.slinkdigital.vendor.dto.EventDto;
import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.dto.VendorOwnerDto;
import com.slinkdigital.vendor.exception.VendorException;
import com.slinkdigital.vendor.mapper.VendorOwnerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.slinkdigital.vendor.repository.VendorOwnerRepository;
import com.slinkdigital.vendor.service.FileService;
import com.slinkdigital.vendor.service.VendorOwnerService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VendorOwnerServiceImpl implements VendorOwnerService{

    private final VendorOwnerRepository vendorOwnerRepository;
    private final VendorOwnerMapper vendorOwnerMapper;
    private final FileService fileService;
    private final HttpServletRequest request;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;
    
    @Override
    public VendorOwnerDto getVendorOwnerById(Long id) {
        try{
            VendorOwner vendorDetails = vendorOwnerRepository.findById(id).orElseThrow(()-> new VendorException("No Vendor Owner Asssociated With This Id"));
            return vendorOwnerMapper.mapVendorOwnerToDto(vendorDetails);
        }catch(VendorException ex){
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> registerAsPersonalVendor(PersonalVendorRegistrationRequest request, MultipartFile identityImage) {
        try {
            Map<String, String> registrationStatus = new HashMap<>();
            if (!Objects.equals(request.getUserId(), getLoggedInUserId())) {
                throw new VendorException("Cannot Identify The User, Therefore operation cannot be performed");
            } else {
                String identityImageUrl = fileService.uploadFile(identityImage);
                VendorOwnerDto vendorOwnerDto = vendorOwnerMapper.mapRegistrationRequestToVendorOwnerDto(request);
                vendorOwnerDto.setVerificationStatus(VerificationStatus.PENDING);
                vendorOwnerDto.setIdentityUrl(identityImageUrl);
                vendorOwnerDto.setCompanyEmail(getLoggedInUserEmail());
                vendorOwnerDto.setUserId(getLoggedInUserId());
                vendorOwnerDto.setCreatedAt(LocalDateTime.now());
                vendorOwnerRepository.save(vendorOwnerMapper.mapDtoToVendorOwner(vendorOwnerDto));
                Map<String, String> data = new HashMap<>();
                EventDto eventDto = EventDto.builder().from("davidogbodu3056@gmail.com").to(getLoggedInUserEmail()).data(data).build();
                kakfaTemplate.send("vendorowner-registration", eventDto);
                registrationStatus.put("success", "Verification takes up to 3 days, please be patient !!!");
                return registrationStatus;
            }
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public VendorOwnerDto updateVerificationStatusForUserAsVendor(VendorOwnerDto vendorDetailsDto) {
        try {
            VendorOwner vendorOwner = vendorOwnerRepository.findById(vendorDetailsDto.getId()).orElseThrow(() -> new VendorException("No vendor associated with such id"));
            vendorOwner.setVerificationStatus(vendorDetailsDto.getVerificationStatus());
            vendorOwner = vendorOwnerRepository.saveAndFlush(vendorOwner);
            return vendorOwnerMapper.mapVendorOwnerToDto(vendorOwner);
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }
    
    private Long getLoggedInUserId() {
        try {
            return Long.parseLong(request.getHeader("x-id"));
        } catch (VendorException ex) {
            throw new VendorException("You Need To Be Logged In !!!");
        }
    }

    private String getLoggedInUserEmail() {
        try {
            return request.getHeader("x-email");
        } catch (VendorException ex) {
            throw new VendorException("You Need To Be Logged In !!!");
        }
    }
    
}
