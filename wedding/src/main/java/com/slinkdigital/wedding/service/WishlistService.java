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

    public List<WishlistDto> getWishlistsForWedding(Long id) {
        try {
            Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
            List<Wishlist> wishlists = wishlistRepository.findByWedding(wedding);
            List<WishlistDto> wishlistDtos = new ArrayList<>();
            wishlists.forEach(wishlist -> {
                wishlistDtos.add(wishlistMapper.mapWishlistToWishlistDto(wishlist));
            });
            return wishlistDtos;
        } catch (NumberFormatException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WishlistDto addWishlist(WishlistDto wishlistDto) {
        try {
            weddingRepository.findById(wishlistDto.getWedding().getId()).orElseThrow(() -> new WeddingException("No such wedding associated"));
            Wishlist wishlist = wishlistMapper.mapWishlistDtoToWishlist(wishlistDto);
            wishlist = wishlistRepository.save(wishlist);
            wishlistDto = wishlistMapper.mapWishlistToWishlistDto(wishlist);
            return wishlistDto;
        } catch (NumberFormatException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WishlistDto editWishlist(WishlistDto wishlistDto) {
        try {
            Wishlist w = wishlistRepository.findById(wishlistDto.getId()).orElseThrow(() -> new WeddingException("No such wedding associated"));
            w.setAmount(wishlistDto.getAmount());
            w.setName(wishlistDto.getName());
            w.setQuantity(wishlistDto.getQuantity());
            w = wishlistRepository.saveAndFlush(w);
            wishlistDto = wishlistMapper.mapWishlistToWishlistDto(w);
            return wishlistDto;
        } catch (NumberFormatException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public WishlistDto getWishlistForWedding(Long id) {
        try {
            Wishlist w = wishlistRepository.findById(id).orElseThrow(() -> new WeddingException("No such wedding associated"));
            return wishlistMapper.mapWishlistToWishlistDto(w);
        } catch (NumberFormatException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    public List<WishlistDto> removeWishlist(Long id) {
        try {
            Wishlist w = wishlistRepository.findById(id).orElseThrow(() -> new WeddingException("No such wedding associated"));
            wishlistRepository.delete(w);
            List<Wishlist> wishlists = wishlistRepository.findByWedding(w.getWedding());
            List<WishlistDto> wishlistDtos = new ArrayList<>();
            wishlists.forEach(wishlist -> {
                wishlistDtos.add(wishlistMapper.mapWishlistToWishlistDto(wishlist));
            });
            return wishlistDtos;
        } catch (NumberFormatException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }
}
