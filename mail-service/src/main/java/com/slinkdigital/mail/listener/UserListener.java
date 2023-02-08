package com.slinkdigital.mail.listener;

import com.slinkdigital.mail.dto.EventDto;
import com.slinkdigital.mail.exception.MailServiceException;
import com.slinkdigital.mail.service.MailService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserListener {

    private final MailService mailService;

    @KafkaListener(topicPattern = "user-registration", groupId = "user")
    public void handleUserRegistration(ConsumerRecord<String, EventDto> record) {
        EventDto eventDto = record.value();
        Map<String, String> data = eventDto.getData();
        data.put("subject", "Verify Your Email");
        data.put("template", "userRegistrationTemplate");
        eventDto.setData(data);

        log.info("Request send user registration recieved: " + record.toString());
        if (eventDto.getFrom() == null) {
            log.warn("Ignoring request to send an e-mail without e-mail address: " + record.toString());
            return;
        }
        try {
            log.info("sending email");
            mailService.sendEmail(eventDto);
        } catch (MailServiceException e) {
            log.error("Could not send e-mail", e);
        }
    }

    @KafkaListener(topicPattern = "user-password-reset", groupId = "user")
    public void handleUserPasswordReset(ConsumerRecord<String, EventDto> record) {
        EventDto eventDto = record.value();
        Map<String, String> data = eventDto.getData();
        data.put("subject", "Reset Your Password");
        data.put("template", "pswResetTemplate");
        eventDto.setData(data);

        log.info("Request send user registration recieved: " + record.toString());
        if (eventDto.getFrom() == null) {
            log.warn("Ignoring request to send an e-mail without e-mail address: " + record.toString());
            return;
        }
        try {
            log.info("sending email");
            mailService.sendEmail(eventDto);
        } catch (MailServiceException e) {
            log.error("Could not send e-mail", e);
        }
    }
}
