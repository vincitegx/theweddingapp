package com.slinkdigital.wedding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author TEGA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PostDto {
    
    private Long id;
    
    private String caption;

    private String fileUrl;
    
    private WeddingDto wedding;
    
    private String duration;
    
    private Author author;
    
    private Integer totalReactions;
    
    private Integer totalComments;
}
