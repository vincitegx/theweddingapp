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
public class VendorListener {
    
    private final MailService mailService;

    @KafkaListener(topics = "vendorowner-registration", groupId = "vendor")
    public void handleGuestInvitation(ConsumerRecord<String, EventDto> consumerRecord) {
        EventDto eventDto = consumerRecord.value();
        Map<String, String> data = eventDto.getData();
        data.put("subject", "Your Registeration is being processed");
        data.put("template", "vendorownerRegTemplate");
        eventDto.setData(data);

        log.info("Email To Vendor Owner Reg Request recieved: " + consumerRecord.toString());
        if (eventDto.getFrom() == null) {
            log.warn("Ignoring request to send an e-mail without e-mail address: " + consumerRecord.toString());
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
