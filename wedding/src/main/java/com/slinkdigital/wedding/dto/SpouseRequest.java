package com.slinkdigital.wedding.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TEGA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpouseRequest {
    
    @Email(message = "invalid email")
    @NotNull
    private String email;
    
    private WeddingDto wedding;
}
