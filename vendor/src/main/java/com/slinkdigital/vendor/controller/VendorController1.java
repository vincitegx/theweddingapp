package com.slinkdigital.vendor.controller;

import com.slinkdigital.vendor.dto.ApiResponse;
import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.dto.VendorDto;
import com.slinkdigital.vendor.service.VendorService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/vu/v1/vendors")
@RequiredArgsConstructor
public class VendorController1 {
    
    private final VendorService vendorService;

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getVendorById(@PathVariable Long id){
        VendorDto vendor = vendorService.getVendorById(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("vendor", vendor))
                        .message("Vendor Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("approved")
    public ResponseEntity<ApiResponse> getApprovedVendors(@RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<VendorDto> allVendors = vendorService.getAllApprovedVendors(
                search.orElse(""),
                PageRequest.of(page.orElse(0), length.orElse(8), direction, sortBy.orElse("id"))
        );
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("vendors", allVendors))
                        .message("List Of Vendors Successful")
                        .status(OK)
                        .build()
        );
    }    
}
