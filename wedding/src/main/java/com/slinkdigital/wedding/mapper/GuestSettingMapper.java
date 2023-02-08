package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.GuestSetting;
import com.slinkdigital.wedding.dto.GuestSettingDto;

/**
 *
 * @author TEGA
 */
public interface GuestSettingMapper {
    GuestSetting mapDtoToSetting(GuestSettingDto guestSettingDto);
    
    GuestSettingDto mapSettingToDto(GuestSetting guestSetting);
}
