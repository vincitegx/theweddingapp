package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.GuestMessageTemplate;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.GuestMessageDto;
import com.slinkdigital.wedding.dto.GuestMessageTemplateDto;
import com.slinkdigital.wedding.dto.MessageTemplateRequest;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.GuestMessageTemplateMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.GuestMessageTemplateRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class GuestMessageTemplateService {

    private final GuestMessageTemplateRepository guestMessageTemplateRepository;
    private final GuestMessageTemplateMapper guestMessageTemplateMapper;
    private final WeddingRepository weddingRepository;
    private final WeddingMapper weddingMapper;
    private final HttpServletRequest request;
    private final GuestMessageService guestMessageService;

    public List<GuestMessageTemplateDto> getAllTemplates() {
        List<GuestMessageTemplate> guestMessageTemplate = guestMessageTemplateRepository.findAll();
        List<GuestMessageTemplateDto> guestMessageTemplateDto = new ArrayList<>();
        guestMessageTemplate.forEach(gmt -> {
            guestMessageTemplateDto.add(guestMessageTemplateMapper.mapToDto(gmt));
        });
        return guestMessageTemplateDto;
    }

    public GuestMessageTemplateDto addTemplate(GuestMessageTemplateDto guestMessageTemplateDto) {
        GuestMessageTemplate guestMessageTemplate = guestMessageTemplateMapper.mapDtoToGuestMessageTemplate(guestMessageTemplateDto);
        guestMessageTemplate = guestMessageTemplateRepository.save(guestMessageTemplate);
        return guestMessageTemplateMapper.mapToDto(guestMessageTemplate);
    }

    public GuestMessageTemplateDto editTemplate(GuestMessageTemplateDto guestMessageTemplateDto) {
        GuestMessageTemplate guestMessageTemplate = guestMessageTemplateRepository.findById(guestMessageTemplateDto.getId()).orElseThrow(() -> new WeddingException("No Such Guest Message Template With ID " + guestMessageTemplateDto.getId().toString()));
        guestMessageTemplate.setContent(guestMessageTemplateDto.getContent());
        guestMessageTemplate.setTitle(guestMessageTemplateDto.getTitle());
        guestMessageTemplate = guestMessageTemplateRepository.save(guestMessageTemplate);
        return guestMessageTemplateMapper.mapToDto(guestMessageTemplate);
    }

    public void deleteTemplate(Long id) {
        GuestMessageTemplate guestMessageTemplate = guestMessageTemplateRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Guest Message Template With ID " + id.toString()));
        guestMessageTemplateRepository.delete(guestMessageTemplate);
    }

    public GuestMessageTemplateDto getTemplate(Long id) {
        GuestMessageTemplate guestMessageTemplate = guestMessageTemplateRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Guest Message Template With ID " + id.toString()));
        return guestMessageTemplateMapper.mapToDto(guestMessageTemplate);
    }

    public GuestMessageDto useTemplate(MessageTemplateRequest messageTemplateRequest) {
        Wedding wedding = weddingRepository.findById(messageTemplateRequest.getWeddingDto().getId()).orElseThrow(() -> new WeddingException("No Such Wedding"));
        Long loggedInUser = getLoggedInUserId();
        String formLink = messageTemplateRequest.getGuestMessageTemplateDto().getLink().concat("&entry.").concat(loggedInUser.toString()).concat("=");
        GuestMessageDto guestMessageDto = GuestMessageDto.builder()
                .title(messageTemplateRequest.getGuestMessageTemplateDto().getTitle())
                .content(messageTemplateRequest.getGuestMessageTemplateDto().getContent())
                .link(formLink)
                .wedding(weddingMapper.mapWeddingToDto(wedding))
                .build();
        return guestMessageService.addMessage(guestMessageDto);
    }

    private Long getLoggedInUserId() {
        return Long.parseLong(request.getHeader("x-id"));
    }
}
