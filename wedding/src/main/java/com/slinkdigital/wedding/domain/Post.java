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
public class Post implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String caption;

    @Column(nullable = false)
    private String fileUrl;
    
    @ManyToOne
    private Wedding wedding;
    
    private Instant createdAt;
}
