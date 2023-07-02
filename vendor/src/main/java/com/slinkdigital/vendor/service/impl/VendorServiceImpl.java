package com.slinkdigital.vendor.service.impl;

import com.slinkdigital.vendor.constant.VerificationStatus;
import com.slinkdigital.vendor.domain.Gallery;
import com.slinkdigital.vendor.domain.PersonalOffer;
import com.slinkdigital.vendor.domain.Vendor;
import com.slinkdigital.vendor.domain.VendorCategory;
import com.slinkdigital.vendor.dto.*;
import com.slinkdigital.vendor.exception.VendorException;
import com.slinkdigital.vendor.mapper.PersonalOfferMapper;
import com.slinkdigital.vendor.mapper.VendorMapper;
import com.slinkdigital.vendor.repository.GalleryRepository;
import com.slinkdigital.vendor.repository.PersonalOfferRepository;
import com.slinkdigital.vendor.repository.VendorCategoryRepository;
import com.slinkdigital.vendor.repository.VendorRepository;
import com.slinkdigital.vendor.service.FileService;
import com.slinkdigital.vendor.service.VendorService;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final HttpServletRequest request;
    private final KafkaTemplate<String, EventDto> kakfaTemplate;
    private final VendorCategoryRepository vendorCategoryRepository;
    private final GalleryRepository galleryRepository;
    private final FileService fileService;
    private final PersonalOfferMapper personalOfferMapper;
    private final PersonalOfferRepository personalOfferRepository;

    @Override
    public Page<VendorDto> getAllVendorsForUser(Long userId, String search, PageRequest pageRequest) {
        try {
            Page<Vendor> vendor = vendorRepository.findByUserIdAndNameContains(userId,search, pageRequest);
            List<Vendor> vendorList = vendor.toList();
            Page<VendorDto> vendorDto = new PageImpl(vendorList);
            vendor.forEach(v -> vendorDto.and(vendorMapper.mapVendorToDto(v)));
            return vendorDto;
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    public VendorDto updateVerificationStatusForUserAsVendor(VendorOwnerDto vendorDetailsDto) {
        try {
            Vendor vendorOwner = vendorRepository.findById(vendorDetailsDto.getId()).orElseThrow(() -> new VendorException("No vendor associated with such id"));
            vendorOwner.setVerificationStatus(vendorDetailsDto.getVerificationStatus());
            vendorOwner = vendorRepository.saveAndFlush(vendorOwner);
            return vendorMapper.mapVendorToDto(vendorOwner);
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public Page<VendorDto> getAllApprovedVendors(String search, PageRequest pageRequest) {
        try {
            Page<Vendor> vendor = vendorRepository.findByVerificationStatusAndNameContains(VerificationStatus.APPROVED,search, pageRequest);
            List<Vendor> vendorList = vendor.toList();
            Page<VendorDto> vendorDto = new PageImpl(vendorList);
            vendor.forEach(v -> vendorDto.and(vendorMapper.mapVendorToDto(v)));
            return vendorDto;
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public List<VendorCategoryDto> getVendorCategories() {
        try {
            List<VendorCategory> vendorCategories = vendorCategoryRepository.findAll();
            return vendorCategories.stream().map(this::mapVendorCategoryToVendorCategoryDto).toList();
        } catch (VendorException ex) {
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
                Vendor vendor = vendorMapper.mapRegistrationRequestToVendorDto(request);
                vendor = vendorRepository.save(vendor);
                request.setIdentityImageUrl(identityImageUrl);
                PersonalOffer personalOffer = personalOfferMapper.mapPersonalOfferRequestToPersonalOffer(request, vendor);
                personalOfferRepository.save(personalOffer);
                Map<String, String> data = new HashMap<>();
                EventDto eventDto = EventDto.builder().from("davidogbodu3056@gmail.com").to(getLoggedInUserEmail()).data(data).build();
                kakfaTemplate.send("vendor-registration", eventDto);
                registrationStatus.put("success", "Verification takes up to 3 days, please be patient !!!");
                return registrationStatus;
            }
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public VendorCategoryDto addVendorCatergory(VendorCategoryDto vendorCategoryDto) {
        try {
            VendorCategory vendorCategory = VendorCategory.builder().name(vendorCategoryDto.getName()).build();
            vendorCategory = vendorCategoryRepository.save(vendorCategory);
            return mapVendorCategoryToVendorCategoryDto(vendorCategory);
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public Map<String, String> addVendor(VendorDto vendorDto, MultipartFile coverImage, List<MultipartFile> gallery) {
        try {
            Map<String, String> registrationStatus = new HashMap<>();
            if (vendorDto.getUserId() == null) {
                throw new VendorException("Cannot Identify The User, Therefore operation cannot be performed");
            } else {
                String coverImageUrl = fileService.uploadFile(coverImage);
                List<String> galleyImageUrls = fileService.uploadFiles(gallery);
                List<Gallery> gallerys = new ArrayList<>();
                galleyImageUrls.forEach(galleryImageUrl -> {
                    Gallery g = Gallery.builder().name(galleryImageUrl).build();
                    g = galleryRepository.save(g);
                    gallerys.add(g);
                });
                vendorDto.setCoverImageUrl(coverImageUrl);
                vendorDto.setCreatedAt(LocalDateTime.now());
                vendorRepository.save(vendorMapper.mapDtoToVendor(vendorDto));
                registrationStatus.put("success", "It will take 3 days to get approved");
                return registrationStatus;
            }
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }
    
    @Override
    public VendorDto updateVerificationStatusForVendor(VendorDto vendorDto) {
        try {
            Vendor vendor = vendorRepository.findById(vendorDto.getId()).orElseThrow(() -> new VendorException("No vendor associated with such id"));
            vendor.setVerificationStatus(vendorDto.getVerificationStatus());
            vendor = vendorRepository.saveAndFlush(vendor);
            return vendorMapper.mapVendorToDto(vendor);
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public VendorDto getVendorById(Long vendorId) {
        try{
            Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(()-> new VendorException("No vendor associated with id"));
            return vendorMapper.mapVendorToDto(vendor);
        }catch(VendorException ex){
            throw new VendorException(ex.getMessage());
        }
    }

    
    @Override
    public Boolean hasRoleVendor(Long userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private VendorCategoryDto mapVendorCategoryToVendorCategoryDto(VendorCategory vendorCategory) {
        return VendorCategoryDto.builder()
                .id(vendorCategory.getId())
                .name(vendorCategory.getName())
                .build();
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
