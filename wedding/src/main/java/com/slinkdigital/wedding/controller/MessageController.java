package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.MessageGuestDto;
import com.slinkdigital.wedding.dto.GuestMessageDto;
import com.slinkdigital.wedding.service.GuestMessageService;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class MessageController {
    
    private final GuestMessageService guestMessageService;
    
    @GetMapping("{weddingId}/guests/messages")
    @ResponseStatus(HttpStatus.OK)
    public List<GuestMessageDto> getGuestMessages(@PathVariable(value = "weddingId") Long id) {
        return guestMessageService.getMessages(id);
    }
    
    @GetMapping("guests/messages/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GuestMessageDto getMessage(@PathVariable Long id) {
        return guestMessageService.getMessage(id);
    }

    @PostMapping("guests/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestMessageDto addMessage(@RequestBody GuestMessageDto message) {
        return guestMessageService.addMessage(message);
    }

    @PutMapping("guests/messages")
    @ResponseStatus(HttpStatus.OK)
    public GuestMessageDto putContent(@RequestBody GuestMessageDto message) {
        return guestMessageService.putContent(message);
    }
    
    @DeleteMapping("guests/messages/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable Long id) {
        guestMessageService.deleteMessage(id);
    }

    @PostMapping("guests/messages/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToGuests(@Valid @RequestBody MessageGuestDto messageGuestDto){
        guestMessageService.sendMessageToGuests(messageGuestDto);
    }    
}
