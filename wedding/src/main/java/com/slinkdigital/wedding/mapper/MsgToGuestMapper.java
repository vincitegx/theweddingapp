package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.MessageToGuest;
import com.slinkdigital.wedding.dto.MessageToGuestDto;

/**
 *
 * @author TEGA
 */
public interface MsgToGuestMapper {
    
    MessageToGuest mapDtoToMessageToGuest(MessageToGuestDto messageToGuestDto);
    
    MessageToGuestDto mapMessageToGuestToDto(MessageToGuest messageToGuest);
    
}
