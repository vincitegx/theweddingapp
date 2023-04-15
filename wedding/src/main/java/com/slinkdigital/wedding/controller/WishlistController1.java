package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.service.WishlistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public List<WishlistDto> getWishlistsForWedding(@PathVariable(value = "weddingId") Long id) {
        return wishlistService.getWishlistsForWedding(id);
    }
    
    @GetMapping("wishlists/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WishlistDto getWishlistForWedding(@RequestParam Long id) {
        return wishlistService.getWishlistForWedding(id);
    }
}
