package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.MessageToGuest;
import com.slinkdigital.wedding.dto.MessageToGuestDto;
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

    public MessageToGuest mapDtoToMessageToGuest(MessageToGuestDto messageToGuestDto) {
        return MessageToGuest.builder()
                .id(messageToGuestDto.getId())
                .wedding(weddingMapper.mapWeddingDtoToWedding(messageToGuestDto.getWedding()))
                .title(messageToGuestDto.getTitle())
                .content(messageToGuestDto.getContent())
                .createdAt(messageToGuestDto.getCreatedAt())
                .build();
    }

    public MessageToGuestDto mapMessageToGuestToDto(MessageToGuest messageToGuest) {
        return MessageToGuestDto.builder()
                .id(messageToGuest.getId())
                .wedding(weddingMapper.mapWeddingToDto(messageToGuest.getWedding()))
                .title(messageToGuest.getTitle())
                .content(messageToGuest.getContent())
                .createdAt(messageToGuest.getCreatedAt())
                .build();
    }

}
