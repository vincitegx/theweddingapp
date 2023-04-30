package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.GuestMessage;
import com.slinkdigital.wedding.dto.GuestMessageDto;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class MsgToGuestMapper {

    private final WeddingMapper weddingMapper;

    public GuestMessage mapDtoToMessageToGuest(GuestMessageDto messageToGuestDto) {
        return GuestMessage.builder()
                .id(messageToGuestDto.getId())
                .wedding(weddingMapper.mapWeddingDtoToWedding(messageToGuestDto.getWedding()))
                .title(messageToGuestDto.getTitle())
                .content(messageToGuestDto.getContent())
                .createdAt(messageToGuestDto.getCreatedAt())
                .build();
    }

    public GuestMessageDto mapMessageToGuestToDto(GuestMessage messageToGuest) {
        return GuestMessageDto.builder()
                .id(messageToGuest.getId())
                .wedding(weddingMapper.mapWeddingToDto(messageToGuest.getWedding()))
                .title(messageToGuest.getTitle())
                .content(messageToGuest.getContent())
                .createdAt(messageToGuest.getCreatedAt())
                .build();
    }

}
