package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Couple;
import com.slinkdigital.wedding.dto.CoupleDto;

/**
 *
 * @author TEGA
 */
public interface CoupleMapper {
    
    CoupleDto getJson(String coupleDto);
    
    Couple mapCoupleDtoToCouple(CoupleDto coupleDto);
    
    CoupleDto mapCoupleToCoupleDto(Couple couple);
}
