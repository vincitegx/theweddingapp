package com.slinkdigital.vendor.dto;

import com.slinkdigital.vendor.constant.VendorOwnerIdentity;
import com.slinkdigital.vendor.domain.VendorCategory;
import java.util.List;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TEGA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalVendorRegistrationRequest {
    private Long userId;
    @Enumerated(EnumType.STRING)
    private VendorOwnerIdentity vendorOwnerIdentity;
    @OneToMany
    private List<VendorCategory> category;
    private String name;
    private String address;
    private String description;
    private String phoneNumber;
    private String identityImageUrl;
}
