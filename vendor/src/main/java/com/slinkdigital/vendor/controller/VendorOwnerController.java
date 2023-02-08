package com.slinkdigital.vendor.controller;

import com.slinkdigital.vendor.dto.ApiResponse;
import com.slinkdigital.vendor.dto.VendorOwnerDto;
import com.slinkdigital.vendor.service.VendorOwnerService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/vu/v1/vendors")
@RequiredArgsConstructor
public class VendorOwnerController {

    private final VendorOwnerService vendorOwnerService;
    
    @GetMapping("{id}/owners")
    public ResponseEntity<ApiResponse> getVendorOwnerById(@PathVariable Long id) {
        VendorOwnerDto vendorOwner = vendorOwnerService.getVendorOwnerById(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("vendorOwner", vendorOwner))
                        .message("VendorOwner Successful")
                        .status(OK)
                        .build()
        );
    }

}
