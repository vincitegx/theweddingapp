package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Guest;
import com.slinkdigital.wedding.dto.GuestDto;

/**
 *
 * @author TEGA
 */
public interface GuestMapper {
    
    GuestDto mapGuestToGuestDto(Guest guest);
    
    Guest mapGuestDtoToGuest(GuestDto guestDto);
}
