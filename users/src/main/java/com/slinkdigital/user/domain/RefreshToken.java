package com.slinkdigital.user.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TEGA
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String token;
    
    @ManyToOne
    private Users user;
    
    @Column(nullable = false)
    protected Instant createdDate;
    
    @Column(nullable = false)
    private Instant expiryDate;
}
