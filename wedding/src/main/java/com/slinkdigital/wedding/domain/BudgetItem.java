package com.slinkdigital.wedding.domain;

import com.slinkdigital.wedding.constant.ItemStatus;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
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
public class BudgetItem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Budget budget;
    
    @Size(max = 15)
    private String item;
    
    private Integer quantity;
    
    private Integer amount;
    
    private String itemLink;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus itemStatus;
}
