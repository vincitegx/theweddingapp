package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.GuestDto;
import com.slinkdigital.wedding.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("api/wu/v1/weddings")
@RequiredArgsConstructor
public class GuestController1 {

    private final GuestService guestService;

    @PostMapping("guests")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestDto userRegistrationAsGuest(@Valid @RequestBody GuestDto guestDto) {
        return guestService.guestSelfAddition(guestDto);
    }

    @PutMapping("guests/response")
    @ResponseStatus(HttpStatus.OK)
    public GuestDto submitResponseToInvitation(@Valid @RequestBody GuestDto guestDto) {
        return guestService.submitInvitationResponse(guestDto);
    }
}
