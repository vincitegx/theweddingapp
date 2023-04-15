package com.slinkdigital.wedding.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
public class Budget implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(max = 15)
    private String title;
    
    @Lob
    private String description;
    
    private Integer amount;
    
    @OneToOne
    private Wedding wedding;    
    
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
}
