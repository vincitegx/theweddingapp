package com.slinkdigital.mail.service.impl;

import com.slinkdigital.mail.dto.EventDto;
import com.slinkdigital.mail.exception.MailServiceException;
import com.slinkdigital.mail.service.MailContentBuilder;
import com.slinkdigital.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class MailServiceImpl implements MailService {

    private final JavaMailSender emailSender;

    private final MailContentBuilder mailContentBuilder;
    
    @Override
    public void sendEmail(EventDto eventDto) throws MailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(eventDto.getFrom());
            messageHelper.setTo(eventDto.getTo());
            messageHelper.setSubject(eventDto.getData().get("subject"));
            messageHelper.setText(mailContentBuilder.build(eventDto.getData()));
        };
        try {
            emailSender.send(messagePreparator);
            log.info("Email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new MailServiceException("Exception occurred when sending mail to " + eventDto.getTo(), e);
        }
        emailSender.send(messagePreparator);
    }

}
