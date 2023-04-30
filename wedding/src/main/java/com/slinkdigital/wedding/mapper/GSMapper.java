package com.slinkdigital.wedding.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.slinkdigital.wedding.domain.GiftAndSupport;
import com.slinkdigital.wedding.dto.GiftAndSupportDto;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GSMapper {

    private final WeddingMapper weddingMapper;

    public GiftAndSupportDto mapGiftAndSupportToDto(GiftAndSupport giftAndSupport) {
        return GiftAndSupportDto.builder()
                .id(giftAndSupport.getId())
                .wedding(weddingMapper.mapWeddingToDto(giftAndSupport.getWedding()))
                .duration(getDuration(giftAndSupport.getCreatedAt()))
                .createdAt(giftAndSupport.getCreatedAt())
                .build();
    }

    public String getDuration(Instant instant) {
        return TimeAgo.using(instant.toEpochMilli());
    }

    public GiftAndSupport mapDtoToGiftAndSupport(GiftAndSupportDto giftAndSupportDto) {
        return GiftAndSupport.builder()
                .id(giftAndSupportDto.getId())
                .wedding(weddingMapper.mapWeddingDtoToWedding(giftAndSupportDto.getWedding()))
                .createdAt(giftAndSupportDto.getCreatedAt())
                .build();
    }

}
