package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.MessageGuestDto;
import com.slinkdigital.wedding.dto.MessageToGuestDto;
import com.slinkdigital.wedding.service.MessageToGuestService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class MessageToGuestController {
    
    private final MessageToGuestService messageToGuestService;
    
    @GetMapping("{weddingId}/guests/messages")
    public ResponseEntity<ApiResponse> getMessagesToGuests(@PathVariable(value = "weddingId") Long id) {
        List<MessageToGuestDto> messages = messageToGuestService.getMessages(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("messages", messages))
                        .message("List Of Messages Successful")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("guests/messages/{id}")
    public ResponseEntity<ApiResponse> getMessage(@PathVariable Long id) {
        MessageToGuestDto message = messageToGuestService.getMessage(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("message", message))
                        .message("Message Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("guests/messages")
    public ResponseEntity<ApiResponse> addMessage(@RequestBody MessageToGuestDto message) {
        message = messageToGuestService.addMessage(message);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("message", message))
                        .message("Message Added Successfully")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("guests/messages")
    public ResponseEntity<ApiResponse> putContent(@RequestBody MessageToGuestDto message) {
        message = messageToGuestService.putContent(message);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("message", message))
                        .message("Message Updated Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("guests/messages/{id}")
    public ResponseEntity<ApiResponse> deleteMessage(@PathVariable Long id) {
        Map<String, String> result = messageToGuestService.deleteMessage(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isDeleted", true))
                        .message(result.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("guests/messages/send")
    public ResponseEntity<ApiResponse> sendMessageToGuests(@Valid @RequestBody MessageGuestDto messageGuestDto){
        Map<String, String> messageStatus = messageToGuestService.sendMessageToGuests(messageGuestDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isMessageSent", true))
                        .message(messageStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }    
}
