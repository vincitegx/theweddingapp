package com.slinkdigital.wedding.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.slinkdigital.wedding.constant.CoupleStatus;
import com.slinkdigital.wedding.constant.WeddingType;
import java.time.LocalDateTime;
import java.util.Date;
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
public class WeddingDto {
    
    private Long id;
    
    private String code;
    
    private Long authorId;
    
    private Long spouseId;
    
    private String title;
    
    private String coverFileUrl;
    
    protected LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfWedding;
    
    private CoupleStatus coupleStatus;
    
    private String venue;
    
    private WeddingType weddingType;
    
    private String colourOfTheDay;

    private String weddingStory;
    
    private boolean isPublished;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;
    
}
