package com.slinkdigital.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TEGA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetRequest {
    
    @NotNull
    private String resetToken;
    
    @NotNull
    @Size(min = 8, max = 15, message="password must be {min} to {max} characters long")
    private String newPassword;
    
    private String confirmPassword;
}
