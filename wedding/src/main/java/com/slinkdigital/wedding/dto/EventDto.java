package com.slinkdigital.wedding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author TEGA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(NON_NULL)
public class EventDto {
    
    protected String to;
    protected String from;
    private Map<String,String> data;
    
}
