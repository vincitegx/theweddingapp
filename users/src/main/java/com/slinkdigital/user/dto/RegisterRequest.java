package com.slinkdigital.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
