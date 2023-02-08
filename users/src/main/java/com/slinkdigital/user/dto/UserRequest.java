package com.slinkdigital.user.dto;

import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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
public class UserRequest {
    
    @Email(message = "invalid email")
    @NotNull
    private String email;
    
    @Lob
    private String message;
}
