package com.slinkdigital.mail.service.impl;

import com.slinkdigital.mail.dto.EmailContentDto;
import com.slinkdigital.mail.dto.ProjectStatusChangeDto;
import com.slinkdigital.mail.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService{
    
//    private final SpringTemplateEngine templateEngine;

    @Override
    public EmailContentDto generateEmail(ProjectStatusChangeDto emailDto) {
//        Context context = new Context();
//        context.setVariable("message", emailDto.getMessage());
//        context.setVariable("subject", emailDto.getSubject());

        return EmailContentDto
            .builder()
                .subject(emailDto.getSubject())
                .message(emailDto.getMessage())
//            .html(templateEngine.process("mailTemplate.html", context))
            .build();
    }
    
}
