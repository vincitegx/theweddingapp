package com.slinkdigital.vendor.domain;

import com.slinkdigital.vendor.constant.VendorOwnerIdentity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Vendor vendor;
    @Enumerated(EnumType.STRING)
    private VendorOwnerIdentity vendorOwnerIdentity;
    private String identityImageUrl;
}
