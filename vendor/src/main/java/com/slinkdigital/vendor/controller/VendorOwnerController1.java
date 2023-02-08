package com.slinkdigital.vendor.controller;

import com.slinkdigital.vendor.dto.ApiResponse;
import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import com.slinkdigital.vendor.mapper.VendorOwnerMapper;
import com.slinkdigital.vendor.service.VendorOwnerService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/vs/v1/vendors/owners")
@RequiredArgsConstructor
public class VendorOwnerController1 {
    
    private VendorOwnerService vendorOwnerService;
    private VendorOwnerMapper vendorOwnerMapper;
    
    @PostMapping(path = "personal",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> registerAsPersonalVendor(@RequestParam("request") String request, 
            @RequestParam("file") MultipartFile identityImage){
        PersonalVendorRegistrationRequest personalVendorRegistrationRequest = vendorOwnerMapper.getPersonalVendorRegistrationRequestJson(request);
        Map<String, String> registrationStatus = vendorOwnerService.registerAsPersonalVendor(personalVendorRegistrationRequest, identityImage);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isRegistered", true))
                        .message(registrationStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }
    
}
