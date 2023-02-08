package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.dto.WishlistDto;
import java.util.List;

/**
 *
 * @author TEGA
 */
public interface WishlistService {

    List<WishlistDto> getWishlistsForWedding(Long id);

    WishlistDto addWishlist(WishlistDto wishlistDto);

    WishlistDto editWishlist(WishlistDto wishlistDto);

    WishlistDto getWishlistForWedding(Long id);

    List<WishlistDto> removeWishlist(Long id);
    
}
