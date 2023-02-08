package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.service.WishlistService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/wu/v1/weddings")
@RequiredArgsConstructor
public class WishlistController1 {
    
    private final WishlistService wishlistService;
    
    @GetMapping("{weddingId}/wishlists")
    public ResponseEntity<ApiResponse> getWishlistsForWedding(@PathVariable(value = "weddingId") Long id) {
        List<WishlistDto> wishlistDtos = wishlistService.getWishlistsForWedding(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wishlists", wishlistDtos))
                        .message("Wishlist displayed successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @GetMapping("wishlists/{id}")
    public ResponseEntity<ApiResponse> getWishlistForWedding(@RequestParam Long id) {
        WishlistDto wishlistDto = wishlistService.getWishlistForWedding(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wishlist", wishlistDto))
                        .message("Wishlist displayed successfully")
                        .status(OK)
                        .build()
        );
    }
}
