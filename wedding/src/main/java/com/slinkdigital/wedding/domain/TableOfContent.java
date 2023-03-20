package com.slinkdigital.wedding.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
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
@Table(name = "toc")
public class TableOfContent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Long id;
    
    private Integer num;
    
    @OneToOne
    private Wedding wedding; 
    
    private String title;
    
    @DateTimeFormat(pattern = "HH:mm")
    @Temporal(jakarta.persistence.TemporalType.DATE)
    private Date startTime;
    
    @DateTimeFormat(pattern = "HH:mm")
    @Temporal(jakarta.persistence.TemporalType.DATE)
    private Date endTime;
    
    private String personInCharge;
    
}
