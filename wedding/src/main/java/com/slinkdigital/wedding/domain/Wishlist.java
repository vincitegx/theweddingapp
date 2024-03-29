package com.slinkdigital.wedding.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Wishlist implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Long id;
    
    @ManyToOne
    private Wedding wedding;
    
    @Column(nullable = false)
    private String name; 
    
    private Integer quantity;
    
    private Integer amount;
    
    private String itemLink;
}
