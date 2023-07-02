package com.slinkdigital.vendor.domain;

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
public class Vendor implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    @OneToMany
    private List<VendorCategory> category;
    private String description;
    private Integer rating;

    private String corporateAccountName;
    private String corporateAccountNo;
    private String corporateAccountBank;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;
    private String coverImageUrl;
    @Enumerated(EnumType.STRING)
    private VendorPlan vendorPlan;
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(jakarta.persistence.TemporalType.DATE)
    private Date dateOfCommencement;
}
