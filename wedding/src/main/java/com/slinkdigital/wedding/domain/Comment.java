package com.slinkdigital.wedding.domain;

import java.io.Serializable;
import java.time.Instant;
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
public class Comment implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    
    private Long userId;
    
    @ManyToOne
    private Wedding wedding;
    
    @Lob
    private String message;
    
    @Column(nullable = false)
    private Instant createdAt;    
}
