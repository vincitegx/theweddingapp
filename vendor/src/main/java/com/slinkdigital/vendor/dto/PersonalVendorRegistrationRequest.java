package com.slinkdigital.vendor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.slinkdigital.vendor.constant.VendorOwnerIdentity;
import com.slinkdigital.vendor.domain.VendorCategory;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author TEGA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonalVendorRegistrationRequest {
    
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    private VendorOwnerIdentity vendorOwnerIdentity;
    
    @ManyToMany
    private List<VendorCategory> category;
    
    private String fullName;
    
    private String address;
}
