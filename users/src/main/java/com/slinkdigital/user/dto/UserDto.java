package com.slinkdigital.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.validation.constraints.Email;
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
public class UserDto {
    
    private Long id;
    
    @Email(message = "invalid email")
    private String email;
    
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private Boolean isNonLocked;
    
    private Boolean isEnabled;
    
    private Set<String> roles;
}
