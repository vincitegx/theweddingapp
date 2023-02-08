package com.slinkdigital.vendor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.slinkdigital.vendor.constant.VendorOwnerIdentity;
import com.slinkdigital.vendor.constant.VerificationStatus;
import java.time.LocalDateTime;
import java.util.Date;
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
public class VendorOwnerDto {
    
    private Long id;
    
    private Long userId;
    
    private VendorOwnerIdentity vendorOwnerIdentity;
    
    private String identityUrl;
    
    private String companyName;
    
    private String companyImageUrl;
    
    private String companyEmail;
    
    private String companyAddress;
    
    private String companyPhoneNo;
    
    private String companyDescription;
    
    private String corporateAccountName;
    
    private String corporateAccountNo;
   
    private String corporateAccountBank;
    
    private String regNo;
    
    private String regCertificateUrl;
    
    private String nameOfAccountSignatory;
    
    private VerificationStatus verificationStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfCommencement;
    protected LocalDateTime createdAt;
}
