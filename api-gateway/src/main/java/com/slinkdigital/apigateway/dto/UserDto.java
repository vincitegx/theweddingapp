package com.slinkdigital.apigateway.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TEGA
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
    
    private Long id;
    private String email;
    private Set<String> roles;
    
}
