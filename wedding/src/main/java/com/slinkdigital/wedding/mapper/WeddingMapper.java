package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.WeddingDto;

/**
 *
 * @author TEGA
 */

public interface WeddingMapper {
    
    Wedding mapWeddingDtoToWedding(WeddingDto weddingDto);
    
    WeddingDto mapWeddingToDto(Wedding wedding);
    
    WeddingDto getJson(String weddingDto);
}
