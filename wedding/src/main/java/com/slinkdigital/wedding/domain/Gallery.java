package com.slinkdigital.wedding.domain;

import java.io.Serializable;
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
public class Gallery implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    private Wedding wedding;
    
    @OneToMany
    private List<Photo> preWeddingPhotos;
    @OneToMany
    private List<Photo> weddingPhotos;
    @OneToMany
    private List<Photo> postWeddingPhotos;
}
