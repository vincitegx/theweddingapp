package com.slinkdigital.vendor.domain;

import com.slinkdigital.vendor.constant.VerificationStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    @ManyToOne
    private VendorOwner vendorOwner;
    
    @ManyToOne
    private VendorCategory category;
    
    private Integer price;
    
    private String description;
    
    private Integer rating;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;
    
    private String coverImageUrl;
    
    @OneToMany
    private List<Gallery> gallery;
    
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
}
