package com.slinkdigital.user.dto;

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
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}
