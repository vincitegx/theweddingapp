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
    
//    Long id;
//
//    String productName;
//
//    String authorEmailAddress;
//
//    String authorFullName;
    
    private String to;
    private String from;
    private String subject;
    private String message;
}
