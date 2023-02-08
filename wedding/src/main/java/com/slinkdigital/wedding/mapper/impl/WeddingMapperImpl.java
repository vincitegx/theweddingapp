package com.slinkdigital.wedding.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.WeddingDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Component
public class WeddingMapperImpl implements WeddingMapper{
    
    @Override
    public WeddingDto mapWeddingToDto(Wedding wedding) {
        return WeddingDto.builder()
                .id(wedding.getId())
                .authorId(wedding.getAuthorId())
                .spouseId(wedding.getSpouseId())
                .createdAt(wedding.getCreatedAt())
                .colourOfTheDay(wedding.getColourOfTheDay())
                .dateOfWedding(wedding.getDateOfWedding())
                .isPublished(wedding.isPublished())
                .publishDate(wedding.getPublishDate())
                .code(wedding.getCode())
                .coupleStatus(wedding.getCoupleStatus())
                .title(wedding.getTitle())
                .venue(wedding.getVenue())
                .weddingStory(wedding.getWeddingStory())
                .weddingType(wedding.getWeddingType())
                .coverFileUrl(wedding.getCoverFileUrl())
                .build();
    }

    @Override
    public Wedding mapWeddingDtoToWedding(WeddingDto weddingDto) {
        return Wedding.builder()
                .id(weddingDto.getId())
                .authorId(weddingDto.getAuthorId())
                .createdAt(weddingDto.getCreatedAt())
                .colourOfTheDay(weddingDto.getColourOfTheDay())
                .dateOfWedding(weddingDto.getDateOfWedding())
                .isPublished(weddingDto.isPublished())
                .publishDate(weddingDto.getPublishDate())
                .code(weddingDto.getCode())
                .coupleStatus(weddingDto.getCoupleStatus())
                .title(weddingDto.getTitle())
                .venue(weddingDto.getVenue())
                .weddingStory(weddingDto.getWeddingStory())
                .weddingType(weddingDto.getWeddingType())
                .spouseId(weddingDto.getSpouseId())
                .coverFileUrl(weddingDto.getCoverFileUrl())
                .build();
    }

    @Override
    public WeddingDto getJson(String weddingDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WeddingDto weddingJson = objectMapper.readValue(weddingDto, WeddingDto.class);
            return weddingJson;
        } catch (WeddingException | JsonProcessingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }
    
}
