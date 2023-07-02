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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatusChangeDto {
    
    private String to;
    private String from;
    private String subject;
    private String message;
}
