package com.slinkdigital.wedding.mapper;

import com.slinkdigital.wedding.domain.GuestMessageTemplate;
import com.slinkdigital.wedding.dto.GuestMessageTemplateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GuestMessageTemplateMapper {
    
    public GuestMessageTemplateDto mapToDto(GuestMessageTemplate guestMessageTemplate){
        return GuestMessageTemplateDto.builder()
                .id(guestMessageTemplate.getId())
                .title(guestMessageTemplate.getTitle())
                .content(guestMessageTemplate.getContent())
                .link(guestMessageTemplate.getLink())
                .build();
    }
    
    public GuestMessageTemplate mapDtoToGuestMessageTemplate(GuestMessageTemplateDto guestMessageTemplateDto){
        return GuestMessageTemplate.builder()
                .id(guestMessageTemplateDto.getId())
                .title(guestMessageTemplateDto.getTitle())
                .content(guestMessageTemplateDto.getContent())
                .link(guestMessageTemplateDto.getLink())
                .build();
    }
}
