package com.slinkdigital.wedding.domain;

import com.slinkdigital.wedding.constant.GenderType;
import java.io.Serializable;
import java.time.LocalDateTime;
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
public class Couple implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId;
    
    @ManyToOne
    private Wedding wedding;
    
    private String firstName;
    
    private String otherNames;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType genderType;
    
    private String imageUrl;
    
    private String family;
    
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
}
