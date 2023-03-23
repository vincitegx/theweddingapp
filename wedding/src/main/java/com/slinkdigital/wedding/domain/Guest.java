package com.slinkdigital.wedding.domain;

import com.slinkdigital.wedding.constant.AvailabilityStatus;
import com.slinkdigital.wedding.constant.GuestStatus;
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
public class Guest implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Wedding wedding;
    
    private String email; 
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus availabilityStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GuestStatus guestStatus;
    
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
    
}
