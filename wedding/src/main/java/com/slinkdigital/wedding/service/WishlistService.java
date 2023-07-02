package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.domain.Wishlist;
import com.slinkdigital.wedding.dto.WishlistDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.WishlistMapper;
import com.slinkdigital.wedding.repository.WeddingRepository;
import com.slinkdigital.wedding.repository.WishlistRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WeddingRepository weddingRepository;
    private final WishlistMapper wishlistMapper;
    private static final String WEDDING_NOT_FOUND = "No Such Wedding";

    public List<WishlistDto> getWishlistsForWedding(Long id) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        List<Wishlist> wishlists = wishlistRepository.findByWedding(wedding);
        List<WishlistDto> wishlistDtos = new ArrayList<>();
        wishlists.forEach(wishlist -> wishlistDtos.add(wishlistMapper.mapWishlistToWishlistDto(wishlist)));
        return wishlistDtos;
    }

    public WishlistDto addWishlist(WishlistDto wishlistDto) {
        weddingRepository.findById(wishlistDto.getWedding().getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        Wishlist wishlist = wishlistMapper.mapWishlistDtoToWishlist(wishlistDto);
        wishlist = wishlistRepository.save(wishlist);
        wishlistDto = wishlistMapper.mapWishlistToWishlistDto(wishlist);
        return wishlistDto;
    }

    public WishlistDto editWishlist(WishlistDto wishlistDto) {
        Wishlist w = wishlistRepository.findById(wishlistDto.getId()).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        w.setAmount(wishlistDto.getAmount());
        w.setName(wishlistDto.getName());
        w.setQuantity(wishlistDto.getQuantity());
        w = wishlistRepository.saveAndFlush(w);
        wishlistDto = wishlistMapper.mapWishlistToWishlistDto(w);
        return wishlistDto;
    }

    public WishlistDto getWishlistForWedding(Long id) {
        Wishlist w = wishlistRepository.findById(id).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        return wishlistMapper.mapWishlistToWishlistDto(w);
    }

    public void removeWishlist(Long id) {
        Wishlist w = wishlistRepository.findById(id).orElseThrow(() -> new WeddingException(WEDDING_NOT_FOUND));
        wishlistRepository.delete(w);
    }
}
