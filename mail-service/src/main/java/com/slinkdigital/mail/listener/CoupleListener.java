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
public class CoupleListener {
    
    private final MailService mailService;
    private String template = "template";
    private String subject = "subject";

    private static final String IGNORE_EMAIL_REQUEST = "Ignoring request to send an e-mail without e-mail address: ";
    private static final String SENDING_MAIL_MSG = "Sending Mail";
    private static final String MAIL_MESSAGE_ERROR = "Could not send e-mail";

    @KafkaListener(topicPattern = "couple-request", groupId = "couple")
    public void handleCoupleRequest(ConsumerRecord<String, EventDto> consumerRecord) {
        EventDto eventDto = consumerRecord.value();
        Map<String, String> data = eventDto.getData();
        data.put(subject, "Couple's Request");
        data.put(template, "coupleRequestTemplate");
        eventDto.setData(data);

        log.info("Couple Request recieved: " + consumerRecord.toString());
        if (eventDto.getFrom() == null) {
            log.warn(IGNORE_EMAIL_REQUEST + consumerRecord.toString());
            return;
        }
        try {
            log.info(SENDING_MAIL_MSG);
            mailService.sendEmail(eventDto);
        } catch (MailServiceException e) {
            log.error(MAIL_MESSAGE_ERROR, e);
        }
    }
    
    @KafkaListener(topicPattern = "couple-request-rejected", groupId = "couple")
    public void handleCoupleRequestRejected(ConsumerRecord<String, EventDto> consumerRecord) {
        EventDto eventDto = consumerRecord.value();
        Map<String, String> data = eventDto.getData();
        data.put(subject, "Request Rejected");
        data.put(template, "coupleRequestRejectedTemplate");
        eventDto.setData(data);

        log.info("Couple Rejected Request recieved: " + consumerRecord.toString());
        if (eventDto.getFrom() == null) {
            log.warn(IGNORE_EMAIL_REQUEST + consumerRecord.toString());
            return;
        }
        try {
            log.info(SENDING_MAIL_MSG);
            mailService.sendEmail(eventDto);
        } catch (MailServiceException e) {
            log.error(MAIL_MESSAGE_ERROR, e);
        }
    }
    
    @KafkaListener(topicPattern = "couple-request-accepted", groupId = "couple")
    public void handleCoupleRequestAccepted(ConsumerRecord<String, EventDto> consumerRecord) {
        EventDto eventDto = consumerRecord.value();
        Map<String, String> data = eventDto.getData();
        data.put(subject, "Request Accepted");
        data.put(template, "coupleRequestAcceptedTemplate");
        eventDto.setData(data);

        log.info("Couple Accepted Request recieved: " + consumerRecord.toString());
        if (eventDto.getFrom() == null) {
            log.warn(IGNORE_EMAIL_REQUEST + consumerRecord.toString());
            return;
        }
        try {
            log.info(SENDING_MAIL_MSG);
            mailService.sendEmail(eventDto);
        } catch (MailServiceException e) {
            log.error(MAIL_MESSAGE_ERROR, e);
        }
    }
    
}
