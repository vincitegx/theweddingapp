package com.slinkdigital.vendor.domain;

import com.slinkdigital.vendor.constant.VendorOwnerIdentity;
import com.slinkdigital.vendor.constant.VendorPlan;
import com.slinkdigital.vendor.constant.VerificationStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author TEGA
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorOwner implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    private VendorOwnerIdentity vendorOwnerIdentity;
    
    private String identityUrl;
    
    @Enumerated(EnumType.STRING)
    private VendorPlan vendorPlan;
    
    @ManyToMany
    private List<VendorCategory> category;
    
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
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfCommencement;
    
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
}
