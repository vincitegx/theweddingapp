package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.GuestDto;
import com.slinkdigital.wedding.dto.GuestSettingDto;
import com.slinkdigital.wedding.service.GuestService;
import com.slinkdigital.wedding.service.GuestSettingService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public ResponseEntity<ApiResponse> getAllGuestsForWedding(@PathVariable(value = "weddingId") Long id,@RequestParam Optional<String> search,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> length,
            @RequestParam Optional<String> sortBy){
        Sort.Direction direction = Sort.Direction.ASC;
        Page<GuestDto> guests = guestService.getAllGuestsForWedding(
                id,
                search.orElse(""),
                PageRequest.of(page.orElse(0), length.orElse(8), direction, sortBy.orElse("id"))
        );
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("guests", guests))
                        .message("List Of Guests Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping("guests")
    public ResponseEntity<ApiResponse> addGuestByCouple(@Valid @RequestBody GuestDto guestDto){
        guestDto = guestService.addGuest(guestDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("guest", guestDto))
                        .message("Guest Addition Successful !!!")
                        .status(OK)
                        .build()
        );
    }
    
    @PostMapping("guests/settings")
    public ResponseEntity<ApiResponse> addGuestSetting(@Valid @RequestBody GuestSettingDto setting){
        setting = guestSettingService.addGuestSetting(setting);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("setting", setting))
                        .message("Guest Setting has been added successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @PutMapping("guests/settings")
    public ResponseEntity<ApiResponse> changeGuestSetting(@Valid @RequestBody GuestSettingDto setting){
        setting = guestSettingService.changeGuestSetting(setting);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("setting", setting))
                        .message("Guest Setting has been changed successfully")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("guests/status")
    public ResponseEntity<ApiResponse> changeGuestStatus(@Valid @RequestBody GuestDto guestDto){
        guestDto = guestService.changeGuestStatus(guestDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("guest", guestDto))
                        .message("Guest Status Changed Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("guest")
    public ResponseEntity<ApiResponse> removeGuest(@Valid @RequestBody GuestDto guestDto){
        Page<GuestDto> guests = guestService.removeGuest(guestDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("guests", guests))
                        .message("Guest removed successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("guests")
    public ResponseEntity<ApiResponse> removeGuests(@Valid @RequestBody List<GuestDto> guestDto){
        Page<GuestDto> guests = guestService.removeGuests(guestDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("guests", guests))
                        .message("Guests removed successfully")
                        .status(OK)
                        .build()
        );
    }
    
//    @PostMapping("guests/invitation")
//    public ResponseEntity<ApiResponse> sendInvitationToGuests(@Valid @RequestBody List<GuestDto> guestDto){
//        Map<String, String> messageStatus = guestService.sendInvitationToGuests(guestDto);
//        return ResponseEntity.ok(
//                ApiResponse.builder()
//                        .timeStamp(LocalDateTime.now())
//                        .data(Map.of("isMessageSent", true))
//                        .message(messageStatus.get("success"))
//                        .status(OK)
//                        .build()
//        );
//    }    
}
