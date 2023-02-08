package com.slinkdigital.vendor.service.impl;

import com.slinkdigital.vendor.constant.VerificationStatus;
import com.slinkdigital.vendor.domain.Gallery;
import com.slinkdigital.vendor.domain.Vendor;
import com.slinkdigital.vendor.domain.VendorCategory;
import com.slinkdigital.vendor.dto.VendorCategoryDto;
import com.slinkdigital.vendor.dto.VendorDto;
import com.slinkdigital.vendor.exception.VendorException;
import com.slinkdigital.vendor.mapper.VendorMapper;
import com.slinkdigital.vendor.repository.GalleryRepository;
import com.slinkdigital.vendor.repository.VendorCategoryRepository;
import com.slinkdigital.vendor.repository.VendorRepository;
import com.slinkdigital.vendor.service.FileService;
import com.slinkdigital.vendor.service.VendorService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.slinkdigital.vendor.repository.VendorOwnerRepository;

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
    private final VendorCategoryRepository vendorCategoryRepository;
    private final GalleryRepository galleryRepository;
    private final FileService fileService;
    private final VendorMapper vendorMapper;

    @Override
    public Page<VendorDto> getAllVendorsForUser(Long userId, String search, PageRequest pageRequest) {
        try {
            Page<Vendor> vendor = vendorRepository.findByUserIdAndNameContains(userId,search, pageRequest);
            List<Vendor> vendorList = vendor.toList();
            Page<VendorDto> vendorDto = new PageImpl(vendorList);
            vendor.forEach(v -> {
                vendorDto.and(vendorMapper.mapVendorToDto(v));
            });
            return vendorDto;
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
            vendor.forEach(v -> {
                vendorDto.and(vendorMapper.mapVendorToDto(v));
            });
            return vendorDto;
        } catch (VendorException ex) {
            throw new VendorException(ex.getMessage());
        }
    }

    @Override
    public List<VendorCategoryDto> getVendorCategories() {
        try {
            List<VendorCategory> vendorCategories = vendorCategoryRepository.findAll();
            List<VendorCategoryDto> vendorCategoryDtos = vendorCategories.stream().map(this::mapVendorCategoryToVendorCategoryDto).toList();
            return vendorCategoryDtos;
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
                vendorDto.setGallery(gallerys);
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

}
