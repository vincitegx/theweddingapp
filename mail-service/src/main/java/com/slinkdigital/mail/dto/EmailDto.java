package com.slinkdigital.mail.dto;

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
public class EmailDto {
    
    private String to;
    private String from;
    private String subject;
    private String message;
    
}
