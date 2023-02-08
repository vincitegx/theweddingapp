package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.GuestSettingDto;


/**
 *
 * @author TEGA
 */
public interface GuestSettingService {
    
//    boolean changeGuestFormStatus(GuestSettingDto  guestSettingDto);
//
//    boolean changeMaxNumberOfAllowedGuests(GuestSettingDto guestSettingDto);

    GuestSettingDto addGuestSetting(GuestSettingDto setting);

    GuestSettingDto changeGuestSetting(GuestSettingDto setting);
}
