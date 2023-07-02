package com.slinkdigital.vendor.controller;

import com.slinkdigital.vendor.dto.*;
import com.slinkdigital.vendor.mapper.VendorMapper;
import com.slinkdigital.vendor.service.VendorService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/vs/v1/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;
    private final VendorMapper vendorMapper;

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getVendorById(@PathVariable Long id) {
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

    @PostMapping(path = "personal",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> registerAsPersonalVendor(@RequestParam("request") String request,
                                                                @RequestParam("file") MultipartFile identityImage){
        PersonalVendorRegistrationRequest personalVendorRegistrationRequest = vendorMapper.getPersonalVendorRegistrationRequestJson(request);
        Map<String, String> registrationStatus = vendorService.registerAsPersonalVendor(personalVendorRegistrationRequest, identityImage);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isRegistered", true))
                        .message(registrationStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }
    @GetMapping("users/{userId}")
    public ResponseEntity<ApiResponse> getVendorsForUser(@PathVariable Long userId,@RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<VendorDto> allVendors = vendorService.getAllVendorsForUser(
                userId,
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
    
    @GetMapping("/category/all")
    public ResponseEntity<ApiResponse> getVendorCategories(){
        List<VendorCategoryDto> vendorCategoryDtos = vendorService.getVendorCategories();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("vendorCategories", vendorCategoryDtos))
                        .message("VendorCategories Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping("/addVendorCategory")
    public ResponseEntity<ApiResponse> addVendorCategory(@Valid @RequestBody VendorCategoryDto categoryDto){
        categoryDto = vendorService.addVendorCatergory(categoryDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("category", categoryDto))
                        .message("Vendor Category Addition Successful !!!")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping(path = "/addVendor",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addVendor(@Valid @RequestBody VendorDto vendorDto, 
            @RequestParam("coverImage") MultipartFile coverImage,@RequestParam("gallery") List<MultipartFile> gallery){
        Map<String, String> vendorStatus = vendorService.addVendor(vendorDto, coverImage, gallery);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isadded", true))
                        .message(vendorStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }
    
    @PutMapping("/updateVerificationStatusForVendor")
    public ResponseEntity<ApiResponse> updateVerificationStatusForVendor(@Valid @RequestBody VendorDto vendorDto){
       vendorDto = vendorService.updateVerificationStatusForVendor(vendorDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("vendorDto", vendorDto))
                        .message("Update Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("/hasRoleVendor/{userId}")
    public ResponseEntity<ApiResponse> hasRoleVendor(@RequestParam Long userId){
        Boolean hasRoleVendor = vendorService.hasRoleVendor(userId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("hasRoleVendor", hasRoleVendor))
                        .message("Status of user having role vendor is :"+ hasRoleVendor)
                        .status(OK)
                        .build()
        );
    }
    
    // Use vendor in wedding, show all vendors sort them by category order by rating, add vendor to wishlist, order vendor item
    // vendor add post
}
