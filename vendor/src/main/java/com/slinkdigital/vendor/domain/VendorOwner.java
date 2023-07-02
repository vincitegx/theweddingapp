package com.slinkdigital.vendor.domain;

import com.slinkdigital.vendor.constant.VendorOwnerIdentity;
import com.slinkdigital.vendor.constant.VendorPlan;
import com.slinkdigital.vendor.constant.VerificationStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
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
    

    
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
}
