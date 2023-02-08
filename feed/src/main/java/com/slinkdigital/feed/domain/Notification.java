package com.slinkdigital.feed.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author TEGA
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    
    private Long id;
   private String content;
   private String sender;
   private NotificationType type;
    
}
