package com.slinkdigital.vendor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.slinkdigital.vendor.constant.VerificationStatus;
import com.slinkdigital.vendor.domain.Gallery;
import com.slinkdigital.vendor.domain.VendorCategory;
import com.slinkdigital.vendor.domain.VendorOwner;
import java.time.LocalDateTime;
import java.util.List;
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
public class VendorDto {
    
    private Long id;
    
    private Long userId;
    
    private String name;
    
    private VendorCategory category;
    
    private Integer price;
    
    private String description;
    
    private Integer rating;
    
    private VerificationStatus verificationStatus;
    
    private String coverImageUrl;
    
    private List<Gallery> gallery;
    
    private LocalDateTime createdAt;
    
    private VendorOwner vendorOwner;
}
