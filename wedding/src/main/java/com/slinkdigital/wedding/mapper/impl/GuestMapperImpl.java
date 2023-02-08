package com.slinkdigital.wedding.mapper.impl;

import com.slinkdigital.wedding.domain.Guest;
import com.slinkdigital.wedding.dto.GuestDto;
import com.slinkdigital.wedding.mapper.GuestMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GuestMapperImpl implements GuestMapper{
    
    private final WeddingMapper weddingMapper;

    @Override
    public GuestDto mapGuestToGuestDto(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .wedding(weddingMapper.mapWeddingToDto(guest.getWedding()))
                .name(guest.getName())
                .email(guest.getEmail())
                .guestStatus(guest.getGuestStatus())
                .availabilityStatus(guest.getAvailabilityStatus())
                .code(guest.getCode())
                .comment(guest.getComment())
                .createdAt(guest.getCreatedAt())
                .build();
    }

    @Override
    public Guest mapGuestDtoToGuest(GuestDto guestDto) {
        return Guest.builder()
                .code(guestDto.getCode())
                .wedding(weddingMapper.mapWeddingDtoToWedding(guestDto.getWedding()))
                .email(guestDto.getEmail())
                .name(guestDto.getName())
                .guestStatus(guestDto.getGuestStatus())
                .availabilityStatus(guestDto.getAvailabilityStatus())
                .comment(guestDto.getComment())
                .createdAt(guestDto.getCreatedAt())
                .build();
    }
    
}
