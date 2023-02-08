package com.slinkdigital.mail.service;

import com.slinkdigital.mail.dto.EventDto;
import org.springframework.mail.MailException;

/**
 *
 * @author TEGA
 */
public interface MailService {
    void sendEmail(EventDto eventDto) throws MailException;
}
