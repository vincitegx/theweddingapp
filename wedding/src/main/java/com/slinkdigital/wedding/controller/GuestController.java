package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.GuestDto;
import com.slinkdigital.wedding.dto.GuestSettingDto;
import com.slinkdigital.wedding.dto.MessageGuestDto;
import com.slinkdigital.wedding.service.GuestService;
import com.slinkdigital.wedding.service.GuestSettingService;
import java.util.Map;
import java.util.Optional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class GuestController {
    
    private final GuestService guestService;
    private final GuestSettingService guestSettingService;
    
    @GetMapping("{weddingId}/guests")
    @ResponseStatus(HttpStatus.OK)
    public Page<GuestDto> getAllGuestsForWedding(@PathVariable(value = "weddingId") Long id,@RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy){
        Sort.Direction direction = Sort.Direction.ASC;
        return guestService.getAllGuestsForWedding(
                id,
                search.orElse(""),
                PageRequest.of(page.orElse(0), length.orElse(8), direction, sortBy.orElse("id"))
        );
    }
    
    @PostMapping("guests")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestDto addGuestByCouple(@Valid @RequestBody GuestDto guestDto){
        return guestService.addGuest(guestDto);
    }
    
    @PostMapping("guests/settings")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestSettingDto addGuestSetting(@Valid @RequestBody GuestSettingDto setting){
        return guestSettingService.addGuestSetting(setting);
    }
    
    @PutMapping("guests/settings")
    @ResponseStatus(HttpStatus.OK)
    public GuestSettingDto changeGuestSetting(@Valid @RequestBody GuestSettingDto setting){
        return guestSettingService.changeGuestSetting(setting);
    }

    @PutMapping("guests/status")
    @ResponseStatus(HttpStatus.OK)
    public GuestDto changeGuestStatus(@Valid @RequestBody GuestDto guestDto){
        return guestService.changeGuestStatus(guestDto);
    }
    
    @DeleteMapping("guest")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGuest(@Valid @RequestBody GuestDto guestDto){
        guestService.removeGuest(guestDto);
    }
    
    @DeleteMapping("guests")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGuests(@Valid @RequestBody List<GuestDto> guestDto){
        guestService.removeGuests(guestDto);
    }
    
    @PostMapping("guests/invitation")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> sendInvitationToGuests(@Valid @RequestBody MessageGuestDto messageGuestDto){
        return guestService.sendInvitationToGuests(messageGuestDto);
    }    
}
