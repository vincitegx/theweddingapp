package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.dto.GuestMessageDto;
import com.slinkdigital.wedding.dto.GuestMessageTemplateDto;
import com.slinkdigital.wedding.dto.MessageTemplateRequest;
import com.slinkdigital.wedding.service.GuestMessageTemplateService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/wu/v1/weddings")
@RequiredArgsConstructor
public class GuestMessageTemplateController {
 
    private final GuestMessageTemplateService guestMessageTemplateService;
    
    @GetMapping("guests/templates")
    @ResponseStatus(HttpStatus.OK)
    public List<GuestMessageTemplateDto> getAllTemplates() {
        return guestMessageTemplateService.getAllTemplates();
    }

    @PostMapping("guests/templates")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestMessageTemplateDto addTemplate(@Valid @RequestBody GuestMessageTemplateDto guestMessageTemplateDto) {
        // for admin role
        return guestMessageTemplateService.addTemplate(guestMessageTemplateDto);
    }
    
    @PutMapping("guests/templates")
    @ResponseStatus(HttpStatus.OK)
    public GuestMessageTemplateDto editTemplate(@Valid @RequestBody GuestMessageTemplateDto guestMessageTemplateDto) {
        // for admin role
        return guestMessageTemplateService.editTemplate(guestMessageTemplateDto);
    }
    
    @DeleteMapping("guests/templates/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTemplate(@PathVariable Long id) {
        // for admin role
        guestMessageTemplateService.deleteTemplate(id);
    }
    
    @GetMapping("guests/templates/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GuestMessageTemplateDto getTemplate(@PathVariable Long id) {
        // add template for admin role
        return guestMessageTemplateService.getTemplate(id);
    }
    
    @PostMapping("guests/templates/template")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestMessageDto useTemplate(@Valid @RequestBody MessageTemplateRequest messageTemplateRequest) {
        return guestMessageTemplateService.useTemplate(messageTemplateRequest);
    }    
}
