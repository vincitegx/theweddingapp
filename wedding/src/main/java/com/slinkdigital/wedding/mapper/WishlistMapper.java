package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Wishlist;
import com.slinkdigital.wedding.dto.WishlistDto;

/**
 *
 * @author TEGA
 */
public interface WishlistMapper {
    
    WishlistDto mapWishlistToWishlistDto(Wishlist wishlist);
    
    Wishlist mapWishlistDtoToWishlist(WishlistDto wishlist);
}
