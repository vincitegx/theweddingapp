package com.slinkdigital.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author TEGA
 */
@Data
@AllArgsConstructor
public class Message {
    private String type;
    private String msg;
}

