package com.slinkdigital.vendor.mapper;

import com.slinkdigital.vendor.domain.PersonalOffer;
import com.slinkdigital.vendor.domain.Vendor;
import com.slinkdigital.vendor.dto.PersonalVendorRegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class PersonalOfferMapper {

    public PersonalOffer mapPersonalOfferRequestToPersonalOffer(PersonalVendorRegistrationRequest request, Vendor vendor){
        return PersonalOffer.builder()
                .identityImageUrl(request.getIdentityImageUrl())
                .vendorOwnerIdentity(request.getVendorOwnerIdentity())
                .vendor(vendor)
                .build();
    }
}
