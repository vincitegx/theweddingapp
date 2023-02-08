package com.slinkdigital.wedding.mapper.impl;

import com.slinkdigital.wedding.domain.Wishlist;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.mapper.WishlistMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class WishlistMapperImpl implements WishlistMapper{

    private final WeddingMapper weddingMapper;
    
    @Override
    public WishlistDto mapWishlistToWishlistDto(Wishlist wishlist) {
        return WishlistDto.builder()
                .id(wishlist.getId())
                .amount(wishlist.getAmount())
                .name(wishlist.getName())
                .quantity(wishlist.getQuantity())
                .itemLink(wishlist.getItemLink())
                .wedding(weddingMapper.mapWeddingToDto(wishlist.getWedding()))
                .build();
    }

    @Override
    public Wishlist mapWishlistDtoToWishlist(WishlistDto wishlist) {
        return Wishlist.builder()
                .id(wishlist.getId())
                .amount(wishlist.getAmount())
                .name(wishlist.getName())
                .quantity(wishlist.getQuantity())
                .itemLink(wishlist.getItemLink())
                .wedding(weddingMapper.mapWeddingDtoToWedding(wishlist.getWedding()))
                .build();
    }
    
}
