package com.slinkdigital.wedding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.slinkdigital.wedding.constant.ItemStatus;
import com.slinkdigital.wedding.domain.Budget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author TEGA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BudgetItemDto {
    
    private Long id;
    
    private Budget budget;
    
    private String item;
    
    private Integer quantity;
    
    private Integer amount;
    
    private String itemLink;
    
    private ItemStatus itemStatus;
}
