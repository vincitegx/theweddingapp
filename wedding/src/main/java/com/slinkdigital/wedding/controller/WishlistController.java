package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("api/ws/v1/weddings/wishlists")
@RequiredArgsConstructor
public class WishlistController {
    
    private final WishlistService wishlistService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistDto addWishlist(@Valid @RequestBody WishlistDto wishlistDto) {
        return wishlistService.addWishlist(wishlistDto);
    }
    
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public WishlistDto editWishlist(@Valid @RequestBody WishlistDto wishlistDto) {
        return wishlistService.editWishlist(wishlistDto);
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void removeWishlist(@PathVariable Long id) {
        wishlistService.removeWishlist(id);
    }
    
}
