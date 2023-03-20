package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.GiftAndSupportDto;
import com.slinkdigital.wedding.service.GiftAndSupportService;
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
public class GiftAndSupportController {
    
    private final GiftAndSupportService giftAndSupportService;
    
    @GetMapping("{weddingId}/gs")
    public ResponseEntity<ApiResponse> getAllGiftAndSupportForWedding(@PathVariable(value = "weddingId") Long id) {
        List<GiftAndSupportDto> comments = giftAndSupportService.getAllGiftAndSupportForWedding(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("gs", comments))
                        .message("List Of Gifts And Supports Successful")
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("gs")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody GiftAndSupportDto giftAndSupportDto) {
        List<GiftAndSupportDto> gs = giftAndSupportService.add(giftAndSupportDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("gs", gs))
                        .message("Gift And Support Added Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @PutMapping("gs")
    public ResponseEntity<ApiResponse> update(@Valid @RequestBody GiftAndSupportDto giftAndSupportDto) {
        List<GiftAndSupportDto> gs = giftAndSupportService.update(giftAndSupportDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("gs", gs))
                        .message("Gift And Support updated Successfully")
                        .status(OK)
                        .build()
        );
    }

    @DeleteMapping("gs/{id}")
    public ResponseEntity<ApiResponse> remove(@PathVariable Long id) {
        List<GiftAndSupportDto> gs = giftAndSupportService.remove(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("gs", gs))
                        .message("Gift And Support Removed Successfully")
                        .status(OK)
                        .build()
        );
    }
}
