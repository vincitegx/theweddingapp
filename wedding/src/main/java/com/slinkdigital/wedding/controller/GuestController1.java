package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.GuestDto;
import com.slinkdigital.wedding.service.GuestService;
import java.time.LocalDateTime;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<ApiResponse> AddGuestByAnonymous(@Valid @RequestBody GuestDto guestDto){
        guestDto = guestService.guestSelfAddition(guestDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("guest", guestDto))
                        .message("Guest Addition Successful !!!")
                        .status(OK)
                        .build()
        );
    }
    
    @PutMapping("guests/response")
    public ResponseEntity<ApiResponse> submitResponseToInvitation(@Valid @RequestBody GuestDto guestDto){
        guestDto = guestService.submitInvitationResponse(guestDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("guest", guestDto))
                        .message("Response Submitted Successfully")
                        .status(OK)
                        .build()
        );
    }    
}
