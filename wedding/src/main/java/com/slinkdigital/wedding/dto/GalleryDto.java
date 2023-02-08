package com.slinkdigital.wedding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.slinkdigital.wedding.domain.Photo;
import com.slinkdigital.wedding.domain.Wedding;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class GalleryDto {
    
    private Long id;
    
    private WeddingDto wedding;
    
    private List<Photo> preWeddingPhotos;
    
    private List<Photo> weddingPhotos;
    
    private List<Photo> postWeddingPhotos;
    
}
