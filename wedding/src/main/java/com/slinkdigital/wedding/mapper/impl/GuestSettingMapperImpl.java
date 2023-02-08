package com.slinkdigital.wedding.mapper.impl;

import com.slinkdigital.wedding.domain.GuestSetting;
import com.slinkdigital.wedding.dto.GuestSettingDto;
import com.slinkdigital.wedding.mapper.GuestSettingMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GuestSettingMapperImpl implements GuestSettingMapper{
    
    private final WeddingMapper weddingMapper;

    @Override
    public GuestSetting mapDtoToSetting(GuestSettingDto guestSettingDto) {
        return GuestSetting.builder()
                .id(guestSettingDto.getId())
                .guestFormOpened(guestSettingDto.isGuestFormOpened())
                .maxNumberOfUnknownGuests(guestSettingDto.getMaxNumberOfUnknownGuests())
                .wedding(weddingMapper.mapWeddingDtoToWedding(guestSettingDto.getWedding()))
                .build();
    }

    @Override
    public GuestSettingDto mapSettingToDto(GuestSetting guestSetting) {
        return GuestSettingDto.builder()
                .id(guestSetting.getId())
                .guestFormOpened(guestSetting.isGuestFormOpened())
                .maxNumberOfUnknownGuests(guestSetting.getMaxNumberOfUnknownGuests())
                .wedding(weddingMapper.mapWeddingToDto(guestSetting.getWedding()))
                .build();
    }
    
}
