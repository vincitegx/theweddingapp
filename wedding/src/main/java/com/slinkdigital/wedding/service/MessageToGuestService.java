package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.MessageGuestDto;
import com.slinkdigital.wedding.dto.MessageToGuestDto;
import java.util.List;
import java.util.Map;

/**
 *
 * @author TEGA
 */
public interface MessageToGuestService {
    
    List<MessageToGuestDto> getMessages(Long weddingId);
    
    MessageToGuestDto getMessage(Long id);
    
    MessageToGuestDto addMessage(MessageToGuestDto messageToGuestDto);
    
    MessageToGuestDto putContent(MessageToGuestDto messageToGuestDto);
    
    Map<String, String> deleteMessage(Long id);

    Map<String, String> sendMessageToGuests(MessageGuestDto messageGuestDto);
}
