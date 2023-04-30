package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.Guest;
import com.slinkdigital.wedding.dto.GuestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GuestMapper {

    private final WeddingMapper weddingMapper;

    public GuestDto mapGuestToGuestDto(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .wedding(weddingMapper.mapWeddingToDto(guest.getWedding()))
                .name(guest.getName())
                .email(guest.getEmail())
                .guestStatus(guest.getGuestStatus())
                .createdAt(guest.getCreatedAt())
                .build();
    }

    public Guest mapGuestDtoToGuest(GuestDto guestDto) {
        return Guest.builder()
                .wedding(weddingMapper.mapWeddingDtoToWedding(guestDto.getWedding()))
                .email(guestDto.getEmail())
                .name(guestDto.getName())
                .guestStatus(guestDto.getGuestStatus())
                .createdAt(guestDto.getCreatedAt())
                .build();
    }

}
