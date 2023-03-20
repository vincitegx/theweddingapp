package com.slinkdigital.wedding.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slinkdigital.wedding.constant.CoupleStatus;
import com.slinkdigital.wedding.constant.WeddingType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author TEGA
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wedding implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Long id;
    
    @Column(unique = true)
    private String code; 
    
    @Column(nullable = false)
    private Long authorId;
    
    private Long spouseId;
    
    @Column(nullable = false)
    private String title;    
    
    private String coverFileUrl;
    
    private String venue;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WeddingType weddingType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoupleStatus coupleStatus;
    
    private String colourOfTheDay;
    
    @Lob
    private String weddingStory;   
    
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(jakarta.persistence.TemporalType.DATE)
    private Date dateOfWedding;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(jakarta.persistence.TemporalType.DATE)
    private Date publishDate;
    
    @Column(nullable = false)
    private boolean isPublished;
}
