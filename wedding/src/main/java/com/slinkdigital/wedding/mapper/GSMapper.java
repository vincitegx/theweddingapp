package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.GiftAndSupport;
import com.slinkdigital.wedding.dto.GiftAndSupportDto;
import java.time.Instant;

/**
 *
 * @author TEGA
 */
public interface GSMapper {
    GiftAndSupportDto mapGiftAndSupportToDto(GiftAndSupport giftAndSupport);
    GiftAndSupport mapDtoToGiftAndSupport(GiftAndSupportDto giftAndSupportDto);
    String getDuration(Instant instant);
}
