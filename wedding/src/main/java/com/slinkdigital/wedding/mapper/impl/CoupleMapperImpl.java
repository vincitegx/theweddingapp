package com.slinkdigital.wedding.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slinkdigital.wedding.domain.Couple;
import com.slinkdigital.wedding.dto.CoupleDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.CoupleMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class CoupleMapperImpl implements CoupleMapper{

    private final WeddingMapper weddingMapper;
    
    @Override
    public CoupleDto getJson(String coupleDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CoupleDto coupleJson = objectMapper.readValue(coupleDto, CoupleDto.class);
            return coupleJson;
        } catch (IOException ex) {
            throw new WeddingException(ex.getMessage());
        }        
    }

    @Override
    public Couple mapCoupleDtoToCouple(CoupleDto coupleDto) {
        return Couple.builder()
                .userId(coupleDto.getUserId())
                .firstName(coupleDto.getFirstName())
                .otherNames(coupleDto.getOtherNames())
                .imageUrl(coupleDto.getImageUrl())
                .genderType(coupleDto.getGenderType())
                .wedding(weddingMapper.mapWeddingDtoToWedding(coupleDto.getWedding()))
                .family(coupleDto.getFamily())
                .createdAt(coupleDto.getCreatedAt())
                .build();
    }

    @Override
    public CoupleDto mapCoupleToCoupleDto(Couple couple) {
        return CoupleDto.builder()
                .userId(couple.getUserId())
                .wedding(weddingMapper.mapWeddingToDto(couple.getWedding()))
                .firstName(couple.getFirstName())
                .otherNames(couple.getOtherNames())
                .genderType(couple.getGenderType())
                .imageUrl(couple.getImageUrl())
                .family(couple.getFamily())
                .build();
    }
    
}
