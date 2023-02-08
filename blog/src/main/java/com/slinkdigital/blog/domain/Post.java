package com.slinkdigital.blog.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 *
 * @author TEGA
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Nullable
    private String url;
    
    @Nullable
    @Lob
    private String description;
    
    @Column(unique = true)
    private String code;
    
    private String userId;
    
    private Instant createdDate;        
}
