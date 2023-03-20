package com.slinkdigital.wedding.domain;

import java.io.Serializable;
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
public class GuestSetting implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Wedding wedding;
    
    @Column(nullable = false)
    private boolean guestFormOpened;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer maxNumberOfUnknownGuests = 0;
}
