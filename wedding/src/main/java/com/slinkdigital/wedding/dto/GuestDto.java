package com.slinkdigital.wedding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.slinkdigital.wedding.constant.AvailabilityStatus;
import com.slinkdigital.wedding.constant.GuestStatus;
import java.time.LocalDateTime;
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
public class GuestDto {
    
    private Long id;
    
    private String code;
    
    private String email; 
    
    private String name;
    
    private WeddingDto wedding;
    
    private String comment;
    
    private GuestStatus guestStatus;
    
    private AvailabilityStatus availabilityStatus;
    
    protected LocalDateTime createdAt;
}
