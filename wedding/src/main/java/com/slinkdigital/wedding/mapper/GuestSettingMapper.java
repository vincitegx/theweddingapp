package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.GuestSetting;
import com.slinkdigital.wedding.dto.GuestSettingDto;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GuestSettingMapper {

    private final WeddingMapper weddingMapper;

    public GuestSetting mapDtoToSetting(GuestSettingDto guestSettingDto) {
        return GuestSetting.builder()
                .id(guestSettingDto.getId())
                .guestFormOpened(guestSettingDto.isGuestFormOpened())
                .maxNumberOfUnknownGuests(guestSettingDto.getMaxNumberOfUnknownGuests())
                .wedding(weddingMapper.mapWeddingDtoToWedding(guestSettingDto.getWedding()))
                .build();
    }

    public GuestSettingDto mapSettingToDto(GuestSetting guestSetting) {
        return GuestSettingDto.builder()
                .id(guestSetting.getId())
                .guestFormOpened(guestSetting.isGuestFormOpened())
                .maxNumberOfUnknownGuests(guestSetting.getMaxNumberOfUnknownGuests())
                .wedding(weddingMapper.mapWeddingToDto(guestSetting.getWedding()))
                .build();
    }

}
