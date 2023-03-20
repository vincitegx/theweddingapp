package com.slinkdigital.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class RegisterRequest {
    
    @Email(message = "invalid email")
    @NotNull
    private String email;
    
    @NotNull
    @Size(min = 8, max = 15, message="password must be {min} to {max} characters long")
    private String password;
    
    @NotNull
    private String confirmPassword;
    
}
