package com.slinkdigital.wedding.mapper.impl;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.slinkdigital.wedding.domain.GiftAndSupport;
import com.slinkdigital.wedding.dto.GiftAndSupportDto;
import com.slinkdigital.wedding.mapper.GSMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GSMapperImpl implements GSMapper{

    private final WeddingMapper weddingMapper;
    
    @Override
    public GiftAndSupportDto mapGiftAndSupportToDto(GiftAndSupport giftAndSupport) {
        return GiftAndSupportDto.builder()
                .id(giftAndSupport.getId())
                .itemLink(giftAndSupport.getItemLink())
                .name(giftAndSupport.getName())
                .wedding(weddingMapper.mapWeddingToDto(giftAndSupport.getWedding()))
                .duration(getDuration(giftAndSupport.getCreatedAt()))
                .createdAt(giftAndSupport.getCreatedAt())
                .build();
    }

    @Override
    public String getDuration(Instant instant) {
        return TimeAgo.using(instant.toEpochMilli());
    }

    @Override
    public GiftAndSupport mapDtoToGiftAndSupport(GiftAndSupportDto giftAndSupportDto) {
        return GiftAndSupport.builder()
                .id(giftAndSupportDto.getId())
                .itemLink(giftAndSupportDto.getItemLink())
                .name(giftAndSupportDto.getName())
                .wedding(weddingMapper.mapWeddingDtoToWedding(giftAndSupportDto.getWedding()))
                .createdAt(giftAndSupportDto.getCreatedAt())
                .build();
    }
    
}
