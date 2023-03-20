package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.ApiResponse;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.service.WishlistService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("api/ws/v1/weddings/wishlists")
@RequiredArgsConstructor
public class WishlistController {
    
    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<ApiResponse> addWishlist(@Valid @RequestBody WishlistDto wishlistDto) {
        WishlistDto wishlist = wishlistService.addWishlist(wishlistDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wishlist", wishlist))
                        .message("Addition of wishlist successful")
                        .status(CREATED)
                        .build()
        );
    }
    
    @PutMapping
    public ResponseEntity<ApiResponse> editWishlist(@Valid @RequestBody WishlistDto wishlistDto) {
        WishlistDto wishlist = wishlistService.editWishlist(wishlistDto);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wishlist", wishlist))
                        .message("Wishlist Updated Successfully")
                        .status(OK)
                        .build()
        );
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> removeWishlist(@PathVariable Long id) {
        List<WishlistDto> wishlists = wishlistService.removeWishlist(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("wishlists", wishlists))
                        .message("Wishlist Deleted Successfully")
                        .status(OK)
                        .build()
        );
    }
    
}
