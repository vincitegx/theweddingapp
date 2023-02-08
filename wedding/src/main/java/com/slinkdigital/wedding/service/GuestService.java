package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.GuestDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author TEGA
 */
public interface GuestService {
    
    Page<GuestDto> getAllGuestsForWedding(Long id,String search, PageRequest pageRequest);

    GuestDto addGuest(GuestDto guestDto);

    GuestDto guestSelfAddition(GuestDto guestDto);
 
    GuestDto changeGuestStatus(GuestDto guestDto);
    
    Page<GuestDto> removeGuest(GuestDto guestDto);
    
    Page<GuestDto> removeGuests(List<GuestDto> guestDto);
    
    Map<String, String> sendInvitationToGuest(GuestDto guestDto);

    Map<String, String> sendInvitationToGuests(List<GuestDto> guestDto);

    GuestDto submitInvitationResponse(GuestDto guestDto);
}
